package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.libs.MD5;
import hcmuaf.nlu.edu.vn.testproject.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {
    public Account login(String name, String password) {
        String query = "SELECT * FROM Account WHERE name = ? AND password = ?";
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
                        rs.getInt("account_id"),
                        rs.getInt("role_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
