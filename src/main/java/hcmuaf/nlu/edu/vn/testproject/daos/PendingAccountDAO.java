package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.libs.MD5;
import hcmuaf.nlu.edu.vn.testproject.models.PendingAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PendingAccountDAO {
    public void insertPendingAccount(PendingAccount pendingAccount) {
        String query = "INSERT INTO pending_accounts (name, password, email, token, expiry_time) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        Connection conn = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, pendingAccount.getName());
            ps.setString(2, MD5.getMD5(pendingAccount.getPassword()));
            ps.setString(3, pendingAccount.getEmail());
            ps.setString(4, pendingAccount.getToken());
            ps.setTimestamp(5, Timestamp.valueOf(pendingAccount.getExpiryTime()));
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

    public PendingAccount getPendingAccountByToken(String token) {
        String query = "SELECT * FROM pending_accounts WHERE token = ?";
        PreparedStatement ps = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new PendingAccount(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("token"),
                        rs.getTimestamp("expiry_time").toLocalDateTime()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void deletePendingAccount(String token) {
        String query = "DELETE FROM pending_accounts WHERE token = ?";
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
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
}