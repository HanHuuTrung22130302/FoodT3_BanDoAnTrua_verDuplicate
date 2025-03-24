package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.Invoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoiceDetail;
import jakarta.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceOrderDao {

    private List<OrderInvoice> data;
    private int id;

    public InvoiceOrderDao(int id) {
        this.data = new ArrayList<>();
        this.id = id;
        getAllInvoice(id);
    }


    // Hàm lấy tất cả các món ăn từ cơ sở dữ liệu
    public void getAllInvoice(int id) {

        String query = "SELECT * FROM invoice inv join order_status os ON inv.invoice_id = os.invoice_id where account_id = ?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                data.add(new OrderInvoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("account_id"),
                        rs.getString("recipient_name"),
                        rs.getString("phone_number"),
                        rs.getString("delivery_address"),
                        rs.getString("note"),
                        rs.getString("order_date"),
                        rs.getInt("total_amount"),
                        rs.getInt("discount_code_id"),
                        rs.getInt("payment_method"),
                        rs.getInt("is_paid"),
                        rs.getInt("order_status"),
                        InvoiceOrderDetailDao.getInvoiceOrderDetails(rs.getInt("invoice_id"))

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
    }

    public void canclInvoice(int id) {

        String query = "UPDATE order_status SET order_status = 3 WHERE invoice_id = ?;";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Phương thức đóng các tài nguyên
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    public List<OrderInvoice> getAll() {
        return data;
    }

    public List<OrderInvoice> getInvoiceShipping() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderStatus() == 1) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public List<OrderInvoice> getInvoiceDelivered() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderStatus() == 2) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public List<OrderInvoice> getInvoiceCancelled() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderStatus() == 3) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public OrderInvoice getInvoiceOrder(int id) {
        for (OrderInvoice oi : data) {
            if (oi.getInvoiceId() == id) {
                return oi;
            }
        }
        return null;
    }

    public List<OrderInvoice> filterOrderByFoodName(String foodName) {
        List<OrderInvoice> filteredOrders = new ArrayList<>();
        for (OrderInvoice order : data) {
            for (OrderInvoiceDetail detail : order.getOrderInvoiceDetail()) {
                if (detail.getFoodName().toLowerCase().contains(foodName.toLowerCase())) {
                    filteredOrders.add(order);
                    break;
                }
            }
        }

        return filteredOrders;
    }

    public int getTotalShippingInvoices() {
        return (int) data.stream()
                .filter(order -> order.getOrderStatus() == 1)
                .count();
    }
}
