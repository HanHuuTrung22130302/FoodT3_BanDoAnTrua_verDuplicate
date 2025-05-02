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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

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

        // Xử lý request AJAX
        String isAjax = request.getParameter("isAjax");
        if ("true".equals(isAjax)) {
            handleAjaxRequest(request, response);
            return;
        }

        // Xử lý request thông thường
        handleNormalRequest(request, response);
    }

    private void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
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
            String categoryId = request.getParameter("categoryId");
            if (option == null || option.isEmpty()) {
                option = "tatca";
            }

            // Lấy danh sách món ăn ban đầu dựa trên option
            if (option.equals("tatca")) {
                foodList = foodDAO.getAll();
            } else if (option.equals("danhgiacao")) {
                foodList = foodServiceListFilter.getTopRate();
            } else if (option.equals("dexuat")) {
                foodList = foodDAO.getTopPropose();
            } else if (option.equals("quantam")) {
                foodList = foodDAO.getTopView();
            } else if (option.equals("banchay")) {
                foodList = foodDAO.getTopSold();
            } else {
                // Nếu option là một số, đó là categoryId
                foodList = foodDAO.getFoodsByCategory(Integer.parseInt(option));
            }

            // Áp dụng bộ lọc danh mục nếu có
            if (categoryId != null && !categoryId.isEmpty() && !categoryId.equals("tatca")) {
                foodList = foodList.stream()
                    .filter(food -> food.getCategoryId() == Integer.parseInt(categoryId))
                    .collect(Collectors.toList());
            }

            // Áp dụng tìm kiếm nếu có
            if (txtSearch != null && !txtSearch.isEmpty()) {
                foodList = foodList.stream()
                    .filter(food -> food.getFoodName().toLowerCase().contains(txtSearch.toLowerCase()))
                    .collect(Collectors.toList());
            }

            // Sắp xếp danh sách theo thời gian tạo mới nhất
            foodList.sort((f1, f2) -> f2.getCreatedAt().compareTo(f1.getCreatedAt()));

            totalFoods = foodList.size();

            if (offset < totalFoods) {
                foodList = foodList.subList(Math.min(offset, totalFoods), Math.min(offset + pageSize, totalFoods));
            } else {
                foodList = new ArrayList<>();
            }

            int totalPages = (int) Math.ceil((double) totalFoods / pageSize);
            List<Category> categoryList = cs.getCategories();

            // Tạo response JSON
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("foods", foodList);
            responseData.put("categories", categoryList);
            responseData.put("currentPage", page);
            responseData.put("totalPages", totalPages);
            responseData.put("currentCategory", option);
            responseData.put("currentCategoryId", categoryId);
            responseData.put("search", txtSearch);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(responseData));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Có lỗi xảy ra khi xử lý yêu cầu\"}");
        }
    }

    private void handleNormalRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Xử lý request AJAX để lấy thông tin món ăn
        String idFood = request.getParameter("idFood");
        if (idFood != null) {
            try {
                Food food = foodDAO.getFoodById(Integer.parseInt(idFood));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(new Gson().toJson(food));
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
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

        // Sắp xếp danh sách theo thời gian tạo mới nhất
        foodList.sort((f1, f2) -> f2.getCreatedAt().compareTo(f1.getCreatedAt()));

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

        if ("delete".equals(action)) {
            int idFood = Integer.parseInt(request.getParameter("idFood"));
            boolean success = foodServiceListFilter.deleteFood(idFood);
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xóa món ăn", success ? "Thành công" : "Thất bại", "Mã món ăn: " + idFood);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\":" + success + "}");
        } else if ("add".equals(action)) {
            try {
                String foodName = request.getParameter("foodName");
                int category = Integer.parseInt(request.getParameter("idCategory"));
                int price = Integer.parseInt(request.getParameter("price"));
                String description = request.getParameter("description");
                String ingredients = request.getParameter("ingredients");

                // Kiểm tra trùng tên (chính xác)
                List<Food> existingFoods = foodDAO.getAll();
                boolean isDuplicate = existingFoods.stream()
                    .anyMatch(f -> f.getFoodName().equalsIgnoreCase(foodName));

                if (isDuplicate) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\":false, \"message\":\"Tên món ăn đã tồn tại\"}");
                    return;
                }

                // Xử lý upload ảnh
                Part filePart = request.getPart("img");
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uploadPath = getServletContext().getRealPath("/") + "Images/Food/";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                filePart.write(uploadPath + fileName);
                String imgPath = "Images/Food/" + fileName;

                // Tạo đối tượng Food mới
                Food newFood = new Food(0, foodName, price, 0, 0, imgPath, description, ingredients, category, 0, 0, new Timestamp(System.currentTimeMillis()), null);

                // Thêm món ăn vào database
                boolean result = foodServiceListFilter.addFood(newFood);

                // Ghi log
                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Thêm món ăn", result ? "Thành công" : "Thất bại", "Tên món ăn: " + foodName);

                // Chuẩn bị response
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                if (result) {
                    // Lấy ID của món vừa thêm
                    int newFoodId = foodDAO.getLastInsertedFoodId();

                    // Cập nhật lại danh sách món ăn trong cache
                    foodDAO.getAllFood();

                    // Lấy thông tin đầy đủ của món vừa thêm
                    Food addedFood = foodDAO.getFoodById(newFoodId);

                    if (addedFood != null) {
                        // Trả về thông tin đầy đủ của món ăn
                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("success", true);
                        responseData.put("foodId", addedFood.getFoodId());
                        responseData.put("foodName", addedFood.getFoodName());
                        responseData.put("price", addedFood.getPrice());
                        responseData.put("description", addedFood.getDescription());
                        responseData.put("categoryId", addedFood.getCategoryId());
                        responseData.put("image", addedFood.getImage());

                        response.getWriter().write(new Gson().toJson(responseData));
                    } else {
                        // Nếu không lấy được thông tin từ database, sử dụng thông tin từ form
                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("success", true);
                        responseData.put("foodId", newFoodId);
                        responseData.put("foodName", foodName);
                        responseData.put("price", price);
                        responseData.put("description", description);
                        responseData.put("categoryId", category);
                        responseData.put("image", imgPath);

                        response.getWriter().write(new Gson().toJson(responseData));
                    }
                } else {
                    response.getWriter().write("{\"success\":false, \"message\":\"Không thể thêm món ăn\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("{\"success\":false, \"message\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
            }
        } else if ("update".equals(action)) {
            try {
                int idFood = Integer.parseInt(request.getParameter("idFood"));
                String foodName = request.getParameter("foodName");
                int category = Integer.parseInt(request.getParameter("idCategory"));
                int price = Integer.parseInt(request.getParameter("price"));
                String description = request.getParameter("description");
                String ingredients = request.getParameter("ingredients");

                // Lấy thông tin món ăn hiện tại
                Food currentFood = foodDAO.getFoodById(idFood);
                if (currentFood == null) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\":false, \"message\":\"Không tìm thấy món ăn\"}");
                    return;
                }

                // Chỉ kiểm tra trùng tên nếu tên mới khác với tên hiện tại
                if (!currentFood.getFoodName().equalsIgnoreCase(foodName)) {
                    List<Food> existingFoods = foodDAO.getAll();
                    boolean isDuplicate = existingFoods.stream()
                        .anyMatch(f -> f.getFoodId() != idFood && f.getFoodName().equalsIgnoreCase(foodName));

                    if (isDuplicate) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\":false, \"message\":\"Tên món ăn đã tồn tại\"}");
                        return;
                    }
                }

                // Xử lý upload ảnh
                Part filePart = request.getPart("img");
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String imgPath = request.getParameter("currentImage");

                if (!fileName.isEmpty()) {
                    String uploadPath = getServletContext().getRealPath("/") + "Images/Food/";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();
                    filePart.write(uploadPath + fileName);
                    imgPath = "Images/Food/" + fileName;
                }

                Food updatedFood = new Food(idFood, foodName, price, 0, 0, imgPath, description, ingredients, category, 0, 0, new Timestamp(System.currentTimeMillis()), null);
                boolean result = foodServiceListFilter.updateFood(updatedFood);

                // Cập nhật lại danh sách món ăn trong cache
                foodDAO.getAllFood();

                logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Cập nhật món ăn", result ? "Thành công" : "Thất bại", "Mã món ăn: " + idFood);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                if (result) {
                    // Lấy thông tin đầy đủ của món vừa cập nhật
                    Food food = foodDAO.getFoodById(idFood);
                    if (food != null) {
                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("success", true);
                        responseData.put("foodId", food.getFoodId());
                        responseData.put("foodName", food.getFoodName());
                        responseData.put("price", food.getPrice());
                        responseData.put("description", food.getDescription());
                        responseData.put("categoryId", food.getCategoryId());
                        responseData.put("image", food.getImage());

                        response.getWriter().write(new Gson().toJson(responseData));
                    } else {
                        response.getWriter().write("{\"success\":true, \"image\":\"" + imgPath + "\"}");
                    }
                } else {
                    response.getWriter().write("{\"success\":false, \"message\":\"Không thể cập nhật món ăn\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("{\"success\":false, \"message\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
            }
        }
    }
}