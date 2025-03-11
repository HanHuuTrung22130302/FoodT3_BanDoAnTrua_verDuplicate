package hcmuaf.nlu.edu.vn.testproject.models;

public class Account {
    private int idAcc;
    private int idRole;
    private String pass;
    private String name;
    private String email;
    private AccDetail accDetail;

    public Account() {
    }

    public Account(int idAcc, int idRole, String pass, String userName, String email) {
        this.idAcc = idAcc;
        this.idRole = idRole;
        this.pass = pass;
        this.name = userName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Account{" +
                "idAcc=" + idAcc +
                ", idRole=" + idRole +
                ", pass='" + pass + '\'' +
                ", userName='" + name + '\'' +
                ", email='" + email + '\'' +
                ", accDetail=" + accDetail +
                '}';
    }

    public int getIdAcc() {
        return idAcc;
    }

    public void setIdAcc(int idAcc) {
        this.idAcc = idAcc;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String userName) {
        this.name = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccDetail(AccDetail accDetail) {
        this.accDetail = accDetail;
    }

    public AccDetail getAccDetail() {
        return this.accDetail;
    }
}
