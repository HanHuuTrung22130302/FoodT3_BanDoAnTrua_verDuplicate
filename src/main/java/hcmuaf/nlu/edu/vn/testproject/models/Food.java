package hcmuaf.nlu.edu.vn.testproject.models;

import java.sql.Timestamp;

public class Food {
    private int idFood;
    private String foodName;
    private int price;
    private int discountPrice;
    private int quantity;
    private String img;
    private String description;
    private String ingredients; // Thêm trường mới
    private int idCategory;
    private int sold;
    private int views;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Food() {

    }

    public Food(int idFood, String foodName, int price, int discountPrice, int quantity, String img, String description, String ingredients, int idCategory, int sold, int views, Timestamp createdAt, Timestamp updatedAt) {
        this.idFood = idFood;
        this.foodName = foodName;
        this.price = price;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.img = img;
        this.description = description;
        this.ingredients = ingredients;
        this.idCategory = idCategory;
        this.sold = sold;
        this.views = views;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Food(int idFood, String foodName, int price, int discountPrice, int quantity, String img, String description, int idCategory, int sold, int views, Timestamp createdAt, Timestamp updatedAt) {
        this.idFood = idFood;
        this.foodName = foodName;
        this.price = price;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.img = img;
        this.description = description;
        this.idCategory = idCategory;
        this.sold = sold;
        this.views = views;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getIdFood() {
        return idFood;
    }

    public void setIdFood(Integer idFood) {
        this.idFood = idFood;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
