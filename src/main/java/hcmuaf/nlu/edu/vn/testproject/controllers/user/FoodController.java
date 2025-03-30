package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.FoodServiceListFilter;
import hcmuaf.nlu.edu.vn.testproject.services.ReviewService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FoodController", value = "/allmenu")
public class FoodController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        FoodServiceListFilter foodServiceListFilter = new FoodServiceListFilter();
        ReviewService reviewService = new ReviewService();

        // Lấy số trang, nếu không có thì mặc định là trang 1
        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        // Tính toán offset
        int pageSize = 10; // Kích thước trang
        int offset = (page - 1) * pageSize;
        List<Food> foodList= new ArrayList<>();
        int totalFoods=0;

        // Lấy giá trị option từ request
        String option = request.getParameter("option");
        if (option != null) {

            foodList = foodServiceListFilter.getOption(option); // Lấy danh sách dựa trên option
            totalFoods = foodList.size(); // Tổng số món theo option

            // Áp dụng phân trang
            foodList = foodList.subList(
                    Math.min(offset, totalFoods),
                    Math.min(offset + pageSize, totalFoods)
            );
        }else{
            response.sendRedirect(request.getContextPath() + "/search");
        }

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalFoods / pageSize);


        for(Food food : foodList){
            food.setRating(reviewService.getRating(food.getFoodId()));

        }

        CategoryService cs = new CategoryService();
        List<Category> categoryList = cs.getCategories();

        // Đặt thuộc tính cho JSP
        request.setAttribute("list", foodList);
        request.setAttribute("listC", categoryList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Forward đến trang JSP
        request.getRequestDispatcher("/views/allMenu.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

}
