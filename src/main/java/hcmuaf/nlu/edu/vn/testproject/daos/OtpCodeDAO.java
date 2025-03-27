package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.OtpCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OtpCodeDAO {
    public void insertOtpCode(OtpCode otpCode) {
        String query = "INSERT INTO otp_codes (account_id, otp_code, expiry_time) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, otpCode.getAccountId());
            ps.setString(2, otpCode.getOtpCode());
            ps.setTimestamp(3, Timestamp.valueOf(otpCode.getExpiryTime()));
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

    public OtpCode getOtpCodeByAccountId(int accountId) {
        String query = "SELECT * FROM otp_codes WHERE account_id = ? ORDER BY expiry_time DESC LIMIT 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, accountId);
            rs = ps.executeQuery();
            if (rs.next()) {
                OtpCode otpCode = new OtpCode();
                otpCode.setId(rs.getInt("id"));
                otpCode.setAccountId(rs.getInt("account_id"));
                otpCode.setOtpCode(rs.getString("otp_code"));
                otpCode.setExpiryTime(rs.getTimestamp("expiry_time").toLocalDateTime());
                return otpCode;
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

    public void deleteOtpCode(int accountId) {
        String query = "DELETE FROM otp_codes WHERE account_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, accountId);
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