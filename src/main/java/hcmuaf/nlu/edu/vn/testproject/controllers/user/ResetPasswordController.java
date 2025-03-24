package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.TokenForgetDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.PasswordResetToken;
import hcmuaf.nlu.edu.vn.testproject.services.ResetService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ResetPasswordController", value = "/resetpassword")
public class ResetPasswordController extends HttpServlet {
    AccountDAO accountDAO = new AccountDAO();
    TokenForgetDAO tokenForgetDAO = new TokenForgetDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        HttpSession session = request.getSession();

        if (token != null) {
            PasswordResetToken tokenForgetPass = tokenForgetDAO.getTokenPassword(token);
            ResetService service = new ResetService();

            if (tokenForgetPass == null) {
                request.setAttribute("mess", "Đã có lỗi xảy ra vui lòng thực hiện lại yêu cầu");
                request.getRequestDispatcher("views/reset_pass.jsp").forward(request, response);
                return;
            }
            if (tokenForgetPass.isUsed()) {
                request.setAttribute("mess", "Bạn đã thay đổi mật khẩu thành công. Vui lòng thực hiện đăng nhập");
                request.getRequestDispatcher("views/reset_pass.jsp").forward(request, response);
                return;
            }
            if (service.isExpireTime(tokenForgetPass.getExperyTime())) {
                request.setAttribute("mess", "Phiên làm việc đã hết hạn. Vui lòng thực hiện lại yêu cầu");
                request.getRequestDispatcher("views/reset_pass.jsp").forward(request, response);
                return;
            }

            Account user = accountDAO.getUserById(tokenForgetPass.getAccountId());
            request.setAttribute("email", user.getEmail());
            session.setAttribute("token", tokenForgetPass.getToken());
            request.getRequestDispatcher("views/reset_pass.jsp").forward(request, response);
        } else {
            response.sendRedirect("forgotpass");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        // validate pass
        if (!password.equals(confirmPassword)) {
            request.setAttribute("mess", "Xác nhận mật khẩu phải giống nhau");
            request.setAttribute("email", email);
            request.getRequestDispatcher("views/reset_pass.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        String tokenSt = (String) session.getAttribute("token");
        PasswordResetToken tokenForgetPass = tokenForgetDAO.getTokenPassword(tokenSt);
        ResetService service = new ResetService();

        // Kiểm tra token tồn tại
        if (tokenForgetPass == null) {
            request.setAttribute("mess", "Đã có lỗi xảy ra vui lòng thực hiện lại yêu cầu");
            request.getRequestDispatcher("views/reset_pass.jsp").forward(request, response);
            return;
        }
        // Kiểm tra token đã được sử dụng
        if (tokenForgetPass.isUsed()) {
            request.setAttribute("mess", "Bạn đã thay đổi mật khẩu thành công. Vui lòng thực hiện đăng nhập");
            request.getRequestDispatcher("views/reset_pass.jsp").forward(request, response);
            return;
        }
        // Kiểm tra token đã quá hạn
        if (service.isExpireTime(tokenForgetPass.getExperyTime())) {
            request.setAttribute("mess", "Phiên làm việc đã hết hạn. Vui lòng thực hiện lại yêu cầu");
            request.getRequestDispatcher("views/reset_pass.jsp").forward(request, response);
            return;
        }

        // Cập nhật những token đã sử dụng
        tokenForgetPass.setToken(tokenSt);
        tokenForgetPass.setUsed(true);

        accountDAO.updatePassword(email, password);
        tokenForgetDAO.updateStatus(tokenForgetPass);

        // Lưu user và trở về sign in
        response.sendRedirect("login");
    }
}