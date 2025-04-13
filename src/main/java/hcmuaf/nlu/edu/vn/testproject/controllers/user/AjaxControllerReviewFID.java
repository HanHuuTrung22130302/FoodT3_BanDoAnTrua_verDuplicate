package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.FoodDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
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
import java.util.List;

@WebServlet(name = "AjaxControllerReviewFID", value = "/AjaxControllerReviewFID")
public class AjaxControllerReviewFID extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int foodID = Integer.parseInt(request.getParameter("text1"));
        int option = Integer.parseInt(request.getParameter("text2"));
        int amount = Integer.parseInt(request.getParameter("exits"));
        ReviewService reviewService = new ReviewService();
        List<ReviewFood> lrvf = reviewService.get10IncrementReviewFood(foodID,amount);
        List<ReviewFood> filterRvf = new ArrayList<>();
        switch (option) {
            case 0:
                filterRvf.addAll(lrvf);
                break;
            case 1:
                for (ReviewFood rf : lrvf) {
                    if (rf.getRating() == 1) filterRvf.add(rf);
                }
                break;
            case 2:
                for (ReviewFood rf : lrvf) {
                    if (rf.getRating() == 2) filterRvf.add(rf);
                }
                break;
            case 3:
                for (ReviewFood rf : lrvf) {
                    if (rf.getRating() == 3) filterRvf.add(rf);
                }
                break;
            case 4:
                for (ReviewFood rf : lrvf) {
                    if (rf.getRating() == 4) filterRvf.add(rf);
                }
                break;
            case 5:
                for (ReviewFood rf : lrvf) {
                    if (rf.getRating() == 5) filterRvf.add(rf);
                }
                break;

        }


        PrintWriter out = response.getWriter();
        if (filterRvf.isEmpty()) {
            out.println("<h2 style=\"max-width: 1200px; text-align: center;\">Chưa có đánh giá</h2>");
        } else {
            out.println("<div id=\"review-list"+foodID+"\">");
            for (ReviewFood rf : filterRvf) {

                out.println("<div class=\"fragmentReview countFragmentReview"+foodID+"\">");
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