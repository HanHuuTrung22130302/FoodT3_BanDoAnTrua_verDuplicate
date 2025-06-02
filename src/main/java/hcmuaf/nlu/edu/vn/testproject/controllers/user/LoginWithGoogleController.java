package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.AccdetailDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.GoogleAccount;
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
            GoogleAccount googleAccount = GoogleLogin.getUserInfo(accessToken);

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
                Account newAccount = new Account(0, 2, "", googleAccount.getName(), googleAccount.getEmail());
                newAccount.setLoginType("google"); // Đánh dấu là tài khoản Google
                accountDAO.insertAccount(newAccount);
                
                // Lấy account_id vừa tạo
                Account createdAccount = accountDAO.getUserByEmail(googleAccount.getEmail());
                if (createdAccount != null) {
                    AccdetailDAO accdetailDAO = new AccdetailDAO();
                    accdetailDAO.addAccDetail(
                        createdAccount.getAccountId(),
                        googleAccount.getName(), // Sử dụng tên từ Google làm fullName
                        "",
                        "",
                        "",
                        0
                    );
                    session.setAttribute("currentUser", createdAccount);
                }
            }

            // Chuyển hướng đến trang chính
            response.sendRedirect("home");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi chi tiết: " + e.getMessage());
            response.sendRedirect("login?error=server_error&message=" + e.getMessage());
        }
    }
}