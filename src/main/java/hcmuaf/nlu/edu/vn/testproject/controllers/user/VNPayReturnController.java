package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.Config;
import hcmuaf.nlu.edu.vn.testproject.daos.InvoiceDAO;
import hcmuaf.nlu.edu.vn.testproject.models.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "VNPayReturnController", value = "/vnpay_return")
public class VNPayReturnController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(VNPayReturnController.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Invoice pendingInvoice = (Invoice) session.getAttribute("pendingInvoice");
        Order order = (Order) session.getAttribute("order");

        if (pendingInvoice == null || order == null) {
            logger.warning("Pending invoice or order is null. Redirecting to cart.");
            response.sendRedirect("cart");
            return;
        }

        // Lấy thông tin từ VNPay
        Map<String, String> vnp_Params = new HashMap<>();
        for (String param : request.getParameterMap().keySet()) {
            String value = request.getParameter(param);
            if (value != null && !value.isEmpty()) {
                vnp_Params.put(param, value);
            }
        }

        String vnp_SecureHash = vnp_Params.remove("vnp_SecureHash");

        // Tạo chuỗi Hash Data để log
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashDataBuilder = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldName.startsWith("vnp_") && fieldValue != null && !fieldValue.isEmpty()) {
                hashDataBuilder.append(fieldName).append("=").append(fieldValue);
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    hashDataBuilder.append("&");
                }
            }
        }
        String hashData = hashDataBuilder.toString();

        // Tính Computed Hash
        String computedHash = Config.hashAllFields(vnp_Params);

        // Log để kiểm tra
        logger.info("VNPay Response Params: " + vnp_Params.toString());
        logger.info("Hash Data: " + hashData);
        logger.info("Received vnp_SecureHash: " + vnp_SecureHash);
        logger.info("Computed Hash: " + computedHash);
        logger.info("Transaction Status: " + vnp_Params.get("vnp_TransactionStatus"));

        if (computedHash.equals(vnp_SecureHash)) {
            if ("00".equals(vnp_Params.get("vnp_TransactionStatus"))) {
                // Thanh toán thành công
                InvoiceDAO invoiceDAO = new InvoiceDAO();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                pendingInvoice.setOrderDate(sdf.format(new Date()));

                try {
                    invoiceDAO.addInvoice(pendingInvoice);
                    for (Item item : order.getItems()) {
                        InvoiceDetail detail = new InvoiceDetail();
                        detail.setIdInvoice(pendingInvoice.getIdInvoice());
                        detail.setIdFood(item.getFood().getIdFood());
                        detail.setQuantity(item.getQuantity());
                        detail.setTotalAmount(item.getQuantity() * item.getFood().getPrice());
                        invoiceDAO.addInvoiceDetail(detail);
                    }
                    logger.info("Invoice saved successfully: " + pendingInvoice.getIdInvoice());
                    session.removeAttribute("pendingInvoice");
                    session.removeAttribute("order");
                    session.setAttribute("paymentSuccessMessage", "Thanh toán VNPay thành công!");
                    response.sendRedirect("cart");
                } catch (Exception e) {
                    logger.severe("Error saving invoice: " + e.getMessage());
                    request.setAttribute("errorMessage", "Lỗi khi lưu hóa đơn: " + e.getMessage());
                    request.setAttribute("order", order);
                    request.setAttribute("totalAmount", pendingInvoice.getTotalAmount());
                    RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
                    dispatcher.forward(request, response);
                }
            } else {
                logger.warning("Payment failed. Transaction Status: " + vnp_Params.get("vnp_TransactionStatus"));
                request.setAttribute("errorMessage", "Thanh toán VNPay thất bại. Mã trạng thái: " + vnp_Params.get("vnp_TransactionStatus"));
                request.setAttribute("order", order);
                request.setAttribute("totalAmount", pendingInvoice.getTotalAmount());
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            logger.warning("Invalid Secure Hash. Computed: " + computedHash + ", Received: " + vnp_SecureHash);
            request.setAttribute("errorMessage", "Thanh toán VNPay thất bại. Lý do: Invalid Secure Hash");
            request.setAttribute("order", order);
            request.setAttribute("totalAmount", pendingInvoice.getTotalAmount());
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/check-out.jsp");
            dispatcher.forward(request, response);
        }
    }
}