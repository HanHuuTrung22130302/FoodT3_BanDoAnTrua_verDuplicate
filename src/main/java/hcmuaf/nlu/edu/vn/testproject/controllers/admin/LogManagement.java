package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.LogEntry;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "LogManagement", value = "/LogManagement")
public class LogManagement extends HttpServlet {
    private LogService logService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.logService = new LogService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        Account currentUser = (Account) request.getSession().getAttribute("currentUser");
        if (currentUser == null || currentUser.getRoleId() == 2) {
            response.sendRedirect("home");
            return;
        }
        
        String filterRoleId = request.getParameter("filterRoleId");
        String filterDate = request.getParameter("filterDate");
        String filterAction = request.getParameter("filterAction");

        Date parsedDate = null;
        if (filterDate != null && !filterDate.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                parsedDate = sdf.parse(filterDate);
            } catch (ParseException e) {
                System.err.println("Lỗi khi parse ngày: " + e.getMessage());
                e.printStackTrace();
            }
        }

        List<LogEntry> logs = logService.getLogs(filterRoleId, parsedDate, filterAction);

        request.setAttribute("selectedRoleId", filterRoleId != null ? filterRoleId : "all");
        request.setAttribute("selectedDate", filterDate);
        request.setAttribute("selectedAction", filterAction);
        
        request.setAttribute("logs", logs);
        
        request.getRequestDispatcher("/views/log_management.jsp").forward(request, response);
    }
}