package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.ReviewDaoByUser;

public class ReviewServiceByUser {
    ReviewDaoByUser reviewDao = new ReviewDaoByUser();
    public ReviewServiceByUser() {

    }
    public void insertReview(int userId, int foodId, int rating, String comment,int invoiceId) {
        reviewDao.insertReview(userId, foodId, rating, comment,invoiceId);
    }
    public boolean checkReview(int invoiceId) {
        return reviewDao.hasReviewedInvoice(invoiceId);
    }

    public static void main(String[] args) {
        ReviewServiceByUser service = new ReviewServiceByUser();
        System.out.println(service.checkReview(2));
    }
}
