package hcmuaf.nlu.edu.vn.testproject.controllers.user; import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
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
    private FoodService foodService;

@Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

}
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Not logged in\"}");
            return;
        }

        int accountId = currentUser.getAccountId();
        String foodIdStr = request.getParameter("foodId");
        String action = request.getParameter("action");

        if (foodIdStr == null || action == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid parameters\"}");
            return;
        }

        int foodId = Integer.parseInt(foodIdStr);
        FoodCartDAO cartDAO = new FoodCartDAO();
        List<Item> cartItems = cartDAO.getCartItems(accountId);

        Item targetItem = cartItems.stream()
                .filter(item -> item.getFood().getFoodId() == foodId)
                .findFirst().orElse(null);

        if (targetItem == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Item not found\"}");
            return;
        }

        int newQuantity = targetItem.getQuantity();
        if ("increment".equals(action)) {
            newQuantity += 1;
        } else if ("decrement".equals(action)) {
            newQuantity = Math.max(0, newQuantity - 1);
        } else if ("remove".equals(action)) {
            cartItems.remove(targetItem);
        } else if ("removeAll".equals(action)) {
            for (Item item : cartItems) {
                cartItems.remove(item);
            }
        }


        if (newQuantity == 0) {
            cartDAO.removeFromCart(accountId, foodId);
        } else {
            cartDAO.updateCartItem(accountId, foodId, newQuantity);
        }

        // Cập nhật totalItems
        int totalItems = 0;
        int subtotal = 0;
        for (Item item : cartDAO.getCartItems(accountId)) {
            totalItems += item.getQuantity();
            subtotal += item.getQuantity() * item.getFood().getPrice();
        }
        session.setAttribute("totalItems", totalItems);

        // Giảm giá
        int discountAmount = session.getAttribute("discountAmount") != null
                ? (int) session.getAttribute("discountAmount") : 0;

        int total = subtotal - discountAmount;

        response.getWriter().write(String.format("{\"success\": true, \"newQuantity\": %d, \"subtotal\": %d, \"total\": %d}", newQuantity, subtotal, total));
    }

}