package hcmuaf.nlu.edu.vn.testproject.models;

import java.time.LocalDateTime;

public class PendingAccount {
    private int pendingId;
    private String name;
    private String password;
    private String email;
    private String token;
    private LocalDateTime expiryTime;

    public PendingAccount() {
    }

    public PendingAccount(String name, String password, String email, String token, LocalDateTime expiryTime) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.token = token;
        this.expiryTime = expiryTime;
    }

    public int getPendingId() {
        return pendingId;
    }

    public void setPendingId(int pendingId) {
        this.pendingId = pendingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}
