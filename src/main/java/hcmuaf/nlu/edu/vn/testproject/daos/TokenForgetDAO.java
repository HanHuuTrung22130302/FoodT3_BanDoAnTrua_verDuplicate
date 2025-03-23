package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.PasswordResetToken;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TokenForgetDAO {

    public String getFormatDate(LocalDateTime dateObj) {
        DateTimeFormatter formattedObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateObj.format(formattedObj);
        return formattedDate;
    }

    public boolean insertTokenForget(PasswordResetToken tokenForget) {
        String query = "insert into password_reset_tokens (token, expiry_time, is_used, account_id) values (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, tokenForget.getToken());
            ps.setTimestamp(2, Timestamp.valueOf(getFormatDate(tokenForget.getExperyTime())));
            ps.setBoolean(3, tokenForget.isUsed()); // Xác định token đc sử dụng chưa
            ps.setInt(4, tokenForget.getUserId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public PasswordResetToken getTokenPassword(String token) {
        String query = "select * from password_reset_tokens where token = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            rs = ps.executeQuery();
            while (rs.next()) {
                return new PasswordResetToken(
                        rs.getInt("token_id"),
                        rs.getBoolean("is_used"),
                        rs.getString("token"),
                        rs.getTimestamp("expiry_time").toLocalDateTime(),
                        rs.getInt("account_id")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStatus(PasswordResetToken token) {
        String query = "update password_reset_tokens set is_used = ? where token = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setBoolean(1, token.isUsed());
            ps.setString(2, token.getToken());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}