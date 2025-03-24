package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.InvoiceDetail;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "StatisticalController", value = "/statistical")
public class StatisticalController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            // Chuyển hướng về trang home nếu người dùng chưa đăng nhập
            response.sendRedirect("home");
            return;
        }

        InvoiceDAO dao = new InvoiceDAO();
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        int totalProducts = 0;


        int totalQuantity = 0;
        int totalPrice = 0;

        String txtSearch = request.getParameter("text");
        if (txtSearch != null && !txtSearch.isEmpty()) {
            invoiceDetails = dao.searchByName(txtSearch);
        } else {
            invoiceDetails = dao.getInvoiceDetails();
            totalProducts = invoiceDetails.size();
            // Tính số lượng món ăn và tổng giá
            for (InvoiceDetail invoiceDetail : invoiceDetails) {
                totalQuantity += invoiceDetail.getQuantity();
                totalPrice += invoiceDetail.getTotalAmount();

            }
        }

        request.setAttribute("invoiceDetails", invoiceDetails);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalQuantity", totalQuantity);
        request.setAttribute("totalRevenue", totalPrice);
        request.setAttribute("search", txtSearch);
        request.getRequestDispatcher("views/statistical.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}