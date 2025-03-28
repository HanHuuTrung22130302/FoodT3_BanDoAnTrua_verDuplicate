package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

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
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
@WebServlet(name = "BannerController", value = "/banner")
public class BannerController extends HttpServlet {
    private BannerService bannerService = new BannerService();
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            logService.logActivity(0, 0, "Xem danh sách banner", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        List<Banner> banners = bannerService.getBanners();
        request.setAttribute("bans", banners);
        request.getRequestDispatcher("views/banner.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() == 2) {
            logService.logActivity(0, 0, "Quản lý banner", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = bannerService.deleteBanner(id);
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xóa banner", success ? "Thành công" : "Thất bại", "Mã banner: " + id);
            response.sendRedirect("banner");
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

            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Thêm banner", success ? "Thành công" : "Thất bại", "Tên file: " + fileName);
            response.sendRedirect("banner");
        }
    }
}