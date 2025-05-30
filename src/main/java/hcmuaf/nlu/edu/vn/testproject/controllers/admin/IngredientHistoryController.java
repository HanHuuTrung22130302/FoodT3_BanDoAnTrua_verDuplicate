package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Ingredients;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "IngredientHistoryController", value = "/IngredientHistory")
public class IngredientHistoryController extends HttpServlet {

    private IngredientDAO ingredientDAO;
    private CheckUserDao checkUserDao;

    @Override
    public void init() {
        this.ingredientDAO = new IngredientDAO();
        this.checkUserDao = new CheckUserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            session.invalidate();
            response.sendRedirect("home");
            return;
        }

        String type = request.getParameter("type");
        String searchTerm = request.getParameter("search");

        List<Ingredients> historyList = new ArrayList<>();
        // Lấy danh sách nhập hàng
        List<Ingredients> importList = ingredientDAO.getAllIngredients();
        // Lấy danh sách xuất hàng
        List<Ingredients> exportList = ingredientDAO.getUsedIngredients();

        // Lọc theo loại
        if ("import".equals(type)) {
            historyList.addAll(importList);
        } else if ("export".equals(type)) {
            historyList.addAll(exportList);
        } else {
            historyList.addAll(importList);
            historyList.addAll(exportList);
        }

        // Tìm kiếm
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String searchPattern = searchTerm.trim().toLowerCase();
            historyList = historyList.stream()
                    .filter(i -> String.valueOf(i.getIngredientId()).contains(searchPattern) ||
                            i.getIngredientName().toLowerCase().contains(searchPattern) ||
                            i.getSupplierName().toLowerCase().contains(searchPattern))
                    .toList();
        }

        request.setAttribute("historyList", historyList);
        request.getRequestDispatcher("/views/ingredient_history.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}