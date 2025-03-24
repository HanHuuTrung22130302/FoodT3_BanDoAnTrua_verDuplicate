package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.Invoice;
import hcmuaf.nlu.edu.vn.testproject.models.InvoiceDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    public void addInvoice(Invoice invoice) {
        String query = "INSERT INTO invoice (account_id, recipient_name, phone_number, delivery_address, note, order_date, total_amount, discount_code_id, payment_method, is_paid) VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, NULL)";
        String query2 = "INSERT INTO order_status (invoice_id, order_status) VALUES (?, 1)";
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Thêm tùy chọn để lấy key được sinh ra

            // Gán các tham số vào câu lệnh SQL
            ps.setInt(1, invoice.getAccountId());
            ps.setString(2, invoice.getRecipientName());
            ps.setString(3, invoice.getPhoneNumber());
            ps.setString(4, invoice.getDeliveryAddress());
            ps.setString(5, invoice.getNote());
            ps.setString(6, invoice.getOrderDate());
            ps.setInt(7, invoice.getTotalAmount());
            ps.setInt(8, invoice.getPaymentMethod());

            // Thực thi câu lệnh INSERT
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Lấy ID của hóa đơn vừa chèn vào
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    invoice.setInvoiceId(rs.getInt(1));
                    int idInvoice = rs.getInt(1);
                    ps2 = conn.prepareStatement(query2);
                    ps2.setInt(1, idInvoice);
                    ps2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void addInvoiceDetail(InvoiceDetail detail) {
        String query = "INSERT INTO invoice_detail (invoice_id, food_id, quantity, total_amount) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, detail.getInvoiceId());
            ps.setInt(2, detail.getFoodId());
            ps.setInt(3, detail.getQuantity());
            ps.setInt(4, detail.getTotalAmount());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        // Truy vấn gộp món ăn theo foodName và tính tổng số lượng, tổng doanh thu
        String query = "SELECT f.food_name, f.image, SUM(id.quantity) AS totalQuantity, SUM(id.total_amount) AS totalAmount " +
                "FROM invoice_detail id " +
                "JOIN food f ON id.food_id = f.food_id " +
                "GROUP BY f.food_name, f.image";

        List<InvoiceDetail> details = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setQuantity(rs.getInt("total_quantity"));  // Tổng số lượng bán
                detail.setTotalAmount(rs.getInt("total_amount"));  // Tổng doanh thu

                Food food = new Food();
                food.setFoodName(rs.getString("food_name"));
                food.setImage(rs.getString("image"));

                detail.setFood(food);
                details.add(detail);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return details;
    }

    public List<InvoiceDetail> searchByName(String textSearch) {
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        for (InvoiceDetail invoiceDetail : getInvoiceDetails()) {
            if (invoiceDetail.getFood().getFoodName().toLowerCase().contains(textSearch.toLowerCase())) {
                invoiceDetails.add(invoiceDetail);
            }
        }
        return invoiceDetails;
    }
}
