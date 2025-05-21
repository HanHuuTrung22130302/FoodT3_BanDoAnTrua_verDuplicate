package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import com.google.gson.Gson;
import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Banner;
import hcmuaf.nlu.edu.vn.testproject.services.BannerService;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
@WebServlet(name = "BannerController", value = "/banner")
public class BannerController extends HttpServlet {
    private BannerService bannerService = new BannerService();
    private LogService logService = new LogService();
    private Gson gson = new Gson();
    private CheckUserDao checkUserDao = new CheckUserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 0, "Xem danh sách banner", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        // Log truy cập trang banner
        logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), 
            "Truy cập trang quản lý banner", "Thành công", "Truy cập trang banner");

        List<Banner> banners = bannerService.getBanners();
        request.setAttribute("bans", banners);
        request.getRequestDispatcher("views/banner.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();
        
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (!checkUserDao.isAdmin(currentUser.getAccountId())) {
            logService.logActivity(0, 0, "Quản lý banner", "Thất bại", "Không có quyền truy cập");
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Không có quyền truy cập");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = bannerService.deleteBanner(id);
            
            // Log hành động xóa banner
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), 
                "Xóa banner", success ? "Thành công" : "Thất bại", "Mã banner: " + id);
            
            jsonResponse.put("success", success);
            out.print(gson.toJson(jsonResponse));
            
        } else if ("add".equals(action)) {
            Part filePart = request.getPart("image");
            String fileName = filePart.getSubmittedFileName();
            String uploadPath = request.getServletContext().getRealPath("/Images/home/") + fileName;

            File uploadDir = new File(getServletContext().getRealPath("/Images/home/"));
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            filePart.write(uploadPath);

            Banner banner = new Banner(0, "Images/home/" + fileName, new java.util.Date());
            boolean success = bannerService.addBanner(banner);

            // Log hành động thêm banner
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), 
                "Thêm banner", success ? "Thành công" : "Thất bại", "Tên file: " + fileName);

            if (success) {
                jsonResponse.put("success", true);
                jsonResponse.put("bannerId", banner.getBannerId());
                jsonResponse.put("url", banner.getUrl());
                jsonResponse.put("date", banner.getDate());
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Thêm banner thất bại");
            }
            out.print(gson.toJson(jsonResponse));
        }
    }
}