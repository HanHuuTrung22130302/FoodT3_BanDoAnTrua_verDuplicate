package hcmuaf.nlu.edu.vn.testproject.services;

import hcmuaf.nlu.edu.vn.testproject.daos.DistanceCheck;
import hcmuaf.nlu.edu.vn.testproject.daos.GHNMasterDataDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.ShippingDAO;

import java.io.IOException;

public class ShippingService {
    private static final String WAREHOUSE_ADDRESS = "Trường Đại học Nông Lâm TP. Hồ Chí Minh, khu phố 6, Thủ Đức, Hồ Chí Minh, Việt Nam";
    private static final int FROM_DISTRICT_ID = 1454; // TP. Thủ Đức (theo danh sách mã GHN)
    private static final int DEFAULT_HEIGHT = 10; // cm
    private static final int DEFAULT_LENGTH = 20; // cm
    private static final int DEFAULT_WIDTH = 15; // cm
    private static final int DEFAULT_WEIGHT = 500; // gram

    /**
     * Tính phí ship dựa trên thông tin địa chỉ người dùng nhập (trong TP. Hồ Chí Minh).
     * @param street Số nhà, tên đường (VD: "Trường Đại học Quốc tế - Đại học Quốc gia")
     * @param ward Phường/Xã (VD: "Phường Linh Trung")
     * @param district Quận/Huyện (VD: "Thành phố Thủ Đức")
     * @return Phí ship (VND) hoặc -1 nếu có lỗi
     */
    public int calculateShippingFee(String street, String ward, String district) {
        try {
            // Tạo địa chỉ đơn giản hơn để Mapbox dễ nhận diện
            String destinationAddress = String.format("%s, %s, TP. Hồ Chí Minh, Việt Nam", ward, district);

            // Kiểm tra địa chỉ đích bằng cách lấy tọa độ (có thể bỏ nếu không cần)
            double[] destinationCoords = DistanceCheck.getCoordinatesFromAddress(destinationAddress);
            if (destinationCoords == null) {
                System.err.println("Cảnh báo: Không lấy được tọa độ, nhưng vẫn tiếp tục tính phí ship.");
                // Tiếp tục xử lý thay vì throw exception
            }

            // Lấy to_district_id từ tên quận/huyện
            int toDistrictId = GHNMasterDataDAO.getDistrictId(district);
            if (toDistrictId == -1) {
                throw new IOException("Không tìm thấy mã quận/huyện cho: " + district);
            }

            // Lấy to_ward_code từ tên phường/xã và to_district_id
            String toWardCode = GHNMasterDataDAO.getWardCode(ward, toDistrictId);
            if (toWardCode == null) {
                throw new IOException("Không tìm thấy mã phường/xã cho: " + ward + " trong quận: " + district);
            }

            // Tính phí ship bằng ShippingDAO
            int shippingFee = ShippingDAO.calculateShippingFee(
                    FROM_DISTRICT_ID,
                    toDistrictId,
                    toWardCode,
                    DEFAULT_HEIGHT,
                    DEFAULT_LENGTH,
                    DEFAULT_WIDTH,
                    DEFAULT_WEIGHT
            );

            if (shippingFee < 0) {
                throw new IOException("Lỗi khi tính phí ship từ API GHN");
            }

            return shippingFee;

        } catch (IOException e) {
            System.err.println("Lỗi khi tính phí ship: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Tính phí ship với thông tin gói hàng tùy chỉnh.
     * @param street Số nhà, tên đường
     * @param ward Phường/Xã
     * @param district Quận/Huyện
     * @param height Chiều cao (cm)
     * @param length Chiều dài (cm)
     * @param width Chiều rộng (cm)
     * @param weight Trọng lượng (gram)
     * @return Phí ship (VND) hoặc -1 nếu có lỗi
     */
    public int calculateShippingFeeWithPackageDetails(
            String street, String ward, String district,
            int height, int length, int width, int weight) {
        try {
            // Tạo địa chỉ đơn giản hơn để Mapbox dễ nhận diện
            String destinationAddress = String.format("%s, %s, TP. Hồ Chí Minh, Việt Nam", ward, district);

            // Kiểm tra địa chỉ đích bằng cách lấy tọa độ (có thể bỏ nếu không cần)
            double[] destinationCoords = DistanceCheck.getCoordinatesFromAddress(destinationAddress);
            if (destinationCoords == null) {
                System.err.println("Cảnh báo: Không lấy được tọa độ, nhưng vẫn tiếp tục tính phí ship.");
                // Tiếp tục xử lý thay vì throw exception
            }

            // Lấy to_district_id từ tên quận/huyện
            int toDistrictId = GHNMasterDataDAO.getDistrictId(district);
            if (toDistrictId == -1) {
                throw new IOException("Không tìm thấy mã quận/huyện cho: " + district);
            }

            // Lấy to_ward_code từ tên phường/xã và to_district_id
            String toWardCode = GHNMasterDataDAO.getWardCode(ward, toDistrictId);
            if (toWardCode == null) {
                throw new IOException("Không tìm thấy mã phường/xã cho: " + ward + " trong quận: " + district);
            }

            // Tính phí ship bằng ShippingDAO
            int shippingFee = ShippingDAO.calculateShippingFee(
                    FROM_DISTRICT_ID,
                    toDistrictId,
                    toWardCode,
                    height,
                    length,
                    width,
                    weight
            );

            if (shippingFee < 0) {
                throw new IOException("Lỗi khi tính phí ship từ API GHN");
            }

            return shippingFee;

        } catch (IOException e) {
            System.err.println("Lỗi khi tính phí ship: " + e.getMessage());
            return -1;
        }
    }

    public static void main(String[] args) {
        ShippingService service = new ShippingService();

        // Ví dụ: Tính phí ship đến Trường Đại học Quốc tế, Phường Linh Trung, TP. Thủ Đức
//        String street = "Trường Đại học Quốc tế Đại học Quốc gia thành phố Hồ Chí Minh";
//        String ward = "Phường Linh Trung";
//        String district = "Thành phố Thủ Đức";

        String street = "123 Lê Lợi";
        String ward = "Phường Bến Nghé";
        String district = "Quận 1";

        int fee = service.calculateShippingFee(street, ward, district);
        System.out.println("Phí ship: " + (fee >= 0 ? fee + " VND" : "Lỗi khi tính phí"));
    }
}