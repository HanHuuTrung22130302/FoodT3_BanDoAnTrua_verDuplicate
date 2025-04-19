package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.ReviewFood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReviewDAO {

    private Map<Integer, ReviewFood> dataReviewFood = new HashMap<>();
    int idFood,offSetReviewFood;

    public ReviewDAO() {
        this.dataReviewFood = new HashMap<>();
        getAllReview();
    }


    public List<ReviewFood> getAll() {
        return new ArrayList<>(dataReviewFood.values());
    }

    // Hàm lấy tất cả các món ăn từ cơ sở dữ liệu
    public void getAllReview() {
        String query = "SELECT  name, rv.account_id, review_id, food_id, rating,comment, rv.created_at from review rv join account ac on rv.account_id = ac.account_id ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Tạo kết nối cơ sở dữ liệu
            con = new DbContext().getConnection();

            // Chuẩn bị câu lệnh SQL
            ps = con.prepareStatement(query);
            // Thực thi câu lệnh
            rs = ps.executeQuery();


            while (rs.next()) {
                dataReviewFood.put(rs.getInt("review_id"), new ReviewFood(
                        rs.getString("name"),
                        rs.getInt("review_id"),
                        rs.getInt("food_id"),
                        rs.getInt("rating"),
                        rs.getInt("account_id"),
                        rs.getDate("created_at"),
                        rs.getString("comment")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo rằng kết nối, câu lệnh và result set được đóng đúng cách
            closeResources(rs, ps, con);
        }
    }

    // Phương thức đóng các tài nguyên
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    public List<Integer> getTop4FoodRate() {
        Map<Integer, Long> countStarLS = dataReviewFood.values().stream().filter(rv -> rv.getRating() == 5).collect(Collectors.groupingBy(ReviewFood::getFoodId, Collectors.counting()));
        List<Integer> lsIdFood = countStarLS.entrySet().stream().sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())).limit(4).map(Map.Entry::getKey).collect(Collectors.toList());
        return lsIdFood;
    }

    public List<Integer> getTopFoodRate() {
        Map<Integer, Long> countStarLS = dataReviewFood.values().stream().filter(rv -> rv.getRating() == 5).collect(Collectors.groupingBy(ReviewFood::getFoodId, Collectors.counting()));
        List<Integer> lsIdFood = countStarLS.entrySet().stream().sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
        return lsIdFood;
    }

    public List<ReviewFood> getReviewByFood(int foodId, int offset, int option) {
        List<ReviewFood> reviewList = new ArrayList<>();
        String query;

        if (option == 0) {
            // Lấy tất cả review
            query = "SELECT name, rv.account_id, review_id, food_id, rating, comment, rv.created_at " +
                    "FROM review rv " +
                    "JOIN account ac ON rv.account_id = ac.account_id " +
                    "WHERE rv.food_id = ? " +
                    "ORDER BY rv.created_at DESC " +
                    "LIMIT 10 OFFSET ?";
        } else {
            // Lọc theo rating
            query = "SELECT name, rv.account_id, review_id, food_id, rating, comment, rv.created_at " +
                    "FROM review rv " +
                    "JOIN account ac ON rv.account_id = ac.account_id " +
                    "WHERE rv.food_id = ? AND rv.rating = ? " +
                    "ORDER BY rv.created_at DESC " +
                    "LIMIT 10 OFFSET ?";
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();
            if (con != null) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại!");
            }

            ps = con.prepareStatement(query);
            ps.setInt(1, foodId);
            if (option == 0) {
                ps.setInt(2, offset);
            } else {
                ps.setInt(2, option); // rating
                ps.setInt(3, offset);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                ReviewFood review = new ReviewFood(
                        rs.getString("name"),
                        rs.getInt("review_id"),
                        rs.getInt("food_id"),
                        rs.getInt("rating"),
                        rs.getInt("account_id"),
                        rs.getDate("created_at"),
                        rs.getString("comment")
                );
                reviewList.add(review);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, ps, con);
        }

        return reviewList;
    }

    public int getTotalReviewCountByFoodId(int foodId) {
        String query = "SELECT COUNT(*) AS total FROM review WHERE food_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int total = 0;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, foodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn tổng số review: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, ps, con);
        }

        return total;
    }

    public static void main(String[] args) {
        ReviewDAO reviewDAO = new ReviewDAO();
        System.out.println(reviewDAO.getTotalReviewCountByFoodId(3));
    }


}
