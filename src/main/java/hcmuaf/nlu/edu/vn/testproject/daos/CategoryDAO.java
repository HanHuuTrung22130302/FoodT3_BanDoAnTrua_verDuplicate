package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Category;

import java.sql.*;
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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
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
            if (con == null) {
                return false;
            }
            ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            int result = ps.executeUpdate();
            
            if (result > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    category.setCategoryId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, ps, con);
        }
    }

    public boolean isCategoryExists(String categoryName) {
        String query = "SELECT COUNT(*) FROM category WHERE category_name = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, categoryName);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(rs, ps, con);
        }
    }

    public Category getCategoryById(int categoryId) {
        String query = "SELECT * FROM category WHERE category_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("description")
                );
            }
            return null;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeResources(rs, ps, con);
        }
    }

    private void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
