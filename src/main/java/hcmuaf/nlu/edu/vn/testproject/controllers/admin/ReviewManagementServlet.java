package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.ReviewDAO;
import hcmuaf.nlu.edu.vn.testproject.models.ReviewFood;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/review-management")
public class ReviewManagementServlet extends HttpServlet {
    private ReviewDAO reviewDAO;
    private static final int PAGE_SIZE = 10;

    @Override
    public void init() throws ServletException {
        reviewDAO = new ReviewDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filterDate = request.getParameter("filterDate");
        String filterProduct = request.getParameter("filterProduct");
        String pageStr = request.getParameter("page");
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;

        if (filterDate == null) filterDate = "";
        if (filterProduct == null) filterProduct = "";

        List<ReviewFood> reviews = reviewDAO.getReviews(filterDate, filterProduct, page, PAGE_SIZE);
        int totalReviews = reviewDAO.getTotalReviews(filterDate, filterProduct);
        int totalPages = (int) Math.ceil((double) totalReviews / PAGE_SIZE);

        if (reviews.isEmpty()) {
            System.out.println("Không có đánh giá nào được tìm thấy với filterDate=" + filterDate + ", filterProduct=" + filterProduct);
            request.setAttribute("errorMessage", "Không tìm thấy đánh giá nào. Vui lòng kiểm tra bộ lọc hoặc cơ sở dữ liệu.");
        }

        request.setAttribute("reviews", reviews);
        request.setAttribute("selectedDate", filterDate);
        request.setAttribute("selectedProduct", filterProduct);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("views/review_management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String reviewId = request.getParameter("reviewId");

        if ("delete".equals(action) && reviewId != null) {
            try {
                reviewDAO.deleteReview(Integer.parseInt(reviewId));
            } catch (NumberFormatException e) {
                System.err.println("Lỗi định dạng reviewId: " + reviewId);
            }
        }

        response.sendRedirect(request.getContextPath() + "/review-management");
    }
}