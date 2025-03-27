package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.LoginDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.OtpRequest;
import hcmuaf.nlu.edu.vn.testproject.services.VerifyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "UnlockController", value = "/unlock")
public class UnlockController extends HttpServlet {
    private final LoginDAO loginDAO = new LoginDAO();
    private final VerifyService verifyService = new VerifyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("request".equals(action)) {
            request.getRequestDispatcher("views/unlock.jsp").forward(request, response);
        } else if ("verify".equals(action)) {
            request.getRequestDispatcher("views/verify-otp.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("request".equals(action)) {
            String username = request.getParameter("username");
            Account account = loginDAO.getAccountByName(username);

            if (account == null) {
                request.setAttribute("error", "Tên người dùng không tồn tại");
                request.getRequestDispatcher("views/unlock.jsp").forward(request, response);
                return;
            }

            if (!account.isLocked()) {
                request.setAttribute("error", "Tài khoản không bị khóa");
                request.getRequestDispatcher("views/unlock.jsp").forward(request, response);
                return;
            }

            String otpCode = verifyService.generateOtpCode();
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
            OtpRequest otpRequest = new OtpRequest(account.getAccountId(), otpCode, expiryTime);
            loginDAO.insertOtpRequest(otpRequest);

            boolean emailSent = verifyService.sendOtpEmail(account.getEmail(), otpCode, account.getName());
            if (emailSent) {
                request.setAttribute("message", "Mã OTP đã được gửi đến email của bạn.");
                request.setAttribute("username", username);
                request.getRequestDispatcher("views/verify-otp.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Không thể gửi email. Vui lòng thử lại.");
                request.getRequestDispatcher("views/unlock.jsp").forward(request, response);
            }
        } else if ("verify".equals(action)) {
            String username = request.getParameter("username");
            String otpCode = request.getParameter("otp");

            Account account = loginDAO.getAccountByName(username);
            if (account == null) {
                request.setAttribute("error", "Tên người dùng không tồn tại");
                request.getRequestDispatcher("views/verify-otp.jsp").forward(request, response);
                return;
            }

            OtpRequest otpRequest = loginDAO.getOtpRequest(account.getAccountId(), otpCode);
            if (otpRequest == null) {
                request.setAttribute("error", "Mã OTP không hợp lệ");
                request.getRequestDispatcher("views/verify-otp.jsp").forward(request, response);
                return;
            }

            if (verifyService.isExpireTime(otpRequest.getExpiryTime())) {
                request.setAttribute("error", "Mã OTP đã hết hạn");
                request.getRequestDispatcher("views/verify-otp.jsp").forward(request, response);
                return;
            }

            loginDAO.resetFailedAttempts(account.getAccountId());
            loginDAO.deleteOtpRequest(otpRequest.getOtpId());

            request.setAttribute("message", "Tài khoản đã được mở khóa. Vui lòng đăng nhập lại.");
            request.getRequestDispatcher("views/signin.jsp").forward(request, response);
        }
    }
}