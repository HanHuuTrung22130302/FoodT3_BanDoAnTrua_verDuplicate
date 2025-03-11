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
import java.util.Scanner;


public class DistanceCheck {
    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiYW5odHVhbjI5MDgiLCJhIjoiY204MzZqeWtrMDA2ZjJsb2tzZmczN3A2byJ9.9pDBnnzNCLrxFaQqJIagSA";
    private static final String DIRECTIONS_API_URL = "https://api.mapbox.com/directions/v5/mapbox/driving/";
    private static final String GEOCODING_API_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/";

    private static final OkHttpClient client = new OkHttpClient();

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

    public static double[] getCoordinatesFromAddress(String address) throws IOException {
        String url = GEOCODING_API_URL + address.replace(" ", "%20") + ".json?access_token=" + MAPBOX_ACCESS_TOKEN;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Lỗi API Geocoding: " + response);
            String responseData = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(responseData).getAsJsonObject();

            JsonArray features = jsonObject.getAsJsonArray("features");
            if (features.size() == 0) throw new IOException("Không tìm thấy địa chỉ: " + address);

            JsonArray coordinates = features.get(0).getAsJsonObject().getAsJsonArray("center");
            double lon = coordinates.get(0).getAsDouble();
            double lat = coordinates.get(1).getAsDouble();

            return new double[]{lat, lon};
        }
    }


    public static void main(String[] args) {
        //TEST: Trường Đại học Nông Lâm TP. Hồ Chí Minh, khu phố 6, Thủ Đức, Hồ Chí Minh, Việt Nam
        //TEST: Công viên Biên Hùng Đ. 30 Tháng 4, Trung Dũng, Biên Hòa, Đồng Nai, Việt Nam
        //TEST: 101/270/8, tổ 13 khu phố 4 Long Bình, Biên Hòa Đồng Nai, Việt Nam
        Scanner scanner = new Scanner(System.in);

        // Nhập địa chỉ điểm bắt đầu và điểm kết thúc
        System.out.println("Nhập địa chỉ điểm bắt đầu:");
        String startAddress = scanner.nextLine();

        System.out.println("Nhập địa chỉ điểm đến:");
        String endAddress = scanner.nextLine();

        try {
            // Lấy tọa độ từ địa chỉ nhập vào
            double[] startCoordinates = DistanceCheck.getCoordinatesFromAddress(startAddress);
            double[] endCoordinates = DistanceCheck.getCoordinatesFromAddress(endAddress);

            // In ra tọa độ để kiểm tra
            System.out.println("Tọa độ điểm đi: " + startCoordinates[0] + ", " + startCoordinates[1]);
            System.out.println("Tọa độ điểm đến: " + endCoordinates[0] + ", " + endCoordinates[1]);

            // Tính khoảng cách giữa hai tọa độ
            double distance = DistanceCheck.getDistanceBetweenPoints(
                    startCoordinates[0], startCoordinates[1],
                    endCoordinates[0], endCoordinates[1]
            );

            // In ra kết quả
            System.out.println("Khoảng cách thực tế: " + distance + " km");

        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý: " + e.getMessage());
        }

        scanner.close();
    }
}

