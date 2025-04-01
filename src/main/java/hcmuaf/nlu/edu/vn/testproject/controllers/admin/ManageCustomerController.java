package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.AccdetailDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.models.AccountDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.services.AccdetailService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ManageCustomerController", value = "/customersevice")
public class ManageCustomerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            // Chuyển hướng về trang home nếu người dùng chưa đăng nhập
            response.sendRedirect("home");
            return;
        }

        AccdetailDAO dao = new AccdetailDAO();
        AccdetailService accdetailService = new AccdetailService();
        List<AccountDetail> listAcc = new ArrayList<>();
        
        // Lấy giá trị role từ select
        String role = request.getParameter("filterRole");
        int roleId = (role != null && role.equals("admin")) ? 1 : 2; // Mặc định là user (2)
        
        String txtSearch = request.getParameter("text");
        if (txtSearch != null && !txtSearch.isEmpty()) {
            // Tìm kiếm kết hợp với role
            listAcc = dao.searchAcc(txtSearch, roleId);
        } else {
            listAcc = accdetailService.getAccDetails(roleId);
        }
        
        request.setAttribute("selectedRole", role); // Để giữ lại giá trị đã chọn
        request.setAttribute("search", txtSearch);
        request.setAttribute("listAcc", listAcc);
        request.getRequestDispatcher("views/customer_sevice.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            HttpSession session = request.getSession();
            Account currentUser = (Account) session.getAttribute("currentUser");

            // Chỉ owner (roleId = 3) mới có quyền vô hiệu hóa admin
            if (currentUser.getRoleId() != 3) {
                response.sendRedirect("customersevice?error=unauthorized");
                return;
            }

            int accountId = Integer.parseInt(request.getParameter("id"));
            AccountDAO accountDAO = new AccountDAO();

            // Kiểm tra xem tài khoản bị vô hiệu hóa có phải là admin không
            Account targetAccount = accountDAO.getUserById(accountId);
            if (targetAccount != null && targetAccount.getRoleId() == 1) {
                accountDAO.softDeleteAccount(accountId);
            }
        }
        response.sendRedirect("customersevice");
    }
}