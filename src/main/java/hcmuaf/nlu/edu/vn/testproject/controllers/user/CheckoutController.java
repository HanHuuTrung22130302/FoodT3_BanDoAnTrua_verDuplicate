package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.DistanceCheck;
import hcmuaf.nlu.edu.vn.testproject.models.*;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;
import hcmuaf.nlu.edu.vn.testproject.daos.Config;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "CheckoutController", value = "/checkout")
public class CheckoutController extends HttpServlet {

    private FoodService foodService;
    private LogService logService = new LogService();
    private static final String STORE_ADDRESS = "Trường Đại học Nông Lâm TP. Hồ Chí Minh, khu phố 6, Thủ Đức, Hồ Chí Minh, Việt Nam";
    private static final double MAX_DELIVERY_DISTANCE = 40.0;
    private static final double FREE_SHIPPING_DISTANCE = 10.0;
    private static final int SHIPPING_FEE_PER_10KM = 10000;

    @Override
    public void init() throws ServletException {
        foodService = new FoodCartDAO();
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

        int totalAmount = 0;
        for (Item item : cartItems) {
            totalAmount += item.getQuantity() * item.getFood().getPrice();
        }

        Order order = new Order(cartItems); // Sử dụng constructor mới
        request.setAttribute("order", order);
        request.setAttribute("totalAmount", totalAmount);

        RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }
        int idAcc = currentUser.getAccountId();

        // Khai báo cartDAO trong doPost
        FoodCartDAO cartDAO = (FoodCartDAO) foodService;

        String recipientName = request.getParameter("tennguoinhan");
        String phoneNumber = request.getParameter("sdtnhan");
        String houseNumber = request.getParameter("sonha");
        String district = request.getParameter("quan");
        String city = request.getParameter("thanhpho");
        String country = "Việt Nam";
        String deliveryAddress = houseNumber + ", " + district + ", " + city + ", " + country;

        String note = request.getParameter("note-order");
        int totalAmount = Integer.parseInt(request.getParameter("totalAmount"));
        int paymentMethod = Integer.parseInt(request.getParameter("paymentMethod"));

        int shippingFee = 0;
        try {
            double[] storeCoords = DistanceCheck.getCoordinatesFromAddress(STORE_ADDRESS);
            double[] deliveryCoords = DistanceCheck.getCoordinatesFromAddress(deliveryAddress);
            double distance = DistanceCheck.getDistanceBetweenPoints(
                    storeCoords[0], storeCoords[1],
                    deliveryCoords[0], deliveryCoords[1]
            );

            if (distance > MAX_DELIVERY_DISTANCE) {
                logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Địa chỉ giao hàng quá xa (> " + MAX_DELIVERY_DISTANCE + "km)");
                request.setAttribute("errorMessage", "Không thể giao hàng vì địa chỉ quá xa cửa hàng (> " + MAX_DELIVERY_DISTANCE + "km).");
                request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
                request.setAttribute("totalAmount", totalAmount);
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
                dispatcher.forward(request, response);
                return;
            }

            if (distance > FREE_SHIPPING_DISTANCE) {
                double extraDistance = distance - FREE_SHIPPING_DISTANCE;
                shippingFee = (int) Math.ceil(extraDistance / 10) * SHIPPING_FEE_PER_10KM;
            }
        } catch (IOException e) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Lỗi kiểm tra địa chỉ: " + e.getMessage());
            request.setAttribute("errorMessage", "Lỗi khi kiểm tra địa chỉ: " + e.getMessage());
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            request.setAttribute("totalAmount", totalAmount);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
            dispatcher.forward(request, response);
            return;
        }

        int finalAmount = totalAmount + shippingFee;

        // Thanh toán bằng VNPay (paymentMethod = 3)
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
                pendingInvoice.setIsPaid(0); // Chưa thanh toán
                session.setAttribute("pendingInvoice", pendingInvoice);
                session.setAttribute("shippingFee", shippingFee);
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

        // Thanh toán thông thường (COD hoặc thẻ ngân hàng)
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
        invoice.setIsPaid(paymentMethod == 2 ? 1 : 0); // Thẻ ngân hàng đã thanh toán, COD chưa thanh toán

        InvoiceDAO invoiceDAO = new InvoiceDAO();

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

            // Xóa giỏ hàng sau khi thanh toán thành công
            cartDAO.clearCart(idAcc);

            String paymentMethodStr = paymentMethod == 1 ? "COD" : "Thẻ ngân hàng";
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thành công", "Mã đơn hàng: " + invoice.getInvoiceId() + ", Phương thức: " + paymentMethodStr + ", Tổng tiền: " + finalAmount);
            session.setAttribute("paymentSuccessMessage", "Thanh toán thành công!");
            response.sendRedirect("cart");
        } catch (Exception e) {
            logService.logActivity(idAcc, currentUser.getRoleId(), "Thanh toán", "Thất bại", "Lỗi hệ thống: " + e.getMessage() + ", Tổng tiền: " + finalAmount);
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("order", new Order(cartDAO.getCartItems(idAcc)));
            request.setAttribute("totalAmount", totalAmount);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
            dispatcher.forward(request, response);
        }
    }

    private String createVNPayPaymentUrl(HttpServletRequest req, int amount) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long vnpAmount = amount * 100; // VNPay yêu cầu nhân 100
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

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

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