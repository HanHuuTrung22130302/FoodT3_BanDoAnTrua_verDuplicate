package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.Config;
import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceDAO;
import hcmuaf.nlu.edu.vn.testproject.models.*;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "VNPayReturnController", value = "/vnpay_return")
public class VNPayReturnController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(VNPayReturnController.class.getName());
    private FoodService foodService = new FoodCartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Invoice pendingInvoice = (Invoice) session.getAttribute("pendingInvoice");
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (pendingInvoice == null || currentUser == null) {
            logger.warning("Pending invoice or user is null. Redirecting to cart.");
            response.sendRedirect("cart");
            return;
        }

        int idAcc = currentUser.getAccountId();
        FoodCartDAO cartDAO = (FoodCartDAO) foodService;
        List<Item> cartItems = cartDAO.getCartItems(idAcc); // Lấy lại giỏ hàng

        // Lấy thông tin từ VNPay
        Map<String, String> vnp_Params = new HashMap<>();
        for (String param : request.getParameterMap().keySet()) {
            String value = request.getParameter(param);
            if (value != null && !value.isEmpty()) {
                vnp_Params.put(param, value);
            }
        }

        String vnp_SecureHash = vnp_Params.remove("vnp_SecureHash");
        String computedHash = Config.hashAllFields(vnp_Params);

        logger.info("VNPay Response Params: " + vnp_Params.toString());
        logger.info("Received vnp_SecureHash: " + vnp_SecureHash);
        logger.info("Computed Hash: " + computedHash);
        logger.info("Transaction Status: " + vnp_Params.get("vnp_TransactionStatus"));

        if ("00".equals(vnp_Params.get("vnp_TransactionStatus"))) {
            // Thanh toán thành công
            InvoiceDAO invoiceDAO = new InvoiceDAO();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            pendingInvoice.setOrderDate(sdf.format(new Date()));
            pendingInvoice.setIsPaid(1); // Đánh dấu đã thanh toán

            try {
                invoiceDAO.addInvoice(pendingInvoice);
                for (Item item : cartItems) { // Sử dụng cartItems thay vì order.getItems()
                    InvoiceDetail detail = new InvoiceDetail();
                    detail.setInvoiceId(pendingInvoice.getInvoiceId());
                    detail.setFoodId(item.getFood().getFoodId());
                    detail.setQuantity(item.getQuantity());
                    detail.setTotalAmount(item.getQuantity() * item.getFood().getPrice());
                    invoiceDAO.addInvoiceDetail(detail);
                }
                logger.info("Invoice saved successfully: " + pendingInvoice.getInvoiceId());
                cartDAO.clearCart(idAcc); // Xóa giỏ hàng sau khi thanh toán thành công
                session.removeAttribute("pendingInvoice");
                session.removeAttribute("shippingFee");
                session.setAttribute("paymentSuccessMessage", "Thanh toán VNPay thành công!");
                response.sendRedirect("cart");
            } catch (Exception e) {
                logger.severe("Error saving invoice: " + e.getMessage());
                request.setAttribute("errorMessage", "Lỗi khi lưu hóa đơn: " + e.getMessage());
                request.setAttribute("order", new Order(cartItems));
                request.setAttribute("totalAmount", pendingInvoice.getTotalAmount());
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            logger.warning("Payment failed. Transaction Status: " + vnp_Params.get("vnp_TransactionStatus"));
            request.setAttribute("errorMessage", "Thanh toán VNPay thất bại. Mã trạng thái: " + vnp_Params.get("vnp_TransactionStatus"));
            request.setAttribute("order", new Order(cartItems));
            request.setAttribute("totalAmount", pendingInvoice.getTotalAmount());
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
            dispatcher.forward(request, response);
        }
    }
}