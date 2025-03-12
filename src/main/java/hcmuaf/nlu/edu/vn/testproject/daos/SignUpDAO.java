package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.libs.MD5;
import hcmuaf.nlu.edu.vn.testproject.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignUpDAO {


    public static void signUp(String name, String password, String email) {
        String query = "INSERT INTO account (name, pass, idRole,email) VALUES (?, ?, 2,?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String hashedPassword = MD5.getMD5(password);

        try {
            con = new DbContext().getConnection();

            ps = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.executeUpdate();

            // Chèn email vào bảng ct_acc
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public static Account checkUserExist(String name, String email) {
        String query = "SELECT * FROM Account WHERE name = ? OR email = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, email);

            rs = ps.executeQuery();
            while(rs.next()) {
                return new Account(
                        rs.getInt("idAcc"),
                        rs.getInt("idRole"),
                        rs.getString("pass"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
