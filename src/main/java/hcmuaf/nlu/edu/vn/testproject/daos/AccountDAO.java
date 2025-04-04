package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.libs.MD5;
import hcmuaf.nlu.edu.vn.testproject.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {
    // Check email tồn tại hay ko
    public Account getUserByEmail(String email) {
        String query = "SELECT * FROM account WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getInt("role_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                account.setLoginType(rs.getString("login_type"));
                account.setFailedAttempts(rs.getInt("failed_attempts"));
                account.setLocked(rs.getBoolean("is_locked"));
                account.setLockTime(rs.getTimestamp("lock_time").toLocalDateTime());
                account.setDeleted(rs.getBoolean("is_deleted"));
                return account;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getUserById(int userId) {
        String query = "SELECT * FROM account WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getInt("role_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                account.setLoginType(rs.getString("login_type"));
                return account;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getUserByName(String name) {
        String query = "SELECT * FROM account WHERE name = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getInt("role_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                account.setLoginType(rs.getString("login_type"));
                return account;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePassword(String email, String password) {
        String query = "UPDATE account SET password = ? WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        String hashedPassword = MD5.getMD5(password);
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, hashedPassword);
            ps.setString(2, email);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAccount(Account account) {
        String query = "INSERT INTO account (role_id, password, name, email, login_type) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, account.getRoleId());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getName());
            ps.setString(4, account.getEmail());
            ps.setString(5, account.getLoginType());
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

    public boolean softDeleteAccount(int accountId) {
        String query = "UPDATE account SET is_deleted = 1 WHERE account_id = ? AND is_deleted = 0";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            System.out.println("Đang thực hiện vô hiệu hóa tài khoản ID: " + accountId);
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, accountId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Kết quả vô hiệu hóa tài khoản ID " + accountId + ": " + (rowsAffected > 0 ? "Thành công" : "Thất bại"));
            
            // Kiểm tra trạng thái sau khi cập nhật
            if (rowsAffected > 0) {
                String checkQuery = "SELECT is_deleted FROM account WHERE account_id = ?";
                PreparedStatement checkPs = conn.prepareStatement(checkQuery);
                checkPs.setInt(1, accountId);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    boolean isDeleted = rs.getBoolean("is_deleted");
                    System.out.println("Trạng thái is_deleted của tài khoản ID " + accountId + " sau khi cập nhật: " + isDeleted);
                }
                checkPs.close();
            }
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Lỗi trong softDeleteAccount: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkAccountDeletedStatus(int accountId) {
        String query = "SELECT is_deleted FROM account WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, accountId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boolean isDeleted = rs.getBoolean("is_deleted");
                System.out.println("Trạng thái is_deleted của tài khoản ID " + accountId + ": " + isDeleted);
                return isDeleted;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Lỗi trong checkAccountDeletedStatus: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean lockAccount(int accountId, int hours) {
        String query = "UPDATE account SET is_locked = 1, lock_time = DATE_ADD(NOW(), INTERVAL ? HOUR) WHERE account_id = ? AND is_deleted = 0";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            System.out.println("Đang thực hiện chặn tài khoản ID: " + accountId + " trong " + hours + " giờ");
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, hours);
            ps.setInt(2, accountId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Kết quả chặn tài khoản ID " + accountId + ": " + (rowsAffected > 0 ? "Thành công" : "Thất bại"));
            
            // Kiểm tra trạng thái sau khi cập nhật
            if (rowsAffected > 0) {
                String checkQuery = "SELECT is_locked, lock_time FROM account WHERE account_id = ?";
                PreparedStatement checkPs = conn.prepareStatement(checkQuery);
                checkPs.setInt(1, accountId);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    boolean isLocked = rs.getBoolean("is_locked");
                    java.sql.Timestamp lockTime = rs.getTimestamp("lock_time");
                    System.out.println("Trạng thái is_locked của tài khoản ID " + accountId + " sau khi cập nhật: " + isLocked);
                    System.out.println("Thời gian chặn của tài khoản ID " + accountId + ": " + lockTime);
                }
                checkPs.close();
            }
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Lỗi trong lockAccount: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean unlockAccount(int accountId) {
        String query = "UPDATE account SET is_locked = 0, lock_time = NULL WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            System.out.println("Đang thực hiện hủy chặn tài khoản ID: " + accountId);
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, accountId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Kết quả hủy chặn tài khoản ID " + accountId + ": " + (rowsAffected > 0 ? "Thành công" : "Thất bại"));
            
            // Kiểm tra trạng thái sau khi cập nhật
            if (rowsAffected > 0) {
                String checkQuery = "SELECT is_locked FROM account WHERE account_id = ?";
                PreparedStatement checkPs = conn.prepareStatement(checkQuery);
                checkPs.setInt(1, accountId);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    boolean isLocked = rs.getBoolean("is_locked");
                    System.out.println("Trạng thái is_locked của tài khoản ID " + accountId + " sau khi cập nhật: " + isLocked);
                }
                checkPs.close();
            }
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Lỗi trong unlockAccount: " + e.getMessage());
            e.printStackTrace();
            return false;
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
