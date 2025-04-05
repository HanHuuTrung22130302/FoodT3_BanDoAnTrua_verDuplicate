package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Ingredients;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "IngredientsController", value = "/Ingredients")
public class IngredientsController extends HttpServlet {

    private IngredientDAO ingredientDAO = new IngredientDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        List<Ingredients> ingredientsList = ingredientDAO.getAllIngredients();
        request.setAttribute("ingredientsList", ingredientsList);
        request.getRequestDispatcher("/views/supplier_food.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}