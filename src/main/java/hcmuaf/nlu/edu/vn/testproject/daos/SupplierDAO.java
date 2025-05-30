package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.context.DbContext;
import hcmuaf.nlu.edu.vn.testproject.models.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // Lấy tất cả nhà cung cấp
    public List<Supplier> getAllSuppliers() {
        return getSuppliersPaginated(1, Integer.MAX_VALUE, null);
    }

    // Lấy nhà cung cấp theo phân trang và tìm kiếm
    public List<Supplier> getSuppliersPaginated(int page, int pageSize, String searchText) {
        List<Supplier> suppliers = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM suppliers");
        if (searchText != null && !searchText.trim().isEmpty()) {
            query.append(" WHERE supplier_name LIKE ? OR phone LIKE ? OR email LIKE ?");
        }
        query.append(" ORDER BY supplier_id LIMIT ? OFFSET ?");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query.toString());
            int paramIndex = 1;
            if (searchText != null && !searchText.trim().isEmpty()) {
                String searchPattern = "%" + searchText.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, (page - 1) * pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getByte("status")
                );
                suppliers.add(supplier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return suppliers;
    }

    // Đếm tổng số nhà cung cấp
    public int countSuppliers(String searchText) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM suppliers");
        if (searchText != null && !searchText.trim().isEmpty()) {
            query.append(" WHERE supplier_name LIKE ? OR phone LIKE ? OR email LIKE ?");
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query.toString());
            if (searchText != null && !searchText.trim().isEmpty()) {
                String searchPattern = "%" + searchText.trim() + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
                ps.setString(3, searchPattern);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    // Tìm kiếm nhà cung cấp theo tên, số điện thoại hoặc email
    public List<Supplier> searchSuppliers(String searchText) {
        return getSuppliersPaginated(1, Integer.MAX_VALUE, searchText);
    }

    // Lấy nhà cung cấp theo ID
    public Supplier getSupplierById(int supplierId) {
        String query = "SELECT * FROM suppliers WHERE supplier_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, supplierId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getByte("status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Thêm nhà cung cấp mới
    public void insertSupplier(Supplier supplier) {
        String query = "INSERT INTO suppliers (supplier_name, address, phone, email, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getAddress());
            ps.setString(3, supplier.getPhone());
            ps.setString(4, supplier.getEmail());
            ps.setInt(5, supplier.getStatus());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Cập nhật thông tin nhà cung cấp
    public void updateSupplier(Supplier supplier) {
        String query = "UPDATE suppliers SET supplier_name = ?, address = ?, phone = ?, email = ?, status = ? WHERE supplier_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getAddress());
            ps.setString(3, supplier.getPhone());
            ps.setString(4, supplier.getEmail());
            ps.setInt(5, supplier.getStatus());
            ps.setInt(6, supplier.getSupplierId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Xóa mềm nhà cung cấp (chuyển status = 0)
    public void softDeleteSupplier(int supplierId) {
        String query = "UPDATE suppliers SET status = 0 WHERE supplier_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DbContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, supplierId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}