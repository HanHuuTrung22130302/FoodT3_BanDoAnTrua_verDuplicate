package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.PendingAccountDAO;
import hcmuaf.nlu.edu.vn.testproject.models.PendingAccount;
import hcmuaf.nlu.edu.vn.testproject.services.VerifyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet(name = "SignUpController", value = "/signup")
public class SignUpController extends HttpServlet {
    private final AccountDAO accountDAO = new AccountDAO();
    private final PendingAccountDAO pendingAccountDAO = new PendingAccountDAO();
    private final VerifyService verifyService = new VerifyService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String userName = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("pass");

        // Kiểm tra thông tin đầu vào
        if (userName == null || email == null || password == null ||
                userName.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            out.print("{\"status\": \"error\", \"message\": \"Vui lòng nhập đầy đủ thông tin\"}");
            out.close();
            return;
        }

        // Kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            out.print("{\"status\": \"error\", \"message\": \"Mật khẩu phải có ít nhất 6 ký tự\"}");
            out.close();
            return;
        }

        // Kiểm tra xem userName hoặc email đã tồn tại trong bảng account chưa
        if (accountDAO.getUserByName(userName) != null) {
            out.print("{\"status\": \"error\", \"message\": \"Tên đăng nhập đã tồn tại\"}");
            out.close();
            return;
        }
        if (accountDAO.getUserByEmail(email) != null) {
            out.print("{\"status\": \"error\", \"message\": \"Email đã được sử dụng\"}");
            out.close();
            return;
        }

        // Tạo token và thời gian hết hạn
        String token = verifyService.generateToken();
        LocalDateTime expiryTime = verifyService.expireDateTime();

        // Lưu vào bảng pending_account
        PendingAccount pendingAccount = new PendingAccount(userName, password, email, token, expiryTime);
        pendingAccountDAO.insertPendingAccount(pendingAccount);

        // Gửi email xác thực
        String verificationLink = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + request.getContextPath() + "/verify?token=" + token;
        boolean emailSent = verifyService.sendVerificationEmail(email, token, userName);

        if (emailSent) {
            out.print("{\"status\": \"success\", \"message\": \"Vui lòng kiểm tra email để xác thực tài khoản\"}");
        } else {
            out.print("{\"status\": \"error\", \"message\": \"Không thể gửi email xác thực\"}");
        }
        out.close();
    }
}