package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.InvoiceDetail;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoiceDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceOrderDetailDao {


    // Hàm lấy tất cả các món ăn từ cơ sở dữ liệu
    public static List<OrderInvoiceDetail> getInvoiceOrderDetails(int idInvoice) {
        List<OrderInvoiceDetail> data = new ArrayList<>();
        String query = "SELECT * FROM invoice_detail id join food f ON id.food_id = f.food_id where invoice_id=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idInvoice);
            rs = ps.executeQuery();

            while (rs.next()) {
                data.add(
                        new OrderInvoiceDetail(
                                rs.getInt("detail_id"),
                                rs.getInt("invoice_id"),
                                rs.getInt("food_id"),
                                rs.getString("food_name"),
                                rs.getInt("quantity"),
                                rs.getInt("total_amount"),
                                rs.getString("image")
                        ));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo rằng kết nối, câu lệnh và result set được đóng đúng cách
            closeResources(rs, ps, con);
        }
        return data;
    }

    // Phương thức đóng các tài nguyên
    private static void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println(getInvoiceOrderDetails(1));
    }
}
