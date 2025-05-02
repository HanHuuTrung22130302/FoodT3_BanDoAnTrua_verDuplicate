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
import java.util.*;

import com.google.gson.Gson;

@WebServlet(name = "IngredientsController", value = "/Ingredients")
public class IngredientsController extends HttpServlet {
    private IngredientDAO ingredientDAO;
    private final Gson gson = new Gson();

    @Override
    public void init() {
        this.ingredientDAO = new IngredientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Ingredients> ingredientsList;
        List<Supplier> supplierList;

        String filter = request.getParameter("filter");

        try {
            if ("nearlyExpired".equals(filter)) {
                ingredientsList = ingredientDAO.getNearlyExpiredIngredients();
            } else {
                ingredientsList = ingredientDAO.getAllIngredients();
            }

            supplierList = ingredientDAO.getAllSuppliers();
            Map<Integer, List<IngredientDTO>> ingredientsBySupplier = new HashMap<>();

            for (Supplier s : supplierList) {
                int supplierId = s.getSupplierId();
                List<IngredientDTO> ingredients = ingredientDAO.getIngredientsDTOBySupplierId(supplierId);
                ingredientsBySupplier.put(supplierId, ingredients);
            }

            request.setAttribute("ingredientsList", ingredientsList);
            request.setAttribute("supplierList", supplierList);
            request.setAttribute("ingredientsBySupplierJson", gson.toJson(ingredientsBySupplier));
            request.getRequestDispatcher("/views/supplier_food.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Lỗi khi truy xuất dữ liệu nguyên liệu", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
