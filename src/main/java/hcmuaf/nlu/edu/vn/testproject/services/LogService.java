package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.LogDAO;
import hcmuaf.nlu.edu.vn.testproject.models.LogEntry;

import java.util.Date;
import java.util.List;

public class LogService {
    private LogDAO logDAO;

    public LogService() {
        this.logDAO = new LogDAO();
    }

    public List<LogEntry> getLogs(String filterRoleId, Date filterDate, String filterAction) {
        return logDAO.getLogs(filterRoleId, filterDate, filterAction);
    }

    public void logActivity(int accountId, int roleId, String action, String result, String details) {
        // Chỉ hiển thị log cho những hành động quan trọng
        if (action.contains("Thêm") || action.contains("Xóa") || action.contains("Cập nhật") || 
            action.contains("Đăng nhập") || action.contains("Đăng xuất")) {
            logDAO.insertLog(accountId, roleId, action, result, details);
        }
    }
}