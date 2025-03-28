package hcmuaf.nlu.edu.vn.testproject.models;

import java.util.List;

public class Order {
    private int orderId;
    private Account customer;
    private List<Item> items;
    private int status;

    public Order() {
    }

    public Order(int orderId, Account customer, List<Item> items, int status) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = items;
        this.status = status;
    }

    // Thêm constructor mới nhận List<Item>
    public Order(List<Item> items) {
        this.items = items;
    }

    public int getIdFood() {
        return orderId;
    }

    public void setIdFood(int idFood) {
        this.orderId = idFood;
    }

    public Account getCustomer() {
        return customer;
    }

    public void setCustomer(Account customer) {
        this.customer = customer;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}