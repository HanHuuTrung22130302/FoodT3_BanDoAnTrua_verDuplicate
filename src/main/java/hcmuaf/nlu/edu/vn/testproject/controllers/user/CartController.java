package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.DiscountDAO;
import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Discount;
import hcmuaf.nlu.edu.vn.testproject.models.Item;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "CartController", value = "/cart")
public class CartController extends HttpServlet {
    private FoodService foodService;

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
            return;
        }

        FoodCartDAO cartDAO = (FoodCartDAO) foodService;
        List<Item> cartItems = cartDAO.getCartItems(currentUser.getAccountId());
        int totalItems = cartItems.stream().mapToInt(Item::getQuantity).sum();

        int subtotal = cartItems.stream().mapToInt(item -> item.getQuantity() * item.getFood().getPrice()).sum();
        int discountAmount = session.getAttribute("discountAmount") != null ? (int) session.getAttribute("discountAmount") : 0;
        String discountCode = (String) session.getAttribute("discountCode");

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("discountAmount", discountAmount);
        request.setAttribute("discountCode", discountCode);
        session.setAttribute("totalItems", totalItems);

        RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
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
        int accountId = currentUser.getAccountId();

        String voucherCode = request.getParameter("voucher");
        FoodCartDAO cartDAO = (FoodCartDAO) foodService;
        List<Item> cartItems = cartDAO.getCartItems(accountId);

        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            DiscountDAO discountDAO = new DiscountDAO();
            Discount discount = discountDAO.getDiscountByCode(voucherCode);
            if (discount != null && isValidDiscount(discount)) {
                if (discountDAO.hasUsedDiscount(accountId, discount.getDiscountCodeId())) {
                    session.setAttribute("cartEmptyMessage", "Bạn đã sử dụng mã giảm giá này trước đây!");
                } else {
                    int subtotal = cartItems.stream().mapToInt(item -> item.getQuantity() * item.getFood().getPrice()).sum();
                    int discountAmount = (int) (subtotal * discount.getDiscountRate());
                    session.setAttribute("appliedDiscount", discount);
                    session.setAttribute("discountAmount", discountAmount);
                    session.setAttribute("discountCode", voucherCode);
                    session.setAttribute("cartEmptyMessage", "Áp dụng mã giảm giá thành công!");
                }
            } else {
                session.setAttribute("cartEmptyMessage", "Mã giảm giá không hợp lệ hoặc đã hết hạn!");
            }
        }

        response.sendRedirect("cart");
    }

    private boolean isValidDiscount(Discount discount) {
        Date now = new Date();
        return discount.getStartDate().before(now) && discount.getEndDate().after(now);
    }
}