package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Ingredients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {

    // Lấy tất cả nguyên liệu
    public List<Ingredients> getAllIngredients() {
        List<Ingredients> ingredients = new ArrayList<>();
        String query = "SELECT * FROM ingredients";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ingredients i = new Ingredients(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getDouble("amount"),
                        rs.getDouble("price"),
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getDate("import_date"),
                        rs.getDate("expiration_date")
                );
                ingredients.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    // Tìm nguyên liệu theo ID
    public Ingredients getIngredientById(int id) {
        String query = "SELECT * FROM ingredients WHERE ingredient_id = ?";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Ingredients(
                            rs.getInt("ingredient_id"),
                            rs.getString("ingredient_name"),
                            rs.getDouble("amount"),
                            rs.getDouble("price"),
                            rs.getInt("supplier_id"),
                            rs.getString("supplier_name"),
                            rs.getDate("import_date"),
                            rs.getDate("expiration_date")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm nguyên liệu mới
    public void insertIngredient(Ingredients ingredient) {
        String query = "INSERT INTO ingredients (ingredient_name, amount, price, supplier_id, supplier_name, import_date, expiration_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, ingredient.getIngredientName());
            ps.setDouble(2, ingredient.getAmount());
            ps.setDouble(3, ingredient.getPrice());
            ps.setInt(4, ingredient.getSupplierId());
            ps.setString(5, ingredient.getSupplierName());
            ps.setDate(6, ingredient.getImportDate());
            ps.setDate(7, ingredient.getExpirationDate());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cập nhật nguyên liệu
    public void updateIngredient(Ingredients ingredient) {
        String query = "UPDATE ingredients SET ingredient_name = ?, amount = ?, price = ?, supplier_id = ?, supplier_name = ?, import_date = ?, expiration_date = ? WHERE ingredient_id = ?";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, ingredient.getIngredientName());
            ps.setDouble(2, ingredient.getAmount());
            ps.setDouble(3, ingredient.getPrice());
            ps.setInt(4, ingredient.getSupplierId());
            ps.setString(5, ingredient.getSupplierName());
            ps.setDate(6, ingredient.getImportDate());
            ps.setDate(7, ingredient.getExpirationDate());
            ps.setInt(8, ingredient.getIngredientId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xoá nguyên liệu theo ID
    public void deleteIngredient(int id) {
        String query = "DELETE FROM ingredients WHERE ingredient_id = ?";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Ingredients> getExpiredIngredients() {
        List<Ingredients> list = new ArrayList<>();
        String query = "SELECT * FROM ingredients WHERE expiration_date < CURDATE()";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ingredients i = new Ingredients(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getDouble("amount"),
                        rs.getDouble("price"),
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getDate("import_date"),
                        rs.getDate("expiration_date")
                );
                list.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}

