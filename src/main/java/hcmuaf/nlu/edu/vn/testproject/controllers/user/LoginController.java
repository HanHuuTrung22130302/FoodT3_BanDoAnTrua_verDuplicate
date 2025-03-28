package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.daos.LoginDAO;
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
            sendErrorResponse(out, request, response, "Tên người dùng và mật khẩu không được để trống");
            return;
        }

        LoginDAO dao = new LoginDAO();
        Account account = dao.getAccountByName(name); // Lấy thông tin tài khoản trước

        if (account == null) {
            sendErrorResponse(out, request, response, "Tài khoản không tồn tại");
            return;
        }

        // Kiểm tra trạng thái khóa
        if (account.isLocked()) {
            long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), account.getLockTime().plusMinutes(LOCK_DURATION_MINUTES));
            if (minutesLeft > 0) {
                out.print("{\"status\": \"locked\", \"message\": \"Tài khoản bị khóa. Vui lòng thử lại sau " + minutesLeft + " phút.\"}");
                return;
            } else {
                dao.resetFailedAttempts(account.getAccountId()); // Mở khóa nếu hết thời gian
                account.setLocked(false);
                account.setLockTime(null);
            }
        }

        // Kiểm tra đăng nhập
        account = dao.login(name, password);
        if (account != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", account);
            out.print("{\"status\": \"success\", \"message\": \"Đăng nhập thành công\"}");
        } else {
            int attempts = dao.getAccountByName(name).getFailedAttempts();
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                out.print("{\"status\": \"locked\", \"message\": \"Tài khoản bị khóa 15 phút do đăng nhập sai quá 5 lần.\"}");
            } else {
                out.print("{\"status\": \"error\", \"message\": \"Sai mật khẩu. Còn " + (MAX_FAILED_ATTEMPTS - attempts) + " lần thử.\"}");
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