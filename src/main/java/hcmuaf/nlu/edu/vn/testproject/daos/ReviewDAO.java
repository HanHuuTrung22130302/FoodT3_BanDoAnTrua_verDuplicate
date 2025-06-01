package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
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
    int idFood, offSetReviewFood;

    public ReviewDAO() {
        this.dataReviewFood = new HashMap<>();
        getAllReview();
    }

    public List<ReviewFood> getAll() {
        return new ArrayList<>(dataReviewFood.values());
    }

    // Hàm lấy tất cả các đánh giá từ cơ sở dữ liệu
    public void getAllReview() {
        String query = "SELECT name, rv.account_id, review_id, food_id, rating, comment, rv.created_at " +
                "FROM review rv JOIN account ac ON rv.account_id = ac.account_id";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();
            if (con == null) {
                System.err.println("Kết nối cơ sở dữ liệu thất bại!");
                return;
            }
            ps = con.prepareStatement(query);
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
            closeResources(rs, ps, con);
        }
    }

    // Lấy món ăn có nhiều đánh giá nhất
    public Food getTopReviewedFood() {
        String query = "SELECT f.food_id, f.food_name, f.price, f.discount_price, f.quantity, f.description, " +
                "f.image, f.category_id, f.ingredients, f.sold, f.views, f.created_at, f.updated_at, " +
                "COUNT(rv.review_id) as review_count " +
                "FROM review rv " +
                "JOIN food f ON rv.food_id = f.food_id " +
                "GROUP BY f.food_id, f.food_name, f.price, f.discount_price, f.quantity, f.description, " +
                "f.image, f.category_id, f.ingredients, f.sold, f.views, f.created_at, f.updated_at " +
                "ORDER BY review_count DESC LIMIT 1";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Food topReviewedFood = null;

        try {
            con = new DbContext().getConnection();
            if (con == null) {
                System.err.println("Kết nối cơ sở dữ liệu thất bại trong getTopReviewedFood!");
                return null;
            }
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                topReviewedFood = new Food(
                        rs.getInt("food_id"),
                        rs.getString("food_name"),
                        rs.getInt("price"),
                        rs.getInt("discount_price"),
                        rs.getInt("quantity"),
                        rs.getString("image"),
                        rs.getString("description"),
                        rs.getString("ingredients"),
                        rs.getInt("category_id"),
                        rs.getInt("sold"),
                        rs.getInt("views"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn món ăn có nhiều đánh giá nhất: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, ps, con);
        }
        return topReviewedFood;
    }


    // Lấy tổng số lượt đánh giá của một món ăn
    public int getTotalReviewCountByFoodId(int foodId) {
        String query = "SELECT COUNT(*) AS total FROM review WHERE food_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int total = 0;

        try {
            con = new DbContext().getConnection();
            if (con == null) {
                System.err.println("Kết nối cơ sở dữ liệu thất bại trong getTotalReviewCountByFoodId!");
                return 0;
            }
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

    // Lấy danh sách đánh giá với bộ lọc và phân trang
    public List<ReviewFood> getReviews(String filterDate, String filterSearch, String filterRating, int page, int pageSize) {
        List<ReviewFood> reviewList = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT ac.name AS customer_name, rv.account_id, rv.review_id, rv.food_id, rv.rating, rv.comment, rv.created_at, f.food_name " +
                        "FROM review rv " +
                        "JOIN account ac ON rv.account_id = ac.account_id " +
                        "JOIN food f ON rv.food_id = f.food_id " +
                        "WHERE 1=1"
        );

        if (!filterDate.isEmpty()) {
            query.append(" AND DATE(rv.created_at) = ?");
        }
        if (!filterSearch.isEmpty()) {
            query.append(" AND (f.food_name LIKE ? OR ac.name LIKE ?)");
        }
        if (!filterRating.isEmpty()) {
            query.append(" AND rv.rating = ?");
        }
        query.append(" ORDER BY rv.created_at DESC LIMIT ? OFFSET ?");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();
            if (con == null) {
                System.err.println("Kết nối cơ sở dữ liệu thất bại trong getReviews!");
                return reviewList;
            }
            ps = con.prepareStatement(query.toString());
            int paramIndex = 1;
            if (!filterDate.isEmpty()) {
                ps.setString(paramIndex++, filterDate);
            }
            if (!filterSearch.isEmpty()) {
                ps.setString(paramIndex++, "%" + filterSearch + "%");
                ps.setString(paramIndex++, "%" + filterSearch + "%");
            }
            if (!filterRating.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(filterRating));
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, (page - 1) * pageSize);

            rs = ps.executeQuery();
            while (rs.next()) {
                ReviewFood review = new ReviewFood(
                        rs.getString("customer_name"),
                        rs.getInt("review_id"),
                        rs.getInt("food_id"),
                        rs.getInt("rating"),
                        rs.getInt("account_id"),
                        rs.getDate("created_at"),
                        rs.getString("comment"),
                        rs.getString("food_name")
                );
                reviewList.add(review);
            }
            System.out.println("Số lượng đánh giá tìm thấy: " + reviewList.size());

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu trong getReviews: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, ps, con);
        }

        return reviewList;
    }

    // Đếm tổng số đánh giá với bộ lọc
    public int getTotalReviews(String filterDate, String filterSearch, String filterRating) {
        StringBuilder query = new StringBuilder(
                "SELECT COUNT(*) AS total " +
                        "FROM review rv " +
                        "JOIN account ac ON rv.account_id = ac.account_id " +
                        "JOIN food f ON rv.food_id = f.food_id " +
                        "WHERE 1=1"
        );

        if (!filterDate.isEmpty()) {
            query.append(" AND DATE(rv.created_at) = ?");
        }
        if (!filterSearch.isEmpty()) {
            query.append(" AND (f.food_name LIKE ? OR ac.name LIKE ?)");
        }
        if (!filterRating.isEmpty()) {
            query.append(" AND rv.rating = ?");
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int total = 0;

        try {
            con = new DbContext().getConnection();
            if (con == null) {
                System.err.println("Kết nối cơ sở dữ liệu thất bại trong getTotalReviews!");
                return total;
            }
            ps = con.prepareStatement(query.toString());
            int paramIndex = 1;
            if (!filterDate.isEmpty()) {
                ps.setString(paramIndex++, filterDate);
            }
            if (!filterSearch.isEmpty()) {
                ps.setString(paramIndex++, "%" + filterSearch + "%");
                ps.setString(paramIndex++, "%" + filterSearch + "%");
            }
            if (!filterRating.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(filterRating));
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
            System.out.println("Tổng số đánh giá: " + total);

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn tổng số đánh giá: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, ps, con);
        }

        return total;
    }

    // Xóa đánh giá theo review_id
    public void deleteReview(int reviewId) {
        String query = "DELETE FROM review WHERE review_id = ?";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = new DbContext().getConnection();
            if (con == null) {
                System.err.println("Kết nối cơ sở dữ liệu thất bại trong deleteReview!");
                return;
            }
            ps = con.prepareStatement(query);
            ps.setInt(1, reviewId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                dataReviewFood.remove(reviewId);
                System.out.println("Đã xóa đánh giá ID: " + reviewId);
            } else {
                System.out.println("Không tìm thấy đánh giá ID: " + reviewId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa đánh giá: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(null, ps, con);
        }
    }

    public List<Integer> getTop4FoodRate() {
        Map<Integer, Long> countStarLS = dataReviewFood.values().stream()
                .filter(rv -> rv.getRating() == 5)
                .collect(Collectors.groupingBy(ReviewFood::getFoodId, Collectors.counting()));
        List<Integer> lsIdFood = countStarLS.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(4)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return lsIdFood;
    }

    public List<Integer> getTopFoodRate() {
        Map<Integer, Long> countStarLS = dataReviewFood.values().stream()
                .filter(rv -> rv.getRating() == 5)
                .collect(Collectors.groupingBy(ReviewFood::getFoodId, Collectors.counting()));
        List<Integer> lsIdFood = countStarLS.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return lsIdFood;
    }

    public List<ReviewFood> getReviewByFood(int foodId, int option, int offset) {
        List<ReviewFood> reviewList = new ArrayList<>();
        String query;

        if (option == 0) {
            query = "SELECT name, rv.account_id, review_id, food_id, rating, comment, rv.created_at " +
                    "FROM review rv " +
                    "JOIN account ac ON rv.account_id = ac.account_id " +
                    "WHERE rv.food_id = ? " +
                    "ORDER BY rv.created_at DESC " +
                    "LIMIT 10 OFFSET ?";
        } else {
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
                ps.setInt(2, option);
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

    public int getTotalReviewCountByFoodId(int foodId, int option) {
        String query;
        if (option == 0) {
            query = "SELECT COUNT(*) AS total FROM review WHERE food_id = ?";
        } else {
            query = "SELECT COUNT(*) AS total FROM review WHERE food_id = ? AND rating = ?";
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int total = 0;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, foodId);
            if (option != 0) {
                ps.setInt(2, option);
            }
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
        Food topFood = reviewDAO.getTopReviewedFood();

        if (topFood != null) {
            int reviewCount = reviewDAO.getTotalReviewCountByFoodId(topFood.getFoodId());
            System.out.println("Món ăn có nhiều đánh giá nhất:");
            System.out.println("Tên: " + topFood.getFoodName());
            System.out.println("Giá: " + topFood.getPrice());
            System.out.println("Thành phần: " + topFood.getIngredients());
            System.out.println("Số lượt đánh giá: " + reviewCount);
        } else {
            System.out.println("Không tìm thấy món ăn nào có đánh giá.");
        }
    }
}