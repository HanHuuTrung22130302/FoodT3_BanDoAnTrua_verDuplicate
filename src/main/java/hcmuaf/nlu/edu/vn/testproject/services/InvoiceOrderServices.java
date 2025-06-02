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
    public InvoiceOrderServices() {
        this.invoiceOrderDao = new InvoiceOrderDao();
    }

//    public List<OrderInvoice> getOption(String option) {
//        List<OrderInvoice> ois = new ArrayList<>();
//        switch (option) {
//            case "0":
//                ois = invoiceOrderDao.getAll();
//                break;
//            case "1":
//                ois = invoiceOrderDao.getInvoiceRequest();
//                break;
//            case "2":
//                ois = invoiceOrderDao.getInvoiceCoooking();
//                break;
//            case "3":
//                ois = invoiceOrderDao.getInvoiceShipping();
//                break;
//            case "4":
//                ois = invoiceOrderDao.getInvoiceSuccess();
//                break;
//            case "5":
//                ois = invoiceOrderDao.getInvoiceCancelled();
//                break;
//            default:
//                ois = invoiceOrderDao.filterOrderByFoodName(option);
//                break;
//
//        }
//        Collections.reverse(ois);
//        return ois;
//    }

    public OrderInvoice getOrder(String orderId) {
        int id = Integer.parseInt(orderId);
        return invoiceOrderDao.getInvoiceOrder(id);
    }

    public void cancelInvoice(String orderId,String reason) {
        int id = Integer.parseInt(orderId);
        invoiceOrderDao.cancelInvoice(id,reason);
    }

    public int getTotalDonHang() {
        return invoiceOrderDao.getTotalShippingInvoices(id);
    }
    public List<OrderInvoice> getOptionInvoice(int userId,String option, int offset) {
        List<OrderInvoice> ois = new ArrayList<>();
        switch (option) {
            case "0":
                ois = invoiceOrderDao.getInvoicesByOption(userId, 0, offset);
                break;
            case "1":
                ois = invoiceOrderDao.getInvoicesByOption(userId, 1, offset);
                break;
            case "2":
                ois = invoiceOrderDao.getInvoicesByOption(userId, 2, offset);
                break;
            case "4":
                ois = invoiceOrderDao.getInvoicesByOption(userId, 4, offset);
                break;
            case "5":
                ois = invoiceOrderDao.getInvoicesByOption(userId, 5, offset);
                break;
            default:
                ois = invoiceOrderDao.searchInvoicesByFoodName(userId,option,offset);
                break;

        }
        return ois;
    }
    public OrderInvoice getOrderInvoice(String orderId) {
        int id = Integer.parseInt(orderId);
        return invoiceOrderDao.getInvoiceOrderById(id);
    }

    public int countOrderInvoices(int userId,String option) {
        return invoiceOrderDao.countInvoicesByFilter(userId,option);
    }

    public static void main(String[] args) {
        InvoiceOrderServices invoiceOrderServices = new InvoiceOrderServices();
        System.out.println(invoiceOrderServices.invoiceOrderDao.searchInvoicesByFoodName(3, "cá",0));
    }
}