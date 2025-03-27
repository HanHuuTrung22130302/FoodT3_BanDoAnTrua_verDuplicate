package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String jsonString = buffer.toString();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        JsonObject queryResult = jsonObject.getAsJsonObject("queryResult");
        JsonObject parameters = queryResult.getAsJsonObject("parameters");

        String taste = parameters.has("taste") && !parameters.get("taste").isJsonNull() ? parameters.get("taste").getAsString() : null;
        JsonArray restrictionsArray = parameters.has("restrictions") && !parameters.get("restrictions").isJsonNull() ? parameters.getAsJsonArray("restrictions") : new JsonArray();
        String category = parameters.has("category") && !parameters.get("category").isJsonNull() ? parameters.get("category").getAsString() : null;
        String product = parameters.has("product") && !parameters.get("product").isJsonNull() ? parameters.get("product").getAsString() : null;
        JsonArray ingredientsArray = parameters.has("ingredients") && !parameters.get("ingredients").isJsonNull() ? parameters.getAsJsonArray("ingredients") : new JsonArray();

        List<String> restrictions = restrictionsArray.size() > 0 ?
                restrictionsArray.asList().stream().map(element -> element.getAsString()).collect(Collectors.toList()) :
                List.of();
        List<String> ingredients = ingredientsArray.size() > 0 ?
                ingredientsArray.asList().stream().map(element -> element.getAsString()).collect(Collectors.toList()) :
                List.of();

        List<Food> foods = foodDAO.getAll();
        JsonObject responseJson = new JsonObject();

        String queryText = queryResult.get("queryText").getAsString().toLowerCase();
        if ((queryText.contains("thành phần") || queryText.contains("nguyên liệu")) && product == null) {
            String[] words = queryText.split("\\s+");
            StringBuilder potentialProduct = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                if (words[i].equals("món") && i + 1 < words.length) {
                    for (int j = i + 1; j < words.length; j++) {
                        if (!words[j].equals("có") && !words[j].equals("những") && !words[j].equals("thành")
                                && !words[j].equals("phần") && !words[j].equals("nguyên") && !words[j].equals("liệu")
                                && !words[j].equals("gì")) {
                            potentialProduct.append(words[j]).append(" ");
                        } else {
                            break;
                        }
                    }
                    product = potentialProduct.toString().trim();
                    break;
                }
            }
        }

        if (product != null) {
            String finalProduct = product.toLowerCase().trim();
            Food matchedFood = foods.stream()
                    .filter(food -> {
                        String foodName = food.getFoodName().toLowerCase().trim();
                        boolean matches = foodName.equals(finalProduct) || foodName.contains(finalProduct);
                        return matches;
                    })
                    .findFirst()
                    .orElse(null);

            if (matchedFood != null) {
                if (queryText.contains("thành phần") || queryText.contains("nguyên liệu")) {
                    responseJson.addProperty("fulfillmentText", String.format("Thành phần của %s: %s.",
                            matchedFood.getFoodName(), matchedFood.getIngredients()));
                } else {
                    responseJson.addProperty("fulfillmentText", String.format("Thông tin về %s: Giá %dđ, Thành phần: %s.",
                            matchedFood.getFoodName(), matchedFood.getPrice(), matchedFood.getIngredients()));
                }
            } else {
                responseJson.addProperty("fulfillmentText", "Tôi không tìm thấy thông tin về " + product + ". Bạn có thể thử hỏi món khác!");
            }
        } else if (taste != null) {
            foods = foods.stream()
                    .filter(food -> food.getDescription().toLowerCase().contains(taste) || food.getFoodName().toLowerCase().contains(taste))
                    .collect(Collectors.toList());

            for (String restriction : restrictions) {
                switch (restriction) {
                    case "thịt bò":
                    case "bò":
                        foods = foods.stream()
                                .filter(food -> !food.getDescription().toLowerCase().contains("bò") && !food.getFoodName().toLowerCase().contains("bò"))
                                .collect(Collectors.toList());
                        break;
                    case "hải sản":
                        foods = foods.stream()
                                .filter(food -> !food.getDescription().toLowerCase().contains("hải sản") && !food.getFoodName().toLowerCase().contains("hải sản"))
                                .collect(Collectors.toList());
                        break;
                    case "tôm":
                        foods = foods.stream()
                                .filter(food -> !food.getDescription().toLowerCase().contains("tôm") && !food.getFoodName().toLowerCase().contains("tôm"))
                                .collect(Collectors.toList());
                        break;
                    default:
                        foods = foods.stream()
                                .filter(food -> !food.getDescription().toLowerCase().contains(restriction) && !food.getFoodName().toLowerCase().contains(restriction))
                                .collect(Collectors.toList());
                        break;
                }
            }
            foods = foods.stream().limit(3).collect(Collectors.toList());
            responseJson.addProperty("fulfillmentText", "Bạn muốn ăn " + taste + "? Tôi gợi ý:");
            responseJson.add("foods", gson.toJsonTree(foods)); // Trả về danh sách món ăn dưới dạng JSON
        } else if (!restrictions.isEmpty()) {
            for (String restriction : restrictions) {
                switch (restriction) {
                    case "thịt bò":
                    case "bò":
                        foods = foods.stream()
                                .filter(food -> !food.getDescription().toLowerCase().contains("bò") && !food.getFoodName().toLowerCase().contains("bò"))
                                .collect(Collectors.toList());
                        break;
                    case "hải sản":
                        foods = foods.stream()
                                .filter(food -> !food.getDescription().toLowerCase().contains("hải sản") && !food.getFoodName().toLowerCase().contains("hải sản"))
                                .collect(Collectors.toList());
                        break;
                    case "tôm":
                        foods = foods.stream()
                                .filter(food -> !food.getDescription().toLowerCase().contains("tôm") && !food.getFoodName().toLowerCase().contains("tôm"))
                                .collect(Collectors.toList());
                        break;
                    default:
                        foods = foods.stream()
                                .filter(food -> !food.getDescription().toLowerCase().contains(restriction) && !food.getFoodName().toLowerCase().contains(restriction))
                                .collect(Collectors.toList());
                        break;
                }
            }
            foods = foods.stream().limit(3).collect(Collectors.toList());
            responseJson.addProperty("fulfillmentText", "Bạn không ăn được " + String.join(" và ", restrictions) + "? Tôi gợi ý:");
            responseJson.add("foods", gson.toJsonTree(foods));
        } else if (category != null) {
            switch (category) {
                case "nước":
                    foods = foods.stream()
                            .filter(food -> food.getCategoryId() == 4)
                            .limit(3)
                            .collect(Collectors.toList());
                    responseJson.addProperty("fulfillmentText", "Bạn khát nước à? Tôi gợi ý vài món nước:");
                    responseJson.add("foods", gson.toJsonTree(foods));
                    break;
                case "cơm":
                    foods = foods.stream()
                            .filter(food -> food.getCategoryId() == 1)
                            .limit(3)
                            .collect(Collectors.toList());
                    responseJson.addProperty("fulfillmentText", "Bạn muốn món cơm? Tôi gợi ý:");
                    responseJson.add("foods", gson.toJsonTree(foods));
                    break;
                case "bún":
                    foods = foods.stream()
                            .filter(food -> food.getCategoryId() == 2)
                            .limit(3)
                            .collect(Collectors.toList());
                    responseJson.addProperty("fulfillmentText", "Bạn muốn món bún? Tôi gợi ý:");
                    responseJson.add("foods", gson.toJsonTree(foods));
                    break;
                case "phở":
                    foods = foods.stream()
                            .filter(food -> food.getCategoryId() == 3)
                            .limit(3)
                            .collect(Collectors.toList());
                    responseJson.addProperty("fulfillmentText", "Bạn muốn món phở? Tôi gợi ý:");
                    responseJson.add("foods", gson.toJsonTree(foods));
                    break;
                default:
                    foods = foods.stream().limit(3).collect(Collectors.toList());
                    responseJson.addProperty("fulfillmentText", "Bạn chưa nói rõ lắm, đây là vài gợi ý ngẫu nhiên:");
                    responseJson.add("foods", gson.toJsonTree(foods));
                    break;
            }
        } else {
            foods = foods.stream().limit(3).collect(Collectors.toList());
            responseJson.addProperty("fulfillmentText", "Bạn chưa nói rõ lắm, đây là vài gợi ý ngẫu nhiên:");
            responseJson.add("foods", gson.toJsonTree(foods));
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(responseJson));
    }
}