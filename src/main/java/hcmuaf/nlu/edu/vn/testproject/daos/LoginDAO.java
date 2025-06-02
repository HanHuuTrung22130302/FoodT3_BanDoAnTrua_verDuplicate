package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.libs.MD5;
import hcmuaf.nlu.edu.vn.testproject.models.Account;

import java.sql.*;
import java.time.LocalDateTime;

public class LoginDAO {
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;

    public Account login(String name, String password) {
        String query = "SELECT * FROM account WHERE name = ?";
        String hashedPassword = MD5.getMD5(password);

        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account account = extractAccountFromResultSet(rs);
                    if (isAccountDeleted(account)) {
                        return null;
                    }
                    if (isAccountLocked(account)) {
                        return null;
                    }
                    if (account.getPassword().equals(hashedPassword)) {
                        if (!account.isLocked()) {
                            resetFailedAttempts(account.getAccountId());
                        }
                        return account;
                    } else {
                        handleFailedLogin(account);
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Account extractAccountFromResultSet(ResultSet rs) throws SQLException {
        Account account = new Account(
                rs.getInt("account_id"),
                rs.getInt("role_id"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
        account.setFailedAttempts(rs.getInt("failed_attempts"));
        account.setLocked(rs.getBoolean("is_locked"));
        account.setDeleted(rs.getBoolean("is_deleted"));
        Timestamp lockTime = rs.getTimestamp("lock_time");
        if (lockTime != null) {
            account.setLockTime(lockTime.toLocalDateTime());
        }
        return account;
    }

    private boolean isAccountLocked(Account account) {
        if (account.isLocked()) {
            if (account.getLockTime() != null) {
                if (LocalDateTime.now().isAfter(account.getLockTime().plusMinutes(LOCK_DURATION_MINUTES))) {
                    String query = "UPDATE account SET is_locked = FALSE, lock_time = NULL, failed_attempts = 0 WHERE account_id = ?";
                    executeUpdate(query, account.getAccountId());
                    account.setLocked(false);
                    account.setLockTime(null);
                    account.setFailedAttempts(0);
                    return false;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private void handleFailedLogin(Account account) {
        try (Connection con = new DbContext().getConnection()) {
            String updateQuery = "UPDATE account SET failed_attempts = failed_attempts + 1 WHERE account_id = ?";
            try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                ps.setInt(1, account.getAccountId());
                ps.executeUpdate();
            }

            String selectQuery = "SELECT failed_attempts FROM account WHERE account_id = ?";
            try (PreparedStatement ps = con.prepareStatement(selectQuery)) {
                ps.setInt(1, account.getAccountId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int newFailedAttempts = rs.getInt("failed_attempts");
                        account.setFailedAttempts(newFailedAttempts);
                        
                        if (newFailedAttempts >= MAX_FAILED_ATTEMPTS) {
                            lockAccount(account.getAccountId());
                            account.setLocked(true);
                            account.setLockTime(LocalDateTime.now());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lockAccount(int accountId) {
        String query = "UPDATE account SET is_locked = TRUE, lock_time = ?, failed_attempts = ? WHERE account_id = ?";
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, MAX_FAILED_ATTEMPTS);
            ps.setInt(3, accountId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Account getAccountByName(String name) {
        String query = "SELECT * FROM account WHERE name = ?";
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractAccountFromResultSet(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void incrementFailedAttempts(int accountId) {
        String query = "UPDATE account SET failed_attempts = failed_attempts + 1 WHERE account_id = ?";
        executeUpdate(query, accountId);
    }

    public void resetFailedAttempts(int accountId) {
        String query = "UPDATE account SET failed_attempts = 0, is_locked = FALSE, lock_time = NULL WHERE account_id = ?";
        executeUpdate(query, accountId);
    }

    private void executeUpdate(String query, int param) {
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, param);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAccountDeleted(Account account) {
        return account.isDeleted();
    }

    public Account loginByEmail(String email, String password) {
        String query = "SELECT * FROM account WHERE email = ?";
        String hashedPassword = MD5.getMD5(password);

        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account account = extractAccountFromResultSet(rs);
                    if (isAccountDeleted(account)) {
                        return null;
                    }
                    if (isAccountLocked(account)) {
                        return null;
                    }
                    if (account.getPassword().equals(hashedPassword)) {
                        if (!account.isLocked()) {
                            resetFailedAttempts(account.getAccountId());
                        }
                        return account;
                    } else {
                        handleFailedLogin(account);
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getAccountByEmail(String email) {
        String query = "SELECT * FROM account WHERE email = ?";
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractAccountFromResultSet(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}