package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryController", value = "/category")
public class CategoryController extends HttpServlet {
    private CategoryService categoryService = new CategoryService();
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            logService.logActivity(0, 0, "Xem danh sách category", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        List<Category> categories = categoryService.getCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("views/category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            logService.logActivity(0, 0, "Quản lý category", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String categoryName = request.getParameter("categoryName");
            String description = request.getParameter("description");
            
            Category category = new Category();
            category.setCategoryName(categoryName);
            category.setDescription(description);
            
            if (categoryService.addCategory(category)) {
                logService.logActivity(currentUser.getAccountId(), 0, "Thêm danh mục", "Thành công", "Thêm danh mục: " + categoryName);
                request.setAttribute("message", "Thêm danh mục thành công!");
            } else {
                logService.logActivity(currentUser.getAccountId(), 0, "Thêm danh mục", "Thất bại", "Thêm danh mục: " + categoryName);
                request.setAttribute("error", "Thêm danh mục thất bại!");
            }
        } else if ("delete".equals(action)) {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            Category category = categoryService.getCategoryById(categoryId);
            
            if (categoryService.deleteCategory(categoryId)) {
                logService.logActivity(currentUser.getAccountId(), 0, "Xóa danh mục", "Thành công", "Xóa danh mục: " + category.getCategoryName());
                request.setAttribute("message", "Xóa danh mục thành công!");
            } else {
                logService.logActivity(currentUser.getAccountId(), 0, "Xóa danh mục", "Thất bại", "Xóa danh mục: " + category.getCategoryName());
                request.setAttribute("error", "Xóa danh mục thất bại!");
            }
        }
        
        // Load lại danh sách category
        List<Category> categories = categoryService.getCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("views/category.jsp").forward(request, response);
    }
}