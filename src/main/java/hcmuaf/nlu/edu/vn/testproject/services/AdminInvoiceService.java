package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.AdminInvoiceOrderDao;
import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceOrderDao;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoiceDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminInvoiceService {

    public AdminInvoiceOrderDao dao;

    public AdminInvoiceService() {
        this.dao = new AdminInvoiceOrderDao();
    }


    public List<OrderInvoice> getOption(String option, int offset) {
        List<OrderInvoice> ois = new ArrayList<>();
        int count = countDash(option);
        if (count > 0) {
            switch (count) {
                case 1:
                    ois = dao.getInvoiceByMonth(option, offset);
                    break;
                case 2:
                    ois = dao.getInvoiceByDate(option, offset);
                    break;
            }
        } else if (count == 0) {
            switch (option) {
                case "all":
                    ois = dao.getAdminInvoiceOrder(0, offset);
                    break;
                case "waitingConfirm":
                    ois = dao.getAdminInvoiceOrder(1, offset);
                    break;
                case "preparing":
                    ois = dao.getAdminInvoiceOrder(2, offset);
                    break;
                case "shipping":
                    ois = dao.getAdminInvoiceOrder(3, offset);
                    break;
                case "delivered":
                    ois = dao.getAdminInvoiceOrder(4, offset);
                    break;
                case "canceled":
                    ois = dao.getAdminInvoiceOrder(5, offset);
                    break;
                case "ghostBuy":
                    ois = dao.getAdminInvoiceOrder(6, offset);
                    break;
                default:
                    ois = dao.getInvoicesByIdOrPrefix(option, offset);
                    if (ois.isEmpty())
                        ois = dao.searchAdminInvoiceByRecipientName(option, offset);
                    break;
            }
        }
        for (OrderInvoice oi : ois) {
            List<OrderInvoiceDetail> details = dao.getInvoiceOrderDetails(oi.getInvoiceId());
            oi.setOrderInvoiceDetail(details);
        }
        setCompletionTimeForInvoices(ois);
        setReasonForInvoices(ois);
        return ois;
    }

    public int countInvoicesByOption(String option) {
        int count = countDash(option);

        if (count > 0) {
            return switch (count) {
                case 1 -> dao.countInvoiceByMonth(option);
                case 2 -> dao.countInvoiceByDate(option);
                default -> 0;
            };
        } else {
            switch (option) {
                case "waitingConfirm":
                    return dao.countAdminInvoiceOrder(1);
                case "preparing":
                    return dao.countAdminInvoiceOrder(2);
                case "shipping":
                    return dao.countAdminInvoiceOrder(3);
                case "delivered":
                    return dao.countAdminInvoiceOrder(4);
                case "canceled":
                    return dao.countAdminInvoiceOrder(5);
                case "all":
                    return dao.countAdminInvoiceOrder(0);
                case "ghostBuy":
                    return dao.countAdminInvoiceOrder(6);
                default:
                    int countResult = dao.countInvoiceByIdOrPrefix(option);
                    if (countResult == 0) {
                        countResult = dao.countInvoiceByRecipientName(option);
                    }
                    return countResult;
            }
        }
    }

    public void setCompletionTimeForInvoices(List<OrderInvoice> invoices) {
        for (OrderInvoice oi : invoices) {
            int status = oi.getOrderStatus();
            if (status == 4 || status == 5||status==6) {
                String completionTime = dao.getCompletionTime(oi.getInvoiceId());
                oi.setCompletionTime(completionTime);
            } else {
                oi.setCompletionTime(null);
            }
        }
    }
    public void setReasonForInvoices(List<OrderInvoice> invoices) {
        for (OrderInvoice oi : invoices) {
            int status = oi.getOrderStatus();
            if (status == 5||status==6) {
                // Lấy reason từ DAO và set
                String reason = dao.getReason(oi.getInvoiceId());
                oi.setReason(reason);
            } else {
                oi.setReason(null);
            }
        }
    }

    public void sendMoveStatusNext(int invoiceId){
        dao.moveOrderStatusForward(invoiceId);
    }
    public void sendMoveStatusBombOrder(int invoiceId,String reason){
        dao.bombOrder(invoiceId,reason);
    }
    public void sendCancelStatusNext(int invoiceId,String reason){
        dao.cancelOrder(invoiceId,reason);
    }

    public static int countDash(String input) {
        if (input == null) return 0;
        int count = 0;
        for (char c : input.toCharArray()) {
            if (c == '-') {
                count++;
            }
        }
        return count;
    }
public boolean isAdmin(int accountId) {
        return dao.isAdmin(accountId);
}



    public OrderInvoice getInvoiceById(int invoiceId) {
        OrderInvoice oi = dao.getInvoiceById(invoiceId);
        if (oi != null) {
            setCompletionTimeForInvoices(Collections.singletonList(oi));
            setReasonForInvoices(Collections.singletonList(oi));
        }
        return oi;
    }
public String getReasonForInvoices(int invoiceId) {
        return dao.getReason(invoiceId);
}

    public static void main(String[] args) {
        AdminInvoiceService as = new AdminInvoiceService();
        System.out.println(as.countInvoicesByOption("canceled"));
    }
}