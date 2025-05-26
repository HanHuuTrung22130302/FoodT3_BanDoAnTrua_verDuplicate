package hcmuaf.nlu.edu.vn.testproject.daos;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GHNMasterDataDAO {
    private static final String GHN_TOKEN = "6bbdc943-2bde-11f0-bd93-06ccc2518db6";
    private static final String DISTRICT_API_URL = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
    private static final String WARD_API_URL = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";

    /**
     * Lấy to_district_id từ tên quận/huyện.
     * @param districtName Tên quận/huyện (VD: "Thành phố Thủ Đức")
     * @return to_district_id hoặc -1 nếu không tìm thấy
     */
    public static int getDistrictId(String districtName) {
        try {
            URL url = new URL(DISTRICT_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Token", GHN_TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");

            int status = conn.getResponseCode();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    status < 400 ? conn.getInputStream() : conn.getErrorStream()
            );
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseStr.append(line);
            }
            reader.close();
            conn.disconnect();

            JSONObject response = new JSONObject(responseStr.toString());
            if (response.has("code") && response.getInt("code") == 200) {
                JSONArray districts = response.getJSONArray("data");
                for (int i = 0; i < districts.length(); i++) {
                    JSONObject district = districts.getJSONObject(i);
                    String name = district.getString("DistrictName");
                    if (normalizeString(name).equalsIgnoreCase(normalizeString(districtName))) {
                        return district.getInt("DistrictID");
                    }
                }
                System.err.println("Không tìm thấy quận/huyện: " + districtName + ", Response: " + response.toString());
                return -1;
            } else {
                System.err.println("GHN API trả về lỗi: " + response.toString());
                return -1;
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy district_id: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Lấy to_ward_code từ tên phường/xã và to_district_id.
     * @param wardName Tên phường/xã (VD: "Phường Linh Trung")
     * @param districtId Mã quận/huyện (VD: 1454)
     * @return to_ward_code hoặc null nếu không tìm thấy
     */
    public static String getWardCode(String wardName, int districtId) {
        try {
            URL url = new URL(WARD_API_URL + "?district_id=" + districtId); // Sửa: Thêm dấu ?
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Token", GHN_TOKEN);
            conn.setRequestProperty("Content-Type", "application/json");

            int status = conn.getResponseCode();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    status < 400 ? conn.getInputStream() : conn.getErrorStream()
            );
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseStr.append(line);
            }
            reader.close();
            conn.disconnect();

            JSONObject response = new JSONObject(responseStr.toString());
            if (response.has("code") && response.getInt("code") == 200) {
                JSONArray wards = response.getJSONArray("data");
                for (int i = 0; i < wards.length(); i++) {
                    JSONObject ward = wards.getJSONObject(i);
                    String name = ward.getString("WardName");
                    if (normalizeString(name).equalsIgnoreCase(normalizeString(wardName))) {
                        return ward.getString("WardCode");
                    }
                }
                System.err.println("Không tìm thấy phường/xã: " + wardName + " trong district_id: " + districtId + ", Response: " + response.toString());
                return null;
            } else {
                System.err.println("GHN API trả về lỗi: " + response.toString());
                return null;
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy ward_code: " + e.getMessage());
            return null;
        }
    }

    /**
     * Chuẩn hóa chuỗi: bỏ dấu, bỏ khoảng trắng thừa, chuyển thành chữ thường.
     * @param input Chuỗi đầu vào
     * @return Chuỗi đã chuẩn hóa
     */
    private static String normalizeString(String input) {
        if (input == null) return "";
        // Bỏ dấu, chuyển thành chữ thường, thay khoảng trắng bằng dấu cách đơn
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
        return normalized;
    }
}