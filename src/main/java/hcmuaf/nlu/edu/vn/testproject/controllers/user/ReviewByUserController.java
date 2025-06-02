package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import hcmuaf.nlu.edu.vn.testproject.services.FoodServiceListFilter;
import hcmuaf.nlu.edu.vn.testproject.services.ReviewService;
import hcmuaf.nlu.edu.vn.testproject.services.ReviewServiceByUser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ReviewByUserController", value = "/reviewbyusercontroller")
public class ReviewByUserController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Đảm bảo tiếng Việt
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        String invoiceIdStr = request.getParameter("invoiceId");
        String[] foodIds = request.getParameterValues("foodId[]");
        String[] ratings = request.getParameterValues("rating[]");
        String[] comments = request.getParameterValues("comment[]");

        if (invoiceIdStr == null || foodIds == null || ratings == null || comments == null) {
            response.getWriter().println("Dữ liệu đánh giá không hợp lệ!");
            return;
        }

        int invoiceId = Integer.parseInt(invoiceIdStr);

        ReviewServiceByUser reviewService = new ReviewServiceByUser();

        // Nếu đơn đã được đánh giá trước đó
        if (reviewService.checkReview(invoiceId)) {
            response.getWriter().println("Đơn hàng này đã được đánh giá trước đó.");
            return;
        }

        for (int i = 0; i < foodIds.length; i++) {
            int foodId = Integer.parseInt(foodIds[i]);
            int rating = Integer.parseInt(ratings[i]);
            String comment = comments[i];

            reviewService.insertReview(currentUser.getAccountId(), foodId, rating, comment, invoiceId); // Gọi hàm mới có thêm invoiceId
        }

        response.sendRedirect("PurchaseOrder");
    }

}
