package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.libs.MD5;
import hcmuaf.nlu.edu.vn.testproject.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AccountDAO {
    // Check email tồn tại hay không
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
            if (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getInt("role_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                account.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
                account.setLocked(rs.getBoolean("is_locked"));
                Timestamp lockTime = rs.getTimestamp("lock_time");
                if (lockTime != null) {
                    account.setLockTime(lockTime.toLocalDateTime());
                }
                return account;
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
            if (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getInt("role_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                account.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
                account.setLocked(rs.getBoolean("is_locked"));
                Timestamp lockTime = rs.getTimestamp("lock_time");
                if (lockTime != null) {
                    account.setLockTime(lockTime.toLocalDateTime());
                }
                return account;
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
            if (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getInt("role_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                account.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
                account.setLocked(rs.getBoolean("is_locked"));
                Timestamp lockTime = rs.getTimestamp("lock_time");
                if (lockTime != null) {
                    account.setLockTime(lockTime.toLocalDateTime());
                }
                return account;
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
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertAccount(Account account) {
        String query = "INSERT INTO account (role_id, password, name, email, failed_login_attempts, is_locked) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, account.getRoleId());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getName());
            ps.setString(4, account.getEmail());
            ps.setInt(5, account.getFailedLoginAttempts());
            ps.setBoolean(6, account.isLocked());
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

    // Cập nhật số lần đăng nhập sai và trạng thái khóa
    public void updateLoginAttempts(Account account) {
        String query = "UPDATE account SET failed_login_attempts = ?, is_locked = ?, lock_time = ? WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, account.getFailedLoginAttempts());
            ps.setBoolean(2, account.isLocked());
            if (account.getLockTime() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(account.getLockTime()));
            } else {
                ps.setNull(3, java.sql.Types.TIMESTAMP);
            }
            ps.setInt(4, account.getAccountId());
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

    // Reset số lần đăng nhập sai và trạng thái khóa
    public void resetLoginAttempts(Account account) {
        account.setFailedLoginAttempts(0);
        account.setLocked(false);
        account.setLockTime(null);
        updateLoginAttempts(account);
    }
}