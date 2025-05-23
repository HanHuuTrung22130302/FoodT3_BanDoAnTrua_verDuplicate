package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoiceDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminInvoiceOrderDao {
    public AdminInvoiceOrderDao() {
    }

    public List<OrderInvoice> getAdminInvoiceOrder(int option, int offset) {
        List<OrderInvoice> invoices = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder query = new StringBuilder(
                "SELECT * FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id "
        );

        if (option >= 1 && option <= 4) {
            query.append("WHERE os.order_status = ? ");
        } else if (option == 5) {
            query.append("WHERE os.order_status BETWEEN 5 AND 6 ");
        }else if (option == 0) {
            query.append("WHERE os.order_status BETWEEN 1 AND 6 ");
        }

        query.append("ORDER BY inv.order_date DESC LIMIT 12 OFFSET ?");

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query.toString());

            int paramIndex = 1;
            if (option >= 1 && option <= 5) {
                ps.setInt(paramIndex++, option);
            }
            ps.setInt(paramIndex, offset);

            rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(new OrderInvoice(
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
                        rs.getInt("order_status")
                ));
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi truy vấn hóa đơn: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return invoices;
    }

    public static void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
        }
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng PreparedStatement: " + e.getMessage());
        }
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng Connection: " + e.getMessage());
        }
    }
    public List<OrderInvoice> getInvoiceByToday(int offset) {
        List<OrderInvoice> invoices = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query =
                "SELECT * FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                        "WHERE DATE(inv.order_date) = CURDATE() AND os.order_status BETWEEN 1 AND 6 " +
                        "ORDER BY inv.order_date DESC LIMIT 12 OFFSET ?";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, offset);

            rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(new OrderInvoice(
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
                        rs.getInt("order_status")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi truy vấn hóa đơn theo ngày: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return invoices;
    }
    public List<OrderInvoice> getInvoiceByThisMonth(int offset) {
        List<OrderInvoice> invoices = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query =
                "SELECT * FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                        "WHERE MONTH(inv.order_date) = MONTH(CURDATE()) " +
                        "AND YEAR(inv.order_date) = YEAR(CURDATE()) " +
                        "AND os.order_status BETWEEN 1 AND 6 " +
                        "ORDER BY inv.order_date DESC LIMIT 12 OFFSET ?";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, offset);

            rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(new OrderInvoice(
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
                        rs.getInt("order_status")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi truy vấn hóa đơn theo tháng: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return invoices;
    }
    public List<OrderInvoice> getInvoiceByThisYear(int offset) {
        List<OrderInvoice> invoices = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query =
                "SELECT * FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                        "WHERE YEAR(inv.order_date) = YEAR(CURDATE()) " +
                        "AND os.order_status BETWEEN 1 AND 6 " +
                        "ORDER BY inv.order_date DESC LIMIT 12 OFFSET ?";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, offset);

            rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(new OrderInvoice(
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
                        rs.getInt("order_status")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi truy vấn hóa đơn theo năm: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return invoices;
    }
    public List<OrderInvoice> getInvoicesByIdOrPrefix(String invoiceId, int offset) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OrderInvoice> invoices = new ArrayList<>();

        String query = "";

        try {
            con = new DbContext().getConnection();

            if (invoiceId.matches("^0*\\d{1,6}$")) {
                int realId = Integer.parseInt(invoiceId.replaceFirst("^0+(?!$)", ""));
                query = "SELECT * FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                        "WHERE inv.invoice_id = ? " +
                        "LIMIT 12 OFFSET ?";
                ps = con.prepareStatement(query);
                ps.setInt(1, realId);
                ps.setInt(2, offset);

            } else if (invoiceId.length() > 0 && invoiceId.length() < 6) {
                query = "SELECT * FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                        "WHERE CAST(inv.invoice_id AS CHAR) LIKE ? " +
                        "LIMIT 12 OFFSET ?";
                ps = con.prepareStatement(query);
                ps.setString(1, "%" + invoiceId + "%");
                ps.setInt(2, offset);
            } else {
                return invoices;
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                OrderInvoice invoice = new OrderInvoice(
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
                        rs.getInt("order_status")
                );
                invoices.add(invoice);
            }

        } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
            System.err.println("Lỗi khi truy vấn đơn hàng theo ID hoặc chuỗi: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return invoices;
    }

    public int countAdminInvoiceOrder(int option) {
        int count = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder query = new StringBuilder(
                "SELECT COUNT(*) AS total FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id "
        );

        if (option >= 1 && option <= 4) {
            query.append("WHERE os.order_status = ? ");
        } else if (option == 5) {
            query.append("WHERE os.order_status BETWEEN 5 AND 6 ");
        }else if (option == 0) {
            query.append("WHERE os.order_status BETWEEN 1 AND 6 ");
        }

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query.toString());
            if (option >= 1 && option <= 5) {
                ps.setInt(1, option);
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
    public int countInvoiceToday() {
        int count = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT COUNT(*) AS total FROM invoice inv " +
                "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                "WHERE DATE(order_date) = CURDATE()";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng hôm nay: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return count;
    }
    public int countInvoiceThisMonth() {
        int count = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT COUNT(*) AS total FROM invoice inv " +
                "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                "WHERE MONTH(order_date) = MONTH(CURDATE()) AND YEAR(order_date) = YEAR(CURDATE())";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng trong tháng: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return count;
    }
    public int countInvoiceThisYear() {
        int count = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT COUNT(*) AS total FROM invoice inv " +
                "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                "WHERE YEAR(order_date) = YEAR(CURDATE())";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng trong năm: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return count;
    }
    public int countInvoiceByIdOrPrefix(String invoiceId) {
        int count = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "";

        try {
            con = new DbContext().getConnection();

            if (invoiceId.length() == 6) {
                int realId = Integer.parseInt(invoiceId);
                query = "SELECT COUNT(*) AS total FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                        "WHERE inv.invoice_id = ?";
                ps = con.prepareStatement(query);
                ps.setInt(1, realId);
            } else if (invoiceId.length() < 6) {
                query = "SELECT COUNT(*) AS total FROM invoice inv " +
                        "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                        "WHERE CAST(inv.invoice_id AS CHAR) LIKE ?";
                ps = con.prepareStatement(query);
                ps.setString(1, "%" + invoiceId + "%");
            } else {
                return 0;
            }

            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng theo id: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return count;
    }
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
    public boolean moveOrderStatusForward(int invoiceId) {
        Connection con = null;
        PreparedStatement psSelect = null;
        PreparedStatement psUpdateStatus = null;
        PreparedStatement psUpdatePaid = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            con = new DbContext().getConnection();

            // Lấy trạng thái hiện tại
            String selectQuery = "SELECT order_status FROM order_status WHERE invoice_id = ?";
            psSelect = con.prepareStatement(selectQuery);
            psSelect.setInt(1, invoiceId);
            rs = psSelect.executeQuery();

            if (rs.next()) {
                int currentStatus = rs.getInt("order_status");

                if (currentStatus < 5) {
                    String updateStatusQuery = "UPDATE order_status SET order_status = ? WHERE invoice_id = ?";
                    psUpdateStatus = con.prepareStatement(updateStatusQuery);
                    psUpdateStatus.setInt(1, currentStatus + 1);
                    psUpdateStatus.setInt(2, invoiceId);

                    int affectedRows = psUpdateStatus.executeUpdate();
                    success = affectedRows > 0;

                    if (currentStatus == 3 && success) {
                        String updatePaidQuery = "UPDATE invoice SET is_paid = 2 WHERE invoice_id = ?";
                        psUpdatePaid = con.prepareStatement(updatePaidQuery);
                        psUpdatePaid.setInt(1, invoiceId);
                        psUpdatePaid.executeUpdate();
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage());
        } finally {
            closeResources(rs, psSelect, null);
            closeResources(null, psUpdateStatus, null);
            closeResources(null, psUpdatePaid, con);
        }

        return success;
    }


    public boolean bombOrder(int invoiceId, String reason) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean success = false;

        String query = "UPDATE order_status SET order_status = 6, reason = ? WHERE invoice_id = ?";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, reason);
            ps.setInt(2, invoiceId);

            int affectedRows = ps.executeUpdate();
            success = affectedRows > 0;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi hủy đơn hàng: " + e.getMessage());
        } finally {
            closeResources(null, ps, con);
        }

        return success;
    }
    public boolean cancelOrder(int invoiceId, String reason) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean success = false;

        String query = "UPDATE order_status SET order_status = 5, reason = ? WHERE invoice_id = ?";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, reason);
            ps.setInt(2, invoiceId);

            int affectedRows = ps.executeUpdate();
            success = affectedRows > 0;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi hủy đơn hàng: " + e.getMessage());
        } finally {
            closeResources(null, ps, con);
        }

        return success;
    }

    public List<OrderInvoice> searchAdminInvoiceByRecipientName(String keyword, int offset) {
        List<OrderInvoice> invoices = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM invoice inv " +
                "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                "WHERE inv.recipient_name LIKE ? AND os.order_status BETWEEN 1 AND 6 " +
                "ORDER BY inv.order_date DESC LIMIT 12 OFFSET ?";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, offset);

            rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(new OrderInvoice(
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
                        rs.getInt("order_status")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi tìm kiếm hóa đơn theo tên người nhận: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return invoices;
    }
    public List<OrderInvoice> getInvoiceByDate(String date, int offset) {
        List<OrderInvoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice inv " +
                "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                "WHERE DATE(inv.order_date) = ? " +
                "ORDER BY inv.order_date DESC LIMIT 12 OFFSET ?";

        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, date); // yyyy-MM-dd
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(new OrderInvoice(
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
                        rs.getInt("order_status")
                ));
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi truy vấn hóa đơn theo ngày: " + e.getMessage());
        }

        return invoices;
    }
    public List<OrderInvoice> getInvoiceByMonth(String monthYear, int offset) {
        List<OrderInvoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice inv " +
                "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                "WHERE DATE_FORMAT(inv.order_date, '%Y-%m') = ? " +
                "ORDER BY inv.order_date DESC LIMIT 12 OFFSET ?";

        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, monthYear); // yyyy-MM
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(new OrderInvoice(
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
                        rs.getInt("order_status")
                ));
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi truy vấn hóa đơn theo tháng: " + e.getMessage());
        }

        return invoices;
    }
    public int countInvoiceByDate(String date) {
        String sql = "SELECT COUNT(*) FROM invoice WHERE DATE(order_date) = ?";
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng theo ngày: " + e.getMessage());
        }
        return 0;
    }
    public int countInvoiceByMonth(String month) {
        String sql = "SELECT COUNT(*) FROM invoice WHERE DATE_FORMAT(order_date, '%Y-%m') = ?";
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng theo tháng: " + e.getMessage());
        }
        return 0;
    }
    public int countInvoiceByRecipientName(String name) {
        String sql = "SELECT COUNT(*) FROM invoice WHERE recipient_name LIKE ?";
        try (Connection con = new DbContext().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đếm đơn hàng theo tên người nhận: " + e.getMessage());
        }
        return 0;
    }

    public String getCompletionTime(int invoiceId) {
        String sql = "SELECT DATE_FORMAT(updated_at, '%H:%i:%s %d/%m/%Y') AS updated_at " +
                "FROM order_status WHERE invoice_id = ? AND order_status IN (4, 5, 6)";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("updated_at");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getReason(int invoiceId) {
        String sql = "SELECT reason FROM order_status WHERE invoice_id = ? AND order_status IN ( 5, 6)";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("reason");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isAdmin(int accountId) {
        String sql = "SELECT role_id, is_locked, is_deleted FROM account WHERE account_id = ?";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int roleId = rs.getInt("role_id");
                int isLocked = rs.getInt("is_locked");
                int isDeleted = rs.getInt("is_deleted");
                return roleId == 1 && isLocked == 0 && isDeleted == 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    public OrderInvoice getInvoiceById(int invoiceId) {
        OrderInvoice invoice = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM invoice inv " +
                "JOIN order_status os ON inv.invoice_id = os.invoice_id " +
                "WHERE inv.invoice_id = ?";

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, invoiceId);
            rs = ps.executeQuery();

            if (rs.next()) {
                List<OrderInvoiceDetail> details = InvoiceOrderDetailDao.getInvoiceOrderDetails(invoiceId);
                invoice = new OrderInvoice(
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
                        details
                );
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi truy vấn hóa đơn theo ID: " + e.getMessage());
        } finally {
            closeResources(rs, ps, con);
        }

        return invoice;
    }


    public static void main(String[] args) {
        AdminInvoiceOrderDao dao = new AdminInvoiceOrderDao();
        System.out.println(dao.isAdmin(3));
    }
}
