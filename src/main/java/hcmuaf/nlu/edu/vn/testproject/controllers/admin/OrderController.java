package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.services.AdminInvoiceService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrderController", value = "/ordermanagement")
public class OrderController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        HttpSession session = request.getSession();
//        Account currentUser = (Account) session.getAttribute("currentUser");
//
//        if (currentUser == null || currentUser.getIdRole() == 2) {
//            // Chuyển hướng về trang home nếu người dùng chưa đăng nhập
//            response.sendRedirect("home");
//            return;
//        }
//        AdminInvoiceService adminInvoiceService = new AdminInvoiceService();
//        String option = request.getParameter("option");
//        if (option == null) option="all";
//        List<OrderInvoice> ois = adminInvoiceService.getOption(option);
//        request.setAttribute("ois", ois);
//
//        request.getRequestDispatcher("views/order.jsp").forward(request, response);

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            // Chuyển hướng về trang home nếu người dùng chưa đăng nhập
            response.sendRedirect("home");
            return;
        }


        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        // Tính toán offset
        int pageSize = 10; // Kích thước trang
        int offset = (page - 1) * pageSize;
        List<OrderInvoice> ois = new ArrayList<>();
        int totalLs = 0;

        // Lấy giá trị option từ request
        String option = request.getParameter("option");
        if (option == null || option.isEmpty()) {
            option = "all"; // Mặc định là "Tất cả"
        }

        AdminInvoiceService adminInvoiceService = new AdminInvoiceService();
        if (option == null) option="all";
        ois = adminInvoiceService.getOption(option);

        totalLs = ois.size();

        // Áp dụng phân trang
        if (offset < totalLs) {
            ois = ois.subList(
                    Math.min(offset, totalLs),
                    Math.min(offset + pageSize, totalLs)
            );
        } else {
            ois = new ArrayList<>(); // Trả về danh sách rỗng nếu offset vượt tổng số món
        }

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalLs / pageSize);

        // Lấy danh sách danh mục

        // Đặt thuộc tính cho JSP
        request.setAttribute("ois", ois);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentCategory", option); // Truyền danh mục hiện tại

        // Chuyển tiếp đến JSP
        request.getRequestDispatcher("views/order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}