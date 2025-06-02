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
    public InvoiceOrderDao() {

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

    public void cancelInvoice(int id, String reason) {
        String query = "UPDATE order_status SET order_status = 5, reason = ? WHERE invoice_id = ?;";
        Connection con = null;
        PreparedStatement ps = null;
        System.out.println(id+" "+reason);
        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, reason);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật đơn hàng: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Nên đóng tài nguyên để tránh rò rỉ kết nối
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
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

    public List<OrderInvoice> getInvoiceRequest() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderStatus() == 1) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public List<OrderInvoice> getInvoiceCoooking() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderStatus() == 2) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public List<OrderInvoice> getInvoiceShipping() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderStatus() == 3) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public List<OrderInvoice> getInvoiceSuccess() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderStatus() == 4) {
                ois.add(oi);
            }
        }
        return ois;
    }

    public List<OrderInvoice> getInvoiceCancelled() {
        List<OrderInvoice> ois = new ArrayList<>();
        for (OrderInvoice oi : data) {
            if (oi.getOrderStatus() == 5) {
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

//    public int getTotalShippingInvoices() {
//        return (int) data.stream()
//                .filter(order -> {
//                    int status = order.getOrderStatus();
//                    return status == 1 || status == 2 || status == 3 ;
//                })
//                .count();
//    }

    public List<OrderInvoice> getInvoicesByOption(int userId, int option, int offset) {
        List<OrderInvoice> data = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder baseQuery = new StringBuilder(
                "SELECT * FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                        "WHERE inv.account_id = ? "
        );

        List<Integer> statusList = new ArrayList<>();

        // Xử lý option
        switch (option) {
            case 1:
                statusList.add(1);
                break;
            case 2:
                statusList.add(2);
                statusList.add(3);
                break;
            case 4:
                statusList.add(4);
                break;
            case 5:
                statusList.add(5);
                statusList.add(6);
                break;
            case 0:
            default:
                break; // không lọc status
        }

        // Nếu có lọc theo status
        if (!statusList.isEmpty()) {
            baseQuery.append("AND os.order_status IN (");
            for (int i = 0; i < statusList.size(); i++) {
                baseQuery.append("?");
                if (i < statusList.size() - 1) baseQuery.append(",");
            }
            baseQuery.append(") ");
        }

        // Thêm phân trang
        baseQuery.append("ORDER BY inv.invoice_id DESC LIMIT 10 OFFSET ?");

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(baseQuery.toString());

            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            for (Integer status : statusList) {
                ps.setInt(paramIndex++, status);
            }

            ps.setInt(paramIndex, offset); // cuối cùng: offset

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
                        rs.getInt("order_status"), // đã JOIN bảng order_status rồi nên cột này có
                        InvoiceOrderDetailDao.getInvoiceOrderDetails(rs.getInt("invoice_id"))
                ));
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return data;
    }

    public int countInvoicesByOption(int userId, int option) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        StringBuilder baseQuery = new StringBuilder(
                "SELECT COUNT(*) AS total FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                        "WHERE inv.account_id = ? "
        );

        List<Integer> statusList = new ArrayList<>();

        // Xử lý option
        switch (option) {
            case 1:
                statusList.add(1);
                break;
            case 2:
                statusList.add(2);
                statusList.add(3);
                break;
            case 4:
                statusList.add(4);
                break;
            case 5:
                statusList.add(5);
                statusList.add(6);
                break;
            case 0:
            default:
                break; // không lọc status
        }

        // Nếu có lọc theo status
        if (!statusList.isEmpty()) {
            baseQuery.append("AND os.order_status IN (");
            for (int i = 0; i < statusList.size(); i++) {
                baseQuery.append("?");
                if (i < statusList.size() - 1) baseQuery.append(",");
            }
            baseQuery.append(")");
        }

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(baseQuery.toString());

            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);
            for (Integer status : statusList) {
                ps.setInt(paramIndex++, status);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return count;
    }
    public OrderInvoice getInvoiceOrderById(int invoiceId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrderInvoice order = null;

        String query = "SELECT * FROM invoice inv " +
                "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                "WHERE inv.invoice_id = ?";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, invoiceId);
            rs = ps.executeQuery();

            if (rs.next()) {
                order = new OrderInvoice(
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
                );
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi truy vấn đơn hàng theo ID: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return order;
    }
    public List<OrderInvoice> searchInvoicesByFoodName(int userId, String foodName, int offset) {
        List<OrderInvoice> filteredOrders = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT DISTINCT inv.*, os.order_status FROM invoice inv " +
                "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                "JOIN invoice_detail id ON inv.invoice_id = id.invoice_id " +
                "JOIN food f ON id.food_id = f.food_id " +
                "WHERE inv.account_id = ? AND LOWER(f.food_name) LIKE ? " +
                "ORDER BY inv.invoice_id DESC " +
                "LIMIT 10 OFFSET ?";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setString(2, "%" + foodName.toLowerCase() + "%");
            ps.setInt(3, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                int invoiceId = rs.getInt("invoice_id");
                filteredOrders.add(new OrderInvoice(
                        invoiceId,
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
                        InvoiceOrderDetailDao.getInvoiceOrderDetails(invoiceId)
                ));
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi tìm kiếm đơn hàng theo tên món ăn (có phân trang): " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
            }
        }

        return filteredOrders;
    }

    public int countInvoicesByFoodName(int userId, String foodName) {
        int count = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = """
        SELECT COUNT(DISTINCT inv.invoice_id) AS total
        FROM invoice inv
        JOIN order_status os ON inv.invoice_id = os.invoice_id
        JOIN invoice_detail id ON inv.invoice_id = id.invoice_id
        JOIN food f ON id.food_id = f.food_id
        WHERE inv.account_id = ? AND f.food_name LIKE ?
    """;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setString(2, "%" + foodName + "%");
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng theo tên món ăn: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
            }
        }

        return count;
    }
    public int getTotalShippingInvoices(int userId) {
        int count = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = """
        SELECT COUNT(*) AS total
        FROM invoice inv
        JOIN order_status os ON inv.invoice_id = os.invoice_id
        WHERE inv.account_id = ? AND os.order_status IN (1, 2, 3)
    """;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng shipping: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
            }
        }

        return count;
    }
    public int countInvoicesByFilter(int userId, String optionOrFoodName) {
        int count = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();

        boolean isOption = optionOrFoodName.matches("\\d+"); // Kiểm tra có phải là option số (0, 1, 2, 4, 5)

        if (isOption) {
            int option = Integer.parseInt(optionOrFoodName);

            query.append("""
            SELECT COUNT(*) AS total
            FROM invoice inv
            JOIN order_status os ON inv.invoice_id = os.invoice_id
            WHERE inv.account_id = ?
        """);

            params.add(userId);

            List<Integer> statusList = switch (option) {
                case 1 -> List.of(1);
                case 2 -> List.of(2, 3);
                case 4 -> List.of(4);
                case 5 -> List.of(5, 6);
                case 0 -> List.of(); // Tất cả đơn
                default -> List.of();
            };

            if (!statusList.isEmpty()) {
                query.append(" AND os.order_status IN (");
                query.append("?,".repeat(statusList.size()));
                query.setLength(query.length() - 1); // Xoá dấu phẩy cuối
                query.append(")");

                params.addAll(statusList);
            }

        } else {
            // Trường hợp tìm kiếm theo tên món ăn
            query.append("""
            SELECT COUNT(DISTINCT inv.invoice_id) AS total
            FROM invoice inv
            JOIN order_status os ON inv.invoice_id = os.invoice_id
            JOIN invoice_detail id ON inv.invoice_id = id.invoice_id
            JOIN food f ON id.food_id = f.food_id
            WHERE inv.account_id = ? AND f.food_name LIKE ?
        """);
            params.add(userId);
            params.add("%" + optionOrFoodName + "%");
        }

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query.toString());

            // Gán tham số
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) {
                    ps.setInt(i + 1, (Integer) p);
                } else if (p instanceof String) {
                    ps.setString(i + 1, (String) p);
                }
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng (filter): " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
            }
        }

        return count;
    }


    public static void main(String[] args) {
        InvoiceOrderDao invoiceOrderDao = new InvoiceOrderDao();
        System.out.println(invoiceOrderDao.countInvoicesByFilter(3,"cơm"));
    }
}