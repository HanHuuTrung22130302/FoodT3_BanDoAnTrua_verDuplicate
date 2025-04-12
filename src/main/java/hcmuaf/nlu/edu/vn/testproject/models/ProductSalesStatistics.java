package hcmuaf.nlu.edu.vn.testproject.models;

public class ProductSalesStatistics {
    private Food food;
    private int quantity;
    private int totalAmount;

    public ProductSalesStatistics() {
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
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
}
