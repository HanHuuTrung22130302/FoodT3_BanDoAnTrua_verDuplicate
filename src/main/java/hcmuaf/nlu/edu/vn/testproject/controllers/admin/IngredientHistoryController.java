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
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "IngredientHistoryController", value = "/IngredientHistory")
public class IngredientHistoryController extends HttpServlet {

    private IngredientDAO ingredientDAO;
    private CheckUserDao checkUserDao;
    private static final int RECORDS_PER_PAGE = 10;

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
        String pageParam = request.getParameter("page");
        int currentPage = pageParam != null ? Integer.parseInt(pageParam) : 1;

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
                    .collect(Collectors.toList());
        }

        // Phân trang
        int totalRecords = historyList.size();
        int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);
        int startIndex = (currentPage - 1) * RECORDS_PER_PAGE;
        int endIndex = Math.min(startIndex + RECORDS_PER_PAGE, totalRecords);
        List<Ingredients> paginatedList = historyList.subList(startIndex, endIndex);

        // Dữ liệu cho biểu đồ
        LocalDate today = LocalDate.now();
        LocalDate expiryThreshold = today.plusDays(7);
        List<Ingredients> nearlyExpired = importList.stream()
                .filter(i -> i.getExpirationDate() != null &&
                        !i.getExpirationDate().toLocalDate().isAfter(expiryThreshold))
                .collect(Collectors.toList());

        Map<String, Double> importByDate = importList.stream()
                .filter(i -> i.getImportDate() != null)
                .collect(Collectors.groupingBy(
                        i -> i.getImportDate().toLocalDate().toString(),
                        Collectors.summingDouble(Ingredients::getAmount)
                ));
        Map<String, Double> exportByDate = exportList.stream()
                .filter(i -> i.getExpirationDate() != null)
                .collect(Collectors.groupingBy(
                        i -> i.getExpirationDate().toLocalDate().toString(),
                        Collectors.summingDouble(Ingredients::getAmount)
                ));

        double totalImportValue = importList.stream()
                .mapToDouble(i -> i.getAmount() * i.getPrice())
                .sum();
        double totalExportValue = exportList.stream()
                .mapToDouble(i -> i.getAmount() * i.getPrice())
                .sum();

        double totalImportAmount = importList.stream().mapToDouble(Ingredients::getAmount).sum();
        double totalExportAmount = exportList.stream().mapToDouble(Ingredients::getAmount).sum();

        // Chuyển dữ liệu sang JSON
        Gson gson = new Gson();
        request.setAttribute("nearlyExpiredJson", gson.toJson(nearlyExpired));
        request.setAttribute("importByDateJson", gson.toJson(importByDate));
        request.setAttribute("exportByDateJson", gson.toJson(exportByDate));
        request.setAttribute("totalImportValue", totalImportValue);
        request.setAttribute("totalExportValue", totalExportValue);
        request.setAttribute("totalImportAmount", totalImportAmount);
        request.setAttribute("totalExportAmount", totalExportAmount);

        // Dữ liệu phân trang
        request.setAttribute("historyList", paginatedList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("type", type);
        request.setAttribute("search", searchTerm);

        request.getRequestDispatcher("/views/ingredient_history.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}