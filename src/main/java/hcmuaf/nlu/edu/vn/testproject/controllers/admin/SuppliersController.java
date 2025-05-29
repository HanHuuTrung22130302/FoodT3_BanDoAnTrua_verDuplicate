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
        supplierDAO = new SupplierDAO();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Supplier> supplierList = supplierDAO.getAllSuppliers();
        request.setAttribute("supplierList", supplierList);
        request.getRequestDispatcher("/views/suppliers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String searchText = request.getParameter("text");
        List<Supplier> supplierList;

        if ("add".equals(action)) {
            // Add new supplier
            String supplierName = request.getParameter("supplierName");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            byte status = Byte.parseByte(request.getParameter("status"));

            Supplier supplier = new Supplier(0, supplierName, address, phone, email, status);
            supplierDAO.insertSupplier(supplier);
            supplierList = supplierDAO.getAllSuppliers();
        } else if ("edit".equals(action)) {
            // Edit existing supplier
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            String supplierName = request.getParameter("supplierName");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            byte status = Byte.parseByte(request.getParameter("status"));

            Supplier supplier = new Supplier(supplierId, supplierName, address, phone, email, status);
            supplierDAO.updateSupplier(supplier);
            supplierList = supplierDAO.getAllSuppliers();
        } else if ("delete".equals(action)) {
            // Delete supplier (soft delete)
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            supplierDAO.softDeleteSupplier(supplierId);
            supplierList = supplierDAO.getAllSuppliers();
        } else {
            // Handle search
            if (searchText != null && !searchText.trim().isEmpty()) {
                supplierList = supplierDAO.searchSuppliers(searchText);
            } else {
                supplierList = supplierDAO.getAllSuppliers();
            }
        }

        request.setAttribute("supplierList", supplierList);
        request.setAttribute("search", searchText);
        request.getRequestDispatcher("/views/suppliers.jsp").forward(request, response);
    }
}