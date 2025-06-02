package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.ReviewFood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewDaoByUser {
    public ReviewDaoByUser() {
    }
    public boolean hasReviewedInvoice(int invoiceId) {
        String query = "SELECT 1 FROM review WHERE invoice_id = ? LIMIT 1";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();
            if (con == null) {
                System.err.println("Kết nối cơ sở dữ liệu thất bại!");
                return false;
            }

            ps = con.prepareStatement(query);
            ps.setInt(1, invoiceId);
            rs = ps.executeQuery();

            return rs.next(); // Nếu có dòng nào, nghĩa là đã đánh giá

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi kiểm tra đánh giá đơn hàng: " + e.getMessage());
            return false;
        } finally {
            closeResources(rs, ps, con);
        }
    }

    // Hàm lấy tất cả các đánh giá từ cơ sở dữ liệu
    public void insertReview(int userId, int foodId, int rating, String comment, int invoiceId) {
        String query = "INSERT INTO review (account_id, food_id, rating, comment, invoice_id, created_at) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setInt(2, foodId);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            ps.setInt(5, invoiceId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }
}
