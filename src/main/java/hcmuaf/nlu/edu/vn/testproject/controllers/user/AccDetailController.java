package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.AccdetailDAO;
import hcmuaf.nlu.edu.vn.testproject.models.AccountDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Category;
import hcmuaf.nlu.edu.vn.testproject.services.CategoryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AccDetailController", value = "/AccDetail")
public class AccDetailController extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");
        CategoryService cs = new CategoryService();
        List<Category> categoryList = cs.getCategories();
        request.setAttribute("listC", categoryList);
        if (currentUser != null) {
            int userId = currentUser.getAccountId();
            AccdetailDAO accountDAO = new AccdetailDAO();
            Account account = accountDAO.getAccountById(userId);
            AccountDetail accDetail = accountDAO.getAccDetailById(userId);

            if (account != null && accDetail != null) {
                request.setAttribute("account", account);
                request.setAttribute("accDetail", accDetail);
                request.getRequestDispatcher("views/UserInformation.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin tài khoản.");
                request.getRequestDispatcher("views/UserInformation.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> result = new HashMap<>();

        try {
            // Lấy thông tin từ form và xử lý null
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String birthDate = request.getParameter("birthDate");
            String genderStr = request.getParameter("gender");
            String address = request.getParameter("address");

            // Validate dữ liệu đầu vào
            if (fullName == null || fullName.trim().isEmpty() ||
                    phoneNumber == null || phoneNumber.trim().isEmpty() ||
                    birthDate == null || birthDate.trim().isEmpty() ||
                    genderStr == null || genderStr.trim().isEmpty() ||
                    address == null || address.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Vui lòng điền đầy đủ thông tin!");
                response.getWriter().write(gson.toJson(result));
                return;
            }

            // Parse gender với xử lý lỗi
            int gender;
            try {
                genderStr = genderStr != null ? genderStr : "0"; // Mặc định là Nam nếu null
                gender = Integer.parseInt(genderStr);
            } catch (NumberFormatException e) {
                gender = 0; // Mặc định là Nam nếu parse lỗi
            }

            // Lấy đối tượng Account từ session
            Account account = (Account) request.getSession().getAttribute("currentUser");

            if (account != null) {
                // Cập nhật thông tin vào cơ sở dữ liệu
                AccdetailDAO accdetailDAO = new AccdetailDAO();
                AccountDetail existingDetail = accdetailDAO.getAccDetailById(account.getAccountId());

                if (existingDetail == null) {
                    // Nếu chưa có AccDetail, thêm mới
                    boolean addResult = accdetailDAO.addAccDetail(account.getAccountId(), fullName, address, phoneNumber, birthDate, gender);
                    if (!addResult) {
                        result.put("success", false);
                        result.put("message", "Không thể thêm thông tin chi tiết tài khoản!");
                        response.getWriter().write(gson.toJson(result));
                        return;
                    }
                } else {
                    // Nếu đã có AccDetail, thực hiện cập nhật
                    accdetailDAO.updateAccdetail(account.getAccountId(), fullName, address, phoneNumber, birthDate, gender);
                }

                // Cập nhật lại thông tin trong session
                AccountDetail updatedDetail = accdetailDAO.getAccDetailById(account.getAccountId());
                account.setAccountDetail(updatedDetail);
                request.getSession().setAttribute("currentUser", account);

                result.put("success", true);
                result.put("message", "Cập nhật thông tin thành công!");
            } else {
                result.put("success", false);
                result.put("message", "Không tìm thấy thông tin tài khoản!");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace(); // Log lỗi để debug
        }
        response.getWriter().write(gson.toJson(result));
    }
}



