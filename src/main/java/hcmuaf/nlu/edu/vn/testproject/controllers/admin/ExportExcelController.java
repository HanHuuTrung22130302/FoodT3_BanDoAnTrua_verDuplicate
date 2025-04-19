package hcmuaf.nlu.edu.vn.testproject.controllers.admin;

import hcmuaf.nlu.edu.vn.testproject.daos.StatisticalDAO;
import hcmuaf.nlu.edu.vn.testproject.models.InvoiceDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

@WebServlet(name = "ExportExcelController", value = "/exportExcel")
public class ExportExcelController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String timeFilter = request.getParameter("timeFilter");
        String search = request.getParameter("search");

        StatisticalDAO statisticalDAO = new StatisticalDAO();
        List<InvoiceDetail> bestSellingProducts = statisticalDAO.getBestSellingProducts(timeFilter, search);
        List<Food> unsoldProducts = statisticalDAO.getUnsoldProductsByTime();
        List<InvoiceDetail> slowSellingProducts = statisticalDAO.getSlowSellingProducts(timeFilter);

        // Lấy thông tin tổng quan
        int totalProducts = bestSellingProducts.size();
        int totalQuantity = 0;
        int totalRevenue = 0;
        for (InvoiceDetail detail : bestSellingProducts) {
            totalQuantity += detail.getQuantity();
            totalRevenue += detail.getTotalAmount();
        }

        // Tạo workbook mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo các style
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        CellStyle moneyStyle = createMoneyStyle(workbook);
        CellStyle summaryStyle = createSummaryStyle(workbook);
        CellStyle summaryHeaderStyle = createSummaryHeaderStyle(workbook);

        // Sheet 1: Tổng quan và sản phẩm bán chạy
        Sheet bestSellingSheet = workbook.createSheet("Sản phẩm bán chạy");
        
        // Thêm phần tổng quan
        Row summaryHeaderRow = bestSellingSheet.createRow(0);
        summaryHeaderRow.createCell(0).setCellValue("THỐNG KÊ BÁN HÀNG");
        summaryHeaderRow.getCell(0).setCellStyle(summaryHeaderStyle);
        bestSellingSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        // Thời gian thống kê
        Row timeFilterRow = bestSellingSheet.createRow(1);
        timeFilterRow.createCell(0).setCellValue("Thời gian thống kê:");
        timeFilterRow.createCell(1).setCellValue(getTimeFilterText(timeFilter));
        timeFilterRow.getCell(0).setCellStyle(summaryStyle);
        timeFilterRow.getCell(1).setCellStyle(summaryStyle);

        // Thông tin tổng quan
        Row summaryRow1 = bestSellingSheet.createRow(2);
        summaryRow1.createCell(0).setCellValue("Tổng số sản phẩm:");
        summaryRow1.createCell(1).setCellValue(totalProducts);
        summaryRow1.getCell(0).setCellStyle(summaryStyle);
        summaryRow1.getCell(1).setCellStyle(summaryStyle);

        Row summaryRow2 = bestSellingSheet.createRow(3);
        summaryRow2.createCell(0).setCellValue("Tổng số lượng bán:");
        summaryRow2.createCell(1).setCellValue(totalQuantity);
        summaryRow2.getCell(0).setCellStyle(summaryStyle);
        summaryRow2.getCell(1).setCellStyle(summaryStyle);

        Row summaryRow3 = bestSellingSheet.createRow(4);
        summaryRow3.createCell(0).setCellValue("Tổng doanh thu:");
        summaryRow3.createCell(1).setCellValue(totalRevenue);
        summaryRow3.getCell(0).setCellStyle(summaryStyle);
        summaryRow3.getCell(1).setCellStyle(moneyStyle);

        // Tạo header cho bảng sản phẩm bán chạy
        Row headerRow1 = bestSellingSheet.createRow(6);
        headerRow1.createCell(0).setCellValue("DANH SÁCH SẢN PHẨM BÁN CHẠY");
        headerRow1.getCell(0).setCellStyle(summaryHeaderStyle);
        bestSellingSheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 6));

        Row headerRow2 = bestSellingSheet.createRow(7);
        String[] columns1 = {"STT", "Mã món", "Tên món", "Số lượng bán", "Đơn giá", "Doanh thu", "Tỷ lệ (%)"};
        for (int i = 0; i < columns1.length; i++) {
            Cell cell = headerRow2.createCell(i);
            cell.setCellValue(columns1[i]);
            cell.setCellStyle(headerStyle);
        }

        // Điền dữ liệu sản phẩm bán chạy
        int rowNum1 = 8;
        double totalAmount = bestSellingProducts.stream().mapToDouble(InvoiceDetail::getTotalAmount).sum();
        
        for (InvoiceDetail product : bestSellingProducts) {
            Row row = bestSellingSheet.createRow(rowNum1++);
            
            row.createCell(0).setCellValue(rowNum1 - 8);
            row.createCell(1).setCellValue(product.getFood().getFoodId());
            row.createCell(2).setCellValue(product.getFood().getFoodName());
            row.createCell(3).setCellValue(product.getQuantity());
            row.createCell(4).setCellValue(product.getFood().getPrice());
            row.createCell(5).setCellValue(product.getTotalAmount());
            // Tính tỷ lệ doanh thu
            double percentage = (product.getTotalAmount() / totalAmount) * 100;
            row.createCell(6).setCellValue(String.format("%.2f", percentage));

            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
            row.getCell(2).setCellStyle(dataStyle);
            row.getCell(3).setCellStyle(dataStyle);
            row.getCell(4).setCellStyle(moneyStyle);
            row.getCell(5).setCellStyle(moneyStyle);
            row.getCell(6).setCellStyle(dataStyle);
        }

        // Sheet 2: Sản phẩm bán chậm
        Sheet slowSellingSheet = workbook.createSheet("Sản phẩm bán chậm");
        
        Row slowHeaderRow1 = slowSellingSheet.createRow(0);
        slowHeaderRow1.createCell(0).setCellValue("DANH SÁCH SẢN PHẨM BÁN CHẬM");
        slowHeaderRow1.getCell(0).setCellStyle(summaryHeaderStyle);
        slowSellingSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        Row slowHeaderRow2 = slowSellingSheet.createRow(1);
        String[] columns2 = {"STT", "Mã món", "Tên món", "Số lượng bán", "Đơn giá", "Doanh thu", "Tỷ lệ (%)"};
        for (int i = 0; i < columns2.length; i++) {
            Cell cell = slowHeaderRow2.createCell(i);
            cell.setCellValue(columns2[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum2 = 2;
        for (InvoiceDetail product : slowSellingProducts) {
            Row row = slowSellingSheet.createRow(rowNum2++);
            
            row.createCell(0).setCellValue(rowNum2 - 2);
            row.createCell(1).setCellValue(product.getFood().getFoodId());
            row.createCell(2).setCellValue(product.getFood().getFoodName());
            row.createCell(3).setCellValue(product.getQuantity());
            row.createCell(4).setCellValue(product.getFood().getPrice());
            row.createCell(5).setCellValue(product.getTotalAmount());
            // Tính tỷ lệ doanh thu
            double percentage = (product.getTotalAmount() / totalAmount) * 100;
            row.createCell(6).setCellValue(String.format("%.2f", percentage));

            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
            row.getCell(2).setCellStyle(dataStyle);
            row.getCell(3).setCellStyle(dataStyle);
            row.getCell(4).setCellStyle(moneyStyle);
            row.getCell(5).setCellStyle(moneyStyle);
            row.getCell(6).setCellStyle(dataStyle);
        }

        // Sheet 3: Sản phẩm chưa bán
        Sheet unsoldSheet = workbook.createSheet("Sản phẩm chưa bán");
        
        Row unsoldHeaderRow1 = unsoldSheet.createRow(0);
        unsoldHeaderRow1.createCell(0).setCellValue("DANH SÁCH SẢN PHẨM CHƯA BÁN ĐƯỢC");
        unsoldHeaderRow1.getCell(0).setCellStyle(summaryHeaderStyle);
        unsoldSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

        Row unsoldHeaderRow2 = unsoldSheet.createRow(1);
        String[] columns3 = {"STT", "Mã món", "Tên món", "Đơn giá", "Trạng thái"};
        for (int i = 0; i < columns3.length; i++) {
            Cell cell = unsoldHeaderRow2.createCell(i);
            cell.setCellValue(columns3[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum3 = 2;
        for (Food product : unsoldProducts) {
            Row row = unsoldSheet.createRow(rowNum3++);
            
            row.createCell(0).setCellValue(rowNum3 - 2);
            row.createCell(1).setCellValue(product.getFoodId());
            row.createCell(2).setCellValue(product.getFoodName());
            row.createCell(3).setCellValue(product.getPrice());
            row.createCell(4).setCellValue(product.getIsDeleted() == 0 ? "Đang bán" : "Ngừng bán");
            
            // Xử lý ngày tháng
            if (product.getCreatedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                row.createCell(5).setCellValue(sdf.format(product.getCreatedAt()));
            } else {
                row.createCell(5).setCellValue("");
            }

            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
            row.getCell(2).setCellStyle(dataStyle);
            row.getCell(3).setCellStyle(moneyStyle);
            row.getCell(4).setCellStyle(dataStyle);
            row.getCell(5).setCellStyle(dataStyle);
        }

        // Tự động điều chỉnh độ rộng cột cho tất cả các sheet
        for (int i = 0; i < columns1.length; i++) {
            bestSellingSheet.autoSizeColumn(i);
            slowSellingSheet.autoSizeColumn(i);
        }
        for (int i = 0; i < columns3.length; i++) {
            unsoldSheet.autoSizeColumn(i);
        }

        // Thiết lập response
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "ThongKeBanHang_" + dateFormat.format(new Date()) + ".xlsx";
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Ghi workbook vào response output stream
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);

        CellStyle style = workbook.createCellStyle();
        style.setFont(headerFont);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createMoneyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));
        return style;
    }

    private CellStyle createSummaryStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    private CellStyle createSummaryHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private String getTimeFilterText(String timeFilter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();

        switch (timeFilter) {
            case "day":
                return "Hôm nay (" + dateFormat.format(now) + ")";
            case "week":
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                String weekStart = dateFormat.format(cal.getTime());
                cal.add(Calendar.DAY_OF_WEEK, 6);
                String weekEnd = dateFormat.format(cal.getTime());
                return "Tuần này (" + weekStart + " - " + weekEnd + ")";
            case "month":
                cal.set(Calendar.DAY_OF_MONTH, 1);
                String monthStart = dateFormat.format(cal.getTime());
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                String monthEnd = dateFormat.format(cal.getTime());
                return "Tháng này (" + monthStart + " - " + monthEnd + ")";
            default:
                return "Tất cả thời gian";
        }
    }
}