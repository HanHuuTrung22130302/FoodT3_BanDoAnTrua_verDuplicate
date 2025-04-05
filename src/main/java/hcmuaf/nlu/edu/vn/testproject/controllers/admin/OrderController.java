package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.services.AdminInvoiceService;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrderController", value = "/ordermanagement")
public class OrderController extends HttpServlet {
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            logService.logActivity(0, 0, "Xem danh sách đơn hàng", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int pageSize = 10;
        int offset = (page - 1) * pageSize;
        List<OrderInvoice> ois = new ArrayList<>();
        int totalLs = 0;

        String option = request.getParameter("option");
        if (option == null || option.isEmpty()) {
            option = "all";
        }

        AdminInvoiceService adminInvoiceService = new AdminInvoiceService();
        ois = adminInvoiceService.getOption(option);
        totalLs = ois.size();

        if (offset < totalLs) {
            ois = ois.subList(Math.min(offset, totalLs), Math.min(offset + pageSize, totalLs));
        } else {
            ois = new ArrayList<>();
        }

        int totalPages = (int) Math.ceil((double) totalLs / pageSize);

        request.setAttribute("ois", ois);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentCategory", option);


        request.getRequestDispatcher("views/order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}