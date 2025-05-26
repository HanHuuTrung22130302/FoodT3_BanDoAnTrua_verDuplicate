package hcmuaf.nlu.edu.vn.testproject.controllers.filterUser;

import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserDao;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.UserStatus;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(filterName = "CheckAccountStatusFilter", urlPatterns = {"/about","/AccDetail","/user","/addtoCart","/allmenu","/home","/PurchaseOrderDetail","/PurchaseOrder","/contact"})
public class CheckAccountStatusFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session != null && session.getAttribute("currentUser") != null) {
            Account currentUser = (Account) session.getAttribute("currentUser");
            int sessionRole = currentUser.getRoleId();
            int accountId = currentUser.getAccountId();

            CheckUserDao dao = new CheckUserDao();
            UserStatus dbStatus = dao.getUserStatus(accountId);

            if (dbStatus != null) {
                boolean isLocked = dbStatus.isLocked != 0;
                boolean isDeleted = dbStatus.isDeleted != 0;
                boolean roleChanged = dbStatus.roleId != sessionRole;

                if (isLocked || isDeleted || roleChanged) {
                    session.invalidate();
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}
