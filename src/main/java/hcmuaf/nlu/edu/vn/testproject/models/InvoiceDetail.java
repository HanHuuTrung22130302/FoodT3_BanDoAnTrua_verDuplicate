package hcmuaf.nlu.edu.vn.testproject.models;

public class InvoiceDetail {
    private int detailId;
    private int invoiceId;
    private int foodId;
    private int quantity;
    private int totalAmount;
    private Food food;

    public InvoiceDetail() {
    }

    public InvoiceDetail(int detailId, int invoiceId, int foodId, int quantity, int totalAmount) {
        this.detailId = detailId;
        this.invoiceId = invoiceId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
