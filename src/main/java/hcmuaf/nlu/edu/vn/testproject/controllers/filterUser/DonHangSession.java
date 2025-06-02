package hcmuaf.nlu.edu.vn.testproject.controllers.filterUser;

import hcmuaf.nlu.edu.vn.testproject.daos.CheckUserBombOrder;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

import hcmuaf.nlu.edu.vn.testproject.services.InvoiceOrderServices;
import jakarta.servlet.http.*;

@WebFilter(filterName = "DonHangSession")
public class DonHangSession implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        if (session.getAttribute("currentUser") != null) {
            Account acc = (Account) session.getAttribute("currentUser");
            InvoiceOrderServices invoiceOrderServices = new InvoiceOrderServices(acc.getAccountId());
            int totaldh = invoiceOrderServices.getTotalDonHang();
            session.setAttribute("totaldh", totaldh);
            CheckUserBombOrder checkUserBombOrder = new CheckUserBombOrder();
            if (checkUserBombOrder.checkOrderStatus6InCurrentMonth(acc.getAccountId())){
                checkUserBombOrder.lockAccountById(acc.getAccountId());
            }
        }
        if (session.getAttribute("currentUser") == null) {

            int totaldh = 0;
            session.setAttribute("totaldh", totaldh);
        }


        chain.doFilter(request, response);
    }
}