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
import java.util.Set;
import java.util.stream.Collectors;
import java.time.YearMonth;
import java.util.LinkedHashMap;

@WebServlet(name = "StatisticalController", value = "/statistical")
public class StatisticalController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        InvoiceDAO invoiceDAO = new InvoiceDAO();
        FoodDAO foodDAO = new FoodDAO();
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        
        // Lấy tham số thời gian
        String timeFilter = request.getParameter("timeFilter");
        if (timeFilter == null || timeFilter.isEmpty()) {
            timeFilter = "month"; // Mặc định là theo tháng
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

        // Xử lý tìm kiếm nếu có
        String txtSearch = request.getParameter("text");
        if (txtSearch != null && !txtSearch.isEmpty()) {
            invoiceDetails = invoiceDAO.searchByNameAndTime(txtSearch, timeFilter);
            // Cập nhật lại các số liệu tổng quan khi tìm kiếm
            totalProducts = invoiceDetails.size();
            totalQuantity = 0;
            totalRevenue = 0;
            for (InvoiceDetail detail : invoiceDetails) {
                totalQuantity += detail.getQuantity();
                totalRevenue += detail.getTotalAmount();
            }
        }

        // Tính tổng số lượng bán của tất cả sản phẩm
        int totalQuantityAll = invoiceDetails.stream()
                .mapToInt(InvoiceDetail::getQuantity)
                .sum();

        // Tính tỷ lệ bán cho mỗi sản phẩm
        for (InvoiceDetail product : invoiceDetails) {
            double percentage = totalQuantityAll > 0 ? 
                (double) product.getQuantity() / totalQuantityAll * 100 : 0;
            product.setSalesPercentage(Math.round(percentage * 100.0) / 100.0);
        }

        // Sắp xếp sản phẩm theo số lượng bán giảm dần
        invoiceDetails.sort(Comparator.comparingInt(InvoiceDetail::getQuantity).reversed());

        // Lấy top 5 sản phẩm bán chạy nhất
        List<InvoiceDetail> bestSellingProducts = new ArrayList<>();
        for (int i = 0; i < Math.min(5, invoiceDetails.size()); i++) {
            bestSellingProducts.add(invoiceDetails.get(i));
        }

        // Lấy 5 sản phẩm bán chậm nhất (có số lượng bán > 0)
        List<InvoiceDetail> slowSellingProducts = new ArrayList<>();
        for (int i = Math.max(0, invoiceDetails.size() - 5); i < invoiceDetails.size(); i++) {
            if (invoiceDetails.get(i).getQuantity() > 0) {
                slowSellingProducts.add(invoiceDetails.get(i));
            }
        }

        // Lấy danh sách sản phẩm không bán được (số lượng bán = 0)
        Set<Integer> soldFoodIds = invoiceDetails.stream()
                .map(detail -> detail.getFood().getFoodId())
                .collect(Collectors.toSet());
        
        List<Food> allFoods = foodDAO.getAllFoods();
        List<Food> unsoldProducts = allFoods.stream()
                .filter(food -> !soldFoodIds.contains(food.getFoodId()))
                .collect(Collectors.toList());

        // Tạo dữ liệu JSON cho biểu đồ
        Gson gson = new Gson();
        
        List<Map<String, Object>> bestSellingData = bestSellingProducts.stream()
                .map(product -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", product.getFood().getFoodName());
                    data.put("quantity", product.getQuantity());
                    return data;
                })
                .collect(Collectors.toList());
        
        List<Map<String, Object>> slowSellingData = slowSellingProducts.stream()
                .map(product -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", product.getFood().getFoodName());
                    data.put("quantity", product.getQuantity());
                    return data;
                })
                .collect(Collectors.toList());
        
        List<Map<String, Object>> unsoldData = unsoldProducts.stream()
                .map(product -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", product.getFoodName());
                    data.put("category", product.getCategory().getCategoryName());
                    return data;
                })
                .collect(Collectors.toList());

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
        request.setAttribute("bestSellingProductsJson", gson.toJson(bestSellingData));
        request.setAttribute("slowSellingProductsJson", gson.toJson(slowSellingData));
        request.setAttribute("unsoldProductsJson", gson.toJson(unsoldData));
        request.getRequestDispatcher("/views/statistical.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}