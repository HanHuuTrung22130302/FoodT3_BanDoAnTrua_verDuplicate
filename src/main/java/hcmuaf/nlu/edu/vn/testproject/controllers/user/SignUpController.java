package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.PendingAccountDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
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
    private final VerifyService emailService = new VerifyService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("pass");

        // Kiểm tra thông tin đầu vào
        if (username == null || email == null || password == null ||
                username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            out.print("{\"status\": \"error\", \"message\": \"Vui lòng nhập đầy đủ thông tin.\"}");
            return;
        }

        // Kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            out.print("{\"status\": \"error\", \"message\": \"Mật khẩu phải có ít nhất 6 ký tự.\"}");
            return;
        }

        // Kiểm tra username hoặc email đã tồn tại trong account
        Account existingUserByUsername = accountDAO.getUserByName(username);
        Account existingUserByEmail = accountDAO.getUserByEmail(email);

        if (existingUserByUsername != null) {
            out.print("{\"status\": \"error\", \"message\": \"Tên người dùng đã tồn tại.\"}");
            return;
        }
        if (existingUserByEmail != null) {
            out.print("{\"status\": \"error\", \"message\": \"Email đã được sử dụng.\"}");
            return;
        }

        // Tạo token và lưu vào pending_accounts với thời gian hết hạn 1 phút
        String token = emailService.generateToken();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(1); // Hết hạn sau 1 phút
        pendingAccountDAO.insertPendingAccount(username, password, email, token, expiryTime);

        // Gửi email xác thực
        boolean emailSent = emailService.sendVerificationEmail(email, token, username);
        if (emailSent) {
            out.print("{\"status\": \"success\", \"message\": \"Vui lòng kiểm tra email để hoàn tất đăng ký trong 1 phút.\"}");
        } else {
            out.print("{\"status\": \"error\", \"message\": \"Gửi email xác thực thất bại.\"}");
        }
    }
}