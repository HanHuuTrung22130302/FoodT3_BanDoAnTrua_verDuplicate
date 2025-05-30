package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.daos.IngredientDAO;
import hcmuaf.nlu.edu.vn.testproject.dto.IngredientDTO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Ingredients;
import hcmuaf.nlu.edu.vn.testproject.models.Supplier;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "IngredientsController", value = "/Ingredients")
public class IngredientsController extends HttpServlet {
    private IngredientDAO ingredientDAO;
    private final Gson gson = new Gson();
    private LogService logService = new LogService();
    private CheckUserDao checkUserDao = new CheckUserDao();
    private static final int RECORDS_PER_PAGE = 10;

    @Override
    public void init() {
        this.ingredientDAO = new IngredientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 0, "Xem danh sách món ăn", "Thất bại", "Không có quyền truy cập");
            session.invalidate();
            response.sendRedirect("home");
            return;
        }

        List<Ingredients> ingredientsList;
        List<Supplier> supplierList;

        String filter = request.getParameter("filter");
        String searchTerm = request.getParameter("search");
        String pageParam = request.getParameter("page");
        int currentPage = pageParam != null ? Integer.parseInt(pageParam) : 1;

        try {
            // Lấy danh sách nguyên liệu dựa trên bộ lọc hoặc tìm kiếm
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                ingredientsList = ingredientDAO.searchIngredients(searchTerm.trim());
            } else if ("nearlyExpired".equals(filter)) {
                ingredientsList = ingredientDAO.getNearlyExpiredIngredients();
            } else {
                ingredientsList = ingredientDAO.getAllIngredients();
            }

            // Phân trang
            int totalRecords = ingredientsList.size();
            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);
            int startIndex = (currentPage - 1) * RECORDS_PER_PAGE;
            int endIndex = Math.min(startIndex + RECORDS_PER_PAGE, totalRecords);
            List<Ingredients> paginatedList = ingredientsList.subList(startIndex, endIndex);

            // Lấy danh sách nhà cung cấp và nguyên liệu theo nhà cung cấp
            supplierList = ingredientDAO.getAllSuppliers();
            Map<Integer, List<IngredientDTO>> ingredientsBySupplier = new HashMap<>();

            for (Supplier s : supplierList) {
                int supplierId = s.getSupplierId();
                List<IngredientDTO> ingredients = ingredientDAO.getIngredientsDTOBySupplierId(supplierId);
                ingredientsBySupplier.put(supplierId, ingredients);
            }

            // Truyền dữ liệu sang JSP
            request.setAttribute("ingredientsList", paginatedList);
            request.setAttribute("supplierList", supplierList);
            request.setAttribute("ingredientsBySupplierJson", gson.toJson(ingredientsBySupplier));
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("filter", filter);
            request.setAttribute("search", searchTerm);

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