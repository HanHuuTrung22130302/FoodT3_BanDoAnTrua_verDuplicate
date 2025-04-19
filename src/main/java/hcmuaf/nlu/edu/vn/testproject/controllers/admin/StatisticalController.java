package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.FoodDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.InvoiceDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.time.YearMonth;
import java.util.LinkedHashMap;

@WebServlet(name = "StatisticalController", value = "/statistical")
public class StatisticalController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        InvoiceDAO invoiceDAO = new InvoiceDAO();
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        
        // Lấy tham số thời gian
        String timeFilter = request.getParameter("timeFilter");
        if (timeFilter == null || timeFilter.isEmpty()) {
            timeFilter = "day"; // Mặc định là theo ngày
        }

        // Xử lý dữ liệu cho card tổng quan theo thời gian được chọn
        int totalProducts = 0;
        int totalQuantity = 0;
        int totalRevenue = 0;
        int totalOrders = 0;

        switch (timeFilter) {
            case "day":
                totalRevenue = invoiceDAO.getRevenueByDay();
                totalOrders = invoiceDAO.getOrderCountByDay();
                invoiceDetails = invoiceDAO.getInvoiceDetailsByDay();
                break;
            case "week":
                totalRevenue = invoiceDAO.getRevenueByWeek();
                totalOrders = invoiceDAO.getOrderCountByWeek();
                invoiceDetails = invoiceDAO.getInvoiceDetailsByWeek();
                break;
            case "month":
                totalRevenue = invoiceDAO.getRevenueByMonth();
                totalOrders = invoiceDAO.getOrderCountByMonth();
                invoiceDetails = invoiceDAO.getInvoiceDetailsByMonth();
                break;
        }

        // Tính toán tổng số sản phẩm và số lượng bán ra
        totalProducts = invoiceDetails.size();
        for (InvoiceDetail detail : invoiceDetails) {
            totalQuantity += detail.getQuantity();
        }

        // Xử lý dữ liệu cho xu hướng 12 tháng gần nhất
        YearMonth currentMonth = YearMonth.now();
        List<YearMonth> last12Months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            last12Months.add(currentMonth.minusMonths(i));
        }

        Map<YearMonth, Integer> revenueStats = new LinkedHashMap<>();
        Map<YearMonth, Integer> orderStats = new LinkedHashMap<>();

        for (YearMonth month : last12Months) {
            int revenue = invoiceDAO.getRevenueBySpecificMonth(month.getYear(), month.getMonthValue());
            int orders = invoiceDAO.getOrderCountBySpecificMonth(month.getYear(), month.getMonthValue());
            revenueStats.put(month, revenue);
            orderStats.put(month, orders);
        }

        // Xử lý tìm kiếm
        String txtSearch = request.getParameter("text");
        if (txtSearch != null && !txtSearch.isEmpty()) {
            invoiceDetails = invoiceDAO.searchByNameAndTime(txtSearch, timeFilter);
            // Cập nhật lại các số liệu tổng quan khi tìm kiếm
            totalProducts = invoiceDetails.size();
            totalQuantity = 0;
            totalRevenue = 0;
            totalOrders = invoiceDAO.getOrderCountBySearch(txtSearch, timeFilter);
            for (InvoiceDetail detail : invoiceDetails) {
                totalQuantity += detail.getQuantity();
                totalRevenue += detail.getTotalAmount();
            }
        }

        // Sắp xếp sản phẩm theo số lượng bán giảm dần
        invoiceDetails.sort(Comparator.comparingInt(InvoiceDetail::getQuantity).reversed());

        // Lấy top 5 sản phẩm bán chạy nhất
        List<InvoiceDetail> bestSellingProducts = new ArrayList<>();
        for (int i = 0; i < Math.min(5, invoiceDetails.size()); i++) {
            bestSellingProducts.add(invoiceDetails.get(i));
        }

        // Lấy sản phẩm bán chậm và chưa bán được theo thời gian
        List<InvoiceDetail> slowSellingProducts = invoiceDAO.getSlowSellingProductsByTime(timeFilter);
        List<Food> unsoldProducts = invoiceDAO.getUnsoldProductsByTime();
        request.setAttribute("unsoldProducts", unsoldProducts);

        // Set attributes
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalQuantity", totalQuantity);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("timeFilter", timeFilter);
        request.setAttribute("search", txtSearch);
        request.setAttribute("last12Months", last12Months);
        request.setAttribute("revenueStats", revenueStats);
        request.setAttribute("orderStats", orderStats);
        request.setAttribute("bestSellingProducts", bestSellingProducts);
        request.setAttribute("slowSellingProducts", slowSellingProducts);
        request.setAttribute("unsoldProducts", unsoldProducts);
        request.getRequestDispatcher("/views/statistical.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}