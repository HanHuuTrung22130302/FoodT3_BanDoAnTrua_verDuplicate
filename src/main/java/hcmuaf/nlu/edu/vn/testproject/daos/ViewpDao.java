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

public class ViewpDao {
    int id;

    public ViewpDao(int id) {
        this.id = id;
        getUReview(id);
        getUFoodReview(id);
    }

    // Hàm lấy tất cả các món ăn từ cơ sở dữ liệu
    public void getUReview(int id) {

        String query = "INSERT INTO product_view (food_id, account_id, view_time)\n" +
                "VALUES (?, 1, NOW());\n";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Tạo kết nối cơ sở dữ liệu
            con = new DbContext().getConnection();

            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(ps, con);
        }
    }

    public void getUFoodReview(int id) {

        String query = "UPDATE food f\n" +
                "SET f.views = (\n" +
                "  SELECT COUNT(*) + 1\n" +
                "  FROM product_view pv\n" +
                "  WHERE pv.food_id = f.food_id\n" +
                ")\n" +
                "WHERE f.food_id = ?;";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Tạo kết nối cơ sở dữ liệu
            con = new DbContext().getConnection();

            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(ps, con);
        }
    }

    private void closeResources(PreparedStatement ps, Connection con) {
        try {
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ViewpDao viewpDao = new ViewpDao(1);
    }
}
