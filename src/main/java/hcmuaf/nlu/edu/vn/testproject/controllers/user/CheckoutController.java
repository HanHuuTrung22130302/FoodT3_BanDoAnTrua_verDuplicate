package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.DistanceCheck;
import hcmuaf.nlu.edu.vn.testproject.models.*;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "CheckoutController", value = "/checkout")
public class CheckoutController extends HttpServlet {

    private FoodService foodService;
    private static final String STORE_ADDRESS = "Trường Đại học Nông Lâm TP. Hồ Chí Minh, khu phố 6, Thủ Đức, Hồ Chí Minh, Việt Nam"; // Địa chỉ cửa hàng cố định
    private static final double MAX_DELIVERY_DISTANCE = 40.0; // Giới hạn giao hàng tối đa 40km
    private static final double FREE_SHIPPING_DISTANCE = 10.0; // Miễn phí giao hàng trong 10km
    private static final int SHIPPING_FEE_PER_10KM = 10000; // 10.000 VNĐ cho mỗi 10km

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
            response.sendRedirect("login");
        } else {
            Order order = (Order) session.getAttribute("order");
            if (order == null || order.getItems().isEmpty()) {
                response.sendRedirect("cart");
            } else {
                int totalAmount = 0;
                for (Item item : order.getItems()) {
                    totalAmount += item.getQuantity() * item.getFood().getPrice();
                }
                request.setAttribute("order", order);
                request.setAttribute("totalAmount", totalAmount);
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
                dispatcher.forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");
        int idAcc = currentUser.getIdAcc();

        // Lấy thông tin từ form
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

        // Tính khoảng cách và phí vận chuyển
        int shippingFee = 0;
        try {
            double[] storeCoords = DistanceCheck.getCoordinatesFromAddress(STORE_ADDRESS);
            double[] deliveryCoords = DistanceCheck.getCoordinatesFromAddress(deliveryAddress);
            double distance = DistanceCheck.getDistanceBetweenPoints(
                    storeCoords[0], storeCoords[1],
                    deliveryCoords[0], deliveryCoords[1]
            );

            if (distance > MAX_DELIVERY_DISTANCE) {
                request.setAttribute("errorMessage", "Không thể giao hàng vì địa chỉ quá xa cửa hàng");
                request.setAttribute("order", session.getAttribute("order"));
                request.setAttribute("totalAmount", totalAmount);
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Tính phí vận chuyển
            if (distance > FREE_SHIPPING_DISTANCE) {
                double extraDistance = distance - FREE_SHIPPING_DISTANCE;
                shippingFee = (int) Math.ceil(extraDistance / 10) * SHIPPING_FEE_PER_10KM;
            }
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi kiểm tra địa chỉ: " + e.getMessage());
            request.setAttribute("order", session.getAttribute("order"));
            request.setAttribute("totalAmount", totalAmount);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Tạo thời gian hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orderDate = sdf.format(new Date());

        // Tạo đối tượng Invoice
        Invoice invoice = new Invoice();
        invoice.setIdAcc(idAcc);
        invoice.setRecipientName(recipientName);
        invoice.setPhoneNumber(phoneNumber);
        invoice.setDeliveryAddress(deliveryAddress);
        invoice.setNote(note);
        invoice.setOrderDate(orderDate);
        invoice.setTotalAmount(totalAmount + shippingFee); // Cộng phí vận chuyển vào tổng tiền
        invoice.setPaymentMethod(paymentMethod);

        // Lưu phí vận chuyển vào request để hiển thị trên JSP nếu cần
        request.setAttribute("shippingFee", shippingFee);

        // Khởi tạo DAO để lưu vào database
        InvoiceDAO invoiceDAO = new InvoiceDAO();

        try {
            invoiceDAO.addInvoice(invoice);
            Order order = (Order) session.getAttribute("order");

            for (Item item : order.getItems()) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setIdInvoice(invoice.getIdInvoice());
                detail.setIdFood(item.getFood().getIdFood());
                detail.setQuantity(item.getQuantity());
                detail.setTotalAmount(item.getQuantity() * item.getFood().getPrice());
                invoiceDAO.addInvoiceDetail(detail);
            }

            session.removeAttribute("order");
            session.setAttribute("paymentSuccessMessage", "Thanh toán thành công!");
            response.sendRedirect("cart");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("checkout.jsp?error=true");
        }
    }
}