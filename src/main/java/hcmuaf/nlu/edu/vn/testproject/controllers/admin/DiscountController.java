package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import com.google.gson.Gson;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Discount;
import hcmuaf.nlu.edu.vn.testproject.services.DiscountService;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DiscountController", value = "/discount")
public class DiscountController extends HttpServlet {
    private DiscountService discountService = new DiscountService();
    private LogService logService = new LogService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        Account currentUser = (Account) request.getSession().getAttribute("currentUser");
        if (currentUser == null || currentUser.getRoleId() != 1) {
            logService.logActivity(0, 1, "Truy cập trang quản lý mã giảm giá", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Discount> discounts = discountService.getAllDiscounts();
        logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), 
                             "Xem danh sách mã giảm giá", "Thành công", 
                             "Đã xem danh sách " + discounts.size() + " mã giảm giá");
        
        request.setAttribute("discounts", discounts);
        request.getRequestDispatcher("views/discount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");
        Map<String, Object> jsonResponse = new HashMap<>();

        if (currentUser == null || currentUser.getRoleId() == 2) {
            logService.logActivity(0, 1, "Quản lý mã giảm giá", "Thất bại", "Không có quyền truy cập");
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Không có quyền truy cập");
            sendJsonResponse(response, jsonResponse);
            return;
        }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String codeName = request.getParameter("codeName");
            Double discountRate = Double.parseDouble(request.getParameter("discountRate"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            Date startDate = java.sql.Date.valueOf(request.getParameter("startDate"));
            Date endDate = java.sql.Date.valueOf(request.getParameter("endDate"));

            Discount discount = new Discount(0, codeName, discountRate / 100, title, description, startDate, endDate);
            
            int newDiscountId = discountService.addDiscount(discount);
            if (newDiscountId > 0) {
                discount.setDiscountCodeId(newDiscountId);
                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), 
                                     "Thêm mã giảm giá", "Thành công", 
                                     "Thêm mã giảm giá: " + codeName + ", Tỉ lệ: " + discountRate + "%");
                jsonResponse.put("success", true);
                jsonResponse.put("discount", discount);
            } else {
                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), 
                                     "Thêm mã giảm giá", "Thất bại", 
                                     "Thêm mã giảm giá: " + codeName + ", Tỉ lệ: " + discountRate + "%");
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Thêm mã giảm giá thất bại");
            }
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (discountService.deleteDiscount(id)) {
                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), 
                                     "Xóa mã giảm giá", "Thành công", 
                                     "Xóa mã giảm giá ID: " + id);
                jsonResponse.put("success", true);
            } else {
                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), 
                                     "Xóa mã giảm giá", "Thất bại", 
                                     "Xóa mã giảm giá ID: " + id);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Xóa mã giảm giá thất bại");
            }
        }

        sendJsonResponse(response, jsonResponse);
    }

    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> jsonResponse) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}