package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Banner;
import hcmuaf.nlu.edu.vn.testproject.models.ReviewFood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BannerDao {
    private List<Banner> banners;

    public BannerDao() {
        this.banners = new ArrayList<>();
        getAllBanner();
    }

    public List<Banner> getBanners() {
        return banners;
    }

    // Hàm lấy tất cả các món ăn từ cơ sở dữ liệu
    public void getAllBanner() {

        String query = "select * from banner";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Tạo kết nối cơ sở dữ liệu
            con = new DbContext().getConnection();
            // Kiểm tra kết nối
            if (con != null) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại!");
                // Trả về danh sách rỗng nếu không kết nối được
            }

            // Chuẩn bị câu lệnh SQL
            ps = con.prepareStatement(query);
            // Thực thi câu lệnh
            rs = ps.executeQuery();

            // Duyệt qua kết quả trả về và tạo danh sách món ăn
            while (rs.next()) {
                banners.add(
                        new Banner(rs.getInt("banner_id"),
                                rs.getString("url"),
                                rs.getDate("created_at")
                        )
                );
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

    public void addBanner(Banner banner) {
        String query = "INSERT INTO banner (url, created_at) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

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
            ps.setString(1, banner.getUrl());
            ps.setDate(2, new java.sql.Date(banner.getDate().getTime()));
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBaner(int id) {
        String query = "DELETE FROM banner WHERE banner_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

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
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        }
    }
}
