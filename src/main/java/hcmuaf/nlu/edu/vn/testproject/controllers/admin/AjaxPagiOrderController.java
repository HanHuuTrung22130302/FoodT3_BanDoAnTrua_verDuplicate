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
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AjaxPagiOrderController", value = "/ajaxpagiordermanagement")
public class AjaxPagiOrderController extends HttpServlet {
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");
        AdminInvoiceService adminInvoiceService = new AdminInvoiceService();

        if (!adminInvoiceService.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xem danh sách đơn hàng", "Thất bại", "Không có quyền truy cập");
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

        int totalLs = adminInvoiceService.countInvoicesByOption(option);
        int totalPages = (int) Math.ceil((double) totalLs / pageSize);

        PrintWriter out = response.getWriter();

// Nút < (previous)
        if (page > 1) {
            out.println("<div class='pagiOrder' onclick=\"tableOrder('" + option + "'," + (page - 1) + ");pagi('" + option + "'," + (page - 1) + ")\"><</div>");
        }

// Hiển thị "1 .." nếu currentPage > 3
        if (page > 3) {
            out.println("<div class='pagiOrder' onclick=\"tableOrder('" + option + "',1);pagi('" + option + "',1)\">1</div>");
            out.println("<div class='pagiOrder'>..</div>");
        }

// Hiển thị trang (page-1, page, page+1)
        for (int i = page - 1; i <= page + 1; i++) {
            if (i > 0 && i <= totalPages) {
                String activeClass = (i == page) ? "active" : "";
                out.println("<div class='pagiOrder " + activeClass + "' onclick=\"tableOrder('" + option + "'," + i + ");pagi('" + option + "'," + i + ")\">" + i + "</div>");
            }
        }

// Hiển thị ".. totalPages" nếu còn nhiều trang phía sau
        if (page < totalPages - 2) {
            out.println("<div class='pagiOrder'>..</div>");
            out.println("<div class='pagiOrder' onclick=\"tableOrder('" + option + "'," + totalPages + ");pagi('" + option + "'," + totalPages + ")\">" + totalPages + "</div>");
        }

// Nút > (next)
        if (page < totalPages) {
            out.println("<div class='pagiOrder' onclick=\"tableOrder('" + option + "'," + (page + 1) + ");pagi('" + option + "'," + (page + 1) + ")\">></div>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}