package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.FoodServiceListFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FoodControllerAjax", value = "/menuajax")
public class FoodControllerAjax extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");



        int page = Integer.parseInt(request.getParameter("page"));

        int pageSize = 10; // Kích thước trang
        int offset = (page - 1) * pageSize;
        int totalFoods = 0;

        // Lấy giá trị option từ request
        String option = request.getParameter("option");
        FoodServiceListFilter foodServiceListFilter = new FoodServiceListFilter();
        List<Food> foodList = foodServiceListFilter.getOption(option); // Lấy danh sách dựa trên option
        totalFoods = foodList.size(); // Tổng số món theo option

        // Áp dụng phân trang
        foodList = foodList.subList(
                Math.min(offset, totalFoods),
                Math.min(offset + pageSize, totalFoods)
        );

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalFoods / pageSize);

        // Lấy danh sách danh mục
        CategoryService cs = new CategoryService();
        List<Category> categoryList = cs.getCategories();

        // Đặt thuộc tính cho JSP
        request.setAttribute("list", foodList);
        request.setAttribute("listC", categoryList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        PrintWriter out = response.getWriter();
        if (foodList.isEmpty()) {
            out.println("<h2 style=\"max-width: 1200px; text-align: center;\"> Không có món ăn phù hợp</h2>");
        } else {
            out.println("<div class=\"content_section\">");
            for (Food food : foodList) {
                String addToCartUrl = request.getContextPath() + "/addtoCart?foodID=" + food.getFoodId();
                out.println("<div class=\"card\" onclick=\"showPopup('" + food.getFoodId() + "')\">\n" +
                        "                <img src=\"" + food.getImage() + "\" alt=\"" + food.getFoodName() + "\"/>\n" +
                        "                <div class=\"card_content\">\n" +
                        "                    <h3>" + food.getFoodName() + "</h3>\n" +
                        "                    <p>" + food.getPrice() + "đ</p>\n" +
                        "                    <a class=\"btn\" href=\"" + addToCartUrl + "\" onclick=\"event.stopPropagation()\">\n" +
                        "                        Thêm vào giỏ hàng\n" +
                        "                    </a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "\n" +
                        "            <!-- Popup chi tiết món ăn -->\n" +
                        "            <div id=\"" + food.getFoodId() + "\" class=\"popup\">\n" +
                        "                <div class=\"popup-content\">\n" +
                        "                    <img src=\"" + food.getImage() + "\" alt=\"" + food.getFoodName() + "\"/>\n" +
                        "                    <h3>" + food.getFoodName() + "</h3>\n" +
                        "                    <p>Giá: " + food.getPrice() + "đ</p>\n" +
                        "                    <span>\n" +
                        "                           " + food.getDescription() + "\n" +
                        "                    </span>\n" +
                        "                    <button class=\"button-cart\">\n" +
                        "                        <a class=\"linktocart\" href=\"" + addToCartUrl + "\">\n" +
                        "                            Thêm vào giỏ hàng\n" +
                        "                        </a>\n" +
                        "                    </button>\n" +
                        "                </div>\n" +
                        "                <span class=\"close\" onclick=\"closePopup('" + food.getFoodId() + "')\">&times;</span>\n" +
                        "            </div>");
            }
            out.println("</div>");

            out.println("<div class=\"pagination\" style=\"width:1200px;margin:0px auto; padding-left:35px; text-align:center;\">");
            for (int i = 1; i <= totalPages; i++) {
                out.println("<button onclick=\"loadSP('" + option + "', " + i + ")\" class=\"" + (page == i ? "active" : "") + "\">" + i + "</button>");
            }
            out.println("</div>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Xử lý POST nếu cần
    }
}
