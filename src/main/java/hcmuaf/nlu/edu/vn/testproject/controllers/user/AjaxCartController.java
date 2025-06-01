package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Item;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AjaxCartController", value = "/AjaxCartController")
public class AjaxCartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        FoodCartDAO dao = new FoodCartDAO();
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Not logged in\"}");
            return;
        }

        int accountId = currentUser.getAccountId();
        String foodIdStr = request.getParameter("foodId");
        String action = request.getParameter("action");

        if ("removeAll".equals(action)) {
            dao.clearCart(accountId);
            session.setAttribute("totalItems", 0);
            response.getWriter().write("{\"success\": true, \"message\": \"All items removed\", \"totalItems\": 0}");
            return;
        }

        if (foodIdStr == null || action == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid parameters\"}");
            return;
        }

        int foodId = Integer.parseInt(foodIdStr);
        List<Item> cartItems = dao.getCartItems(accountId);

        Item targetItem = cartItems.stream()
                .filter(item -> item.getFood().getFoodId() == foodId)
                .findFirst().orElse(null);

        if (targetItem == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Item not found\"}");
            return;
        }

        int newQuantity = targetItem.getQuantity();
        int unitPrice = targetItem.getFood().getPrice();

        if ("increment".equals(action)) {
            newQuantity += 1;
        } else if ("decrement".equals(action)) {
            newQuantity = Math.max(0, newQuantity - 1);
        } else if ("remove".equals(action)) {
            dao.removeFromCart(accountId, foodId);
            newQuantity = 0;
        }

        if (newQuantity == 0) {
            dao.removeFromCart(accountId, foodId);
        } else {
            dao.updateCartItem(accountId, foodId, newQuantity);
        }

        // Tính lại giỏ hàng
        int totalItems = 0;
        int subtotal = 0;
        for (Item item : dao.getCartItems(accountId)) {
            totalItems += item.getQuantity();
            subtotal += item.getQuantity() * item.getFood().getPrice();
        }
        session.setAttribute("totalItems", totalItems);

        int discountAmount = session.getAttribute("discountAmount") != null
                ? (int) session.getAttribute("discountAmount") : 0;
        int total = subtotal - discountAmount;

        response.getWriter().write(String.format(
                "{\"success\": true, \"newQuantity\": %d, \"subtotal\": %d, \"total\": %d, \"unitPrice\": %d, \"totalItems\": %d}",
                newQuantity, subtotal, total, unitPrice, totalItems
        ));

    }
}
