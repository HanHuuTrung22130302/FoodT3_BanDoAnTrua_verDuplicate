package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.SupplierDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Supplier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SuppliersController", value = "/suppliers")
public class SuppliersController extends HttpServlet {

    private SupplierDAO supplierDAO;

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO(); // Khởi tạo DAO
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String filter = request.getParameter("filter");
        String search = request.getParameter("search");
        String pageStr = request.getParameter("page");
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int pageSize = 10; // Số nhà cung cấp mỗi trang

        if (filter == null) filter = "all";

        // Lấy danh sách nhà cung cấp từ DAO
        List<Supplier> supplierList = supplierDAO.getSuppliers(filter, search, page, pageSize);
        int totalSuppliers = supplierDAO.getTotalSuppliers(filter, search);
        int totalPages = (int) Math.ceil((double) totalSuppliers / pageSize);

        // Xử lý yêu cầu Ajax để lấy thông tin nhà cung cấp
        String action = request.getParameter("action");
        if ("get".equals(action)) {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            Supplier supplier = supplierDAO.getSupplierById(supplierId);
            if (supplier != null) {
                JSONObject json = new JSONObject();
                json.put("supplierId", supplier.getSupplierId());
                json.put("supplierName", supplier.getSupplierName());
                json.put("address", supplier.getAddress());
                json.put("phone", supplier.getPhone());
                json.put("email", supplier.getEmail());
                json.put("status", supplier.getStatus());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json.toString());
            }
            return;
        }

        // Đặt các thuộc tính cho JSP
        request.setAttribute("supplierList", supplierList);
        request.setAttribute("filter", filter);
        request.setAttribute("search", search);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("views/suppliers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 1) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            Supplier supplier = new Supplier();
            supplier.setSupplierName(request.getParameter("supplierName"));
            supplier.setAddress(request.getParameter("address"));
            supplier.setPhone(request.getParameter("phone"));
            supplier.setEmail(request.getParameter("email"));
            supplier.setStatus(Integer.parseInt(request.getParameter("status")));

            boolean success = supplierDAO.addSupplier(supplier);
            if (success) {
                session.setAttribute("message", "Thêm nhà cung cấp thành công!");
            } else {
                session.setAttribute("error", "Lỗi khi thêm nhà cung cấp!");
            }
        } else if ("update".equals(action)) {
            Supplier supplier = new Supplier();
            supplier.setSupplierId(Integer.parseInt(request.getParameter("supplierId")));
            supplier.setSupplierName(request.getParameter("supplierName"));
            supplier.setAddress(request.getParameter("address"));
            supplier.setPhone(request.getParameter("phone"));
            supplier.setEmail(request.getParameter("email"));
            supplier.setStatus(Integer.parseInt(request.getParameter("status")));

            boolean success = supplierDAO.updateSupplier(supplier);
            if (success) {
                session.setAttribute("message", "Cập nhật nhà cung cấp thành công!");
            } else {
                session.setAttribute("error", "Lỗi khi cập nhật nhà cung cấp!");
            }
        } else if ("delete".equals(action)) {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            boolean success = supplierDAO.deleteSupplier(supplierId);
            if (success) {
                session.setAttribute("message", "Xóa nhà cung cấp thành công!");
            } else {
                session.setAttribute("error", "Lỗi khi xóa nhà cung cấp!");
            }
        }

        response.sendRedirect("suppliers");
    }
}