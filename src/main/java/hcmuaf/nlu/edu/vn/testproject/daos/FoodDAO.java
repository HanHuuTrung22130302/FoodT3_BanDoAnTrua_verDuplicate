package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.models.Food;

import java.sql.*;
import java.util.*;

public class FoodDAO {

    private Map<Integer, Food> data;

    public FoodDAO() {
        this.data = new HashMap<>();
        getAllFood();
    }


    // Hàm lấy tất cả các món ăn từ cơ sở dữ liệu
    public void getAllFood() {

        String query = "SELECT * FROM food";
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

            // Duyệt qua kết quả trả về và tạo danh sách món ăn
            while (rs.next()) {
                data.put(rs.getInt("food_id"),
                        new Food(
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

    // Thêm phương thức lấy món ăn theo danh sách thành phần
    public List<Food> getFoodsByIngredients(List<String> ingredients) {
        List<Food> foodList = new ArrayList<>();
        String query = "SELECT * FROM food WHERE " +
                String.join(" AND ", Collections.nCopies(ingredients.size(), "LOWER(ingredients) LIKE ?"));

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();

            ps = con.prepareStatement(query);
            for (int i = 0; i < ingredients.size(); i++) {
                ps.setString(i + 1, "%" + ingredients.get(i).toLowerCase() + "%");
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                foodList.add(new Food(
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
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, ps, con);
        }
        return foodList;
    }


    public List<Food> getFoodsByCategory(int idCategory) {

        String query = "SELECT * FROM food WHERE category_id = ?";
        List<Food> foodList = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Tạo kết nối cơ sở dữ liệu
            con = new DbContext().getConnection();

            // Chuẩn bị câu lệnh SQL
            ps = con.prepareStatement(query);
            ps.setInt(1, idCategory);
            // Thực thi câu lệnh
            rs = ps.executeQuery();

            // Duyệt qua kết quả trả về và tạo danh sách món ăn
            while (rs.next()) {
                foodList.add(
                        new Food(
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
        return foodList;
    }

    public List<Food> searchByName(String textSearch) {
        List<Food> foodList = new ArrayList<>();
        for (Food food : data.values()) {
            if (food.getFoodName().toLowerCase().contains(textSearch.toLowerCase())) {
                foodList.add(food);
            }
        }
        return foodList;
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

    public List<Food> getPaginatedFoods(int offset, int pageSize) {
        List<Food> foodList = new ArrayList<>();
        int start = offset;
        int end = Math.min(offset + pageSize, data.size());

        List<Integer> keys = new ArrayList<>(data.keySet());
        keys.sort(Integer::compareTo); // Đảm bảo các khóa được sắp xếp tăng dần

        for (int i = start; i < end; i++) {
            foodList.add(data.get(keys.get(i)));
        }
        return foodList;
    }

    public int getTotalFoods() {
        return data.size();
    }



    public List<Food> getTop4View() {
        List<Food> foodList = new ArrayList<>(data.values());
        foodList.sort((f1, f2) -> Integer.compare(f2.getViews(), f1.getViews()));

        // Check the size of foodList and adjust the sublist range
        int toIndex = Math.min(4, foodList.size());
        List<Food> top4View = foodList.subList(0, toIndex);
        return top4View;
    }

    public List<Food> getTop4Sold() {
        List<Food> foodList = new ArrayList<>(data.values());
        foodList.sort((f1, f2) -> Integer.compare(f2.getSold(), f1.getSold()));
        int toIndex = Math.min(4, foodList.size());
        List<Food> top4Sold = foodList.subList(0, toIndex);
        return top4Sold;
    }

    public List<Food> getTop4Propose() {
        List<Food> foodList = new ArrayList<>(data.values());
        foodList.sort((f1, f2) -> f2.getCreatedAt().compareTo(f1.getCreatedAt()));
        int toIndex = Math.min(4, foodList.size());
        List<Food> top4Propose = foodList.subList(0, toIndex);
        return top4Propose;
    }

    public Food getById(int id) {
        if (!data.containsKey(id)) return null;
        return data.get(id);
    }


    public List<Food> getAll() {
        return new ArrayList<>(data.values());
    }

    public List<Food> getTopSold() {
        List<Food> foodList = new ArrayList<>(data.values());
        foodList.sort((f1, f2) -> Integer.compare(f2.getSold(), f1.getSold()));
        return foodList;
    }

    public List<Food> getTopView() {
        List<Food> foodList = new ArrayList<>(data.values());
        foodList.sort((f1, f2) -> Integer.compare(f2.getViews(), f1.getViews()));
        return foodList;
    }

    public List<Food> getTopPropose() {
        List<Food> foodList = new ArrayList<>(data.values());
        foodList.sort((f1, f2) -> f2.getCreatedAt().compareTo(f1.getCreatedAt()));
        return foodList;
    }

    // Phương thức xóa món ăn
    public void deleteFood(int idFood) {
        String query = "DELETE FROM food WHERE food_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idFood);
            ps.executeUpdate();

            data.remove(idFood); // Xóa món ăn khỏi danh sách trong bộ nhớ
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // phương thức thêm món ăn
    public boolean addFood(Food food) {
        String query = "INSERT INTO food (category_id, food_name, price, discount_price, " +
                "image, description,ingredients, quantity, sold, created_at, updated_at, views) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, food.getCategoryId());
            ps.setString(2, food.getFoodName());
            ps.setInt(3, food.getPrice());
            ps.setObject(4, food.getDiscountPrice()); // NULL nếu không có
            ps.setString(5, food.getImage());
            ps.setString(6, food.getDescription());
            ps.setString(7, food.getIngredients());
            ps.setObject(8, food.getQuantity());
            ps.setInt(9, food.getSold());
            ps.setTimestamp(10, food.getCreatedAt());
            ps.setObject(11, food.getUpdatedAt());
            ps.setInt(12, food.getViews());

            int rowInserted = ps.executeUpdate();
            return rowInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(null, ps, conn);
        }
    }

    // Phuơng thức cập nhật thông tin món ăn
    public boolean updateFood(Food food) {
        String query = "UPDATE food SET category_id = ?, food_name = ?, price = ?, image = ?, " +
                "description = ?, ingredients = ?,  updated_at = ? WHERE food_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);

            ps.setInt(1, food.getCategoryId());
            ps.setString(2, food.getFoodName());
            ps.setInt(3, food.getPrice());
            ps.setString(4, food.getImage());
            ps.setString(5, food.getDescription());
            ps.setString(6, food.getIngredients());
            ps.setTimestamp(7, food.getUpdatedAt());
            ps.setInt(8, food.getFoodId());

            int rowUpdated = ps.executeUpdate();
            return rowUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(null, ps, conn);
        }
    }
}
