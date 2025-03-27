package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.OtpCodeDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.OtpCode;
import hcmuaf.nlu.edu.vn.testproject.services.VerifyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "UnlockAccountController", value = "/unlock-account")
public class UnlockAccountController extends HttpServlet {
    private final AccountDAO accountDAO = new AccountDAO();
    private final OtpCodeDAO otpCodeDAO = new OtpCodeDAO();
    private final VerifyService verifyService = new VerifyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/unlock-account.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("request-otp".equals(action)) {
            // Yêu cầu gửi mã OTP
            String email = request.getParameter("email");
            Account account = accountDAO.getUserByEmail(email);

            if (account == null) {
                request.setAttribute("error", "Email không tồn tại.");
                request.getRequestDispatcher("views/unlock-account.jsp").forward(request, response);
                return;
            }

            if (!account.isLocked()) {
                request.setAttribute("error", "Tài khoản của bạn không bị khóa.");
                request.getRequestDispatcher("views/unlock-account.jsp").forward(request, response);
                return;
            }

            // Tạo và gửi mã OTP
            String otpCode = verifyService.generateOtpCode();
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // OTP hết hạn sau 5 phút
            OtpCode otp = new OtpCode(account.getAccountId(), otpCode, expiryTime);
            otpCodeDAO.insertOtpCode(otp);

            // Gửi email chứa mã OTP
            boolean emailSent = verifyService.sendOtpEmail(email, otpCode, account.getName());
            if (emailSent) {
                session.setAttribute("accountId", account.getAccountId());
                request.setAttribute("message", "Mã OTP đã được gửi đến email của bạn.");
            } else {
                request.setAttribute("error", "Không thể gửi mã OTP. Vui lòng thử lại.");
            }
            request.getRequestDispatcher("views/unlock-account.jsp").forward(request, response);

        } else if ("verify-otp".equals(action)) {
            // Xác thực mã OTP
            String otpCode = request.getParameter("otp");
            Integer accountId = (Integer) session.getAttribute("accountId");

            if (accountId == null) {
                request.setAttribute("error", "Phiên làm việc đã hết hạn. Vui lòng yêu cầu mã OTP mới.");
                request.getRequestDispatcher("views/unlock-account.jsp").forward(request, response);
                return;
            }

            OtpCode otp = otpCodeDAO.getOtpCodeByAccountId(accountId);
            if (otp == null || !otp.getOtpCode().equals(otpCode)) {
                request.setAttribute("error", "Mã OTP không đúng.");
                request.getRequestDispatcher("views/unlock-account.jsp").forward(request, response);
                return;
            }

            if (LocalDateTime.now().isAfter(otp.getExpiryTime())) {
                request.setAttribute("error", "Mã OTP đã hết hạn.");
                request.getRequestDispatcher("views/unlock-account.jsp").forward(request, response);
                return;
            }

            // Mở khóa tài khoản
            Account account = accountDAO.getUserById(accountId);
            accountDAO.resetLoginAttempts(account);
            otpCodeDAO.deleteOtpCode(accountId);
            session.removeAttribute("accountId");
            session.setAttribute("captchaRequired", false);

            request.setAttribute("message", "Tài khoản của bạn đã được mở khóa. Vui lòng đăng nhập lại.");
            request.getRequestDispatcher("views/unlock-account.jsp").forward(request, response);
        }
    }
}