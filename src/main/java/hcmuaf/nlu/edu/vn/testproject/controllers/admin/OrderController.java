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
        AdminInvoiceService adminInvoiceService = new AdminInvoiceService();


        if (!adminInvoiceService.isAdmin(currentUser.getAccountId()) || currentUser == null) {
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xem danh sách đơn hàng", "Thất bại", "Không có quyền truy cập");
            session.invalidate();
            response.sendRedirect("home");
            return;
        }

        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int pageSize = 12; // DAO đã giới hạn 12 bản ghi rồi
        int offset = (page - 1) * pageSize;

        String option = request.getParameter("option");
        if (option == null || option.isEmpty()) {
            option = "all";
        }


        List<OrderInvoice> ois = adminInvoiceService.getOption(option, offset);

        int totalLs = adminInvoiceService.countInvoicesByOption(option);
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