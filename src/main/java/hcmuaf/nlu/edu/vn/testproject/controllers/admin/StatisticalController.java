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
        int totalProducts = 0;
        int totalQuantity = 0;
        int totalPrice = 0;

        // Lấy tham số thời gian
        String timeFilter = request.getParameter("timeFilter");
        if (timeFilter == null || timeFilter.isEmpty()) {
            timeFilter = "day"; // Mặc định là theo ngày
        }
        
        // Thống kê doanh thu theo thời gian
        Map<String, Integer> revenueStats = new HashMap<>();
        int dayRevenue = invoiceDAO.getRevenueByDay();
        int weekRevenue = invoiceDAO.getRevenueByWeek();
        int monthRevenue = invoiceDAO.getRevenueByMonth();
        
        revenueStats.put("day", dayRevenue);
        revenueStats.put("week", weekRevenue);
        revenueStats.put("month", monthRevenue);

        // Thống kê số đơn hàng theo thời gian
        Map<String, Integer> orderStats = new HashMap<>();
        int dayOrders = invoiceDAO.getOrderCountByDay();
        int weekOrders = invoiceDAO.getOrderCountByWeek();
        int monthOrders = invoiceDAO.getOrderCountByMonth();
        
        orderStats.put("day", dayOrders);
        orderStats.put("week", weekOrders);
        orderStats.put("month", monthOrders);

        // Lấy dữ liệu chi tiết hóa đơn theo thời gian đã chọn
        String txtSearch = request.getParameter("text");
        if (txtSearch != null && !txtSearch.isEmpty()) {
            invoiceDetails = invoiceDAO.searchByName(txtSearch);
        } else {
            switch (timeFilter) {
                case "day":
                    invoiceDetails = invoiceDAO.getInvoiceDetailsByDay();
                    break;
                case "week":
                    invoiceDetails = invoiceDAO.getInvoiceDetailsByWeek();
                    break;
                case "month":
                    invoiceDetails = invoiceDAO.getInvoiceDetailsByMonth();
                    break;
                default:
                    invoiceDetails = invoiceDAO.getInvoiceDetails();
            }
        }
        
        // Tính toán tổng số lượng và doanh thu từ danh sách chi tiết hóa đơn
        totalProducts = invoiceDetails.size();
        totalQuantity = 0;
        totalPrice = 0;

        for (InvoiceDetail invoiceDetail : invoiceDetails) {
            if (invoiceDetail.getQuantity() > 0) { // Chỉ tính các sản phẩm có số lượng > 0
                totalQuantity += invoiceDetail.getQuantity();
                totalPrice += invoiceDetail.getTotalAmount();
            }
        }

        // Tính tổng số lượng bán của tất cả sản phẩm
        int totalQuantityAll = invoiceDetails.stream()
                .mapToInt(InvoiceDetail::getQuantity)
                .sum();

        // Tính tỷ lệ bán cho mỗi sản phẩm
        for (InvoiceDetail product : invoiceDetails) {
            double percentage = (double) product.getQuantity() / totalQuantityAll * 100;
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

        request.setAttribute("invoiceDetails", invoiceDetails);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalQuantity", totalQuantity);
        request.setAttribute("totalRevenue", totalPrice);
        request.setAttribute("search", txtSearch);
        request.setAttribute("timeFilter", timeFilter);
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