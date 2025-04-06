package hcmuaf.nlu.edu.vn.testproject.models;

import java.sql.Timestamp;

public class LogEntry {
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

    // Getters
    public Timestamp getTimestamp() { return timestamp; }
    public int getAccountId() { return accountId; }
    public int getRoleId() { return roleId; }
    public String getRoleName() { return roleName; }
    public String getAction() { return action; }
    public String getResult() { return result; }
    public String getDetails() { return details; }
}