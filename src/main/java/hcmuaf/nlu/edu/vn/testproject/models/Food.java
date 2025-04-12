package hcmuaf.nlu.edu.vn.testproject.models;

import java.sql.Timestamp;
import java.util.List;

public class Food {
    private int foodId;
    private String foodName;
    private int price;
    private int discountPrice;
    private int quantity;
    private String image;
    private String description;
    private String ingredients; // Thêm trường mới
    private int categoryId;
    private int sold;
    private int views;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private double rating;
    private int isDeleted;

    public Food() {
    }

    public Food(int foodId, String foodName, int price, int discountPrice, int quantity, String image, String description, String ingredients, int categoryId, int sold, int views, Timestamp createdAt, Timestamp updatedAt) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.image = image;
        this.description = description;
        this.ingredients = ingredients;
        this.categoryId = categoryId;
        this.sold = sold;
        this.views = views;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Food(int foodId, String foodName, int price, int discountPrice, int quantity, String image, String description, int categoryId, int sold, int views, Timestamp createdAt, Timestamp updatedAt) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.image = image;
        this.description = description;
        this.categoryId = categoryId;
        this.sold = sold;
        this.views = views;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Food(int foodId, String foodName, int price, int discountPrice, int quantity, String image, String description, String ingredients, int categoryId, int sold, int views,double rating, Timestamp createdAt, Timestamp updatedAt) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.image = image;
        this.description = description;
        this.ingredients = ingredients;
        this.categoryId = categoryId;
        this.sold = sold;
        this.views = views;
        this.rating = rating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
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

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
