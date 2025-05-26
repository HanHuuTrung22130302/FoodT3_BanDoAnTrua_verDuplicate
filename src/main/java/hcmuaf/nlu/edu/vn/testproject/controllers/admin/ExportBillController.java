package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoice;
import hcmuaf.nlu.edu.vn.testproject.models.OrderInvoiceDetail;
import hcmuaf.nlu.edu.vn.testproject.services.AdminInvoiceService;
import hcmuaf.nlu.edu.vn.testproject.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "ExportBillController", value = "/exportBillController")
public class ExportBillController extends HttpServlet {
    private final AdminInvoiceService invoiceService = new AdminInvoiceService();
    private LogService logService = new LogService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");
        if (!invoiceService.isAdmin(currentUser.getAccountId()) || currentUser == null) {
            logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xuất pdf", "Thất bại", "Không có quyền truy cập");
            session.invalidate();
            response.sendRedirect("home");
            return;
        }
        String idParam = request.getParameter("id");
        logService.logActivity(currentUser.getAccountId(), currentUser.getRoleId(), "Xuất pdf", "Thành công", "Không có quyền truy cập");

        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing invoice id");
            return;
        }

        int invoiceId;
        try {
            invoiceId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid invoice id");
            return;
        }

        OrderInvoice invoice = invoiceService.getInvoiceById(invoiceId);
        if (invoice == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invoice not found");
            return;
        }

        List<OrderInvoiceDetail> details = invoice.getOrderInvoiceDetail();
        if (details == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invoice detail not found");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bill_" + invoiceId + ".pdf");

        try {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            String logoPath = getServletContext().getRealPath("/resources/images/logo.png");
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(200, 150);
            logo.setAlignment(Element.ALIGN_LEFT);
            logo.setSpacingAfter(10);
            document.add(logo);

            // Font tiếng Việt
            String fontPath = getServletContext().getRealPath("/resources/fonts/SVN-Arial Regular.ttf");
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font normalFont = new Font(baseFont, 13);
            Font boldFont = new Font(baseFont, 13, Font.BOLD);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);

            // Định dạng số tiền
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

            // Tiêu đề
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Thông tin đơn hàng
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);
            infoTable.setWidths(new float[]{3, 6});

            addInfoRow(infoTable, "Mã hóa đơn:", String.format("%06d", invoice.getInvoiceId()), boldFont, normalFont);
            addInfoRow(infoTable, "Tên người nhận:", invoice.getRecipientName(), boldFont, normalFont);
            addInfoRow(infoTable, "Số điện thoại:", invoice.getPhoneNumber(), boldFont, normalFont);
            addInfoRow(infoTable, "Địa chỉ giao hàng:", invoice.getDeliveryAddress(), boldFont, normalFont);
            addInfoRow(infoTable, "Ngày tạo:", invoice.getOrderDate(), boldFont, normalFont);
            addInfoRow(infoTable, "Phương thức thanh toán:", getPaymentMethodText(invoice.getPaymentMethod()), boldFont, normalFont);

            document.add(infoTable);

            // Bảng sản phẩm
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 2, 1, 2});
            table.setSpacingAfter(20);
            table.setHeaderRows(1);

            addTableHeader(table, boldFont, "Tên món", "Đơn giá", "SL", "Thành tiền");

            for (OrderInvoiceDetail item : details) {
                int unitPrice = item.getTotalAmount() / item.getQuantity();
                table.addCell(new PdfPCell(new Phrase(item.getFoodName(), normalFont)));
                table.addCell(getRightAlignedCell(currencyFormat.format(unitPrice) + "đ", normalFont));
                table.addCell(getCenterAlignedCell(String.valueOf(item.getQuantity()), normalFont));
                table.addCell(getRightAlignedCell(currencyFormat.format(item.getTotalAmount()) + "đ", normalFont));
            }

            document.add(table);

            // Tổng tiền
            Paragraph total = new Paragraph("Tổng cộng: " + currencyFormat.format(invoice.getTotalAmount()) + "đ", boldFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addInfoRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, labelFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase(value, valueFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell2);
    }

    private void addTableHeader(PdfPTable table, Font font, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private PdfPCell getRightAlignedCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell getCenterAlignedCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private String getPaymentMethodText(int method) {
        return switch (method) {
            case 1 -> "Thanh toán khi nhận hàng";
            case 2 -> "Chuyển khoản ngân hàng";
            default -> "Không rõ";
        };
    }

    private String getOrderStatusText(int status) {
        return switch (status) {
            case 1 -> "Chờ xác nhận";
            case 2 -> "Đang chuẩn bị";
            case 3 -> "Đang giao";
            case 4 -> "Đã giao";
            case 5 -> "Đã hủy";
            default -> "Không rõ";
        };
    }
    private PdfPCell getLeftAlignedCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

}
