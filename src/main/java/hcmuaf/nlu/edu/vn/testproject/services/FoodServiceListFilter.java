package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.FoodDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.ReviewDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodServiceListFilter {
    private FoodDAO foodDAO;
    private ReviewDAO reviewDAO;

    public FoodServiceListFilter() {
        this.reviewDAO = new ReviewDAO();
        this.foodDAO = new FoodDAO();
    }

    public List<Food> getTop4Views() {
        return foodDAO.getTop4View();
    }

    public List<Food> getTop4Sold() {
        return foodDAO.getTop4Sold();
    }

    public List<Food> getTop4Propose() {
        return foodDAO.getTop4Propose();
    }

    public List<Food> getTop4Rate() {
        List<Food> lsIdFood = new ArrayList<>();
        for (int i : reviewDAO.getTop4FoodRate()) {
            lsIdFood.add(foodDAO.getById(i));
        }
        return lsIdFood;
    }

    public List<Food> getTopRate() {
        List<Food> lsIdFood = new ArrayList<>();
        for (int i : reviewDAO.getTopFoodRate()) {
            lsIdFood.add(foodDAO.getById(i));
        }
        return lsIdFood;
    }

    public List<Food> getOption(String option) {
        List<Food> lsFood = new ArrayList<>();
        switch (option) {
            case "tatca":
                lsFood = foodDAO.getAll();
                break;
            case "danhgiacao":
                lsFood = getTopRate();
                break;
            case "dexuat":
                lsFood = foodDAO.getTopPropose();
                break;
            case "quantam":
                lsFood = foodDAO.getTopView();
                break;
            case "banchay":
                lsFood = foodDAO.getTopSold();
                break;
            case "1":
                lsFood = foodDAO.getFoodsByCategory(Integer.parseInt(option));
                break;
            case "2":
                lsFood = foodDAO.getFoodsByCategory(Integer.parseInt(option));
                break;
            case "3":
                lsFood = foodDAO.getFoodsByCategory(Integer.parseInt(option));
                break;
            case "4":
                lsFood = foodDAO.getFoodsByCategory(Integer.parseInt(option));
                break;
            default:
                lsFood = foodDAO.searchByName(option);
                break;
        }
        return lsFood;
    }

    public boolean deleteFood(int idFood) {
        try {
            foodDAO.deleteFood(idFood);
            foodDAO.getAllFood();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addFood(Food food) {
        try {
            boolean result = foodDAO.addFood(food);
            if (result) {
                foodDAO.getAllFood();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFood(Food food) {
        boolean isUpdated = foodDAO.updateFood(food);
        foodDAO.getAllFood();
        return isUpdated;
    }
}
