package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.AccountDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccdetailDAO {

    public List<AccountDetail> getAllAccDetail(int roleId) {
        List<AccountDetail> listAcc = new ArrayList<AccountDetail>();
        String query = "SELECT account_detail.*, account.email" +
                " FROM account_detail RIGHT JOIN account ON account_detail.account_id = account.account_id " +
                "WHERE account.role_id = ? AND account.is_deleted = 0";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, roleId);
            rs = ps.executeQuery();
            while (rs.next()) {
                listAcc.add(new AccountDetail(
                        rs.getInt("account_id"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getInt("gender"),
                        rs.getString("birth_date"),
                        rs.getString("email")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return listAcc;
    }

    public Account getAccountById(int idAcc) {
        String query = "SELECT name, email FROM account WHERE account_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Account account = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idAcc);
            rs = ps.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setName(rs.getString("name"));
                account.setEmail(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public AccountDetail getAccDetailById(int idAcc) {
        String query = "SELECT full_name, phone_number,address, birth_date, gender FROM account_detail WHERE account_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AccountDetail accDetail = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idAcc);
            rs = ps.executeQuery();
            if (rs.next()) {
                accDetail = new AccountDetail();
                accDetail.setFullName(rs.getString("full_name"));
                accDetail.setPhoneNumber(rs.getString("phone_number"));
                accDetail.setAddress(rs.getString("address"));
                accDetail.setBirthDate(rs.getString("birth_date"));
                accDetail.setGender(rs.getInt("gender"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accDetail;
    }

    public void updateAccdetail(int idAcc, String fullName, String address, String phoneNumber, String birthDate, int gender) {
        Connection con = null;
        PreparedStatement ps = null;
        String query = "UPDATE account_detail SET full_name = ?, phone_number = ?,address = ?, birth_date = ?, gender = ? WHERE account_id = ?";
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, fullName);
            ps.setString(2, phoneNumber);
            ps.setString(3, address);
            ps.setString(4, birthDate);
            ps.setInt(5, gender);
            ps.setInt(6, idAcc);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức tìm kiếm theo tên
    public List<AccountDetail> searchAcc(String textSearch, int roleId) {
        List<AccountDetail> listAcc = new ArrayList<AccountDetail>();
        String query = "SELECT account_detail.*, account.email " +
                "FROM account_detail RIGHT JOIN account ON account_detail.account_id = account.account_id " +
                "WHERE account.role_id = ? AND account.is_deleted = 0 " +
                "AND (account_detail.full_name LIKE ? OR account_detail.phone_number LIKE ? OR account.email LIKE ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, roleId);
            String searchPattern = "%" + textSearch + "%";
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            rs = ps.executeQuery();
            while (rs.next()) {
                listAcc.add(new AccountDetail(
                        rs.getInt("account_id"),
                        rs.getString("full_name"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getInt("gender"),
                        rs.getString("birth_date"),
                        rs.getString("email")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return listAcc;
    }


    // Phương thức đóng các tài nguyên
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        AccdetailDAO dao = new AccdetailDAO();
        System.out.println(dao.getAccountById(16));
        System.out.println(dao.getAccDetailById(16));
    }

    public void addAccDetail(int idAcc, String fullName, String address, String phoneNumber, String birthDate, int gender) {
        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO account_detail (account_id, full_name, phone_number, address, birth_date, gender, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, NOW(),NOW())";
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idAcc);
            ps.setString(2, fullName);
            ps.setString(3, phoneNumber);
            ps.setString(4, address);
            ps.setString(5, birthDate);
            ps.setInt(6, gender);

            // Thực thi câu lệnh thêm mới
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng tài nguyên sau khi thực hiện xong
            closeResources(null, ps, con);
        }
    }

}
