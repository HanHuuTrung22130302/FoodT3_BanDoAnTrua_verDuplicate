package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.SupplierDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Supplier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        // Lấy danh sách nhà cung cấp từ DAO
        List<Supplier> supplierList = supplierDAO.getAllSuppliers(); // Giả sử SupplierDAO có phương thức này
        request.setAttribute("supplierList", supplierList); // Đặt danh sách vào request attribute

        // Chuyển tiếp đến suppliers.jsp
        request.getRequestDispatcher("/views/suppliers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Xử lý tìm kiếm nếu có
        String searchText = request.getParameter("text");
        List<Supplier> supplierList;

        if (searchText != null && !searchText.trim().isEmpty()) {
            // Tìm kiếm nhà cung cấp theo tên, số điện thoại hoặc email
            supplierList = supplierDAO.searchSuppliers(searchText); // Giả sử SupplierDAO có phương thức này
        } else {
            // Nếu không có tìm kiếm, lấy toàn bộ danh sách
            supplierList = supplierDAO.getAllSuppliers();
        }

        request.setAttribute("supplierList", supplierList);
        request.setAttribute("search", searchText); // Giữ giá trị tìm kiếm để hiển thị lại trên form
        request.getRequestDispatcher("/views/suppliers.jsp").forward(request, response);
    }
}