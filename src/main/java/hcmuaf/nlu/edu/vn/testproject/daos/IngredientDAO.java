package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.dto.IngredientDTO;
import hcmuaf.nlu.edu.vn.testproject.models.Ingredients;
import hcmuaf.nlu.edu.vn.testproject.models.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Ingredients> getNearlyExpiredIngredients() throws SQLException {
        List<Ingredients> list = new ArrayList<>();
        String query = "SELECT * FROM Ingredients WHERE DATEDIFF(expiration_date, CURDATE()) <= 5";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()){
            while  (rs.next()) {
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Ingredients> getMostUsedIngredients() throws SQLException {
        List<Ingredients> list = new ArrayList<>();
        Connection connection = null;
        String sql = "SELECT i.*, SUM(u.used_amount) AS total_used " +
                "FROM Ingredients i JOIN UsedIngredients u ON i.id = u.ingredient_id " +
                "GROUP BY i.id ORDER BY total_used DESC LIMIT 5";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ingredients ing = new Ingredients(
                    rs.getInt("ingredient_id"),
                    rs.getString("ingredient_name"),
                    rs.getDouble("amount"),
                    rs.getDouble("price"),
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getDate("import_date"),
                    rs.getDate("expiration_date")
            );
            list.add(ing);
        }
        return list;
    }

    public Map<Integer, List<Ingredients>> getIngredientsGroupedBySupplier() {
        Map<Integer, List<Ingredients>> result = new HashMap<>();
        String query = "SELECT * FROM ingredients ORDER BY supplier_id";

        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ingredients ing = new Ingredients(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getDouble("amount"),
                        rs.getDouble("price"),
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getDate("import_date"),
                        rs.getDate("expiration_date")
                );

                int supplierId = ing.getSupplierId();
                result.computeIfAbsent(supplierId, k -> new ArrayList<>()).add(ing);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Ingredients> getIngredientsBySupplierId(int supplierId) {
        List<Ingredients> list = new ArrayList<>();
        String query = "SELECT * FROM ingredients WHERE supplier_id = ?";

        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, supplierId);
            ResultSet rs = ps.executeQuery();
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

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers WHERE status = 1"; // Chỉ lấy các nhà cung cấp có status = 1
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setSupplierName(rs.getString("supplier_name"));
                supplier.setAddress(rs.getString("address"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setEmail(rs.getString("email"));
                supplier.setStatus(rs.getInt("status"));
                suppliers.add(supplier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public void addIngredient(int supplierId, String supplierName, String ingredientName, double amount, double price, String importDate, String expirationDate) throws SQLException {
        String insertQuery = "INSERT INTO ingredients (ingredient_name, amount, price, supplier_id, supplier_name, import_date, expiration_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = new DbContext().getConnection()) {
            // Bắt đầu giao dịch
            conn.setAutoCommit(false);

            try {
                // Thêm bản ghi mới, để MySQL tự tăng ingredient_id
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, ingredientName);
                insertStmt.setDouble(2, amount);
                insertStmt.setDouble(3, price);
                insertStmt.setInt(4, supplierId);
                insertStmt.setString(5, supplierName);
                insertStmt.setString(6, importDate);
                insertStmt.setString(7, expirationDate);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Thêm nguyên liệu thành công: ingredient_name = " + ingredientName);
                } else {
                    throw new SQLException("Không thể thêm nguyên liệu!");
                }

                // Commit giao dịch
                conn.commit();
            } catch (SQLException e) {
                // Rollback nếu có lỗi
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<IngredientDTO> getIngredientsDTOBySupplierId(int supplierId) {
        List<IngredientDTO> list = new ArrayList<>();
        String query = "SELECT ingredient_id, ingredient_name FROM ingredients WHERE supplier_id = ? GROUP BY ingredient_id, ingredient_name";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, supplierId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                IngredientDTO dto = new IngredientDTO(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name")
                );
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getIngredientNameById(int ingredientId) throws SQLException {
        String query = "SELECT ingredient_name FROM ingredients WHERE ingredient_id = ? LIMIT 1";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ingredientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("ingredient_name");
            }
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSupplierNameById(int supplierId) throws SQLException {
        String query = "SELECT supplier_name FROM suppliers WHERE supplier_id = ? LIMIT 1";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, supplierId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("supplier_name");
            }
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws SQLException {
        IngredientDAO dao = new IngredientDAO();
        List<IngredientDTO> list = dao.getIngredientsDTOBySupplierId(1);
        System.out.println(list);
    }
}

