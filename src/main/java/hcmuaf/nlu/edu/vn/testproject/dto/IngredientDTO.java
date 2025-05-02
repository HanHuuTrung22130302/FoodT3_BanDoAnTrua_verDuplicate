package hcmuaf.nlu.edu.vn.testproject.dto;

public class IngredientDTO {
    private int ingredientId;
    private String ingredientName;

    // Constructor
    public IngredientDTO(int ingredientId, String ingredientName) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
    }

    // Getters và Setters
    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    // toString để kiểm tra dữ liệu (nếu cần)
    @Override
    public String toString() {
        return "IngredientDTO{" +
                "ingredientId=" + ingredientId +
                ", ingredientName='" + ingredientName + '\'' +
                '}';
    }
}