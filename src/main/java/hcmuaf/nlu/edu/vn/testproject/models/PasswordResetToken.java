package hcmuaf.nlu.edu.vn.testproject.models;

import java.time.LocalDateTime;

public class PasswordResetToken {
    private int tokenId;
    private boolean isUsed;
    private String token;
    private LocalDateTime experyTime;
    private int accountId;

    public PasswordResetToken() {
    }

    public PasswordResetToken(int tokenId, boolean isUsed, String token, LocalDateTime experyTime, int accountId) {
        this.tokenId = tokenId;
        this.isUsed = isUsed;
        this.token = token;
        this.experyTime = experyTime;
        this.accountId = accountId;
    }

    public PasswordResetToken(int accountId, boolean isUsed, String token, LocalDateTime experyTime) {
        this.isUsed = isUsed;
        this.token = token;
        this.experyTime = experyTime;
        this.accountId = accountId;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int id) {
        this.tokenId = id;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExperyTime() {
        return experyTime;
    }

    public void setExperyTime(LocalDateTime experyTime) {
        this.experyTime = experyTime;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
