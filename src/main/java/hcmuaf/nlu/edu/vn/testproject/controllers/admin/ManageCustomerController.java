package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.AccdetailDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.AccountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.daos.LogDAO;
import hcmuaf.nlu.edu.vn.testproject.models.AccountDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.services.AccdetailService;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ManageCustomerController", value = "/customersevice")
public class ManageCustomerController extends HttpServlet {
    private CheckUserDao checkUserDao = new CheckUserDao();
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 0, "Xem danh sách thành viên", "Thất bại", "Không có quyền truy cập");
            session.invalidate();
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
        request.setAttribute("currentUser", currentUser);

        // Kiểm tra nếu là AJAX request
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            request.getRequestDispatcher("views/customer_sevice.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("views/customer_sevice.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Bạn không có quyền thực hiện thao tác này");
            out.print(jsonResponse.toString());
            session.invalidate();
            return;
        }

        String accountIdStr = request.getParameter("id");
        int accountId = 0;
        try {
            accountId = Integer.parseInt(accountIdStr);
        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "ID tài khoản không hợp lệ");
            out.print(jsonResponse.toString());
            return;
        }

        AccountDAO accountDAO = new AccountDAO();
        Account targetAccount = accountDAO.getUserById(accountId);

        if (targetAccount == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Không tìm thấy tài khoản");
            out.print(jsonResponse.toString());
            return;
        }

        // Kiểm tra quyền thực hiện thao tác
        boolean hasPermission = false;
        
        // Owner (roleId = 3) có quyền thực hiện với tất cả tài khoản
        if (currentUser.getRoleId() == 3) {
            hasPermission = true;
        }
        // Admin (roleId = 1) chỉ có quyền thực hiện với user (roleId = 2)
        else if (currentUser.getRoleId() == 1 && targetAccount.getRoleId() == 2) {
            hasPermission = true;
        }

        if (!hasPermission) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Bạn không có quyền thực hiện thao tác này");
            out.print(jsonResponse.toString());
            return;
        }

        LogDAO logDAO = new LogDAO();
        String logAction = "";
        String logResult = "";
        String logDetails = "";

        try {
            if ("delete".equals(action)) {
                boolean success = accountDAO.softDeleteAccount(accountId);
                if (success) {
                    logAction = "Vô hiệu hóa tài khoản";
                    logResult = "Thành công";
                    logDetails = "Vô hiệu hóa tài khoản ID: " + accountId;
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Đã vô hiệu hóa tài khoản thành công");
                } else {
                    logAction = "Vô hiệu hóa tài khoản";
                    logResult = "Thất bại";
                    logDetails = "Không thể vô hiệu hóa tài khoản ID: " + accountId;
                    jsonResponse.put("success", false);
                    jsonResponse.put("error", "Không thể vô hiệu hóa tài khoản");
                }
            } else if ("lock".equals(action)) {
                String hoursStr = request.getParameter("hours");
                int hours = 24;
                try {
                    hours = Integer.parseInt(hoursStr);
                } catch (NumberFormatException e) {
                    // Sử dụng giá trị mặc định
                }

                boolean success = accountDAO.lockAccount(accountId, hours);
                if (success) {
                    logAction = "Chặn tài khoản";
                    logResult = "Thành công";
                    logDetails = "Chặn tài khoản ID: " + accountId + " trong " + hours + " giờ";
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Đã chặn tài khoản thành công trong " + hours + " giờ");
                } else {
                    logAction = "Chặn tài khoản";
                    logResult = "Thất bại";
                    logDetails = "Không thể chặn tài khoản ID: " + accountId;
                    jsonResponse.put("success", false);
                    jsonResponse.put("error", "Không thể chặn tài khoản");
                }
            } else if ("unlock".equals(action)) {
                boolean success = accountDAO.unlockAccount(accountId);
                if (success) {
                    logAction = "Hủy chặn tài khoản";
                    logResult = "Thành công";
                    logDetails = "Hủy chặn tài khoản ID: " + accountId;
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Đã hủy chặn tài khoản thành công");
                } else {
                    logAction = "Hủy chặn tài khoản";
                    logResult = "Thất bại";
                    logDetails = "Không thể hủy chặn tài khoản ID: " + accountId;
                    jsonResponse.put("success", false);
                    jsonResponse.put("error", "Không thể hủy chặn tài khoản");
                }
            } else if ("activate".equals(action)) {
                boolean success = accountDAO.activateAccount(accountId);
                if (success) {
                    logAction = "Kích hoạt tài khoản";
                    logResult = "Thành công";
                    logDetails = "Kích hoạt tài khoản ID: " + accountId;
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Đã kích hoạt tài khoản thành công");
                } else {
                    logAction = "Kích hoạt tài khoản";
                    logResult = "Thất bại";
                    logDetails = "Không thể kích hoạt tài khoản ID: " + accountId;
                    jsonResponse.put("success", false);
                    jsonResponse.put("error", "Không thể kích hoạt tài khoản");
                }
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Hành động không hợp lệ");
            }

            // Ghi log nếu có hành động
            if (!logAction.isEmpty()) {
                logDAO.insertLog(currentUser.getAccountId(), currentUser.getRoleId(), logAction, logResult, logDetails);
            }
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
    }
}