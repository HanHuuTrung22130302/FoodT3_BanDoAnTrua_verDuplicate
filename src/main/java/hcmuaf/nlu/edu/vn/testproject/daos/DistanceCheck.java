package hcmuaf.nlu.edu.vn.testproject.daos;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;


public class DistanceCheck {
    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiYW5odHVhbjI5MDgiLCJhIjoiY204MzZqeWtrMDA2ZjJsb2tzZmczN3A2byJ9.9pDBnnzNCLrxFaQqJIagSA";
    private static final String DIRECTIONS_API_URL = "https://api.mapbox.com/directions/v5/mapbox/driving/";


    public static double getDistanceBetweenPoints(double lat1, double lon1, double lat2, double lon2) throws IOException {
        String url = DIRECTIONS_API_URL + lon1 + "," + lat1 + ";" + lon2 + "," + lat2
                + "?geometries=geojson&overview=full&access_token=" + MAPBOX_ACCESS_TOKEN;


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Lỗi API Mapbox Directions: " + response);
            String responseData = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(responseData).getAsJsonObject();


            JsonArray routes = jsonObject.getAsJsonArray("routes");
            if (routes.size() == 0) throw new IOException("Không tìm thấy tuyến đường phù hợp.");


            JsonObject route = routes.get(0).getAsJsonObject();
            double distanceMeters = route.get("distance").getAsDouble();
            double distanceKm = distanceMeters / 1000.0;


            return distanceKm;
        }
    }




    public static void main(String[] args) {
        try {
            double lat1 = 10.8700, lon1 = 106.7921; // đại học nông lâm
            double lat2 = 10.95173331905438, lon2 = 106.82234818989188; // công viên biên hùng


            double distance = getDistanceBetweenPoints(lat1, lon1, lat2, lon2);
            System.out.println("Khoảng cách thực tế: " + distance + " km");
        } catch (IOException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
    }
}

