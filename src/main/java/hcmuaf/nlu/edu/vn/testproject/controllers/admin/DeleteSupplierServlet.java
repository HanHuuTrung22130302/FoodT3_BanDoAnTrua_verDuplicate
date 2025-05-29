package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "DeleteSupplierServlet", value = "/suppliers_delete")
public class DeleteSupplierServlet extends HttpServlet {

    private SupplierDAO supplierDAO;

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO();
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            supplierDAO.softDeleteSupplier(supplierId);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", true);
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Lỗi khi xóa nhà cung cấp: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(jsonResponse.toString());
        }
    }
}