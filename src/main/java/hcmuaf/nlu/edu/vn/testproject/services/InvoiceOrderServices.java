package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceOrderDao;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoiceDetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InvoiceOrderServices {
    public InvoiceOrderDao invoiceOrderDao;
    private int id;

    public InvoiceOrderServices(int id) {
        this.id = id;
        this.invoiceOrderDao = new InvoiceOrderDao(id);
    }

    public List<OrderInvoice> getOption(String option) {
        List<OrderInvoice> ois = new ArrayList<>();
        switch (option) {
            case "0":
                ois = invoiceOrderDao.getAll();
                break;
            case "1":
                ois = invoiceOrderDao.getInvoiceShipping();
                break;
            case "2":
                ois = invoiceOrderDao.getInvoiceDelivered();
                break;
            case "3":
                ois = invoiceOrderDao.getInvoiceCancelled();
                break;
            default:
                ois = invoiceOrderDao.filterOrderByFoodName(option);
                break;

        }
        Collections.reverse(ois);
        return ois;
    }

    public OrderInvoice getOrder(String orderId) {
        int id = Integer.parseInt(orderId);
        return invoiceOrderDao.getInvoiceOrder(id);
    }

    public void cancelInvoice(String orderId) {
        int id = Integer.parseInt(orderId);
        invoiceOrderDao.canclInvoice(id);
    }

    public int getTotalDonHang() {
        return invoiceOrderDao.getTotalShippingInvoices();
    }
}
