package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.libs.MD5;
import hcmuaf.nlu.edu.vn.testproject.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {
    public Account login(String name, String password) {
        String query = "SELECT * FROM Account WHERE name = ? AND pass = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String hashedPassword = MD5.getMD5(password);
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, hashedPassword);
            rs = ps.executeQuery();
            while (rs.next()) {
                return new Account(
                        rs.getInt("idAcc"),
                        rs.getInt("idRole"),
                        rs.getString("pass"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        LoginDAO loginDAO = new LoginDAO();
        try {
            Account account = loginDAO.login("trung", "12345678");
            if (account != null) {
                System.out.println("Đăng nhập thành công!");
            } else {
                System.out.println("Tên người dùng hoặc mật khẩu không đúng.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
