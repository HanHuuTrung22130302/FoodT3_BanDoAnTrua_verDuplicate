package hcmuaf.nlu.edu.vn.testproject.models;

public class Ingredients {
    private int ingredientId;
    private String ingredientName;
    private double amount;
    private double price;
    private int supplierId;
    private String supplierName;
    private java.sql.Date importDate;
    private java.sql.Date expirationDate;

    // Constructor
    public Ingredients() {}

    public Ingredients(int ingredientId, String ingredientName, double amount, double price,
                      int supplierId, String supplierName, java.sql.Date importDate, java.sql.Date expirationDate) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.amount = amount;
        this.price = price;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.importDate = importDate;
        this.expirationDate = expirationDate;
    }

    // Getters and Setters
    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public java.sql.Date getImportDate() {
        return importDate;
    }

    public void setImportDate(java.sql.Date importDate) {
        this.importDate = importDate;
    }

    public java.sql.Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(java.sql.Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}

