package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import hcmuaf.nlu.edu.vn.testproject.dto.IngredientDTO;
import hcmuaf.nlu.edu.vn.testproject.models.Ingredients;
import hcmuaf.nlu.edu.vn.testproject.models.Supplier;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

@WebServlet(name = "IngredientsController", value = "/Ingredients")
public class IngredientsController extends HttpServlet {

    private IngredientDAO ingredientDAO;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        this.ingredientDAO = new IngredientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String filter = request.getParameter("filter");
        List<Ingredients> ingredientsList;
        List<Supplier> supplierList;

        try {
            if ("nearlyExpired".equals(filter)) {
                ingredientsList = ingredientDAO.getNearlyExpiredIngredients();
            } else {
                ingredientsList = ingredientDAO.getAllIngredients();
            }
            supplierList = ingredientDAO.getAllSuppliers();

            // Lấy danh sách nguyên liệu cho từng supplierId
            Map<Integer, List<IngredientDTO>> ingredientsBySupplier = new HashMap<>();
            for (Supplier s : supplierList) {
                int supplierId = s.getSupplierId();
                List<IngredientDTO> ingredients = ingredientDAO.getIngredientsDTOBySupplierId(supplierId);
                ingredientsBySupplier.put(supplierId, ingredients);
            }
            // Chuyển đổi thành JSON trong servlet
            String ingredientsBySupplierJson = gson.toJson(ingredientsBySupplier);
            request.setAttribute("ingredientsBySupplierJson", ingredientsBySupplierJson);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("ingredientsList", ingredientsList);
        request.setAttribute("supplierList", supplierList);
        request.getRequestDispatcher("/views/supplier_food.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}