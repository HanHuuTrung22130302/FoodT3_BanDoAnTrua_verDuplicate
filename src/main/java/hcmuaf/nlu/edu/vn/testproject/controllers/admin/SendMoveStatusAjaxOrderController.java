package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoiceDetail;
import hcmuaf.nlu.edu.vn.testproject.services.AdminInvoiceService;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;

@WebServlet(name = "SendMoveStatusAjaxOrderController", value = "/sendmovestatusajaxordermanagement")
public class SendMoveStatusAjaxOrderController extends HttpServlet {
    private LogService logService = new LogService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        int invoiceId = Integer.parseInt(request.getParameter("invoiceId"));

        if (currentUser == null || currentUser.getRoleId() == 2) {
            logService.logActivity(0, 0, "Xem danh sách đơn hàng", "Thất bại", "Không có quyền truy cập");
            response.sendRedirect("home");
            return;
        }

        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int pageSize = 12; // DAO đã giới hạn 12 bản ghi rồi
        int offset = (page - 1) * pageSize;

        String option = request.getParameter("option");
        if (option == null || option.isEmpty()) {
            option = "all";
        }

        AdminInvoiceService adminInvoiceService = new AdminInvoiceService();
        adminInvoiceService.sendMoveStatusNext(invoiceId);

        List<OrderInvoice> ois = adminInvoiceService.getOption(option, offset);


        int totalLs = adminInvoiceService.countInvoicesByOption(option);
        int totalPages = (int) Math.ceil((double) totalLs / pageSize);

        DecimalFormat moneyFormat = new DecimalFormat("#,###");
        PrintWriter out = response.getWriter();
        for (OrderInvoice oi : ois) {
            String statusText = "";
            switch (oi.getOrderStatus()) {
                case 1: statusText = "Chờ xác nhận"; break;
                case 2: statusText = "Đang chuẩn bị"; break;
                case 3: statusText = "Đang giao hàng"; break;
                case 4: statusText = "Đã hoàn thành"; break;
                case 5: statusText = "Đã hủy"; break;
            }

            boolean isDisabled = (oi.getOrderStatus() == 4 || oi.getOrderStatus() == 5);
            String formattedTotal = moneyFormat.format(oi.getTotalAmount());

            out.println("<tr>");
            out.println("  <td>" + String.format("%06d", oi.getInvoiceId()) + "</td>");
            out.println("  <td>" + oi.getRecipientName() + "</td>");
            out.println("  <td>" + oi.getOrderDate() + "</td>");
            out.println("  <td class='money'>" + formattedTotal + "</td>");

            // Nút hành động
            out.println("  <td>");
            out.println("    <button class='details-button' onclick=\"showPopup('check" + oi.getInvoiceId() + "')\" " +
                    (isDisabled ? "disabled" : "") + ">");
            out.println("      " + statusText);
            out.println("    </button>");

            // Popup xác nhận hành động kế tiếp
            out.println("    <div id='check" + oi.getInvoiceId() + "' class='popup'>");
            out.println("      <div class='popup-content-check'>");
            out.println("        <div class='closeDetail' onclick=\"closePopup('check" + oi.getInvoiceId() + "')\">&times;</div>");
            out.println("        <div class='checkText'>Thực hiện hành động kế tiếp cho đơn hàng ID: #" +
                    String.format("%06d", oi.getInvoiceId()) + "?</div>");
            out.println("        <div class='popup-actions'>");
            out.println("          <div class='buttonSubmitCheck' onclick=\"movestatus("+oi.getInvoiceId()+","+option+","+page+")\">");

            // Xác nhận theo trạng thái
            if (oi.getOrderStatus() == 1) {
                out.println("Xác nhận làm đơn hàng id: #" + String.format("%06d", oi.getInvoiceId()));
            } else if (oi.getOrderStatus() == 2) {
                out.println("Xác nhận chuyển đơn hàng cho shipper");
            } else if (oi.getOrderStatus() == 3) {
                out.println("Xác nhận hoàn thành đơn hàng");
            }

            // Nút hủy đơn
            out.println("          </div>");
            out.println("          <button class='button-cancel-order' onclick='showCancelPopup(" + oi.getInvoiceId() + ")'>Hủy đơn hàng</button>");

            // Popup hủy
            out.println("          <div id='cancelPopup" + oi.getInvoiceId() + "' class='cancel-popup' style='display: none;'>");
            out.println("            <div class='popup-content-cancel'>");
            out.println("              <div class='popup-header'>Xác nhận hủy đơn hàng ID: #" +
                    String.format("%06d", oi.getInvoiceId()) + "</div>");
            out.println("              <textarea class='cancel-reason' placeholder='Lý do hủy...'></textarea>");
            out.println("              <div class='popup-actions-cancel'>");
            out.println("                <button class='button-confirm-cancel' onclick='confirmCancelOrder(" + oi.getInvoiceId() + ")'>Xác nhận</button>");
            out.println("                <button class='button-cancel-cancel' onclick=\"closePopup('cancelPopup" + oi.getInvoiceId() + "')\">Đóng</button>");
            out.println("              </div></div></div>");
            out.println("        </div></div></div>");
            out.println("  </td>");

            // Nút xem chi tiết
            out.println("  <td>");
            out.println("    <button class='buttonDetailInvoice' onclick=\"showPopup('detail" + oi.getInvoiceId() + "');scrollToTop('detail" + oi.getInvoiceId() + "')\">Chi tiết</button>");

            // Popup chi tiết đơn
            out.println("    <div id='detail" + oi.getInvoiceId() + "' class='popup'>");
            out.println("      <div class='popup-content-detail'>");
            out.println("        <div class='closeDetail' onclick=\"closePopup('detail" + oi.getInvoiceId() + "')\">&times;</div>");
            out.println("        <div class='popup-body'>");
            out.println("          <div class='order-card'>");
            out.println("            <div class='popup-order-top'>");
            out.println("              <div class='popup-order-id'><span class='popup-order-label'>ID đơn hàng:</span> <span class='popup-order-value'>#" +
                    String.format("%06d", oi.getInvoiceId()) + "</span></div>");
            out.println("              <div class='popup-order-status'><span class='popup-order-label'>Tình trạng:</span> <span class='popup-order-value'>" + statusText + "</span></div>");
            out.println("            </div>");

            out.println("            <div class='line_st'></div>");
            out.println("            <div class='popup-order-info'>");
            out.println("              <div class='popup-order-row'><span class='popup-order-label'>Họ tên người nhận:</span><span class='popup-order-value'>" + oi.getRecipientName() + "</span></div>");
            out.println("              <div class='popup-order-row'><span class='popup-order-label'>Số điện thoại:</span><span class='popup-order-value'>" + oi.getPhoneNumber() + "</span></div>");
            out.println("              <div class='popup-order-row'><span class='popup-order-label'>Địa chỉ nhận hàng:</span><span class='popup-order-value'>" + oi.getDeliveryAddress() + "</span></div>");
            out.println("            </div>");
            out.println("<div class=\"line_st\"></div>\n" +
                    "                                        <div class=\"noteOrder\">\n" +
                    "                                            <div class=\"popup-order-row\">\n" +
                    "                                                <span class=\"popup-order-label\">Ghi chú:</span>\n" +
                    "                                                <span class=\"popup-order-value\">"+oi.getNote()+"</span>\n" +
                    "                                            </div>\n" +
                    "                                        </div>");
            out.println("            <div class='line_st'></div>");

            for (OrderInvoiceDetail item : oi.getOrderInvoiceDetail()) {
                String formattedItemTotal = moneyFormat.format(item.getTotalAmount());
                out.println("          <div class='product-item'>");
                out.println("            <img src='" + item.getImage() + "' class='product-image'/>");
                out.println("            <div class='product-info'><h3 class='product-name'>" + item.getFoodName() + "</h3><p class='product-quantity'>Số lượng: " + item.getQuantity() + "</p></div>");
                out.println("            <div class='product-total'><div class='money'>" + formattedItemTotal + "&nbsp;đ</div></div>");
                out.println("          </div>");
            }

            out.println("            <div class='line_end'></div>");
            out.println("            <div class='order-total'>Tổng tiền: <span class='total-money' style='font-size: 22px'>" + formattedTotal  + "&nbsp;đ</span></div>");
            out.println("          </div>");
            out.println("        </div></div></div>");
            out.println("  </td>");
            out.println("</tr>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}