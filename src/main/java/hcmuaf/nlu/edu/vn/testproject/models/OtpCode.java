package hcmuaf.nlu.edu.vn.testproject.models;

import java.time.LocalDateTime;

public class OtpCode {
    private int id;
    private int accountId;
    private String otpCode;
    private LocalDateTime expiryTime;

    public OtpCode() {
    }

    public OtpCode(int accountId, String otpCode, LocalDateTime expiryTime) {
        this.accountId = accountId;
        this.otpCode = otpCode;
        this.expiryTime = expiryTime;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}