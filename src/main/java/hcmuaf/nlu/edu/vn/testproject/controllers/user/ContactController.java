package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.ContactService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ContactControlle", value = "/contactcontrolle")
public class ContactController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        CategoryService cs = new CategoryService();
        List<Category> categoryList = cs.getCategories();
        request.setAttribute("listC", categoryList);
        request.getRequestDispatcher("views/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("currentUser");
        int id =1;
        if (account != null)
        id = account.getAccountId();
        String name = request.getParameter("name");
        if(name == null || name.isEmpty())name="";
        String email = request.getParameter("email");
        if(email == null || email.isEmpty())email="";
        String title = request.getParameter("title");
        if(title == null || title.isEmpty())title="";
        String message = request.getParameter("message");
        if(message == null || message.isEmpty())message="";
        ContactService cs = new ContactService();
        cs.sendContactSV(id, name, email, title, message);
        response.sendRedirect("contactcontrolle");

    }
}