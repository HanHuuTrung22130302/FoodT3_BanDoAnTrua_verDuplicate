package hcmuaf.nlu.edu.vn.testproject.controllers.user;


import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.LoginDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Item;
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
import java.util.List;

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

        String username = request.getParameter("user");
        String password = request.getParameter("pass");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            sendErrorResponse(out, request, response, "Tên đăng nhập/Email và mật khẩu không được để trống");
            return;
        }

        LoginDAO dao = new LoginDAO();
        Account account = null;
        boolean isEmail = username.contains("@");

        if (isEmail) {
            account = dao.getAccountByEmail(username);
        } else {
            account = dao.getAccountByName(username);
        }

        if (account == null) {
            sendErrorResponse(out, request, response, "Tài khoản không tồn tại");
            return;
        }

        if (account.isDeleted()) {
            out.print("{\"status\": \"error\", \"message\": \"Tài khoản của bạn đã bị chặn\"}");
            return;
        }

        if (account.isLocked()) {
            long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), account.getLockTime().plusMinutes(LOCK_DURATION_MINUTES));
            if (minutesLeft > 0) {
                out.print("{\"status\": \"locked\", \"message\": \"Tài khoản bị khóa. Vui lòng thử lại sau " + minutesLeft + " phút.\"}");
                return;
            } else {
                dao.resetFailedAttempts(account.getAccountId());
                account.setLocked(false);
                account.setLockTime(null);
            }
        }

        if (isEmail) {
            account = dao.loginByEmail(username, password);
        } else {
            account = dao.login(username, password);
        }

        if (account != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", account);

            // Lấy dữ liệu giỏ hàng và tính totalItems
            FoodCartDAO cartDAO = new FoodCartDAO();
            List<Item> cartItems = cartDAO.getCartItems(account.getAccountId());
            int totalItems = 0;
            for (Item item : cartItems) {
                totalItems += item.getQuantity();
            }
            session.setAttribute("totalItems", totalItems);

            out.print("{\"status\": \"success\", \"message\": \"Đăng nhập thành công\"}");
        } else {
            Account failedAccount = isEmail ? dao.getAccountByEmail(username) : dao.getAccountByName(username);
            if (failedAccount != null) {
                int attempts = failedAccount.getFailedAttempts();
                if (attempts >= MAX_FAILED_ATTEMPTS) {
                    out.print("{\"status\": \"locked\", \"message\": \"Tài khoản bị khóa 15 phút do đăng nhập sai quá 5 lần.\"}");
                } else {
                    out.print("{\"status\": \"error\", \"message\": \"Sai mật khẩu. Còn " + (MAX_FAILED_ATTEMPTS - attempts) + " lần thử.\"}");
                }
            } else {
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

