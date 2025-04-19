package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.LogEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogDAO {
    public List<LogEntry> getLogs(String filterRoleId, Date filterDate, String filterAction) {
        List<LogEntry> logs = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT al.log_id, al.timestamp, al.account_id, al.role_id, r.role_name, al.action, al.result, al.details ");
        query.append("FROM activity_logs al ");
        query.append("JOIN role r ON al.role_id = r.role_id ");
        query.append("WHERE 1=1 ");

        if (filterRoleId != null && !filterRoleId.equals("all")) {
            query.append("AND al.role_id = ? ");
        }
        if (filterDate != null) {
            query.append("AND DATE(al.timestamp) = ? ");
        }
        if (filterAction != null && !filterAction.isEmpty()) {
            query.append("AND al.action LIKE ? ");
        }
        query.append("ORDER BY al.timestamp DESC");

        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            
            int paramIndex = 1;
            if (filterRoleId != null && !filterRoleId.equals("all")) {
                ps.setInt(paramIndex++, Integer.parseInt(filterRoleId));
            }
            if (filterDate != null) {
                java.sql.Date sqlDate = new java.sql.Date(filterDate.getTime());
                ps.setDate(paramIndex++, sqlDate);
            }
            if (filterAction != null && !filterAction.isEmpty()) {
                ps.setString(paramIndex++, "%" + filterAction + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LogEntry log = new LogEntry(
                    rs.getTimestamp("timestamp"),
                    rs.getInt("account_id"),
                    rs.getInt("role_id"),
                    rs.getString("role_name"),
                    rs.getString("action"),
                    rs.getString("result"),
                    rs.getString("details")
                );
                log.setLogId(rs.getInt("log_id"));
                logs.add(log);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi lấy log: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    public void insertLog(int accountId, int roleId, String action, String result, String details) {
        String query = "INSERT INTO activity_logs (account_id, role_id, action, result, details) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, accountId);
            ps.setInt(2, roleId);
            ps.setString(3, action);
            ps.setString(4, result);
            ps.setString(5, details);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi ghi log: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
