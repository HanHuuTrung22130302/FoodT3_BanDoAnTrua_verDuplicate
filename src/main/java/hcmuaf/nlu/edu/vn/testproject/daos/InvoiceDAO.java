package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
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
                detail.setQuantity(rs.getInt("totalQuantity"));  // Tổng số lượng bán
                detail.setTotalAmount(rs.getInt("totalAmount"));  // Tổng doanh thu

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

    public List<InvoiceDetail> getInvoiceDetailsByTime(String timeFilter) {
        List<InvoiceDetail> list = new ArrayList<>();
        String query = "SELECT id.food_id, f.food_name, f.image, SUM(id.quantity) as total_quantity, " +
                      "SUM(id.total_amount) as total_amount " +
                      "FROM invoice_detail id " +
                      "JOIN invoice i ON id.invoice_id = i.invoice_id " +
                      "JOIN food f ON id.food_id = f.food_id " +
                      "WHERE i.is_paid = 1 "; // Thêm lại điều kiện is_paid = 1
        
        // Thêm điều kiện thời gian
        if ("day".equals(timeFilter)) {
            query += "AND DATE(i.order_date) = CURDATE() ";
        } else if ("week".equals(timeFilter)) {
            query += "AND YEARWEEK(i.order_date, 1) = YEARWEEK(CURDATE(), 1) ";
        } else if ("month".equals(timeFilter)) {
            query += "AND MONTH(i.order_date) = MONTH(CURDATE()) AND YEAR(i.order_date) = YEAR(CURDATE()) ";
        }
        
        query += "GROUP BY id.food_id, f.food_name, f.image ORDER BY total_amount DESC";
        
        System.out.println("Query: " + query); // Giữ lại để debug
        
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ResultSet rs = ps.executeQuery();
            boolean hasData = rs.next();
            System.out.println("Có dữ liệu: " + hasData); // Kiểm tra xem có dữ liệu không
            
            if (hasData) {
                do {
                    InvoiceDetail detail = new InvoiceDetail();
                    Food food = new Food();
                    food.setFoodId(rs.getInt("food_id"));
                    food.setFoodName(rs.getString("food_name"));
                    food.setImage(rs.getString("image"));
                    
                    detail.setFood(food);
                    detail.setQuantity(rs.getInt("total_quantity"));
                    detail.setTotalAmount(rs.getInt("total_amount"));
                    
                    list.add(detail);
                } while (rs.next());
            }
            
            System.out.println("Số lượng kết quả: " + list.size()); // In ra số lượng kết quả
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<InvoiceDetail> searchByNameAndTime(String txtSearch, String timeFilter) {
        List<InvoiceDetail> list = new ArrayList<>();
        String query = "SELECT id.food_id, f.food_name, f.image, SUM(id.quantity) as total_quantity, " +
                      "SUM(id.total_amount) as total_amount " +
                      "FROM invoice_detail id " +
                      "JOIN invoice i ON id.invoice_id = i.invoice_id " +
                      "JOIN food f ON id.food_id = f.food_id " +
                      "WHERE i.is_paid = 1 AND f.food_name LIKE ? ";
        
        // Thêm điều kiện thời gian
        if ("day".equals(timeFilter)) {
            query += "AND DATE(i.order_date) = CURDATE() ";
        } else if ("week".equals(timeFilter)) {
            query += "AND YEARWEEK(i.order_date, 1) = YEARWEEK(CURDATE(), 1) ";
        } else if ("month".equals(timeFilter)) {
            query += "AND MONTH(i.order_date) = MONTH(CURDATE()) AND YEAR(i.order_date) = YEAR(CURDATE()) ";
        }
        
        query += "GROUP BY id.food_id, f.food_name, f.image ORDER BY total_amount DESC";
        
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, "%" + txtSearch + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                Food food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setFoodName(rs.getString("food_name"));
                food.setImage(rs.getString("image"));
                
                detail.setFood(food);
                detail.setQuantity(rs.getInt("total_quantity"));
                detail.setTotalAmount(rs.getInt("total_amount"));
                
                list.add(detail);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public int getRevenueByDay() {
        String query = "SELECT SUM(total_amount) FROM invoice WHERE is_paid = 1 AND DATE(order_date) = CURDATE()";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public int getRevenueByWeek() {
        String query = "SELECT SUM(total_amount) FROM invoice WHERE is_paid = 1 AND YEARWEEK(order_date, 1) = YEARWEEK(CURDATE(), 1)";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public int getRevenueByMonth() {
        String query = "SELECT SUM(total_amount) FROM invoice WHERE is_paid = 1 AND MONTH(order_date) = MONTH(CURDATE()) AND YEAR(order_date) = YEAR(CURDATE())";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public int getOrderCountByDay() {
        String query = "SELECT COUNT(*) FROM invoice WHERE is_paid = 1 AND DATE(order_date) = CURDATE()";
        return getOrderCount(query);
    }

    public int getOrderCountByWeek() {
        String query = "SELECT COUNT(*) FROM invoice WHERE is_paid = 1 AND YEARWEEK(order_date, 1) = YEARWEEK(CURDATE(), 1)";
        return getOrderCount(query);
    }

    public int getOrderCountByMonth() {
        String query = "SELECT COUNT(*) FROM invoice WHERE is_paid = 1 AND MONTH(order_date) = MONTH(CURDATE()) AND YEAR(order_date) = YEAR(CURDATE())";
        return getOrderCount(query);
    }

    private int getOrderCount(String query) {
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public List<InvoiceDetail> getInvoiceDetailsByDay() {
        List<InvoiceDetail> details = new ArrayList<>();
        String query = "SELECT id.*, f.food_name, f.image, f.price, " +
                      "SUM(id.quantity) as quantity, SUM(id.total_amount) as total_amount " +
                      "FROM invoice_detail id " +
                      "JOIN food f ON id.food_id = f.food_id " +
                      "JOIN invoice i ON id.invoice_id = i.invoice_id " +
                      "WHERE DATE(i.order_date) = CURDATE() " +
                      "AND i.is_paid = 1 " +
                      "GROUP BY id.food_id, f.food_name, f.image, f.price";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                Food food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setFoodName(rs.getString("food_name"));
                food.setImage(rs.getString("image"));
                food.setPrice(rs.getInt("price"));
                detail.setFood(food);
                detail.setQuantity(rs.getInt("quantity"));
                detail.setTotalAmount(rs.getInt("total_amount"));
                details.add(detail);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return details;
    }

    public List<InvoiceDetail> getInvoiceDetailsByWeek() {
        List<InvoiceDetail> details = new ArrayList<>();
        String query = "SELECT id.*, f.food_name, f.image, f.price, " +
                      "SUM(id.quantity) as quantity, SUM(id.total_amount) as total_amount " +
                      "FROM invoice_detail id " +
                      "JOIN food f ON id.food_id = f.food_id " +
                      "JOIN invoice i ON id.invoice_id = i.invoice_id " +
                      "WHERE i.order_date >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                      "AND i.order_date <= CURDATE() " +
                      "AND i.is_paid = 1 " +
                      "GROUP BY id.food_id, f.food_name, f.image, f.price";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                Food food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setFoodName(rs.getString("food_name"));
                food.setImage(rs.getString("image"));
                food.setPrice(rs.getInt("price"));
                detail.setFood(food);
                detail.setQuantity(rs.getInt("quantity"));
                detail.setTotalAmount(rs.getInt("total_amount"));
                details.add(detail);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return details;
    }

    public List<InvoiceDetail> getInvoiceDetailsByMonth() {
        List<InvoiceDetail> details = new ArrayList<>();
        String query = "SELECT id.*, f.food_name, f.image, f.price, " +
                      "SUM(id.quantity) as quantity, SUM(id.total_amount) as total_amount " +
                      "FROM invoice_detail id " +
                      "JOIN food f ON id.food_id = f.food_id " +
                      "JOIN invoice i ON id.invoice_id = i.invoice_id " +
                      "WHERE MONTH(i.order_date) = MONTH(CURDATE()) " +
                      "AND YEAR(i.order_date) = YEAR(CURDATE()) " +
                      "AND i.is_paid = 1 " +
                      "GROUP BY id.food_id, f.food_name, f.image, f.price";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                Food food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setFoodName(rs.getString("food_name"));
                food.setImage(rs.getString("image"));
                food.setPrice(rs.getInt("price"));
                detail.setFood(food);
                detail.setQuantity(rs.getInt("quantity"));
                detail.setTotalAmount(rs.getInt("total_amount"));
                details.add(detail);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return details;
    }

    public int getRevenueBySpecificMonth(int year, int month) {
        String query = "SELECT SUM(total_amount) FROM invoice WHERE is_paid = 1 " +
                      "AND YEAR(order_date) = ? AND MONTH(order_date) = ?";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public int getOrderCountBySpecificMonth(int year, int month) {
        String query = "SELECT COUNT(*) FROM invoice WHERE is_paid = 1 " +
                      "AND YEAR(order_date) = ? AND MONTH(order_date) = ?";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public int getPreviousMonthRevenue(int year, int month) {
        // Xử lý trường hợp tháng 1
        if (month == 1) {
            year--;
            month = 12;
        } else {
            month--;
        }
        return getRevenueBySpecificMonth(year, month);
    }

    public int getPreviousMonthOrderCount(int year, int month) {
        // Xử lý trường hợp tháng 1
        if (month == 1) {
            year--;
            month = 12;
        } else {
            month--;
        }
        return getOrderCountBySpecificMonth(year, month);
    }
}