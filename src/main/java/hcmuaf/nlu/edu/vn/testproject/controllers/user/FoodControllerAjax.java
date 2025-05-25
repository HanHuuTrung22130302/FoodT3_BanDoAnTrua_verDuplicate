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

        ReviewService reviewService = new ReviewService();

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
                food.setRating(reviewService.getRating(food.getFoodId()));
                String addToCartUrl = request.getContextPath() + "/addtoCart?foodID=" + food.getFoodId();
                int soldValue = food.getSold();

                String displaySold;
                if (soldValue >= 1000) {
                    double soldInThousands = soldValue / 1000.0;
                    displaySold = String.format("%.1fk", soldInThousands);
                } else
                    displaySold = String.valueOf(soldValue);

                String formattedPrice = String.format("%,d", food.getPrice()) + "đ";

                out.println("<div class=\"card\"\n" +
                        "                     onclick=\"showPopup('" + food.getFoodId() + "');scrollToTop(" + food.getFoodId() + ");getU('" + food.getFoodId() + "');ajaxGetReviewFID(" + food.getFoodId() + ",0)\">\n" +
                        "                    <img src=\"" + food.getImage() + "\" alt=\"" + food.getFoodName() + "\"/>\n" +
                        "                    <div class=\"card_content\">\n" +
                        "                        <div class=\"nameFood\">" + food.getFoodName() + "</div>\n" +
                        "                        <div class=\"priceFood\">\n" + formattedPrice +
                        "                        </div>\n" +
                        "                        <div class=\"card_footer\">\n" +
                        "                            <a class=\"btn\" onclick=\"event.stopPropagation();getU("+food.getFoodId()+");\" href=\"" + addToCartUrl + "\">\n" +
                        "                                Thêm vào giỏ\n" +
                        "                            </a>\n" +
                        "                            <div class=\"reviewFood\">\n" +
                        "                                <div class=\"ratingFood\">\n" +
                        "                                    <i class=\"fas fa-star\"></i>\n" +
                        "                                    <span class=\"rating-value\">" + food.getRating() + "</span>\n" +
                        "                                </div>\n" +
                        "                                <div class=\"soldFood\">\n" +
                        "                                    <span class=\"sales-text\">Đã bán</span>\n" +
                        "                                    <span class=\"sales-value\">\n" + displaySold +
                        "                                </span>\n" +
                        "                                </div>\n" +
                        "                            </div>\n" +
                        "                        </div>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "\n" +
                        "                <!-- Popup chi tiết món ăn -->\n" +
                        "                <div id=\"" + food.getFoodId() + "\" class=\"popup\">\n" +
                        "\n" +
                        "                    <div class=\"popup-content\">\n" +
                        "                        <div class=\"close\" onclick=\"scrollToTop(" + food.getFoodId() + ");closePopup('" + food.getFoodId() + "');\">&times;</div>\n" +
                        "\n" +
                        "                        <div class=\"popup-body\">\n" +
                        "                            <img src=\"" + food.getImage() + "\" alt=\"" + food.getImage() + "\"/>\n" +
                        "                            <div class=\"containePopup\">\n" +
                        "\n" +
                        "                                <div class=\"nameAndSold\">\n" +
                        "                                    <div class=\"nameFoodPopup\">" + food.getFoodName() + "</div>\n" +
                        "                                    <div class=\"ratingAndSold\">\n" +
                        "                                        <div class=\"soldFoodPopup\">\n" +
                        "                                            <span class=\"sales-textPopup\">Đã bán</span>\n" +
                        "                                            <span class=\"sales-valuePopup\">\n" + displaySold +
                        "                                </span>\n" +
                        "                                        </div>\n" +
                        "                                        <div class=\"ratingFoodPopup\">\n" +
                        "                                            <i class=\"fas fa-star\"></i>\n" +
                        "                                            <span class=\"rating-valuePopup\">" + food.getRating() + "</span>\n" +
                        "                                        </div>\n" +
                        "                                    </div>\n" +
                        "                                </div>\n" +
                        "                                <div class=\"priceFoodPopup\"><span style=\"color: black;font-size: 15px\">Giá: </span>\n" + formattedPrice +
                        "                                </div>\n" +
                        "                                <div class=\"descriptionFoodPopup\">" + food.getDescription() + "</div>\n" +
                        "\n" +
                        "                                <div id=\"scrollbody" + food.getFoodId() + "\" class=\"danhgiasanpham\">Đánh giá sản phẩm</div>\n" +
                        "                                <div class=\"rating-filter\">\n" +
                        "                                    <button onclick=\"scrollToReviewList(" + food.getFoodId() + ");ajaxGetReviewFID(" + food.getFoodId() + ",0)\">Tất cả</button>\n" +
                        "                                    <button onclick=\"scrollToReviewList(" + food.getFoodId() + ");ajaxGetReviewFID(" + food.getFoodId() + ",5)\">5⭐</button>\n" +
                        "                                    <button onclick=\"scrollToReviewList(" + food.getFoodId() + ");ajaxGetReviewFID(" + food.getFoodId() + ",4)\">4⭐</button>\n" +
                        "                                    <button onclick=\"scrollToReviewList(" + food.getFoodId() + ");ajaxGetReviewFID(" + food.getFoodId() + ",3)\">3⭐</button>\n" +
                        "                                    <button onclick=\"scrollToReviewList(" + food.getFoodId() + ");ajaxGetReviewFID(" + food.getFoodId() + ",2)\">2⭐</button>\n" +
                        "                                    <button onclick=\"scrollToReviewList(" + food.getFoodId() + ");ajaxGetReviewFID(" + food.getFoodId() + ",1)\">1⭐</button>\n" +
                        "                                </div>\n" +
                        "                                <div class=\"user-reviews\">\n" +
                        "                                    <div id=\"review-list" + food.getFoodId() + "\">\n" +
                        "\n" +
                        "                                    </div>\n" +
                        "                                </div>\n" +
                        "                            </div>\n" +
                        "                        </div>\n" +
                        "                        <button class=\"scrollToTop\" onclick=\"scrollToTop(" + food.getFoodId() + ")\">^</button>\n" +
                        "                        <div class=\"popup-footer\">\n" +
                        "                            <button onclick=\"getU("+food.getFoodId()+");\" class=\"button-cart\">\n" +
                        "                                <a class=\"linktocart\" href=\"" + addToCartUrl + "\">\n" +
                        "                                    Thêm vào giỏ hàng\n" +
                        "                                </a>\n" +
                        "                            </button>\n" +
                        "                        </div>\n" +
                        "                    </div>\n" +
                        "\n" +
                        "\n" +
                        "                </div>");
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
