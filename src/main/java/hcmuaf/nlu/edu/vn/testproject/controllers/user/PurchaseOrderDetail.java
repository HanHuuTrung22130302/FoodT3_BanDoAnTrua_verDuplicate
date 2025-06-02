package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.InvoiceOrderServices;
import hcmuaf.nlu.edu.vn.testproject.services.ReviewService;
import hcmuaf.nlu.edu.vn.testproject.services.ReviewServiceByUser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PurchaseOrderDetail", value = "/PurchaseOrderDetail")
public class PurchaseOrderDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String id = request.getParameter("id");
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("currentUser");
        InvoiceOrderServices invoiceOrderServices = new InvoiceOrderServices();

        ReviewServiceByUser rs = new ReviewServiceByUser();


        OrderInvoice oi = invoiceOrderServices.getOrderInvoice(id);
        int check = 0;
        if (rs.checkReview(Integer.parseInt(id)))
            check = 1;
        oi.setIsReview(check);
        System.out.println(check);
        request.setAttribute("order", oi);

        CategoryService cs = new CategoryService();
        List<Category> categoryList = cs.getCategories();
        request.setAttribute("listC", categoryList);


        request.getRequestDispatcher("/views/details.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}