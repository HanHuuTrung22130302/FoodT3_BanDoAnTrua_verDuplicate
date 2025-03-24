package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.PendingAccountDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.PendingAccount;
import hcmuaf.nlu.edu.vn.testproject.services.VerifyService;
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
    private final VerifyService verifyService = new VerifyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("message", "Token không hợp lệ.");
            request.getRequestDispatcher("/verification.jsp").forward(request, response);
            return;
        }

        PendingAccount pendingAccount = pendingAccountDAO.getPendingAccountByToken(token);
        if (pendingAccount == null) {
            request.setAttribute("message", "Token không tồn tại.");
            request.getRequestDispatcher("views/verification.jsp").forward(request, response);
            return;
        }

        // Kiểm tra token đã hết hạn chưa
        if (verifyService.isExpireTime(pendingAccount.getExpiryTime())) {
            pendingAccountDAO.deletePendingAccount(token);
            request.setAttribute("message", "Token đã hết hạn.");
            request.getRequestDispatcher("views/verification.jsp").forward(request, response);
            return;
        }

        // Tạo tài khoản chính thức
        Account account = new Account();
        account.setRoleId(2); // Vai trò mặc định là user
        account.setName(pendingAccount.getName());
        account.setPassword(pendingAccount.getPassword()); // Mật khẩu đã được mã hóa
        account.setEmail(pendingAccount.getEmail());
        accountDAO.insertAccount(account);

        // Xóa bản ghi tạm
        pendingAccountDAO.deletePendingAccount(token);

        // Chuyển đến verification.jsp với thông báo thành công
        request.setAttribute("message", "Đăng ký thành công, vui lòng đăng nhập.");
        request.getRequestDispatcher("views/verification.jsp").forward(request, response);
    }
}