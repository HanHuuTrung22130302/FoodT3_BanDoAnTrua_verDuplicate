package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.LoginDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("user");
        String password = request.getParameter("pass");

        if (name == null || password == null || name.trim().isEmpty() || password.trim().isEmpty()) {
            logService.logActivity(0, 0, "Đăng nhập", "Thất bại", "Tên người dùng hoặc mật khẩu trống");
            sendErrorResponse(out, request, response, "Tên người dùng và mật khẩu không được để trống");
            return;
        }

        LoginDAO dao = new LoginDAO();
        Account account = dao.getAccountByName(name);

        if (account == null) {
            logService.logActivity(0, 0, "Đăng nhập", "Thất bại", "Tài khoản không tồn tại: " + name);
            sendErrorResponse(out, request, response, "Tài khoản không tồn tại");
            return;
        }

        if (account.isLocked()) {
            long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), account.getLockTime().plusMinutes(LOCK_DURATION_MINUTES));
            if (minutesLeft > 0) {
                logService.logActivity(account.getAccountId(), account.getRoleId(), "Đăng nhập", "Thất bại", "Tài khoản bị khóa còn " + minutesLeft + " phút");
                out.print("{\"status\": \"locked\", \"message\": \"Tài khoản bị khóa. Vui lòng thử lại sau " + minutesLeft + " phút.\"}");
                return;
            } else {
                dao.resetFailedAttempts(account.getAccountId());
                account.setLocked(false);
                account.setLockTime(null);
            }
        }

        account = dao.login(name, password);
        if (account != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", account);
            logService.logActivity(account.getAccountId(), account.getRoleId(), "Đăng nhập", "Thành công", "Người dùng đã đăng nhập");
            out.print("{\"status\": \"success\", \"message\": \"Đăng nhập thành công\"}");
        } else {
            Account failedAccount = dao.getAccountByName(name);
            if (failedAccount != null) {
                int attempts = failedAccount.getFailedAttempts();
                if (attempts >= MAX_FAILED_ATTEMPTS) {
                    logService.logActivity(failedAccount.getAccountId(), failedAccount.getRoleId(), "Đăng nhập", "Thất bại", "Tài khoản bị khóa do quá số lần thử");
                    out.print("{\"status\": \"locked\", \"message\": \"Tài khoản bị khóa 15 phút do đăng nhập sai quá 5 lần.\"}");
                } else {
                    logService.logActivity(failedAccount.getAccountId(), failedAccount.getRoleId(), "Đăng nhập", "Thất bại", "Sai mật khẩu, còn " + (MAX_FAILED_ATTEMPTS - attempts) + " lần thử");
                    out.print("{\"status\": \"error\", \"message\": \"Sai mật khẩu. Còn " + (MAX_FAILED_ATTEMPTS - attempts) + " lần thử.\"}");
                }
            } else {
                logService.logActivity(0, 0, "Đăng nhập", "Thất bại", "Tài khoản không tồn tại: " + name);
                out.print("{\"status\": \"error\", \"message\": \"Tài khoản không tồn tại\"}");
            }
        }
    }

    private void sendErrorResponse(PrintWriter out, HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            out.print("{\"status\": \"error\", \"message\": \"" + message + "\"}");
        } else {
            request.setAttribute("error", message);
            request.getRequestDispatcher("views/signin.jsp").forward(request, response);
        }
    }
}