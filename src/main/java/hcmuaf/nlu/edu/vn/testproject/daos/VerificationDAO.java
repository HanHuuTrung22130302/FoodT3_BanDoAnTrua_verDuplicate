package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class VerificationDAO {
    public void insertVerificationToken(int account_id, String token, LocalDateTime expiryTime) {
        String query = "INSERT INTO verify_tokens (account_id, token, expiry_time, is_used) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        Connection conn = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, account_id);
            ps.setString(2, token);
            ps.setTimestamp(3, Timestamp.valueOf(expiryTime));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean verifyToken(String token) {
        String query = "SELECT * FROM verify_tokens WHERE token = ? AND is_used = 0 AND expiry_time > NOW()";
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Đánh dấu token đã sử dụng
                String updateQuery = "UPDATE verify_tokens SET is_used = 1 WHERE token = ?";
                ps = conn.prepareStatement(updateQuery);
                ps.setString(1, token);
                ps.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getAccountIdByToken(String token) {
        String query = "SELECT account_id FROM verify_tokens WHERE token = ?";
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("account_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
