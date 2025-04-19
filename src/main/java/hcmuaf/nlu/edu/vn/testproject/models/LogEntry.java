package hcmuaf.nlu.edu.vn.testproject.models;

import java.sql.Timestamp;

public class LogEntry {
    private int logId;
    private Timestamp timestamp;
    private int accountId;
    private int roleId;
    private String roleName;
    private String action;
    private String result;
    private String details;

    public LogEntry(Timestamp timestamp, int accountId, int roleId, String roleName, String action, String result, String details) {
        this.timestamp = timestamp;
        this.accountId = accountId;
        this.roleId = roleId;
        this.roleName = roleName;
        this.action = action;
        this.result = result;
        this.details = details;
    }

    public LogEntry() {
    }

    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "logId=" + logId +
                ", timestamp=" + timestamp +
                ", accountId=" + accountId +
                ", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", action='" + action + '\'' +
                ", result='" + result + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}