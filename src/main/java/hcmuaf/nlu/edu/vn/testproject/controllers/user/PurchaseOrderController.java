package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.InvoiceOrderServices;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PurchaseOrderController", value = "/PurchaseOrder")
public class PurchaseOrderController extends HttpServlet {
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("currentUser");
        InvoiceOrderServices invoiceOrderServices = new InvoiceOrderServices();
        int userId = acc.getAccountId();
        int offset = 0;
        if (request.getParameter("offset") != null) {
            offset = Integer.parseInt(request.getParameter("offset"));
            System.out.println(offset);
        }
        System.out.println(offset);
        if (acc == null)
            response.sendRedirect("home");

        CategoryService cs = new CategoryService();
        List<Category> categoryList = cs.getCategories();
        request.setAttribute("listC", categoryList);

        String id = request.getParameter("id");


        String optionOrder = "0";
        if (optionOrder == null)
            optionOrder = request.getParameter("optionOrder");

        List<OrderInvoice> ois = invoiceOrderServices.getOptionInvoice(userId,optionOrder,offset);
        request.setAttribute("ois", ois);

        request.getRequestDispatcher("views/PurchaseOrder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}