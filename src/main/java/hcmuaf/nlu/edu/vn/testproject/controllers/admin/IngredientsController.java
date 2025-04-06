package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Ingredients;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "IngredientsController", value = "/Ingredients")
public class IngredientsController extends HttpServlet {

    private IngredientDAO ingredientDAO = new IngredientDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String filter = request.getParameter("filter");
        List<Ingredients> ingredientsList;

        try {
            if ("nearlyExpired".equals(filter)) {
                ingredientsList = ingredientDAO.getNearlyExpiredIngredients();
            } else {
                ingredientsList = ingredientDAO.getAllIngredients();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("ingredientsList", ingredientsList);
        request.getRequestDispatcher("/views/supplier_food.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}