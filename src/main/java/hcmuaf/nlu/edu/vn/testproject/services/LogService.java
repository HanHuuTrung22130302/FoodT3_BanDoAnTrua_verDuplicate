package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class LogService {

    public void logActivity(int accountId, int roleId, String action, String result, String details) {
        String query = "INSERT INTO activity_logs (timestamp, account_id, role_id, action, result, details) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, accountId);
            ps.setInt(3, roleId); // Sử dụng role_id (int) thay vì role (String)
            ps.setString(4, action);
            ps.setString(5, result);
            ps.setString(6, details);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi ghi log: " + e.getMessage());
        }
    }
}