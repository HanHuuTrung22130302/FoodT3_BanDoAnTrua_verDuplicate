package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoiceDetail;
import hcmuaf.nlu.edu.vn.testproject.services.InvoiceOrderServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "PurchaseOrderAjaxControler", value = "/PurchaseOrderAjaxControler")
public class PurchaseOrderAjaxControler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("currentUser");
        InvoiceOrderServices invoiceOrderServices = new InvoiceOrderServices();


        int userId = acc.getAccountId();

        String optionOrder = request.getParameter("text");
        int offset = 0;

        if (request.getParameter("offset") != null) {
            offset = Integer.parseInt(request.getParameter("offset"));
        }

        if (optionOrder == null) {
            optionOrder = "0";
        }
        System.out.println(optionOrder);
        if (request.getParameter("currentOption") != null) {
            String curentOption = request.getParameter("currentOption");
            if (!optionOrder.equals(curentOption))
                offset = 0;
        }

        List<OrderInvoice> ois = invoiceOrderServices.getOptionInvoice(userId, optionOrder, offset);
        PrintWriter out = response.getWriter();
        if (ois.size() < 10) {
            out.println("<div class=\"endofflag\" style=\"display: none;\"></div>");

        }
        if (ois.isEmpty()) {
            out.println("<h2 style=\"max-width: 1200px; text-align: center;\">Không có đơn hàng nào</h2>");
        } else {
            for (OrderInvoice oi : ois) {
                String formattedId = String.format("%06d", oi.getInvoiceId());
                String orderStatus;
                switch (oi.getOrderStatus()) {
                    case 1:
                        orderStatus = "Đang chờ xác nhận đơn hàng";
                        break;
                    case 2:
                        orderStatus = "Đơn hàng đang được chuẩn bị";
                        break;
                    case 3:
                        orderStatus = "Đơn hàng đang được giao";
                        break;
                    case 4:
                        orderStatus = "Đã hoàn thành";
                        break;
                    case 5:
                    case 6:
                        orderStatus = "Đơn hàng đã hủy";
                        break;
                    default:
                        orderStatus = "Không xác định";
                }

                out.println(
                        "<div class=\"order-container countOrder currentOption" + optionOrder + "\">\n" +
                                "    <div class=\"order-card\">\n" +
                                "        <div class=\"toporder\">\n" +
                                "            <div class=\"idDonHang\">\n" +
                                "                <i class=\"fa-regular fa-copy\"></i> " + formattedId + "\n" +
                                "            </div>\n" +
                                "            <div class=\"order-status\">" + orderStatus + "</div>\n" +
                                "        </div>\n" +
                                "        <div class=\"line_st\"></div>\n"
                );

                for (OrderInvoiceDetail oid : oi.getOrderInvoiceDetail()) {
                    out.println(
                            "        <div class=\"product-item\">\n" +
                                    "            <img src=\"" + oid.getImage() + "\" class=\"product-image\"/>\n" +
                                    "            <div class=\"product-info\">\n" +
                                    "                <h3 class=\"product-name\">" + oid.getFoodName() + "</h3>\n" +
                                    "                <p class=\"product-quantity\">Số lượng: " + oid.getQuantity() + "</p>\n" +
                                    "            </div>\n" +
                                    "            <div class=\"product-total\">\n" +
                                    "                <div class=\"money\">" + oid.getTotalAmount() + " đ</div>\n" +
                                    "            </div>\n" +
                                    "        </div>\n"
                    );
                }

                out.println(
                        "        <div class=\"line_end\"></div>\n" +
                                "        <div class=\"order-total\">\n" +
                                "            <span style=\"font-weight: 700; font-size: 17px\">Thành tiền:</span>\n" +
                                "            <span class=\"total-money\" id=\"totalAmount\" style=\"font-size: 22px\">" + oi.getTotalAmount() + " đ</span>\n" +
                                "        </div>\n" +
                                "        <div class=\"order-footer\">\n"
                );

                if (oi.getOrderStatus() == 1) {
                    out.println(
                            "            <div class=\"cancel-order-button\" href=\"javascript:void(0);\" onclick=\"showPopup('cancelPopup" + oi.getInvoiceId() + "')\" style=\"text-decoration: none\">Hủy đơn hàng</div>\n" +
                                    "            <div id=\"popupWrapper" + oi.getInvoiceId() + "\" style=\"display: none;\">\n" +
                                    "                <div class=\"overlay\" onclick=\"closePopup('cancelPopup" + oi.getInvoiceId() + "')\"></div>\n" +
                                    "                <div id=\"cancelPopup" + oi.getInvoiceId() + "\" class=\"cancel-popup\">\n" +
                                    "                    <div class=\"popup-content-cancel\">\n" +
                                    "                        <div class=\"popup-header\">Xác nhận hủy đơn hàng ID: " + formattedId + "</div>\n" +
                                    "                        <textarea class=\"cancel-reason cancel-reason" + oi.getInvoiceId() + "\" placeholder=\"Lý do hủy...\"></textarea>\n" +
                                    "                        <div class=\"popup-actions-cancel\">\n" +
                                    "                            <button class=\"button-confirm-cancel\" onclick=\"confirmCancel(" + oi.getInvoiceId() + ")\">Xác nhận</button>\n" +
                                    "                            <button class=\"button-cancel-cancel\" onclick=\"closePopup('cancelPopup" + oi.getInvoiceId() + "')\">Đóng</button>\n" +
                                    "                        </div>\n" +
                                    "                    </div>\n" +
                                    "                </div>\n" +
                                    "            </div>\n"
                    );
                }

                out.println(
                        "            <a class=\"info-order-button\" href=\"PurchaseOrderDetail?id=" + oi.getInvoiceId() + "\" style=\"text-decoration: none\">Chi tiết</a>\n" +
                                "        </div>\n" +
                                "    </div>\n" +
                                "</div>\n"
                );
            }
        }
    }

}