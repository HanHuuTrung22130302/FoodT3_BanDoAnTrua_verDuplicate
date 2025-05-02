package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import hcmuaf.nlu.edu.vn.testproject.dto.IngredientDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetIngredientsBySupplier", value = "/getIngredientsBySupplier")
public class GetIngredientsBySupplier extends HttpServlet {
    private IngredientDAO ingredientDAO = new IngredientDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sid = request.getParameter("supplierId");
        int supplierId;

        // Xử lý trường hợp supplierId không hợp lệ
        try {
            if (sid == null || sid.trim().isEmpty()) {
                // Trả về danh sách rỗng nếu supplierId rỗng
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                new com.google.gson.Gson().toJson(new ArrayList<>(), response.getWriter());
                return;
            }
            supplierId = Integer.parseInt(sid);
        } catch (NumberFormatException e) {
            // Trả về lỗi 400 nếu supplierId không phải số
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid supplierId");
            return;
        }

        // Lấy danh sách nguyên liệu dưới dạng IngredientDTO
        List<IngredientDTO> ingredients = ingredientDAO.getIngredientsDTOBySupplierId(supplierId);
        System.out.println("Danh sách nguyên liệu DTO cho supplierId " + supplierId + ": " + ingredients);

        // Trả về dữ liệu dưới dạng JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new com.google.gson.Gson().toJson(ingredients, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}