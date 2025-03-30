package hcmuaf.nlu.edu.vn.testproject.models;

import java.util.Date;

public class ReviewFood {
    private  String name;
    private int reviewId;
    private int foodId;
    private int rating;
    private int accountId;
    private Date date;
    private String comment;

    public ReviewFood(String name, int reviewId, int foodId, int rating, int accountId, Date date) {
        this.name = name;
        this.reviewId = reviewId;
        this.foodId = foodId;
        this.rating = rating;
        this.accountId = accountId;
        this.date = date;
    }

    public ReviewFood(String name, int reviewId, int foodId, int rating, int accountId, Date date, String comment) {
        this.name = name;
        this.reviewId = reviewId;
        this.foodId = foodId;
        this.rating = rating;
        this.accountId = accountId;
        this.date = date;
        this.comment = comment;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
