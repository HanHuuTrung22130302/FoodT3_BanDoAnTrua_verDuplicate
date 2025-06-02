package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ExportIngredientController", value = "/ExportIngredientController")
public class ExportIngredientController extends HttpServlet {

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
        String ingredientIdStr = request.getParameter("ingredientId");
        String usedAmountStr = request.getParameter("usedAmount");
        String usedDate = request.getParameter("usedDate");

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
            int ingredientId = Integer.parseInt(ingredientIdStr);
            double usedAmount = Double.parseDouble(usedAmountStr);

            if (usedAmount <= 0) {
                throw new IllegalArgumentException("Số lượng sử dụng phải lớn hơn 0!");
            }

            // Lấy tên nguyên liệu
            String ingredientName = ingredientDAO.getIngredientNameById(ingredientId);

            // Cập nhật số lượng trong kho
            boolean updated = ingredientDAO.updateIngredientAmount(ingredientId, usedAmount);
            if (!updated) {
                throw new SQLException("Số lượng trong kho không đủ hoặc nguyên liệu không tồn tại!");
            }

            // Ghi lịch sử sử dụng
            ingredientDAO.insertUsedIngredient(ingredientId, usedAmount, usedDate);

            // Ghi log xuất nguyên liệu thành công
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Xuất nguyên liệu",
                "Thành công",
                "Xuất " + usedAmount + "kg " + ingredientName
            );

            successMessage = "Xuất nguyên liệu thành công!";
        } catch (NumberFormatException e) {
            errorMessage = "Giá trị nhập không hợp lệ!";
            // Ghi log lỗi xuất nguyên liệu
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Xuất nguyên liệu",
                "Thất bại",
                "Lỗi: Giá trị nhập không hợp lệ"
            );
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
            // Ghi log lỗi xuất nguyên liệu
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Xuất nguyên liệu",
                "Thất bại",
                "Lỗi: " + e.getMessage()
            );
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            // Ghi log lỗi xuất nguyên liệu
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Xuất nguyên liệu",
                "Thất bại",
                "Lỗi: " + e.getMessage()
            );
        }

        // Chuyển tiếp thông báo
        if (successMessage != null) {
            request.setAttribute("success", successMessage);
        }
        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
        }

        // Forward về trang Ingredients
        request.getRequestDispatcher("/Ingredients").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/Ingredients");
    }
}