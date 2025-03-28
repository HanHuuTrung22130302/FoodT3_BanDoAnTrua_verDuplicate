package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.LogEntry;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LogManagement", value = "/LogManagement")
public class LogManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filterRoleId = request.getParameter("filterRoleId");
        String filterDate = request.getParameter("filterDate");
        String filterAction = request.getParameter("filterAction");

        List<LogEntry> logs = fetchLogs(filterRoleId, filterDate, filterAction);

        request.setAttribute("logs", logs);
        request.getRequestDispatcher("views/log_management.jsp").forward(request, response);
    }

    private List<LogEntry> fetchLogs(String filterRoleId, String filterDate, String filterAction) {
        List<LogEntry> logs = new ArrayList<>();
        String query = "SELECT al.*, r.role_name FROM activity_logs al JOIN role r ON al.role_id = r.role_id WHERE 1=1";
        if (filterRoleId != null && !filterRoleId.equals("all")) {
            query += " AND al.role_id = ?";
        }
        if (filterDate != null && !filterDate.isEmpty()) {
            query += " AND DATE(al.timestamp) = ?";
        }
        if (filterAction != null && !filterAction.isEmpty()) {
            query += " AND al.action LIKE ?";
        }

        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            int paramIndex = 1;
            if (filterRoleId != null && !filterRoleId.equals("all")) {
                ps.setInt(paramIndex++, Integer.parseInt(filterRoleId));
            }
            if (filterDate != null && !filterDate.isEmpty()) {
                ps.setString(paramIndex++, filterDate);
            }
            if (filterAction != null && !filterAction.isEmpty()) {
                ps.setString(paramIndex++, "%" + filterAction + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                logs.add(new LogEntry(
                        rs.getTimestamp("timestamp"),
                        rs.getInt("account_id"),
                        rs.getInt("role_id"), // Lưu role_id
                        rs.getString("action"),
                        rs.getString("result"),
                        rs.getString("details")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi lấy log: " + e.getMessage());
        }
        return logs;
    }
}