package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.libs.MD5;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return food;
    }

    public static void main(String[] args) {
        // Tạo đối tượng FoodCartDAO
        FoodCartDAO foodCartDAO = new FoodCartDAO();

        // Gọi phương thức getFoodByID với idFood = 1 (ví dụ)
        int foodID = 1; // Thay đổi id nếu cần
        Food food = foodCartDAO.getFoodByID(foodID);

        // Kiểm tra kết quả và in ra thông tin món ăn
        if (food != null) {
            System.out.println("Món ăn tìm thấy:");
            System.out.println("ID: " + food.getFoodId());
            System.out.println("Tên: " + food.getFoodName());
            System.out.println("Giá: " + food.getPrice() + "đ");
            System.out.println("Hình ảnh: " + food.getImage());
            System.out.println("Mô tả: " + food.getDescription());
        } else {
            System.out.println("Không tìm thấy món ăn với ID: " + foodID);
        }
    }
}
