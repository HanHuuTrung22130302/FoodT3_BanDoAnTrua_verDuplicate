package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.FoodDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.ReviewCache;
import hcmuaf.nlu.edu.vn.testproject.models.ReviewFood;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.FoodServiceListFilter;
import hcmuaf.nlu.edu.vn.testproject.services.ReviewService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "AjaxControllerReviewFID", value = "/AjaxControllerReviewFID")
public class AjaxControllerReviewFID extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int foodID = Integer.parseInt(request.getParameter("text1"));
        int option = Integer.parseInt(request.getParameter("text2"));
        int amount = Integer.parseInt(request.getParameter("exits"));
        int countOption = Integer.parseInt(request.getParameter("countOption"));
        ReviewService reviewService = new ReviewService();
        List<ReviewFood> nextTen;
        PrintWriter out = response.getWriter();

        int countReview = reviewService.getCountReview(foodID, option);
        int countReviewFoodAll= reviewService.getCountReview(foodID, countOption);

        if (countOption == 0)
            nextTen = reviewService.get10IncrementReviewFood(foodID, option, countOption);
        else
            nextTen = reviewService.get10IncrementReviewFood(foodID, option, amount);

        out.print("<div class=\" option" + option + "\">");
        if (countReview == 0) {
            out.println("<div class=\"fragmentReview countFragmentReview" + foodID + "\">");
            out.println("<h2 style=\"max-width: 1200px; text-align: center;\">Chưa có đánh giá</h2>");
            out.println("</div>");
            out.println("<div class=\"endOfReviewFlag\" style=\"display:none\"></div>");

        } else if (nextTen.isEmpty() && amount > 0) {
//            out.println("<div class=\"fragmentReview countFragmentReview" + foodID + "\">");
//            out.println("<h3 style=\"max-width: 1200px; text-align: center;\">Đã hết đánh giá</h3>");
//            out.println("</div>");
            out.println("<div class=\"endOfReviewFlag\" style=\"display:none\"></div>");

        } else if (nextTen.size() < 10 || amount==countReviewFoodAll) {
            for (ReviewFood rf : nextTen) {
                out.println("<div class=\"fragmentReview countFragmentReview" + foodID + "\">");
                out.println("    <div class=\"nameAndDateRatingUser\">");
                out.println("        <div class=\"nameUser\">" + rf.getName() + "</div>");
                out.println("        <div class=\"dateRatingUser\">" + rf.getDate() + "</div>");
                out.println("    </div>");
                out.println("    <div class=\"ratingUser\">");
                for (int i = 1; i <= rf.getRating(); i++) {
                    out.println("        <i class=\"fa-solid fa-star\"></i>");
                }
                for (int i = rf.getRating() + 1; i <= 5; i++) {
                    out.println("        <i class=\"fa-regular fa-star\"></i>");
                }
                out.println("    </div>");
                out.println("    <div class=\"cmtRatingUser\">" + rf.getComment() + "</div>");
                out.println("</div>");
            }
            out.println("</div>");
            out.println("<div class=\"endOfReviewFlag\" style=\"display:none\"></div>");
        } else {
            for (ReviewFood rf : nextTen) {
                out.println("<div class=\"fragmentReview countFragmentReview" + foodID + "\">");
                out.println("    <div class=\"nameAndDateRatingUser\">");
                out.println("        <div class=\"nameUser\">" + rf.getName() + "</div>");
                out.println("        <div class=\"dateRatingUser\">" + rf.getDate() + "</div>");
                out.println("    </div>");
                out.println("    <div class=\"ratingUser\">");
                for (int i = 1; i <= rf.getRating(); i++) {
                    out.println("        <i class=\"fa-solid fa-star\"></i>");
                }
                for (int i = rf.getRating() + 1; i <= 5; i++) {
                    out.println("        <i class=\"fa-regular fa-star\"></i>");
                }
                out.println("    </div>");
                out.println("    <div class=\"cmtRatingUser\">" + rf.getComment() + "</div>");
                out.println("</div>");
            }
            out.println("</div>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

}