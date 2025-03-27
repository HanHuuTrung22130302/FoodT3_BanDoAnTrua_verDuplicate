package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.LoginDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/signin.jsp").forward(request, response);
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
        String captchaResponse = request.getParameter("g-recaptcha-response");

        if (name == null || password == null || name.trim().isEmpty() || password.trim().isEmpty()) {
            sendJsonResponse(out, "error", "Tên người dùng và mật khẩu không được để trống", null);
            return;
        }

        LoginDAO dao = new LoginDAO();
        Account account = dao.getAccountByName(name);

        if (account == null) {
            sendJsonResponse(out, "error", "Tên người dùng không tồn tại", null);
            return;
        }

        if (account.isLocked()) {
            sendJsonResponse(out, "error", "Tài khoản của bạn đã bị khóa. Vui lòng mở khóa qua email.", null);
            return;
        }

        if (account.getFailedAttempts() >= LoginDAO.getCaptchaThreshold()) {
            if (captchaResponse == null || captchaResponse.isEmpty()) {
                sendJsonResponse(out, "error", "Vui lòng xác minh CAPTCHA", null);
                return;
            }
            if (!verifyCaptcha(captchaResponse)) {
                sendJsonResponse(out, "error", "Xác minh CAPTCHA thất bại", null);
                return;
            }
        }

        account = dao.login(name, password);
        if (account == null) {
            Account updatedAccount = dao.getAccountByName(name);
            if (updatedAccount == null) {
                sendJsonResponse(out, "error", "Tên người dùng không tồn tại", null);
                return;
            }
            if (updatedAccount.isLocked()) {
                sendJsonResponse(out, "error", "Tài khoản đã bị khóa do nhập sai quá nhiều lần. Vui lòng mở khóa qua email.", null);
            } else {
                int remainingAttempts = LoginDAO.getMaxFailedAttempts() - updatedAccount.getFailedAttempts();
                sendJsonResponse(out, "error", "Mật khẩu không đúng. Số lần thử còn lại: " + remainingAttempts, null);
            }
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", account);
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                sendJsonResponse(out, "success", "Đăng nhập thành công", "home");
            } else {
                response.sendRedirect("home");
            }
        }
        out.close();
    }

    private void sendJsonResponse(PrintWriter out, String status, String message, String redirect) {
        String json = "{\"status\": \"" + status + "\", \"message\": \"" + message + "\"";
        if (redirect != null) {
            json += ", \"redirect\": \"" + redirect + "\"";
        }
        json += "}";
        out.print(json);
    }

    private boolean verifyCaptcha(String captchaResponse) {
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";
            String secretKey = "YOUR_RECAPTCHA_SECRET_KEY"; // Thay bằng Secret Key của bạn
            String params = "secret=" + secretKey + "&response=" + captchaResponse;

            java.net.URL obj = new java.net.URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            java.io.DataOutputStream wr = new java.io.DataOutputStream(con.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            while (in.readLine() != null) {
                response.append(in.readLine());
            }
            in.close();

            com.google.gson.JsonObject jsonObject = com.google.gson.JsonParser.parseString(response.toString()).getAsJsonObject();
            return jsonObject.get("success").getAsBoolean();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}