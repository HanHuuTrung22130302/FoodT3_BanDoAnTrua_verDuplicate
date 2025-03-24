package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Discount;
import hcmuaf.nlu.edu.vn.testproject.services.DiscountService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "DiscountController", value = "/discount")
public class DiscountController extends HttpServlet {

    DiscountService discountService = new DiscountService();


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

        List<Discount> discounts = discountService.getAllDiscounts();
        request.setAttribute("discounts", discounts);
        request.getRequestDispatcher("views/discount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String codeName = request.getParameter("codeName");
            Double discountRate = Double.parseDouble(request.getParameter("discountRate"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            Date startDate = java.sql.Date.valueOf(request.getParameter("startDate"));
            Date endDate = java.sql.Date.valueOf(request.getParameter("endDate"));

            Discount discount = new Discount(0, codeName, discountRate / 100, title, description, startDate, endDate);

            if (discountService.addDiscount(discount)) {
                response.sendRedirect("discount?success=add");
            } else {
                response.sendRedirect("discount?error=add");
            }
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (discountService.deleteDiscount(id)) {
                response.sendRedirect("discount?success=delete");
            } else {
                response.sendRedirect("discount?error=delete");
            }
        }
    }
}