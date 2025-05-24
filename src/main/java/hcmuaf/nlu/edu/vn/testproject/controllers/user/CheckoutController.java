package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.DiscountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.GHNMasterDataDAO;
import hcmuaf.nlu.edu.vn.testproject.models.*;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;
import hcmuaf.nlu.edu.vn.testproject.services.ShippingService;
import hcmuaf.nlu.edu.vn.testproject.daos.Config;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(name = "CheckoutController", value = {"/checkout", "/calculate-shipping"})
public class CheckoutController extends HttpServlet {

    private FoodService foodService;
    private ShippingService shippingService;
    private GHNMasterDataDAO ghnMasterDataDAO;
    private LogService logService = new LogService();
    private static final String STORE_ADDRESS = "Trường Đại học Nông Lâm TP. Hồ Chí Minh, khu phố 6, Thủ Đức, Hồ Chí Minh, Việt Nam";
    private static final Set<String> VALID_DISTRICTS = new HashSet<>(Arrays.asList(
            "Quận 1", "Quận 3", "Quận 4", "Quận 5", "Quận 6", "Quận 7", "Quận 8",
            "Quận 10", "Quận 11", "Quận 12", "Quận Bình Tân", "Quận Bình Thạnh",
            "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình", "Quận Tân Phú",
            "Thành phố Thủ Đức", "Huyện Bình Chánh", "Huyện Cần Giờ", "Huyện Củ Chi",
            "Huyện Hóc Môn", "Huyện Nhà Bè"
    ));

    @Override
    public void init() throws ServletException {
        foodService = new FoodCartDAO();
        shippingService = new ShippingService();
        ghnMasterDataDAO = new GHNMasterDataDAO();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null) {
            logService.logActivity(0, 0, "Thanh toán", "Thất bại", "Người dùng chưa đăng nhập");
            response.sendRedirect("login");
            return;
        }

        FoodCartDAO cartDAO = (FoodCartDAO) foodService;
        List<Item> cartItems = cartDAO.getCartItems(currentUser.getAccountId());
        if (cartItems.isEmpty()) {
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Thanh toán", "Thất bại", "Giỏ hàng trống");
            response.sendRedirect("cart");
            return;
        }

        int subtotal = cartItems.stream().mapToInt(item -> item.getQuantity() * item.getFood().getPrice()).sum();
        int discountAmount = session.getAttribute("discountAmount") != null ? (int) session.getAttribute("discountAmount") : 0;
        int totalAmount = subtotal - discountAmount;

        Order order = new Order(cartItems);
        request.setAttribute("order", order);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("discountAmount", discountAmount);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("discountCode", session.getAttribute("discountCode"));

        RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }
        int idAcc = currentUser.getAccountId();
        FoodCartDAO cartDAO = (FoodCartDAO) foodService;

        if ("/calculate-shipping".equals(servletPath)) {
            calculateShippingFee(request, response);
            return;
        }

        String recipientName = request.getParameter("tennguoinhan");
        String phoneNumber = request.getParameter("sdtnhan");
        String houseNumber = request.getParameter("sonha");
        String ward = request.getParameter("phuongxa");
        String district = request.getParameter("quan");
        String city = request.getParameter("thanhpho");
        String country = "Việt Nam";

        // Chuẩn hóa đầu vào
        if (houseNumber == null || houseNumber.trim().isEmpty()) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Số nhà, tên đường không được để trống");
            request.setAttribute("errorMessage", "Vui lòng nhập số nhà, tên đường");
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            forwardWithError(request, response, idAcc);
            return;
        }

        if (ward == null || ward.trim().isEmpty()) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Phường/xã không được để trống");
            request.setAttribute("errorMessage", "Vui lòng nhập phường/xã");
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            forwardWithError(request, response, idAcc);
            return;
        }

        if (district == null || district.trim().isEmpty()) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Quận/huyện không được để trống");
            request.setAttribute("errorMessage", "Vui lòng nhập quận/huyện");
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            forwardWithError(request, response, idAcc);
            return;
        }

        if (city == null || city.trim().isEmpty()) {
            city = "TP. Hồ Chí Minh";
        }

        district = district.replaceAll("-\\s*Hồ Chí Minh", "").trim();
        if (!ward.toLowerCase().startsWith("phường")) {
            ward = "Phường " + ward.trim();
        }

        if (!VALID_DISTRICTS.contains(district)) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Quận/huyện không hợp lệ: " + district);
            request.setAttribute("errorMessage", "Chỉ hỗ trợ giao hàng trong TP. Hồ Chí Minh. Vui lòng chọn quận/huyện hợp lệ.");
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            forwardWithError(request, response, idAcc);
            return;
        }

        String deliveryAddress = String.format("%s, %s, %s, %s, %s", houseNumber, ward, district, city, country);
        String note = request.getParameter("note-order");

        int totalAmount;
        try {
            String totalAmountStr = request.getParameter("totalAmount");
            if (totalAmountStr == null || totalAmountStr.trim().isEmpty()) {
                throw new NumberFormatException("totalAmount không được để trống");
            }
            totalAmount = Integer.parseInt(totalAmountStr);
            if (totalAmount <= 0) {
                throw new NumberFormatException("totalAmount phải lớn hơn 0");
            }
        } catch (NumberFormatException e) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Giá trị totalAmount không hợp lệ: " + e.getMessage());
            request.setAttribute("errorMessage", "Giá trị tổng tiền không hợp lệ. Vui lòng kiểm tra lại giỏ hàng.");
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            forwardWithError(request, response, idAcc);
            return;
        }

        int paymentMethod;
        try {
            String paymentMethodStr = request.getParameter("paymentMethod");
            if (paymentMethodStr == null || paymentMethodStr.trim().isEmpty()) {
                throw new NumberFormatException("paymentMethod không được để trống");
            }
            paymentMethod = Integer.parseInt(paymentMethodStr);
        } catch (NumberFormatException e) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Phương thức thanh toán không hợp lệ: " + e.getMessage());
            request.setAttribute("errorMessage", "Phương thức thanh toán không hợp lệ. Vui lòng chọn lại.");
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            request.setAttribute("totalAmount", totalAmount);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Integer shippingFee = (Integer) session.getAttribute("shippingFee");
        if (shippingFee == null) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Chưa tính phí vận chuyển");
            request.setAttribute("errorMessage", "Vui lòng kiểm tra phí vận chuyển trước khi đặt hàng.");
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            request.setAttribute("totalAmount", totalAmount);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
            dispatcher.forward(request, response);
            return;
        }

        int finalAmount = totalAmount + shippingFee;

        if (paymentMethod == 3) {
            String paymentUrl = createVNPayPaymentUrl(request, finalAmount);
            if (paymentUrl != null) {
                Invoice pendingInvoice = new Invoice();
                pendingInvoice.setAccountId(idAcc);
                pendingInvoice.setRecipientName(recipientName);
                pendingInvoice.setPhoneNumber(phoneNumber);
                pendingInvoice.setDeliveryAddress(deliveryAddress);
                pendingInvoice.setNote(note);
                pendingInvoice.setTotalAmount(finalAmount);
                pendingInvoice.setPaymentMethod(paymentMethod);
                pendingInvoice.setIsPaid(0);
                pendingInvoice.setDiscountCode((String) session.getAttribute("discountCode"));
                pendingInvoice.setEstimatedDeliveryTime((String) session.getAttribute("estimatedDeliveryTime"));
                session.setAttribute("pendingInvoice", pendingInvoice);
                logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Đang xử lý", "Chuyển hướng đến VNPay, Tổng tiền: " + finalAmount);
                response.sendRedirect(paymentUrl);
                return;
            } else {
                logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Lỗi tạo yêu cầu thanh toán VNPay");
                request.setAttribute("errorMessage", "Lỗi khi tạo yêu cầu thanh toán VNPay.");
                request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
                request.setAttribute("totalAmount", totalAmount);
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orderDate = sdf.format(new Date());

        Invoice invoice = new Invoice();
        invoice.setAccountId(idAcc);
        invoice.setRecipientName(recipientName);
        invoice.setPhoneNumber(phoneNumber);
        invoice.setDeliveryAddress(deliveryAddress);
        invoice.setNote(note);
        invoice.setOrderDate(orderDate);
        invoice.setTotalAmount(finalAmount);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setIsPaid(0);
        invoice.setDiscountCode((String) session.getAttribute("discountCode"));
        invoice.setEstimatedDeliveryTime((String) session.getAttribute("estimatedDeliveryTime"));

        InvoiceDAO invoiceDAO = new InvoiceDAO();
        DiscountDAO discountDAO = new DiscountDAO();

        try {
            invoiceDAO.addInvoice(invoice);
            List<Item> cartItems = cartDAO.getCartItems(idAcc);

            for (Item item : cartItems) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setInvoiceId(invoice.getInvoiceId());
                detail.setFoodId(item.getFood().getFoodId());
                detail.setQuantity(item.getQuantity());
                detail.setTotalAmount(item.getQuantity() * item.getFood().getPrice());
                invoiceDAO.addInvoiceDetail(detail);
            }

            Discount appliedDiscount = (Discount) session.getAttribute("appliedDiscount");
            if (appliedDiscount != null) {
                discountDAO.recordDiscountUsage(idAcc, appliedDiscount.getDiscountCodeId());
            }

            cartDAO.clearCart(idAcc);

            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thành công", "Mã đơn hàng: " + invoice.getInvoiceId() + ", Phương thức: COD, Tổng tiền: " + finalAmount);
            session.setAttribute("paymentSuccessMessage", "Thanh toán thành công!");
            session.removeAttribute("appliedDiscount");
            session.removeAttribute("discountAmount");
            session.removeAttribute("discountCode");
            session.removeAttribute("shippingFee");
            session.removeAttribute("estimatedDeliveryTime");
            session.removeAttribute("formData");
            response.sendRedirect("cart");
        } catch (Exception e) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            request.setAttribute("totalAmount", totalAmount);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void calculateShippingFee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int idAcc = ((Account) session.getAttribute("currentUser")).getAccountId();
        FoodCartDAO cartDAO = (FoodCartDAO) foodService;

        // Check if request is JSON (AJAX)
        String contentType = request.getContentType();
        Map<String, String> params = new HashMap<>();

        if (contentType != null && contentType.contains("application/json")) {
            // Handle JSON request
            BufferedReader reader = request.getReader();
            StringBuilder jsonInput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonInput.append(line);
            }
            JSONObject jsonObject = new JSONObject(jsonInput.toString());
            params.put("tennguoinhan", jsonObject.optString("tennguoinhan"));
            params.put("sdtnhan", jsonObject.optString("sdtnhan"));
            params.put("sonha", jsonObject.optString("sonha"));
            params.put("phuongxa", jsonObject.optString("phuongxa"));
            params.put("quan", jsonObject.optString("quan"));
            params.put("thanhpho", jsonObject.optString("thanhpho", "TP. Hồ Chí Minh"));
            params.put("note-order", jsonObject.optString("note-order"));
            params.put("paymentMethod", jsonObject.optString("paymentMethod"));
        } else {
            // Handle form-data request
            params.put("tennguoinhan", request.getParameter("tennguoinhan"));
            params.put("sdtnhan", request.getParameter("sdtnhan"));
            params.put("sonha", request.getParameter("sonha"));
            params.put("phuongxa", request.getParameter("phuongxa"));
            params.put("quan", request.getParameter("quan"));
            params.put("thanhpho", request.getParameter("thanhpho"));
            params.put("note-order", request.getParameter("note-order"));
            params.put("paymentMethod", request.getParameter("paymentMethod"));
        }

        String recipientName = params.get("tennguoinhan");
        String phoneNumber = params.get("sdtnhan");
        String houseNumber = params.get("sonha");
        String ward = params.get("phuongxa");
        String district = params.get("quan");
        String city = params.get("thanhpho");
        String note = params.get("note-order");
        String paymentMethod = params.get("paymentMethod");

        // Store form data in session
        Map<String, String> formData = new HashMap<>();
        formData.put("tennguoinhan", recipientName);
        formData.put("sdtnhan", phoneNumber);
        formData.put("sonha", houseNumber);
        formData.put("phuongxa", ward);
        formData.put("quan", district);
        formData.put("thanhpho", city);
        formData.put("note-order", note);
        formData.put("paymentMethod", paymentMethod);
        session.setAttribute("formData", formData);

        List<Item> cartItems = cartDAO.getCartItems(idAcc);
        if (cartItems.isEmpty()) {
            logService.logActivity(idAcc, ((Account) session.getAttribute("currentUser")).getRoleId(), "Tính phí ship", "Thất bại", "Giỏ hàng trống");
            sendJsonResponse(response, -1, null, "Giỏ hàng trống. Vui lòng thêm sản phẩm.");
            return;
        }

        int subtotal = cartItems.stream().mapToInt(item -> item.getQuantity() * item.getFood().getPrice()).sum();
        int discountAmount = session.getAttribute("discountAmount") != null ? (int) session.getAttribute("discountAmount") : 0;
        int totalAmount = subtotal - discountAmount;

        if (houseNumber == null || houseNumber.trim().isEmpty()) {
            logService.logActivity(idAcc, ((Account) session.getAttribute("currentUser")).getRoleId(), "Tính phí ship", "Thất bại", "Số nhà, tên đường không được để trống");
            sendJsonResponse(response, -1, null, "Vui lòng nhập số nhà, tên đường");
            return;
        }

        if (ward == null || ward.trim().isEmpty()) {
            logService.logActivity(idAcc, ((Account) session.getAttribute("currentUser")).getRoleId(), "Tính phí ship", "Thất bại", "Phường/xã không được để trống");
            sendJsonResponse(response, -1, null, "Vui lòng nhập phường/xã");
            return;
        }

        if (district == null || district.trim().isEmpty()) {
            logService.logActivity(idAcc, ((Account) session.getAttribute("currentUser")).getRoleId(), "Tính phí ship", "Thất bại", "Quận/huyện không được để trống");
            sendJsonResponse(response, -1, null, "Vui lòng nhập quận/huyện");
            return;
        }

        if (city == null || city.trim().isEmpty()) {
            city = "TP. Hồ Chí Minh";
        }

        district = district.replaceAll("-\\s*Hồ Chí Minh", "").trim();
        if (!ward.toLowerCase().startsWith("phường")) {
            ward = "Phường " + ward.trim();
        }

        if (!VALID_DISTRICTS.contains(district)) {
            logService.logActivity(idAcc, ((Account) session.getAttribute("currentUser")).getRoleId(), "Tính phí ship", "Thất bại", "Quận/huyện không hợp lệ: " + district);
            sendJsonResponse(response, -1, null, "Chỉ hỗ trợ giao hàng trong TP. Hồ Chí Minh. Vui lòng chọn quận/huyện hợp lệ.");
            return;
        }

        try {
            Map<String, Object> shippingResult = shippingService.calculateShippingFee(houseNumber, ward, district);
            int shippingFee = (int) shippingResult.get("shippingFee");
            String estimatedDeliveryTime = (String) shippingResult.get("estimatedDeliveryTime");

            if (shippingFee == -1) {
                logService.logActivity(idAcc, ((Account) session.getAttribute("currentUser")).getRoleId(),
                        "Tính phí ship", "Thất bại", "Không thể tính phí ship");
                sendJsonResponse(response, -1, null, "Không thể tính phí ship cho địa chỉ này.");
                return;
            }

            session.setAttribute("shippingFee", shippingFee);
            session.setAttribute("estimatedDeliveryTime", estimatedDeliveryTime);

            logService.logActivity(idAcc, ((Account) session.getAttribute("currentUser")).getRoleId(),
                    "Tính phí ship", "Thành công", "Phí ship: " + shippingFee + " VND, Dự kiến giao: " + estimatedDeliveryTime);

            if (contentType != null && contentType.contains("application/json")) {
                sendJsonResponse(response, shippingFee, estimatedDeliveryTime, null);
            } else {
                request.setAttribute("shippingFee", shippingFee);
                request.setAttribute("estimatedDeliveryTime", estimatedDeliveryTime);
                request.setAttribute("order", new Order(cartItems));
                request.setAttribute("totalAmount", totalAmount);
                request.setAttribute("subtotal", subtotal);
                request.setAttribute("discountAmount", discountAmount);
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            logService.logActivity(idAcc, ((Account) session.getAttribute("currentUser")).getRoleId(),
                    "Tính phí ship", "Thất bại", "Lỗi: " + e.getMessage());
            sendJsonResponse(response, -1, null, "Lỗi khi tính phí ship: " + e.getMessage());
        }
    }

    private void sendJsonResponse(HttpServletResponse response, int shippingFee, String estimatedDeliveryTime, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("shippingFee", shippingFee);
        jsonResponse.put("estimatedDeliveryTime", estimatedDeliveryTime != null ? estimatedDeliveryTime : "");
        if (errorMessage != null) {
            jsonResponse.put("errorMessage", errorMessage);
        }
        System.out.println("Sending JSON: " + jsonResponse.toString()); // Log JSON
        response.getWriter().write(jsonResponse.toString());
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, int idAcc) throws ServletException, IOException {
        FoodCartDAO cartDAO = (FoodCartDAO) foodService;
        List<Item> cartItems = cartDAO.getCartItems(idAcc);
        int subtotal = cartItems.stream().mapToInt(item -> item.getQuantity() * item.getFood().getPrice()).sum();
        HttpSession session = request.getSession();
        int discountAmount = session.getAttribute("discountAmount") != null ? (int) session.getAttribute("discountAmount") : 0;
        int totalAmount = subtotal - discountAmount;
        request.setAttribute("order", new Order(cartItems));
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("discountAmount", discountAmount);
        RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
        dispatcher.forward(request, response);
    }

    private String createVNPayPaymentUrl(HttpServletRequest req, int amount) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long vnpAmount = amount * 100;
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnpAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 1500000000);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Ghi log tất cả tham số
        System.out.println("VNPay Params: " + vnp_Params);
        System.out.println("vnp_ReturnUrl: " + Config.vnp_ReturnUrl);
        System.out.println("vnp_TxnRef: " + vnp_TxnRef);
        System.out.println("==== VNPay Log ====");
        System.out.println("vnp_CreateDate: " + vnp_CreateDate);
        System.out.println("vnp_ExpireDate: " + vnp_ExpireDate);
        System.out.println("Server LocalDateTime.now(): " + LocalDateTime.now());

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        return Config.vnp_PayUrl + "?" + query.toString();
    }
}