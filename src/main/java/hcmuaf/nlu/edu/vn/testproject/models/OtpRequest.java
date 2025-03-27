package hcmuaf.nlu.edu.vn.testproject.models;

import java.time.LocalDateTime;

public class OtpRequest {
    private int otpId;
    private int accountId;
    private String otpCode;
    private LocalDateTime expiryTime;

    public OtpRequest() {
    }

    public OtpRequest(int accountId, String otpCode, LocalDateTime expiryTime) {
        this.accountId = accountId;
        this.otpCode = otpCode;
        this.expiryTime = expiryTime;
    }

    public int getOtpId() {
        return otpId;
    }

    public void setOtpId(int otpId) {
        this.otpId = otpId;
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

    @Override
    public String toString() {
        return "OtpRequest{" +
                "otpId=" + otpId +
                ", accountId=" + accountId +
                ", otpCode='" + otpCode + '\'' +
                ", expiryTime=" + expiryTime +
                '}';
    }
}
