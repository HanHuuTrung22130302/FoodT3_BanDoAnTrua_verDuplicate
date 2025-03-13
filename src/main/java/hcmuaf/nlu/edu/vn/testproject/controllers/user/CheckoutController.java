package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.DistanceCheck;
import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceDAO;
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

    @Override
    public void init() throws ServletException {
        foodService = new FoodCartDAO();
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        // Lấy thông tin từ form
        String recipientName = request.getParameter("tennguoinhan");
        String phoneNumber = request.getParameter("sdtnhan");
        String deliveryAddress = request.getParameter("diachinhan");
        String note = request.getParameter("note-order");
        int totalAmount = Integer.parseInt(request.getParameter("totalAmount"));
        int paymentMethod = Integer.parseInt(request.getParameter("paymentMethod"));

        // Kiểm tra địa chỉ hợp lệ
        try {
            double[] coordinates = DistanceCheck.getCoordinatesFromAddress(deliveryAddress);
            if (coordinates == null || coordinates.length < 2) {
                request.setAttribute("error", "Địa chỉ không hợp lệ, vui lòng kiểm tra lại.");
                request.getRequestDispatcher("views/check-out.jsp").forward(request, response);
                return;
            }

            // Tính phí vận chuyển dựa trên khoảng cách từ kho hàng (ví dụ: Hồ Chí Minh)
            double[] warehouseCoordinates = {10.7769, 106.7009}; // Tọa độ giả định của kho
            double distanceKm = DistanceCheck.getDistanceBetweenPoints(warehouseCoordinates[0], warehouseCoordinates[1], coordinates[0], coordinates[1]);
            int shippingFee = (distanceKm > 5) ? 30000 : 15000; // 30.000đ nếu xa hơn 5km, 15.000đ nếu gần

            // Tạo hóa đơn
            Invoice invoice = new Invoice();
            invoice.setIdAcc(currentUser.getIdAcc());
            invoice.setRecipientName(recipientName);
            invoice.setPhoneNumber(phoneNumber);
            invoice.setDeliveryAddress(deliveryAddress);
            invoice.setNote(note);
            invoice.setOrderDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            invoice.setTotalAmount(totalAmount + shippingFee);
            invoice.setPaymentMethod(paymentMethod);

            // Lưu vào database
            InvoiceDAO invoiceDAO = new InvoiceDAO();
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

        } catch (IOException e) {
            request.setAttribute("error", "Lỗi khi kiểm tra địa chỉ: " + e.getMessage());
            request.getRequestDispatcher("views/check-out.jsp").forward(request, response);
        }
    }
}
