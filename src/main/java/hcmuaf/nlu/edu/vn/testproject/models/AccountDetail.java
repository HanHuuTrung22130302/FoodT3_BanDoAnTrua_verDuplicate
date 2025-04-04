package hcmuaf.nlu.edu.vn.testproject.models;

import java.time.LocalDateTime;

public class AccountDetail {
    private int accountId;
    private String fullName;
    private String phoneNumber;
    private String address;
    private int gender; // 0: nam, 1:nữ, 2:khác
    private String birthDate;
    private String email;
    private String loginType; // Loại đăng nhập (normal/google)
    private boolean deleted; // Trạng thái vô hiệu hóa
    private boolean locked; // Trạng thái khóa
    private LocalDateTime lockTime;
    private int roleId;

    public AccountDetail() {
        this.loginType = "normal"; // Mặc định là đăng nhập thông thường
        this.deleted = false;
        this.locked = false;
    }

    public AccountDetail(int accountId, String fullName, String phoneNumber, String address, int gender, String birthDate) {
        this.accountId = accountId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public AccountDetail(int accountId, String fullName, String phoneNumber, String address, int gender, String birthDate, String email) {
        this.accountId = accountId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.loginType = "normal"; // Mặc định là đăng nhập thông thường
        this.deleted = false;
        this.locked = false;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "AccountDetail{" +
                "accountId=" + accountId +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", birthDate='" + birthDate + '\'' +
                ", email='" + email + '\'' +
                ", loginType='" + loginType + '\'' +
                ", deleted=" + deleted +
                ", locked=" + locked +
                ", lockTime=" + lockTime +
                ", roleId=" + roleId +
                '}';
    }
}
