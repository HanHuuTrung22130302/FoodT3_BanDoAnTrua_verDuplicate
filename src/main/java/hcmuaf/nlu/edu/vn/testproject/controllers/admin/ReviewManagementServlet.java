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
    private static final int PAGE_SIZE = 10; // Số đánh giá mỗi trang

    @Override
    public void init() throws ServletException {
        reviewDAO = new ReviewDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy tham số bộ lọc và trang
        String filterDate = request.getParameter("filterDate");
        String filterSearch = request.getParameter("filterSearch");
        String filterRating = request.getParameter("filterRating");
        String pageStr = request.getParameter("page");
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;

        // Nếu không có tham số, đặt giá trị mặc định
        if (filterDate == null) filterDate = "";
        if (filterSearch == null) filterSearch = "";
        if (filterRating == null) filterRating = "";

        // Lấy danh sách đánh giá từ DAO
        List<ReviewFood> reviews = reviewDAO.getReviews(filterDate, filterSearch, filterRating, page, PAGE_SIZE);
        int totalReviews = reviewDAO.getTotalReviews(filterDate, filterSearch, filterRating);
        int totalPages = (int) Math.ceil((double) totalReviews / PAGE_SIZE);

        // Kiểm tra dữ liệu
        if (reviews.isEmpty()) {
            System.out.println("Không có đánh giá nào được tìm thấy với filterDate=" + filterDate + ", filterSearch=" + filterSearch + ", filterRating=" + filterRating);
            request.setAttribute("errorMessage", "Không tìm thấy đánh giá nào. Vui lòng kiểm tra bộ lọc hoặc cơ sở dữ liệu.");
        }

        // Đặt các thuộc tính để hiển thị trên JSP
        request.setAttribute("reviews", reviews);
        request.setAttribute("selectedDate", filterDate);
        request.setAttribute("selectedSearch", filterSearch);
        request.setAttribute("selectedRating", filterRating);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Chuyển hướng đến JSP
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

        // Chuyển hướng lại trang để làm mới danh sách
        response.sendRedirect(request.getContextPath() + "/review-management");
    }
}