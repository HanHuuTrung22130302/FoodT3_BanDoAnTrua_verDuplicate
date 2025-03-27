package hcmuaf.nlu.edu.vn.testproject.models;

import java.time.LocalDateTime;

public class Account {
    private int accountId;
    private int roleId;
    private String password;
    private String name;
    private String email;
    private AccountDetail accountDetail;
    private int failedAttempts; // Số lần đăng nhập sai
    private boolean isLocked; // Trạng thái khóa
    private LocalDateTime lockTime; // Thời gian khóa

    public Account() {
    }

    public Account(int accountId, int roleId, String password, String name, String email) {
        this.accountId = accountId;
        this.roleId = roleId;
        this.password = password;
        this.name = name;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountDetail getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(AccountDetail accountDetail) {
        this.accountDetail = accountDetail;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", roleId=" + roleId +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", accountDetail=" + accountDetail +
                ", failedAttempts=" + failedAttempts +
                ", isLocked=" + isLocked +
                ", lockTime=" + lockTime +
                '}';
    }
}