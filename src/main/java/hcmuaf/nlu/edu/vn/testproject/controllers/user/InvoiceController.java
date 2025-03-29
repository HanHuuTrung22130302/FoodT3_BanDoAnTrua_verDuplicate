package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.services.InvoiceOrderServices;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "InvoiceController", value = "/InvoiceController")
public class InvoiceController extends HttpServlet {
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("currentUser");

        if (acc == null) {
            logService.logActivity(0, 0, "Xem tổng đơn hàng", "Thất bại", "Người dùng chưa đăng nhập");
            response.sendRedirect("login");
            return;
        }

        InvoiceOrderServices invoiceOrderServices = new InvoiceOrderServices(acc.getAccountId());
        int totaldh = invoiceOrderServices.getTotalDonHang();

        session.setAttribute("totaldh", totaldh);
        logService.logActivity(acc.getAccountId(), acc.getRoleId(), "Xem tổng đơn hàng", "Thành công", "Tổng số đơn hàng: " + totaldh);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}