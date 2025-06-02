package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.daos.SupplierDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "DeleteSupplierServlet", value = "/suppliers_delete")
public class DeleteSupplierServlet extends HttpServlet {

    private SupplierDAO supplierDAO;
    private LogService logService;
    private CheckUserDao checkUserDao = new CheckUserDao();

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO();
        logService = new LogService();
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 1, "Truy cập trang quản lý danh mục", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        try {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            supplierDAO.softDeleteSupplier(supplierId);
            
            // Ghi log xóa nhà cung cấp
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Xóa nhà cung cấp",
                "Thành công",
                "Xóa nhà cung cấp ID: " + supplierId
            );
            
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", true);
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            // Ghi log lỗi xóa nhà cung cấp
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Xóa nhà cung cấp",
                "Thất bại",
                "Lỗi: " + e.getMessage()
            );
            
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Lỗi khi xóa nhà cung cấp: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(jsonResponse.toString());
        }
    }
}