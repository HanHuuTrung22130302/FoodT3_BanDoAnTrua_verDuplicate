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
        List<OrderInvoice> ois= new ArrayList<>();
        switch (option) {
            case "all":
                ois = dao.getAdminInvoiceOrder(0,offset);
                break;
            case "waitingConfirm":
                ois = dao.getAdminInvoiceOrder(1,offset);
                break;
            case "preparing":
                ois = dao.getAdminInvoiceOrder(2,offset);
                break;
            case "shipping":
                ois = dao.getAdminInvoiceOrder(3,offset);
                break;
            case "delivered":
                ois = dao.getAdminInvoiceOrder(4,offset);
                break;
            case "canceled":
                ois = dao.getAdminInvoiceOrder(5,offset);
                break;
            case "today":
                ois = dao.getInvoiceByToday(offset);
                break;
            case "sameMonth":
                ois = dao.getInvoiceByToday(offset);
                break;
            case "sameYear":
                ois = dao.getInvoiceByThisYear(offset);
                break;
            default:
                ois =  dao.getInvoicesByIdOrPrefix(option,offset);
                break;
        }
        for (OrderInvoice oi : ois) {
            List<OrderInvoiceDetail> details = dao.getInvoiceOrderDetails(oi.getInvoiceId());
            oi.setOrderInvoiceDetail(details);
        }
        return ois;
    }

    public int countInvoicesByOption(String option) {
        return switch (option) {
            case "waitingConfirm" -> dao.countAdminInvoiceOrder(1);
            case "preparing"      -> dao.countAdminInvoiceOrder(2);
            case "shipping"       -> dao.countAdminInvoiceOrder(3);
            case "delivered"      -> dao.countAdminInvoiceOrder(4);
            case "canceled"       -> dao.countAdminInvoiceOrder(5);
            case "today"          -> dao.countInvoiceToday();
            case "sameMonth"      -> dao.countInvoiceThisMonth();
            case "sameYear"       -> dao.countInvoiceThisYear();
            case "all"            -> dao.countAdminInvoiceOrder(0);
            default               -> dao.countInvoiceByIdOrPrefix(option); // tìm theo mã đơn hàng
        };
    }

}