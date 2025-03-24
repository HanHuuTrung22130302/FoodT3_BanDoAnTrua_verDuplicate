package hcmuaf.nlu.edu.vn.testproject.models;

public class Invoice {
    private int invoiceId;
    private int accountId;
    private String recipientName;
    private String phoneNumber;
    private String deliveryAddress;
    private String note;
    private String orderDate;
    private int totalAmount;
    private int discountCodeId;
    private int paymentMethod;
    private int isPaid;

    public Invoice() {
    }

    public Invoice(int invoiceId, int accountId, String recipientName, String phoneNumber, String deliveryAddress, String note, String orderDate, int totalAmount, int discountCodeId, int paymentMethod, int isPaid) {
        this.invoiceId = invoiceId;
        this.accountId = accountId;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.note = note;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.discountCodeId = discountCodeId;
        this.paymentMethod = paymentMethod;
        this.isPaid = isPaid;
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

    public int getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(int discountCodeId) {
        this.discountCodeId = discountCodeId;
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
}
