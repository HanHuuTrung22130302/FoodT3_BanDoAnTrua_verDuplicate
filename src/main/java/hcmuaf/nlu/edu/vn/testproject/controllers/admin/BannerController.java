package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Banner;
import hcmuaf.nlu.edu.vn.testproject.services.BannerService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold =  1024*1024*2, // 2MB
        maxFileSize = 1024*1024*10, // 10MB
        maxRequestSize = 1024*1024*50 // 50MB
)

@WebServlet(name = "BannerController", value = "/banner")
public class BannerController extends HttpServlet {
    BannerService bannerService = new BannerService();

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
        BannerService bannerService = new BannerService();
        List<Banner> banners = bannerService.getBanners();
        request.setAttribute("bans", banners);
        request.getRequestDispatcher("views/banner.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            bannerService.deleteBanner(id);
            response.sendRedirect("banner");
        } else if ("add".equals(action)) {
            Part filePart = request.getPart("image");
            String fileName = filePart.getSubmittedFileName();
            String uploadPath = request.getServletContext().getRealPath("/Images/home/") + fileName;

            // Tạo thư mục nếu chưa tồn tại, có rồi thì thôi
            File uploadDir = new File(getServletContext().getRealPath("/Images/home/"));
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Lưu file vào thư mục Images của project
            filePart.write(uploadPath);

            Banner banner = new Banner(0, "Images/home/" + fileName, new java.util.Date());
            new BannerService().addBanner(banner);
            response.sendRedirect("banner");
        }
    }
}