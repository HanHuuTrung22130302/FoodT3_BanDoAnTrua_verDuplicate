package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.services.GoogleLogin;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet(name = "LoginWithGoogleController", value = "/loginGoogle")
public class LoginWithGoogleController extends HttpServlet {

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            response.sendRedirect("login?error=missing_code");
            return;
        }

        try {
            // Lấy access token và thông tin người dùng từ Google
            String accessToken = GoogleLogin.getToken(code);
            Account googleAccount = GoogleLogin.getUserInfo(accessToken);
            System.out.println(googleAccount);

            if (googleAccount == null || googleAccount.getEmail() == null) {
                response.sendRedirect("login?error=google_auth_failed");
                return;
            }

            // Kiểm tra xem email đã tồn tại trong hệ thống chưa
            Account existingAccount = accountDAO.getUserByEmail(googleAccount.getEmail());

            HttpSession session = request.getSession();
            if (existingAccount != null) {
                // Nếu tài khoản đã tồn tại, đăng nhập
                session.setAttribute("currentUser", existingAccount);
            } else {
                // Nếu chưa tồn tại, tạo tài khoản mới (idRole mặc định là 2, pass để trống hoặc random)
                Account newAccount = new Account(1, 2, "", googleAccount.getUserName(), googleAccount.getEmail());
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