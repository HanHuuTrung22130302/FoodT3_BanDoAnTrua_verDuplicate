package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.FoodDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "ChatbotWebhook", value = "/webhook")
public class ChatbotWebhook extends HttpServlet {
    private FoodCartDAO foodCartDAO;
    private FoodDAO foodDAO;

    @Override
    public void init() throws ServletException {
        foodCartDAO = new FoodCartDAO();
        foodDAO = new FoodDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đọc JSON từ request
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String jsonString = buffer.toString();

        // Parse JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        JsonObject queryResult = jsonObject.getAsJsonObject("queryResult");
        JsonObject parameters = queryResult.getAsJsonObject("parameters");

        // Lấy tham số
        String taste = parameters.has("taste") && !parameters.get("taste").isJsonNull() ? parameters.get("taste").getAsString() : null;
        String restriction = parameters.has("restriction") && !parameters.get("restriction").isJsonNull() ? parameters.get("restriction").getAsString() : null;
        String category = parameters.has("category") && !parameters.get("category").isJsonNull() ? parameters.get("category").getAsString() : null;

        // Logic gợi ý món ăn
        List<Food> foods = foodDAO.getAll();
        String reply;

        // Xử lý theo từng trường hợp
        if (taste != null) {
            foods = foods.stream()
                    .filter(food -> food.getDescription().toLowerCase().contains(taste) || food.getFoodName().toLowerCase().contains(taste))
                    .limit(3)
                    .collect(Collectors.toList());
            reply = "Bạn thích món " + taste + "? Tôi gợi ý: " + formatFoodList(foods, request);
        } else if (restriction != null) {
            switch (restriction) {
                case "thịt bò":
                    foods = foods.stream()
                            .filter(food -> !food.getDescription().toLowerCase().contains("thịt bò") && !food.getFoodName().toLowerCase().contains("thịt bò"))
                            .limit(3)
                            .collect(Collectors.toList());
                    reply = "Bạn không ăn được thịt bò? Tôi gợi ý: " + formatFoodList(foods, request);
                    break;
                case "hải sản":
                    foods = foods.stream()
                            .filter(food -> !food.getDescription().toLowerCase().contains("hải sản") && !food.getFoodName().toLowerCase().contains("hải sản"))
                            .limit(3)
                            .collect(Collectors.toList());
                    reply = "Bạn dị ứng với hải sản? Tôi gợi ý: " + formatFoodList(foods, request);
                    break;
                case "tôm":
                    foods = foods.stream()
                            .filter(food -> !food.getDescription().toLowerCase().contains("tôm") && !food.getFoodName().toLowerCase().contains("tôm"))
                            .limit(3)
                            .collect(Collectors.toList());
                    reply = "Bạn không ăn được tôm? Tôi gợi ý: " + formatFoodList(foods, request);
                    break;
                default:
                    foods = foods.stream()
                            .filter(food -> !food.getDescription().toLowerCase().contains(restriction) && !food.getFoodName().toLowerCase().contains(restriction))
                            .limit(3)
                            .collect(Collectors.toList());
                    reply = "Bạn không ăn được " + restriction + "? Tôi gợi ý: " + formatFoodList(foods, request);
                    break;
            }
        } else if (category != null) {
            switch (category) {
                case "nước":
                    foods = foods.stream()
                            .filter(food -> food.getIdCategory() == 4) // 4 là món nước
                            .limit(3)
                            .collect(Collectors.toList());
                    reply = "Bạn khát nước à? Tôi gợi ý vài món nước: " + formatFoodList(foods, request);
                    break;
                case "cơm":
                    foods = foods.stream()
                            .filter(food -> food.getIdCategory() == 1) // 1 là món cơm
                            .limit(3)
                            .collect(Collectors.toList());
                    reply = "Bạn muốn món cơm? Tôi gợi ý: " + formatFoodList(foods, request);
                    break;
                case "bún":
                    foods = foods.stream()
                            .filter(food -> food.getIdCategory() == 2) // 2 là món bún
                            .limit(3)
                            .collect(Collectors.toList());
                    reply = "Bạn muốn món bún? Tôi gợi ý: " + formatFoodList(foods, request);
                    break;
                case "phở":
                    foods = foods.stream()
                            .filter(food -> food.getIdCategory() == 3) // 3 là món phở
                            .limit(3)
                            .collect(Collectors.toList());
                    reply = "Bạn muốn món phở? Tôi gợi ý: " + formatFoodList(foods, request);
                    break;
                default:
                    foods = foods.stream().limit(3).collect(Collectors.toList());
                    reply = "Bạn chưa nói rõ lắm, đây là vài gợi ý ngẫu nhiên: " + formatFoodList(foods, request);
                    break;
            }
        } else {
            foods = foods.stream().limit(3).collect(Collectors.toList());
            reply = "Bạn chưa nói rõ lắm, đây là vài gợi ý ngẫu nhiên: " + formatFoodList(foods, request);
        }

        // Tạo JSON phản hồi
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("fulfillmentText", reply);

        // Gửi phản hồi
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(responseJson));
    }

    private String formatFoodList(List<Food> foods, HttpServletRequest request) {
        if (foods.isEmpty()) return "Không tìm thấy món nào phù hợp.";
        String contextPath = request.getContextPath();
        return foods.stream()
                .map(food -> food.getFoodName() + " (" + food.getPrice() + "đ)")
                .collect(Collectors.joining(", "));
    }
}