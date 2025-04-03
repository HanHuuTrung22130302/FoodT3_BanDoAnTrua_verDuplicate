package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.ReviewDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.ReviewFood;

import java.util.ArrayList;
import java.util.List;

public class ReviewService {
    private ReviewDAO reviewDAO;

    public ReviewService() {
        reviewDAO = new ReviewDAO();
    }

    public Double getRating(int idFood) {
        List<ReviewFood> rvf = reviewDAO.getAll();
        int count = 0;
        double rate = 0;
        for (ReviewFood rf : rvf) {
            if (rf.getFoodId() == idFood) {
                count++;
                rate += rf.getRating();
            }
        }

        return Math.round(rate / count * 10.0) / 10.0;
    }

    public List<ReviewFood> getReviewFood(int idFood) {
        List<ReviewFood> all = reviewDAO.getAll();
        List<ReviewFood> rvf = new ArrayList<>();
        for (ReviewFood rf : all) {
            if (rf.getFoodId() == idFood)
                rvf.add(rf);
        }
        return rvf;
    }

}
