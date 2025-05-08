package hcmuaf.nlu.edu.vn.testproject.daos;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShippingDAO {

    private static final String GHN_TOKEN = "6bbdc943-2bde-11f0-bd93-06ccc2518db6";
    private static final int SHOP_ID = 5767145;
    private static final String API_URL = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

    public static int calculateShippingFee(int fromDistrictId, int toDistrictId, String toWardCode, int height, int length, int width, int weight) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Thiết lập header
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Token", GHN_TOKEN);
            conn.setRequestProperty("ShopId", String.valueOf(SHOP_ID));
            conn.setDoOutput(true);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("from_district_id", fromDistrictId);
            jsonBody.put("service_id", 53320); // dịch vụ tiêu chuẩn GHN
            jsonBody.put("to_district_id", toDistrictId);
            jsonBody.put("to_ward_code", toWardCode); // chuỗi
            jsonBody.put("height", height);
            jsonBody.put("length", length);
            jsonBody.put("weight", weight);
            jsonBody.put("width", width);
            jsonBody.put("insurance_value", 0);

            // Gửi request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Đọc phản hồi
            int status = conn.getResponseCode();
            InputStream inputStream = (status < 400) ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseStr.append(line);
            }

            reader.close();
            conn.disconnect();

            // Parse kết quả
            JSONObject response = new JSONObject(responseStr.toString());
            if (response.has("code") && response.getInt("code") == 200) {
                JSONObject data = response.optJSONObject("data");
                if (data != null) {
                    return data.getInt("total");
                } else {
                    System.err.println("Không có dữ liệu phí ship (data=null): " + response.toString());
                    return -1;
                }
            }else {
                System.err.println("GHN API trả về lỗi: " + response.toString());
                System.out.println("GHN response: " + response.toString(2));
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static void main(String[] args) {
        // Từ Quận Gò Vấp (1442) → Quận 1 (1450), phường Bến Nghé (ward_code "20108")
        int fromDistrictId = 1442;
        int toDistrictId = 1450;
        String toWardCode = "20108";

        int height = 10;
        int length = 20;
        int width = 15;
        int weight = 500;

        int fee = calculateShippingFee(fromDistrictId, toDistrictId, toWardCode, height, length, width, weight);

        System.out.println("Shipping Fee: " + (fee >= 0 ? fee + " VND" : "Lỗi khi tính phí"));
    }

}

