package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.services.InvoiceOrderServices;
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
        InvoiceOrderServices invoiceOrderServices = new InvoiceOrderServices(acc.getAccountId());
        OrderInvoice oi = invoiceOrderServices.getOrder(id);
        request.setAttribute("order", oi);


        request.getRequestDispatcher("/views/details.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}