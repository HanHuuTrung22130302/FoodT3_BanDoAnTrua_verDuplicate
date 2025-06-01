package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.Invoice;
import hcmuaf.nlu.edu.vn.testproject.models.InvoiceDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvoiceDAO {
    private static final String BASE_QUERY = "SELECT f.food_id, f.food_name, f.image, f.price, " +
            "COALESCE(SUM(id.quantity), 0) as total_quantity, " +
            "COALESCE(SUM(id.total_amount), 0) as total_amount " +
            "FROM food f " +
            "LEFT JOIN invoice_detail id ON f.food_id = id.food_id " +
            "LEFT JOIN invoice i ON id.invoice_id = i.invoice_id " +
            "WHERE (i.is_paid = 1 OR i.is_paid IS NULL) ";

    // Phương thức thêm hóa đơn và chi tiết hóa đơn
    public void addInvoice(Invoice invoice) {
        String query = "INSERT INTO invoice (account_id, recipient_name, phone_number, delivery_address, note, order_date, total_amount, discount_code_id, payment_method, is_paid) VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, NULL)";
        String query2 = "INSERT INTO order_status (invoice_id, order_status) VALUES (?, 1)";
        
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement ps2 = conn.prepareStatement(query2)) {
            
            // Thêm hóa đơn
            ps.setInt(1, invoice.getAccountId());
            ps.setString(2, invoice.getRecipientName());
            ps.setString(3, invoice.getPhoneNumber());
            ps.setString(4, invoice.getDeliveryAddress());
            ps.setString(5, invoice.getNote());
            ps.setString(6, invoice.getOrderDate());
            ps.setInt(7, invoice.getTotalAmount());
            ps.setInt(8, invoice.getPaymentMethod());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idInvoice = rs.getInt(1);
                        invoice.setInvoiceId(idInvoice);
                        
                        // Thêm trạng thái đơn hàng
                        ps2.setInt(1, idInvoice);
                        ps2.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addInvoiceDetail(InvoiceDetail detail) {
        String query = "INSERT INTO invoice_detail (invoice_id, food_id, quantity, total_amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, detail.getInvoiceId());
            ps.setInt(2, detail.getFoodId());
            ps.setInt(3, detail.getQuantity());
            ps.setInt(4, detail.getTotalAmount());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<InvoiceDetail> searchByNameAndTime(String txtSearch, String timeFilter) {
        String query = BASE_QUERY + "AND f.food_name LIKE ? " + 
                getTimeFilterCondition(timeFilter) +
                "GROUP BY f.food_id, f.food_name, f.image, f.price ORDER BY total_amount DESC";
        
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, "%" + txtSearch + "%");
            return executeInvoiceDetailQuery(ps);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Phương thức lấy số liệu thống kê
    public int getRevenueBySpecificMonth(int year, int month) {
        String query = "SELECT SUM(total_amount) FROM invoice WHERE is_paid = 1 " +
                "AND YEAR(order_date) = ? AND MONTH(order_date) = ?";
        return executeSumQuery(query, year, month);
    }

    public int getOrderCountBySpecificMonth(int year, int month) {
        String query = "SELECT COUNT(*) FROM invoice WHERE is_paid = 1 " +
                "AND YEAR(order_date) = ? AND MONTH(order_date) = ?";
        return executeCountQuery(query, year, month);
    }

    // Phương thức helper
    private String getTimeFilterCondition(String timeFilter) {
        switch (timeFilter) {
            case "day":
                return "AND DATE(i.order_date) = CURDATE()";
            case "week":
                return "AND YEARWEEK(i.order_date, 1) = YEARWEEK(CURDATE(), 1)";
            case "month":
                return "AND MONTH(i.order_date) = MONTH(CURDATE()) AND YEAR(i.order_date) = YEAR(CURDATE())";
            default:
                return "";
        }
    }

    private List<InvoiceDetail> executeInvoiceDetailQuery(PreparedStatement ps) {
        try (ResultSet rs = ps.executeQuery()) {
            List<InvoiceDetail> details = new ArrayList<>();
            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                Food food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setFoodName(rs.getString("food_name"));
                food.setImage(rs.getString("image"));
                food.setPrice(rs.getInt("price"));
                detail.setFood(food);
                detail.setQuantity(rs.getInt("total_quantity"));
                detail.setTotalAmount(rs.getInt("total_amount"));
                details.add(detail);
            }
            return details;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private int executeCountQuery(String query, int year, int month) {
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int executeSumQuery(String query, int year, int month) {
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        // Phương thức này không được sử dụng trong StatisticalController
        return new ArrayList<>();
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

    public int getOrderCountBySearch(String txtSearch, String timeFilter) {
        String query = "SELECT COUNT(DISTINCT i.invoice_id) FROM invoice i " +
                      "JOIN invoice_detail id ON i.invoice_id = id.invoice_id " +
                      "JOIN food f ON id.food_id = f.food_id " +
                      "WHERE i.is_paid = 1 " +
                      "AND f.food_name LIKE ? " +
                      getTimeFilterCondition(timeFilter);
        
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, "%" + txtSearch + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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

    public List<Food> getUnsoldProductsByTime() {
        String query = "SELECT f.food_id, f.food_name, f.image, f.price, f.is_deleted " +
                "FROM food f " +
                "WHERE f.sold = 0 " +
                "ORDER BY f.food_name";
        
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                List<Food> unsoldProducts = new ArrayList<>();
                while (rs.next()) {
                    Food food = new Food();
                    food.setFoodId(rs.getInt("food_id"));
                    food.setFoodName(rs.getString("food_name"));
                    food.setImage(rs.getString("image"));
                    food.setPrice(rs.getInt("price"));
                    food.setIsDeleted(rs.getInt("is_deleted"));
                    unsoldProducts.add(food);
                }
                return unsoldProducts;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<InvoiceDetail> getSlowSellingProductsByTime(String timeFilter) {
        String query = "SELECT f.food_id, f.food_name, f.image, f.price, " +
                      "COALESCE(SUM(id.quantity), 0) as total_quantity, " +
                      "COALESCE(SUM(id.total_amount), 0) as total_amount " +
                      "FROM food f " +
                      "LEFT JOIN invoice_detail id ON f.food_id = id.food_id " +
                      "LEFT JOIN invoice i ON id.invoice_id = i.invoice_id " +
                      "WHERE i.is_paid = 1 " +
                      getTimeFilterCondition(timeFilter) +
                      "GROUP BY f.food_id, f.food_name, f.image, f.price " +
                      "HAVING total_quantity > 0 " +
                      "ORDER BY total_quantity ASC " +
                      "LIMIT 5";
        
        return executeInvoiceDetailQuery(query);
    }

    private List<InvoiceDetail> executeInvoiceDetailQuery(String query) {
        List<InvoiceDetail> details = new ArrayList<>();
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
                detail.setQuantity(rs.getInt("total_quantity"));
                detail.setTotalAmount(rs.getInt("total_amount"));
                details.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }
}