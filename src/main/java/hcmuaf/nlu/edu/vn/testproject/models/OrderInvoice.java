package hcmuaf.nlu.edu.vn.testproject.models;

import java.util.ArrayList;
import java.util.List;

public class OrderInvoice {
    private int invoiceId;
    private int accountId;
    private String recipientName;
    private String phoneNumber;
    private String deliveryAddress;
    private String note;
    private String orderDate;
    private int totalAmount;
    private int idCode;
    private int paymentMethod;
    private int isPaid;
    private int orderStatus;
    private String completionTime;
    private String reason;
    private List<OrderInvoiceDetail> orderInvoiceDetail;
    private int isReview;

    public OrderInvoice() {
    }

    public OrderInvoice(int invoiceId, int accountId, String recipientName, String phoneNumber, String deliveryAddress, String note, String orderDate, int totalAmount, int idCode, int paymentMethod, int isPaid, int orderStatus, List<OrderInvoiceDetail> orderInvoiceDetail) {
        this.invoiceId = invoiceId;
        this.accountId = accountId;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.note = note;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.idCode = idCode;
        this.paymentMethod = paymentMethod;
        this.isPaid = isPaid;
        this.orderStatus = orderStatus;
        this.orderInvoiceDetail = orderInvoiceDetail;
    }

    public OrderInvoice(int invoiceId, int accountId, String recipientName, String phoneNumber, String deliveryAddress, String note, String orderDate, int totalAmount, int idCode, int paymentMethod, int isPaid, int orderStatus) {
        this.invoiceId = invoiceId;
        this.accountId = accountId;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.note = note;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.idCode = idCode;
        this.paymentMethod = paymentMethod;
        this.isPaid = isPaid;
        this.orderStatus = orderStatus;
    }

    public int getIsReview() {
        return isReview;
    }

    public void setIsReview(int isReview) {
        this.isReview = isReview;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getIdCode() {
        return idCode;
    }

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderInvoiceDetail> getOrderInvoiceDetail() {
        return orderInvoiceDetail;
    }

    public void setOrderInvoiceDetail(List<OrderInvoiceDetail> orderInvoiceDetail) {
        this.orderInvoiceDetail = orderInvoiceDetail;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "OrderInvoice{" +
                "invoiceId=" + invoiceId +
                ", completionTime='" + completionTime + '\'' +
                '}';
    }
}
