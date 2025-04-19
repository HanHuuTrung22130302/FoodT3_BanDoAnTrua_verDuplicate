package hcmuaf.nlu.edu.vn.testproject.daos;

import hcmuaf.nlu.edu.vn.testproject.models.InvoiceDetail;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import java.util.List;

public class StatisticalDAO {
    private final InvoiceDAO invoiceDAO;

    public StatisticalDAO() {
        this.invoiceDAO = new InvoiceDAO();
    }

    public List<InvoiceDetail> getBestSellingProducts(String timeFilter, String search) {
        List<InvoiceDetail> invoiceDetails;
        
        if (search != null && !search.isEmpty()) {
            invoiceDetails = invoiceDAO.searchByNameAndTime(search, timeFilter);
        } else {
            switch (timeFilter) {
                case "day":
                    invoiceDetails = invoiceDAO.getInvoiceDetailsByDay();
                    break;
                case "week":
                    invoiceDetails = invoiceDAO.getInvoiceDetailsByWeek();
                    break;
                case "month":
                    invoiceDetails = invoiceDAO.getInvoiceDetailsByMonth();
                    break;
                default:
                    invoiceDetails = invoiceDAO.getInvoiceDetailsByDay();
            }
        }

        // Sắp xếp theo số lượng bán giảm dần
        invoiceDetails.sort((a, b) -> Integer.compare(b.getQuantity(), a.getQuantity()));
        return invoiceDetails;
    }

    public List<Food> getUnsoldProductsByTime() {
        return invoiceDAO.getUnsoldProductsByTime();
    }

    public List<InvoiceDetail> getSlowSellingProducts(String timeFilter) {
        return invoiceDAO.getSlowSellingProductsByTime(timeFilter);
    }
} 