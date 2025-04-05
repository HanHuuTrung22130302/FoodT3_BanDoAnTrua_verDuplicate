package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> getCategories() {

        List<Category> categoryList = new ArrayList<Category>();
        String query = "SELECT * FROM category";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                categoryList.add(
                        new Category(rs.getInt("category_id"),
                                     rs.getString("category_name"),
                                     rs.getString("description")));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo rằng kết nối, câu lệnh và result set được đóng đúng cách
            closeResources(rs, ps, con);
        }
        return categoryList;
    }

    public boolean addCategory(Category category) {
        String query = "INSERT INTO category (category_name, description) VALUES (?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi thêm danh mục: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, ps, con);
        }
    }

    public boolean deleteCategory(int categoryId) {
        String query = "DELETE FROM category WHERE category_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, categoryId);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi xóa danh mục: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, ps, con);
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
}
