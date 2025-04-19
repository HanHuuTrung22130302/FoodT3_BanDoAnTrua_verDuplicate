package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.DiscountDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Discount;

import java.util.List;

public class DiscountService {
    DiscountDAO dao = new DiscountDAO();

    public List<Discount> getAllDiscounts() {
        List<Discount> discounts = dao.getAllDiscounts();
        return discounts;
    }

    public int addDiscount(Discount discount) {
        return dao.addDiscount(discount);
    }

    public boolean deleteDiscount(int idCode) {
        return dao.deleteDiscount(idCode);
    }
}
