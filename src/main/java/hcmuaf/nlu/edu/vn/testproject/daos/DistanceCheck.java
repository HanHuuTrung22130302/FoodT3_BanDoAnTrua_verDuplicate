package hcmuaf.nlu.edu.vn.testproject.daos;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Scanner;

public class DistanceCheck {
    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiYW5odHVhbjI5MDgiLCJhIjoiY21hZjhpOXNyMDAyZTJscXdjZHFzazR6NiJ9.DFqnLc2OOdbEMxof03unrg";
    private static final String DIRECTIONS_API_URL = "https://api.mapbox.com/directions/v5/mapbox/driving/";
    private static final String GEOCODING_API_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/";
    private static final OkHttpClient client = new OkHttpClient();

    public static double getDistanceBetweenPoints(double lat1, double lon1, double lat2, double lon2) throws IOException {
        String url = DIRECTIONS_API_URL + lon1 + "," + lat1 + ";" + lon2 + "," + lat2
                + "?geometries=geojson&overview=full&access_token=" + MAPBOX_ACCESS_TOKEN;

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
            if (!response.isSuccessful()) {
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                throw new IOException("Lỗi API Geocoding: " + response + ", Response body: " + responseBody);
            }
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
        Scanner scanner = new Scanner(System.in);

        System.out.println("Nhập địa chỉ điểm bắt đầu:");
        String startAddress = scanner.nextLine();

        System.out.println("Nhập địa chỉ điểm đến:");
        String endAddress = scanner.nextLine();

        try {
            double[] startCoordinates = DistanceCheck.getCoordinatesFromAddress(startAddress);
            double[] endCoordinates = DistanceCheck.getCoordinatesFromAddress(endAddress);

            System.out.println("Tọa độ điểm đi: " + startCoordinates[0] + ", " + startCoordinates[1]);
            System.out.println("Tọa độ điểm đến: " + endCoordinates[0] + ", " + endCoordinates[1]);

            double distance = DistanceCheck.getDistanceBetweenPoints(
                    startCoordinates[0], startCoordinates[1],
                    endCoordinates[0], endCoordinates[1]
            );
            System.out.println("Khoảng cách thực tế: " + distance + " km");

        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý: " + e.getMessage());
        }
        scanner.close();
        //Nhập địa chỉ điểm bắt đầu:
        //"Trường Đại học Nông Lâm TP. Hồ Chí Minh, khu phố 6, Thủ Đức, Hồ Chí Minh";
        //Nhập địa chỉ điểm đến:
        //Trường Đại học Quốc tế Đại học Quốc gia Hồ Chí Minh, Phường Linh Trung, Thành phố Thủ Đức
    }
}