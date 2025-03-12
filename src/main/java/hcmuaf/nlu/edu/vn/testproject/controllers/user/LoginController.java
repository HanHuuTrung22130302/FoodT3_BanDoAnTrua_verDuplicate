package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.daos.LoginDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {

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
        String name = request.getParameter("user");
        String password = request.getParameter("pass");

        PrintWriter out = response.getWriter();

        if (name == null || password == null || name.trim().isEmpty() || password.trim().isEmpty()) {
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                out.print("{\"status\": \"error\", \"message\": \"Tên người dùng và mật khẩu không được để trống\"}");
            } else {
                request.setAttribute("error", "Tên người dùng và mật khẩu không được để trống");
                request.getRequestDispatcher("views/signin.jsp").forward(request, response);
            }
            return;
        }

        LoginDAO dao = new LoginDAO();
        Account account = dao.login(name, password);

        if (account == null) {
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                out.print("{\"status\": \"error\", \"message\": \"Đăng nhập không thành công\"}");
            } else {
                request.setAttribute("error", "Đăng nhập không thành công");
                request.getRequestDispatcher("views/signin.jsp").forward(request, response);
            }
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", account);

            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                out.print("{\"status\": \"success\", \"message\": \"Đăng nhập thành công\"}");
            } else {
                response.sendRedirect("home");
            }
        }
    }

}