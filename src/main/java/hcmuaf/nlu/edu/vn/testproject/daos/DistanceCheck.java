package hcmuaf.nlu.edu.vn.testproject.daos;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DistanceCheck {
    private static final String GOOGLE_API_KEY = "AIzaSyA2AwodiYvCmSeeW4frMAIUEVu_fWp2n-U"; // Thay bằng API key của bạn
    private static final double RESTAURANT_LAT = 21.0285;
    private static final double RESTAURANT_LNG = 105.8542;
    private static final double MAX_DISTANCE = 10.0; // Giới hạn khoảng cách (10km)

    public static double calculateDistance(String destination) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Tạo URL API
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json" +
                "?origins=" + RESTAURANT_LAT + "," + RESTAURANT_LNG +
                "&destinations=" + destination.replace(" ", "+") +
                "&key=" + GOOGLE_API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = response.body();

        // Phân tích JSON
        JSONObject json = new JSONObject(jsonResponse);
        String status = json.getString("status");
        if (!"OK".equals(status)) {
            throw new Exception("Google API trả về lỗi: " + status);
        }

        JSONObject rows = json.getJSONArray("rows").getJSONObject(0);
        JSONObject elements = rows.getJSONArray("elements").getJSONObject(0);
        String elementStatus = elements.getString("status");

        if (!"OK".equals(elementStatus)) {
            throw new Exception("Không thể tính khoảng cách: " + elementStatus);
        }

        int distanceInMeters = elements.getJSONObject("distance").getInt("value");
        return distanceInMeters / 1000.0; // Chuyển sang kilômét
    }

    public static boolean isWithinRange(String destination) throws Exception {
        double distance = calculateDistance(destination);
        return distance <= MAX_DISTANCE;
    }
}
