package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.AccountDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.InvoiceDetail;
import hcmuaf.nlu.edu.vn.testproject.services.AccdetailService;
import hcmuaf.nlu.edu.vn.testproject.services.FoodServiceListFilter;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminController", value = "/admin")
public class AdminController extends HttpServlet {
    private LogService logService = new LogService();
    private CheckUserDao checkUserDao = new CheckUserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 0, "Xem bảng quản trị", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        FoodServiceListFilter foodServiceListFilter = new FoodServiceListFilter();
        AccdetailService accdetailService = new AccdetailService();
        try {
            List<Food> allFood = foodServiceListFilter.getOption("tatca");
            int totalFoods = allFood.size();

            List<Food> lst4Sold = foodServiceListFilter.getTop4Sold();

            List<AccountDetail> allAcc = accdetailService.getAccDetails(2);
            int totalAcc = allAcc.size();

            InvoiceDAO dao = new InvoiceDAO();
            List<InvoiceDetail> invoiceDetails = dao.getInvoiceDetails();
            int totalPrice = 0;
            for (InvoiceDetail invoiceDetail : invoiceDetails) {
                totalPrice += invoiceDetail.getTotalAmount();
            }

            request.setAttribute("totalFoods", totalFoods);
            request.setAttribute("totalAccs", totalAcc);
            request.setAttribute("lst4Sold", lst4Sold);
            request.setAttribute("totalRevenue", totalPrice);

            request.getRequestDispatcher("views/admin.jsp").forward(request, response);
        } catch (Exception e) {
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xem bảng quản trị", "Thất bại", "Lỗi: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra trong quá trình xử lý.");
            request.getRequestDispatcher("views/admin.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}