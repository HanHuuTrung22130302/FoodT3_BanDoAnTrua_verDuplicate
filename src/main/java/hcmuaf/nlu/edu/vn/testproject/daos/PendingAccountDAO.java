package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PendingAccountDAO {
    public void insertPendingAccount(String name, String password, String email, String token, LocalDateTime expiryTime) {
        String query = "INSERT INTO pending_accounts (name, password, email, token, expiry_time) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        Connection conn = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, token);
            ps.setTimestamp(5, Timestamp.valueOf(expiryTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifyToken(String token) {
        String query = "SELECT * FROM pending_accounts WHERE token = ? AND expiry_time > NOW()";
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Account getPendingAccountByToken(String token) {
        String query = "SELECT * FROM pending_accounts WHERE token = ?";
        PreparedStatement ps = null;
        Connection conn = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            return new Account(
                    0,
                    2,
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
            );
        }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
    }
}