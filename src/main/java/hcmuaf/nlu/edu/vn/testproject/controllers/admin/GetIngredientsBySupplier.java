package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Ingredients;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetIngredientsBySupplier", value = "/getIngredientsBySupplier")
public class GetIngredientsBySupplier extends HttpServlet {
    private IngredientDAO ingredientDAO = new IngredientDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sid = request.getParameter("supplierId");
        int supplierId = Integer.parseInt(sid);

        List<Ingredients> ingredients = ingredientDAO.getIngredientsBySupplierId(supplierId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new com.google.gson.Gson().toJson(ingredients, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}