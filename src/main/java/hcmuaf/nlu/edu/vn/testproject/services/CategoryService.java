package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.CategoryDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Category;

import java.util.List;

public class CategoryService {

    static CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getCategories() {
        return categoryDAO.getCategories();
    }
}
