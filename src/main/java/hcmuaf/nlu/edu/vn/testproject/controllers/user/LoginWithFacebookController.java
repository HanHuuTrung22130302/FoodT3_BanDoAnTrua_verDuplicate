package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.services.FacebookLogin;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LoginWithFacebookController", value = "/loginFacebook")
public class LoginWithFacebookController extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAO(); // Khởi tạo DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            response.sendRedirect("login?error=missing_code");
            return;
        }

        try {
            // Lấy access token và thông tin người dùng từ Google
            String accessToken = FacebookLogin.getToken(code);
            Account fbAccount = FacebookLogin.getUserInfo(accessToken);

            if (fbAccount == null || fbAccount.getEmail() == null) {
                response.sendRedirect("login?error=google_auth_failed");
                return;
            }

            // Kiểm tra xem email đã tồn tại trong hệ thống chưa
            Account existingAccount = accountDAO.getUserByEmail(fbAccount.getEmail());

            HttpSession session = request.getSession();
            if (existingAccount != null) {
                // Nếu tài khoản đã tồn tại, đăng nhập
                session.setAttribute("currentUser", existingAccount);
            } else {
                // Nếu chưa tồn tại, tạo tài khoản mới (idRole mặc định là 1, pass để trống hoặc random)
                Account newAccount = new Account(0, 2, "", fbAccount.getName(), fbAccount.getEmail());
                // Lưu vào database (cần thêm hàm insert vào AccountDAO)
                accountDAO.insertAccount(newAccount); // Bạn cần tự viết hàm này
                session.setAttribute("currentUser", newAccount);
            }

            // Chuyển hướng đến trang chính
            response.sendRedirect("home");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login?error=server_error");
        }
    }
}