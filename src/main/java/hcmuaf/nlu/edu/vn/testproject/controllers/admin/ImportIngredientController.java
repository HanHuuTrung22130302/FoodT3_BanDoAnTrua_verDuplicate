package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ImportIngredientController", value = "/ImportIngredientController")
public class ImportIngredientController extends HttpServlet {

    private IngredientDAO ingredientDAO;
    private LogService logService;
    private CheckUserDao checkUserDao = new CheckUserDao();

    @Override
    public void init() throws ServletException {
        this.ingredientDAO = new IngredientDAO();
        this.logService = new LogService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String supplierIdStr = request.getParameter("supplierId");
        String ingredientIdStr = request.getParameter("ingredientId");
        String amountStr = request.getParameter("amount");
        String priceStr = request.getParameter("price");
        String importDate = request.getParameter("importDate");
        String expirationDate = request.getParameter("expirationDate");

        String errorMessage = null;
        String successMessage = null;
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 1, "Truy cập trang quản lý danh mục", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        try {
            // Chuyển đổi dữ liệu
            int supplierId = Integer.parseInt(supplierIdStr);
            int ingredientId = Integer.parseInt(ingredientIdStr);
            double amount = Double.parseDouble(amountStr);
            double price = Double.parseDouble(priceStr);

            // Lấy ingredient_name và supplier_name từ DAO
            String ingredientName = ingredientDAO.getIngredientNameById(ingredientId);
            if (ingredientName == null) {
                throw new SQLException("Không tìm thấy nguyên liệu với ID: " + ingredientId);
            }
            String supplierName = ingredientDAO.getSupplierNameById(supplierId);
            if (supplierName == null) {
                throw new SQLException("Không tìm thấy nhà cung cấp với ID: " + supplierId);
            }

            // Thêm nguyên liệu mới
            ingredientDAO.addIngredient(supplierId, supplierName, ingredientName, amount, price, importDate, expirationDate);

            // Ghi log nhập nguyên liệu thành công
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Nhập nguyên liệu",
                "Thành công",
                "Nhập " + amount + "kg " + ingredientName + " từ " + supplierName
            );

            // Thiết lập thông báo thành công
            successMessage = "Thêm nguyên liệu thành công!";
        } catch (NumberFormatException e) {
            errorMessage = "Giá trị nhập không hợp lệ!";
            // Ghi log lỗi nhập nguyên liệu
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Nhập nguyên liệu",
                "Thất bại",
                "Lỗi: Giá trị nhập không hợp lệ"
            );
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            // Ghi log lỗi nhập nguyên liệu
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Nhập nguyên liệu",
                "Thất bại",
                "Lỗi: " + e.getMessage()
            );
        }

        // Thêm thông báo vào request
        if (successMessage != null) {
            request.setAttribute("success", successMessage);
        }
        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
        }

        // Forward yêu cầu đến lại trang "Ingredients.jsp" mà không redirect
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Ingredients");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng đến trang Ingredients nếu không có yêu cầu POST
        response.sendRedirect(request.getContextPath() + "/Ingredients");
    }

}