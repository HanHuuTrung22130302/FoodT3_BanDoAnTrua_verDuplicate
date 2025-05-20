package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import com.google.gson.Gson;
import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CategoryController", value = "/category")
public class CategoryController extends HttpServlet {
    private CategoryService categoryService = new CategoryService();
    private LogService logService = new LogService();
    private Gson gson = new Gson();
    private CheckUserDao checkUserDao = new CheckUserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 1, "Truy cập trang quản lý danh mục", "Thất bại", "Không có quyền truy cập");
            session.invalidate();
            response.sendRedirect("home");
            return;
        }

        List<Category> categories = categoryService.getCategories();
        logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), 
                             "Xem danh sách danh mục", "Thành công", 
                             "Đã xem danh sách " + categories.size() + " danh mục");
        
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/views/category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");
        Map<String, Object> jsonResponse = new HashMap<>();

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 1, "Quản lý danh mục", "Thất bại", "Không có quyền truy cập");
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Không có quyền truy cập");
            sendJsonResponse(response, jsonResponse);
            session.invalidate();
            return;
        }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String categoryName = request.getParameter("categoryName");
            String description = request.getParameter("description");
            
            // Kiểm tra category đã tồn tại chưa
            if (categoryService.isCategoryExists(categoryName)) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Danh mục đã tồn tại!");
                sendJsonResponse(response, jsonResponse);
                return;
            }
            
            Category category = new Category();
            category.setCategoryName(categoryName);
            category.setDescription(description);
            
            if (categoryService.addCategory(category)) {
                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Thêm danh mục", "Thành công", "Thêm danh mục: " + categoryName + ", Mô tả: " + description);
                jsonResponse.put("success", true);
                jsonResponse.put("category", category);
            } else {
                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Thêm danh mục", "Thất bại", "Thêm danh mục: " + categoryName + ", Mô tả: " + description);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Thêm danh mục thất bại");
            }
        } else if ("delete".equals(action)) {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            Category category = categoryService.getCategoryById(categoryId);
            
            if (categoryService.deleteCategory(categoryId)) {
                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xóa danh mục", "Thành công", "Xóa danh mục: " + category.getCategoryName() + " (ID: " + categoryId + ")");
                jsonResponse.put("success", true);
            } else {
                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xóa danh mục", "Thất bại", "Xóa danh mục: " + category.getCategoryName() + " (ID: " + categoryId + ")");
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Xóa danh mục thất bại");
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