package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.VerificationDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "VerificationController", value = "/VerificationController")
public class VerificationController extends HttpServlet {
    private final VerificationDAO verificationDAO = new VerificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null && token.trim().isEmpty()) {
            request.setAttribute("message", "Token không hợp lệ.");
            request.getRequestDispatcher("/verification.jsp").forward(request, response);
            return;
        }

        boolean isValid = verificationDAO.verifyToken(token);
        if (isValid) {
            request.setAttribute("message", "Xác thực email thành công! Bạn có thể đăng nhập.");
        } else {
            request.setAttribute("message", "Phiên làm việc đã hết. Vui lòng thực hiện lại!");
        }
        request.getRequestDispatcher("/verification.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}