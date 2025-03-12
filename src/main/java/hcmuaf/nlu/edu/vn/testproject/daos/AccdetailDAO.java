package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.libs.MD5;
import hcmuaf.nlu.edu.vn.testproject.models.AccDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccdetailDAO {

    public List<AccDetail> getAllAccDetail() {

        List<AccDetail> listAcc = new ArrayList<AccDetail>();
        String query = "SELECT accdetail.*, account.email" +
                " FROM accdetail RIGHT JOIN account ON accdetail.idAcc = account.idAcc WHERE account.idRole = 2";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                listAcc.add(new AccDetail(
                        rs.getInt("idAcc"),
                        rs.getString("fullName"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getInt("gender"),
                        rs.getString("birthDate"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo rằng kết nối, câu lệnh và result set được đóng đúng cách
            closeResources(rs, ps, conn);
        }
        return listAcc;
    }

    public Account getAccountById(int idAcc) {
        String query = "SELECT name, email FROM account WHERE idAcc = ?";
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

    public AccDetail getAccDetailById(int idAcc) {
        String query = "SELECT fullName, phoneNumber,address, birthDate, gender FROM accDetail WHERE idAcc = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AccDetail accDetail = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idAcc);
            rs = ps.executeQuery();
            if (rs.next()) {
                accDetail = new AccDetail();
                accDetail.setFullName(rs.getString("fullName"));
                accDetail.setPhoneNumber(rs.getString("phoneNumber"));
                accDetail.setAddress(rs.getString("address"));
                accDetail.setBirthDate(rs.getString("birthDate"));
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
        String query = "UPDATE accDetail SET fullName = ?, phoneNumber = ?,address = ?, birthDate = ?, gender = ? WHERE idAcc = ?";
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
    public List<AccDetail> searchAcc(String textSearch) {
        List<AccDetail> listAcc = new ArrayList<>();

        // Duyệt qua danh sách các AccDetail
        for (AccDetail accDetail : getAllAccDetail()) {
            // Kiểm tra nếu bất kỳ thuộc tính nào của AccDetail trong textSearch (không phân biệt chữ hoa/thường)
            if ((accDetail.getEmail() != null && accDetail.getEmail().toLowerCase().contains(textSearch.toLowerCase())) ||
                    (accDetail.getFullName() != null && accDetail.getFullName().toLowerCase().contains(textSearch.toLowerCase())) ||
                    (accDetail.getAddress() != null && accDetail.getAddress().toLowerCase().contains(textSearch.toLowerCase())) ||
                    (accDetail.getPhoneNumber() != null && accDetail.getPhoneNumber().toLowerCase().contains(textSearch.toLowerCase()))) {
                listAcc.add(accDetail);
            }
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
        String query = "INSERT INTO accDetail (idAcc, fullName, phoneNumber, address, birthDate, gender, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, NOW(),NOW())";
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
