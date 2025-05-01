package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.FoodDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.FoodServiceListFilter;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
@WebServlet(name = "ManageFoodController", value = "/foodservice")
public class ManageFoodController extends HttpServlet {
    private FoodDAO foodDAO = new FoodDAO();
    private CategoryService cs = new CategoryService();
    private FoodServiceListFilter foodServiceListFilter = new FoodServiceListFilter();
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            logService.logActivity(0, 0, "Xem danh sách món ăn", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        String txtSearch = request.getParameter("text");
        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int pageSize = 10;
        int offset = (page - 1) * pageSize;
        List<Food> foodList = new ArrayList<>();
        int totalFoods = 0;

        String option = request.getParameter("option");
        if (option == null || option.isEmpty()) {
            option = "tatca";
        }

        if (txtSearch != null && !txtSearch.isEmpty()) {
            foodList = foodDAO.searchByName(txtSearch);
        } else {
            foodList = foodServiceListFilter.getOption(option);
        }

        totalFoods = foodList.size();

        if (offset < totalFoods) {
            foodList = foodList.subList(Math.min(offset, totalFoods), Math.min(offset + pageSize, totalFoods));
        } else {
            foodList = new ArrayList<>();
        }

        int totalPages = (int) Math.ceil((double) totalFoods / pageSize);
        List<Category> categoryList = cs.getCategories();

        request.setAttribute("list", foodList);
        request.setAttribute("listC", categoryList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentCategory", option);
        request.setAttribute("search", txtSearch);

        request.getRequestDispatcher("views/food_service.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            logService.logActivity(0, 0, "Quản lý món ăn", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        String action = request.getParameter("action");
        int idFood = 0;

        if ("delete".equals(action)) {
            idFood = Integer.parseInt(request.getParameter("idFood"));
            boolean success = foodServiceListFilter.deleteFood(idFood);
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xóa món ăn", success ? "Thành công" : "Thất bại", "Mã món ăn: " + idFood);
            response.sendRedirect("foodservice");
        } else if ("add".equals(action)) {
            String foodName = request.getParameter("foodName");
            int category = Integer.parseInt(request.getParameter("idCategory"));
            int price = Integer.parseInt(request.getParameter("price"));
            String description = request.getParameter("description");
            String ingredients = request.getParameter("ingredients");

            Part filePath = request.getPart("img");
            String fileName = Paths.get(filePath.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/") + "Images/Food/";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            filePath.write(uploadPath + fileName);
            String imgPath = "Images/Food/" + fileName;

            Food newFood = new Food(0, foodName, price, 0, 0, imgPath, description, ingredients, category, 0, 0, new Timestamp(System.currentTimeMillis()), null);
            boolean result = foodServiceListFilter.addFood(newFood);

            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Thêm món ăn", result ? "Thành công" : "Thất bại", "Tên món ăn: " + foodName);
            response.sendRedirect("foodservice?status=" + (result ? "success" : "error"));
        } else if ("update".equals(action)) {
            idFood = Integer.parseInt(request.getParameter("idFood"));
            String foodName = request.getParameter("foodName");
            int category = Integer.parseInt(request.getParameter("idCategory"));
            int price = Integer.parseInt(request.getParameter("price"));
            String description = request.getParameter("description");
            String ingredients = request.getParameter("ingredients");

            Part filePath = request.getPart("img");
            String fileName = Paths.get(filePath.getSubmittedFileName()).getFileName().toString();
            String imgPath = request.getParameter("currentImage");

            if (!fileName.isEmpty()) {
                String uploadPath = getServletContext().getRealPath("/") + "Images/Food/";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                filePath.write(uploadPath + fileName);
                imgPath = "Images/Food/" + fileName;
            }

            Food updatedFood = new Food(idFood, foodName, price, 0, 0, imgPath, description, ingredients, category, 0, 0, new Timestamp(System.currentTimeMillis()), null);
            boolean result = foodServiceListFilter.updateFood(updatedFood);

            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Cập nhật món ăn", result ? "Thành công" : "Thất bại", "Mã món ăn: " + idFood);
            response.sendRedirect("foodservice?status=" + (result ? "success" : "error"));
        }
    }
}