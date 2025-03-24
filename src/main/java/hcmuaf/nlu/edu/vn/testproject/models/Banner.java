package hcmuaf.nlu.edu.vn.testproject.models;

import java.util.Date;

public class Banner {
    private int bannerId;
    private String url;
    private Date date;

    public Banner(int bannerId, String url, Date date) {

        this.bannerId = bannerId;
        this.url = url;
        this.date = date;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
