package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Discount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO {
    private List<Discount> discounts;

    public DiscountDAO() {
        discounts = new ArrayList<Discount>();
    }

    public List<Discount> getAllDiscounts() {
        // Xóa danh sách cũ trước khi thêm dữ liệu mới
        discounts.clear();

        String query = "SELECT * FROM discount_code";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Tạo kết nối cơ sở dữ liệu
            conn = new DbContext().getConnection();
            // Kiểm tra kết nối
            if (conn != null) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại!");
                // Trả về danh sách rỗng nếu không kết nối được
            }

            // Chuẩn bị câu lệnh SQL
            ps = conn.prepareStatement(query);
            // Thực thi câu lệnh
            rs = ps.executeQuery();

            // Duyệt qua kết quả trả về và tạo danh sách món ăn
            while (rs.next()) {
                discounts.add(new Discount(
                        rs.getInt("discount_code_id"),
                        rs.getString("code_name"),
                        rs.getDouble("discount_rate"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo rằng kết nối, câu lệnh và result set được đóng đúng cách
            closeResources(rs, ps, conn);
        }
        return discounts;
    }

    // Phương thúc thêm mã giảm giá
    public boolean addDiscount(Discount discount) {
        String query = "INSERT INTO discount_code (code_name, discount_rate, title, description, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, discount.getCodeName());
            ps.setDouble(2, discount.getDiscountRate());
            ps.setString(3, discount.getTitle());
            ps.setString(4, discount.getDescription());
            ps.setDate(5, new java.sql.Date(discount.getStartDate().getTime()));
            ps.setDate(6, new java.sql.Date(discount.getEndDate().getTime()));

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi thêm mã giảm giá: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, ps, conn);
        }
    }

    public boolean deleteDiscount(int idCode) {
        String query = "DELETE FROM discount_code WHERE discount_code_id = ?";
        try (Connection conn = new DbContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi khi xóa mã giảm giá: " + e.getMessage());
        }
        return false;
    }

    // Phương thức đóng các tài nguyên
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        }
    }
}
