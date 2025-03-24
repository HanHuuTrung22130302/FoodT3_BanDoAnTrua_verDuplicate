package hcmuaf.nlu.edu.vn.testproject.models;

import java.util.Date;

public class Discount {
    private int discountCodeId;
    private String codeName;
    private Double discountRate;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;

    public Discount() {
    }

    public Discount(int discountCodeId, String codeName, Double discountRate, String title, String description, Date startDate, Date endDate) {
        this.discountCodeId = discountCodeId;
        this.codeName = codeName;
        this.discountRate = discountRate;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(int discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
