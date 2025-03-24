package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.PendingAccountDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "VerificationController", value = "/verify")
public class VerificationController extends HttpServlet {
    private final PendingAccountDAO pendingAccountDAO = new PendingAccountDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("message", "Token không hợp lệ.");
            request.getRequestDispatcher("views/verification.jsp").forward(request, response);
            return;
        }

        // Kiểm tra token hợp lệ
        boolean isValid = pendingAccountDAO.verifyToken(token);
        if (isValid) {
            // Lấy thông tin từ pending_accounts
            Account pendingAccount = pendingAccountDAO.getPendingAccountByToken(token);
            if (pendingAccount != null) {
                // Chuyển sang account
                accountDAO.insertAccount(pendingAccount);
                // Xóa khỏi pending_accounts
                pendingAccountDAO.deletePendingAccount(token);
                request.setAttribute("message", "Xác thực email thành công! Bạn có thể đăng nhập.");
            } else {
                request.setAttribute("message", "Không tìm thấy thông tin đăng ký.");
            }
        } else {
            request.setAttribute("message", "Token không hợp lệ hoặc đã hết hạn.");
        }
        request.getRequestDispatcher("views/verification.jsp").forward(request, response);
    }
}