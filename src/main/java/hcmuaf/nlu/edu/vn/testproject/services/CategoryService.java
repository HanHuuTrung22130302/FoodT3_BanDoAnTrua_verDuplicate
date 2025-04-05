package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.CategoryDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Category;

import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getCategories() {
        return categoryDAO.getCategories();
    }

    public boolean addCategory(Category category) {
        return categoryDAO.addCategory(category);
    }

    public boolean deleteCategory(int categoryId) {
        return categoryDAO.deleteCategory(categoryId);
    }

    public Category getCategoryById(int categoryId) {
        List<Category> categories = categoryDAO.getCategories();
        for (Category category : categories) {
            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }
        return null;
    }
}
