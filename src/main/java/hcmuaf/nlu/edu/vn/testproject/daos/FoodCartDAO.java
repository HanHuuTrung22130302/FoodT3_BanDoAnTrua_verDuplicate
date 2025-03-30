package hcmuaf.nlu.edu.vn.testproject.daos;


import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.Item;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FoodCartDAO implements FoodService {


    @Override
    public Food getFoodByID(int id) {
        String query = "SELECT * FROM food WHERE food_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Food food = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();


            if (rs.next()) {
                food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setFoodName(rs.getString("food_name"));
                food.setPrice(rs.getInt("price"));
                food.setImage(rs.getString("image"));
                food.setDescription(rs.getString("description"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, ps, con);
        }
        return food;
    }


    // Thêm món ăn vào giỏ hàng
    public void addToCart(int accountId, int foodId, int quantity) {
        String query = "INSERT INTO cart (account_id, food_id, quantity) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity), updated_at = NOW()";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, accountId);
            ps.setInt(2, foodId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(null, ps, con);
        }
    }


    // Cập nhật số lượng món ăn trong giỏ hàng
    public void updateCartItem(int accountId, int foodId, int quantity) {
        String query = "UPDATE cart SET quantity = ?, updated_at = NOW() WHERE account_id = ? AND food_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, quantity);
            ps.setInt(2, accountId);
            ps.setInt(3, foodId);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(null, ps, con);
        }
    }


    // Xóa món ăn khỏi giỏ hàng
    public void removeFromCart(int accountId, int foodId) {
        String query = "DELETE FROM cart WHERE account_id = ? AND food_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, accountId);
            ps.setInt(2, foodId);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(null, ps, con);
        }
    }


    // Xóa toàn bộ giỏ hàng
    public void clearCart(int accountId) {
        String query = "DELETE FROM cart WHERE account_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, accountId);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(null, ps, con);
        }
    }


    // Lấy danh sách món ăn trong giỏ hàng
    public List<Item> getCartItems(int accountId) {
        List<Item> items = new ArrayList<>();
        String query = "SELECT c.food_id, c.quantity, f.food_name, f.price, f.image, f.description " +
                "FROM cart c JOIN food f ON c.food_id = f.food_id WHERE c.account_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, accountId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Food food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setFoodName(rs.getString("food_name"));
                food.setPrice(rs.getInt("price"));
                food.setImage(rs.getString("image"));
                food.setDescription(rs.getString("description"));


                Item item = new Item();
                item.setFood(food);
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(food.getPrice());
                items.add(item);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, ps, con);
        }
        return items;
    }


    // Phương thức đóng tài nguyên
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

