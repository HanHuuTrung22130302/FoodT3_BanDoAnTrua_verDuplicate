package hcmuaf.nlu.edu.vn.testproject.models;

public class UserStatus {
    public int roleId;
    public int isLocked;
    public int isDeleted;

    public UserStatus(int roleId, int isLocked, int isDeleted) {
        this.roleId = roleId;
        this.isLocked = isLocked;
        this.isDeleted = isDeleted;
    }
}