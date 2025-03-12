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

@WebServlet(name = "ForgotPassController", value = "/forgotpass")
public class ForgotPassController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/forgot_pass.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountDAO accountDAO = new AccountDAO();
        String email = request.getParameter("email");

        // email có tồn tại trong database
        Account account = accountDAO.getUserByEmail(email);
        if (account == null) {
            request.setAttribute("mess", "Email không tồn tại");
            request.getRequestDispatcher("views/forgot_pass.jsp").forward(request, response);
            return;
        }
        ResetService service = new ResetService();
        String token = service.generateToken();
        String linkReset = "http://localhost:8080/testProject/resetpassword?token=" + token;
        PasswordResetToken newToken = new PasswordResetToken(account.getIdAcc(), false, token, service.expireDateTime());

        //Send link to this email
        TokenForgetDAO tokenForgetDAO = new TokenForgetDAO();
        boolean isInsert = tokenForgetDAO.insertTokenForget(newToken);
        if (!isInsert) {
            request.setAttribute("mess", "have error in server");
            request.getRequestDispatcher("views/forgot_pass.jsp").forward(request, response);
            return;
        }

        boolean isSend = service.sendEmail(email, linkReset, account.getName());
        if (!isSend) {
            request.setAttribute("mess", "Không thể gửi yêu cầu");
            request.getRequestDispatcher("views/forgot_pass.jsp").forward(request, response);
            return;
        }
        request.setAttribute("mess", "Gửi yêu cầu thành công");
        request.getRequestDispatcher("views/forgot_pass.jsp").forward(request, response);
    }
}