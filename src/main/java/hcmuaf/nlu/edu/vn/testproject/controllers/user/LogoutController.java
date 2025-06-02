package hcmuaf.nlu.edu.vn.testproject.controllers.user;


import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;


@WebServlet(name = "LogoutController", value = "/logout")
public class LogoutController extends HttpServlet {
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
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");


        if (currentUser != null) {
            // Ghi log đăng xuất
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Đăng xuất",
                "Thành công",
                "Đăng xuất khỏi hệ thống"
            );
        }


        session.removeAttribute("currentUser");
        session.removeAttribute("order");
        session.invalidate();
        response.sendRedirect("home");
    }
}

