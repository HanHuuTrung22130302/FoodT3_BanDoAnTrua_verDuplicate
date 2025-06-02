package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.daos.SupplierDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Supplier;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SuppliersController", value = "/suppliers")
public class SuppliersController extends HttpServlet {

    private SupplierDAO supplierDAO;
    private static final int RECORDS_PER_PAGE = 5;
    private LogService logService;
    private CheckUserDao checkUserDao = new CheckUserDao();

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO();
        logService = new LogService();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageParam = request.getParameter("page");
        int currentPage = pageParam != null ? Integer.parseInt(pageParam) : 1;
        String searchText = request.getParameter("text");

        List<Supplier> supplierList = supplierDAO.getSuppliersPaginated(currentPage, RECORDS_PER_PAGE, searchText);
        int totalRecords = supplierDAO.countSuppliers(searchText);
        int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

        request.setAttribute("supplierList", supplierList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", searchText);
        request.getRequestDispatcher("/views/suppliers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String searchText = request.getParameter("text");
        String pageParam = request.getParameter("page");
        int currentPage = pageParam != null ? Integer.parseInt(pageParam) : 1;

        List<Supplier> supplierList;
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 1, "Truy cập trang quản lý danh mục", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        if ("add".equals(action)) {
            String supplierName = request.getParameter("supplierName");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            byte status = Byte.parseByte(request.getParameter("status"));

            Supplier supplier = new Supplier(0, supplierName, address, phone, email, status);
            supplierDAO.insertSupplier(supplier);
            
            // Ghi log thêm nhà cung cấp
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Thêm nhà cung cấp",
                "Thành công",
                "Thêm nhà cung cấp: " + supplierName
            );
        } else if ("edit".equals(action)) {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            String supplierName = request.getParameter("supplierName");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            byte status = Byte.parseByte(request.getParameter("status"));

            Supplier supplier = new Supplier(supplierId, supplierName, address, phone, email, status);
            supplierDAO.updateSupplier(supplier);
            
            // Ghi log cập nhật nhà cung cấp
            logService.logActivity(
                currentUser.getAccountId(),
                currentUser.getRoleId(),
                "Cập nhật nhà cung cấp",
                "Thành công",
                "Cập nhật nhà cung cấp ID: " + supplierId
            );
        }

        supplierList = supplierDAO.getSuppliersPaginated(currentPage, RECORDS_PER_PAGE, searchText);
        int totalRecords = supplierDAO.countSuppliers(searchText);
        int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

        request.setAttribute("supplierList", supplierList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", searchText);
        request.getRequestDispatcher("/views/suppliers.jsp").forward(request, response);
    }
}