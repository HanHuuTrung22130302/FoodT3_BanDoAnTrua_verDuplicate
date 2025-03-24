package hcmuaf.nlu.edu.vn.testproject.models;

public class OrderInvoiceDetail {
    private int detailId;
    private int invoiceId;
    private int foodId;
    private String foodName;
    private int quantity;
    private int totalAmount;
    private String image;

    public OrderInvoiceDetail() {
    }

    public OrderInvoiceDetail(int detailId, int invoiceId, int foodId, String foodName, int quantity, int totalAmount, String image) {
        this.detailId = detailId;
        this.invoiceId = invoiceId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.image = image;
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

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
