package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Food;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDao {
    public boolean sendContact(int id, String name, String email, String title, String message) {

        String query = "INSERT INTO contact (account_id, full_name, email ,title, content) VALUES (?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;
        int row = 0;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, title);
            ps.setString(5, message);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo rằng kết nối, câu lệnh và result set được đóng đúng cách
            closeResources(ps, con);
        }
        return row > 0;
    }

    private void closeResources(PreparedStatement ps, Connection con) {
        try {

            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }
}
