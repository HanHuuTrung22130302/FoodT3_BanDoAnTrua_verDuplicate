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

public class CheckUserBombOrder {
    public CheckUserBombOrder() {
    }

    public boolean checkOrderStatus6InCurrentMonth(int userId) {
        String query = """
        SELECT 1 FROM invoice inv
        JOIN order_status os ON inv.invoice_id = os.invoice_id
        WHERE inv.account_id = ?
          AND os.order_status = 6
          AND MONTH(os.updated_at) = MONTH(CURRENT_DATE())
          AND YEAR(os.updated_at) = YEAR(CURRENT_DATE())
        LIMIT 1
    """;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            return rs.next(); // Nếu có ít nhất 1 dòng, return true

        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra order_status = 6: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, ps, con);
        }

        return false; // Không có đơn hàng nào status = 6 trong tháng
    }

    private void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        CheckUserBombOrder checkUserBombOrder = new CheckUserBombOrder();
        checkUserBombOrder.lockAccountById(2);
    }
    public boolean lockAccountById(int accountId) {
        String query = "UPDATE account SET is_deleted = 1, updated_at = NOW() WHERE account_id = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = new DbContext().getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, accountId);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
