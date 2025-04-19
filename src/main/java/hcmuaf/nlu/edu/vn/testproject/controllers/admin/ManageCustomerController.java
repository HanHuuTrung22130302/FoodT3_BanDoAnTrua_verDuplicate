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

            String accountIdStr = request.getParameter("id");
            int accountId = 0;
            try {
                accountId = Integer.parseInt(accountIdStr);
            } catch (NumberFormatException e) {
                System.err.println("Lỗi chuyển đổi accountId: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "ID tài khoản không hợp lệ");
                doGet(request, response);
                return;
            }

            AccountDAO accountDAO = new AccountDAO();
            Account targetAccount = accountDAO.getUserById(accountId);

            if (targetAccount != null) {
                if (currentUser.getRoleId() == 3 || (currentUser.getRoleId() == 1 && targetAccount.getRoleId() == 2)) {
                    // Kiểm tra trạng thái hiện tại
                    boolean currentStatus = accountDAO.checkAccountDeletedStatus(accountId);
                    boolean success = accountDAO.softDeleteAccount(accountId);
                    if (success) {
                        // Kiểm tra lại sau khi cập nhật
                        boolean newStatus = accountDAO.checkAccountDeletedStatus(accountId);
                        if (newStatus) {
                            request.setAttribute("success", "Vô hiệu hóa tài khoản thành công");
                        } else {
                            request.setAttribute("error", "Tài khoản không được vô hiệu hóa. Vui lòng thử lại sau.");
                        }
                    } else {
                        request.setAttribute("error", "Không thể vô hiệu hóa tài khoản. Vui lòng thử lại sau.");
                    }
                } else {
                    request.setAttribute("error", "Bạn không có quyền vô hiệu hóa tài khoản này");
                }
            } else {
                request.setAttribute("error", "Không tìm thấy tài khoản với ID: " + accountId);
            }
        } else if ("lock".equals(action)) {
            HttpSession session = request.getSession();
            Account currentUser = (Account) session.getAttribute("currentUser");
            String accountIdStr = request.getParameter("id");
            int accountId = 0;
            try {
                accountId = Integer.parseInt(accountIdStr);
            } catch (NumberFormatException e) {
                System.err.println("Lỗi chuyển đổi accountId: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "ID tài khoản không hợp lệ");
                doGet(request, response);
                return;
            }

            String hoursStr = request.getParameter("hours");
            int hours = 24; // Mặc định là 24 giờ
            try {
                hours = Integer.parseInt(hoursStr);
            } catch (NumberFormatException e) {
                System.err.println("Lỗi chuyển đổi hours: " + e.getMessage());
                e.printStackTrace();
                // Sử dụng giá trị mặc định
            }

            AccountDAO accountDAO = new AccountDAO();
            Account targetAccount = accountDAO.getUserById(accountId);
            if (targetAccount != null) {
                if (currentUser.getRoleId() == 3 || (currentUser.getRoleId() == 1 && targetAccount.getRoleId() == 2)) {
                    boolean success = accountDAO.lockAccount(accountId, hours);
                    if (success) {
                        request.setAttribute("success", "Đã chặn tài khoản thành công trong " + hours + " giờ");
                    } else {
                        request.setAttribute("error", "Không thể chặn tài khoản. Vui lòng thử lại sau.");
                    }
                } else {
                    request.setAttribute("error", "Bạn không có quyền chặn tài khoản này");
                }
            } else {
                request.setAttribute("error", "Không tìm thấy tài khoản với ID: " + accountId);
            }
        } else if ("unlock".equals(action)) {
            HttpSession session = request.getSession();
            Account currentUser = (Account) session.getAttribute("currentUser");
            String accountIdStr = request.getParameter("id");
            int accountId = 0;
            try {
                accountId = Integer.parseInt(accountIdStr);
            } catch (NumberFormatException e) {
                System.err.println("Lỗi chuyển đổi accountId: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "ID tài khoản không hợp lệ");
                doGet(request, response);
                return;
            }

            AccountDAO accountDAO = new AccountDAO();
            Account targetAccount = accountDAO.getUserById(accountId);
            if (targetAccount != null) {
                if (currentUser.getRoleId() == 3 || (currentUser.getRoleId() == 1 && targetAccount.getRoleId() == 2)) {
                    boolean success = accountDAO.unlockAccount(accountId);
                    if (success) {
                        request.setAttribute("success", "Đã hủy chặn tài khoản thành công");
                    } else {
                        request.setAttribute("error", "Không thể hủy chặn tài khoản. Vui lòng thử lại sau.");
                    }
                } else {
                    request.setAttribute("error", "Bạn không có quyền hủy chặn tài khoản này");
                }
            } else {
                request.setAttribute("error", "Không tìm thấy tài khoản với ID: " + accountId);
            }
        }
        doGet(request, response);
    }
}