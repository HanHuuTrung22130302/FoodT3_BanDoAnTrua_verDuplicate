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
                        resetFailedAttempts(account.getAccountId());
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
            if (LocalDateTime.now().isAfter(account.getLockTime().plusMinutes(LOCK_DURATION_MINUTES))) {
                resetFailedAttempts(account.getAccountId());
                account.setLocked(false);
                account.setLockTime(null);
                return false;
            }
            return true;
        }
        return false;
    }

    private void handleFailedLogin(Account account) {
        incrementFailedAttempts(account.getAccountId());
        account.setFailedAttempts(account.getFailedAttempts() + 1);
        if (account.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
            lockAccount(account.getAccountId());
            account.setLocked(true);
            account.setLockTime(LocalDateTime.now());
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

    private void lockAccount(int accountId) {
        String query = "UPDATE account SET is_locked = TRUE, lock_time = ? WHERE account_id = ?";
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, accountId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}