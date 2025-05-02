package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ImportIngredientController", value = "/ImportIngredientController")
public class ImportIngredientController extends HttpServlet {

    private IngredientDAO ingredientDAO;

    @Override
    public void init() throws ServletException {
        this.ingredientDAO = new IngredientDAO();
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

            // Thiết lập thông báo thành công
            successMessage = "Thêm nguyên liệu thành công!";
        } catch (NumberFormatException e) {
            errorMessage = "Giá trị nhập không hợp lệ!";
        } catch (SQLException e) {
            errorMessage = e.getMessage();
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