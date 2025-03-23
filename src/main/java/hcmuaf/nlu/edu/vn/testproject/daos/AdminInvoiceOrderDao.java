package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoiceDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminInvoiceOrderDao {

    private List<OrderInvoice> data;
    private int id;

    public AdminInvoiceOrderDao() {
        this.data = new ArrayList<>();
        this.id = id;
        getAllInvoice();
    }


    // Hàm lấy tất cả các món ăn từ cơ sở dữ liệu
    public void getAllInvoice() {

        String query = "SELECT * FROM invoice inv join order_status os ON inv.invoice_id = os.invoice_id ;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
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
                        rs.getInt("paymentMethod"),
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
            if (oi.getOrderSt() == 1) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public List<OrderInvoice> getInvoiceDelivered() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderSt() == 2) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public List<OrderInvoice> getInvoiceCancelled() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderSt() == 3) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public List<OrderInvoice> getInvoiceOrders(String input) {
        List<OrderInvoice> result = new ArrayList<>();

        for (OrderInvoice oi : data) {
            if (isNumeric(input)) {
                if (oi.getIdInvoice() == Integer.parseInt(input)) {
                    result.add(oi); // Thêm vào danh sách kết quả
                }
            } else {
                if (oi.getRecipientName().equalsIgnoreCase(input)) {
                    result.add(oi);
                }
            }
        }

        return result; // Trả về danh sách các OrderInvoice tìm được
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<OrderInvoice> getInvoicesByCurrentMonthAndYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String currentMonthYear = sdf.format(new Date());

        List<OrderInvoice> result = new ArrayList<>();

        for (OrderInvoice oi : data) {
            if (oi.getOrderDate().substring(0, 7).equals(currentMonthYear)) {
                result.add(oi);
            }
        }

        return result;
    }

    public List<OrderInvoice> getInvoicesByCurrentYear() {
        // Lấy năm hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String currentYear = sdf.format(new Date());

        List<OrderInvoice> result = new ArrayList<>();

        for (OrderInvoice oi : data) {
            // So sánh năm trong orderDate với năm hiện tại
            if (oi.getOrderDate().substring(0, 4).equals(currentYear)) {
                result.add(oi);
            }
        }
        return result;
    }
}
