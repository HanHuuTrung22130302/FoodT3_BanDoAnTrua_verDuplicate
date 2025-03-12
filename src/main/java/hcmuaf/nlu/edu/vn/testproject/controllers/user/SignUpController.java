package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.SignUpDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SignUpController", value = "/signup")
public class SignUpController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8"); // Đảm bảo trả về JSON

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("pass");

        PrintWriter out = response.getWriter();

        // Kiểm tra nếu có thông tin thiếu
        if (name == null || password == null || email == null || name.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            out.print("{\"status\": \"error\", \"message\": \"Vui lòng nhập đầy đủ thông tin\"}");
            return;
        }

        Account account = SignUpDAO.checkUserExist(name, email);
        // Nếu chưa có tên người dùng hoặc email đã tồn tại
        if (account == null) {
            // Đăng ký người dùng mới
            SignUpDAO.signUp(name, password, email);
            out.print("{\"status\": \"success\", \"message\": \"Đăng ký thành công, vui lòng đăng nhập.\"}");
        } else {
            String errorMessage;
            if (account.getName().equals(name)) {
                errorMessage = "Tên người dùng đã tồn tại.";
            } else {
                errorMessage = "Email đã được sử dụng.";
            }

            out.print("{\"status\": \"error\", \"message\": \"" + errorMessage + "\"}");
        }
    }
}
