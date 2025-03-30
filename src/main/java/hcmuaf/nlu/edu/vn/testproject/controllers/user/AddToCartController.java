package hcmuaf.nlu.edu.vn.testproject.controllers.user;


import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Account;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.Item;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "AddToCartController", value = "/addtoCart")
public class AddToCartController extends HttpServlet {
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
            response.sendRedirect("login"); // Yêu cầu đăng nhập để thêm vào giỏ hàng
            return;
        }
        int accountId = currentUser.getAccountId();


        int quantity = 1;
        String removeFoodID = request.getParameter("removeFoodID");
        String removeAll = request.getParameter("removeAll");
        String increment = request.getParameter("increment");
        String decrement = request.getParameter("decrement");


        FoodCartDAO cartDAO = (FoodCartDAO) foodService;


        if (removeAll != null) {
            cartDAO.clearCart(accountId);
            session.setAttribute("totalItems", 0); // Cập nhật totalItems
            response.sendRedirect("cart");
        } else if (removeFoodID != null) {
            int foodIdToRemove = Integer.parseInt(removeFoodID);
            cartDAO.removeFromCart(accountId, foodIdToRemove);
            updateTotalItems(session, cartDAO, accountId); // Cập nhật totalItems
            response.sendRedirect("cart");
        } else if (increment != null) {
            int foodIdToIncrement = Integer.parseInt(increment);
            int currentQuantity = cartDAO.getCartItems(accountId).stream()
                    .filter(item -> item.getFood().getFoodId() == foodIdToIncrement)
                    .findFirst().map(Item::getQuantity).orElse(0);
            cartDAO.updateCartItem(accountId, foodIdToIncrement, currentQuantity + 1);
            updateTotalItems(session, cartDAO, accountId); // Cập nhật totalItems
            response.sendRedirect("cart");
        } else if (decrement != null) {
            int foodIdToDecrement = Integer.parseInt(decrement);
            int currentQuantity = cartDAO.getCartItems(accountId).stream()
                    .filter(item -> item.getFood().getFoodId() == foodIdToDecrement)
                    .findFirst().map(Item::getQuantity).orElse(0);
            if (currentQuantity > 1) {
                cartDAO.updateCartItem(accountId, foodIdToDecrement, currentQuantity - 1);
            } else {
                cartDAO.removeFromCart(accountId, foodIdToDecrement);
            }
            updateTotalItems(session, cartDAO, accountId); // Cập nhật totalItems
            response.sendRedirect("cart");
        } else if (request.getParameter("foodID") != null) {
            int foodId = Integer.parseInt(request.getParameter("foodID"));
            Food food = foodService.getFoodByID(foodId);
            if (food != null) {
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }
                cartDAO.addToCart(accountId, foodId, quantity);
                updateTotalItems(session, cartDAO, accountId); // Cập nhật totalItems
                response.sendRedirect("cart");
            } else {
                response.sendRedirect("home");
            }
        } else {
            response.sendRedirect("home");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    // Phương thức cập nhật totalItems
    private void updateTotalItems(HttpSession session, FoodCartDAO cartDAO, int accountId) {
        List<Item> cartItems = cartDAO.getCartItems(accountId);
        int totalItems = 0;
        for (Item item : cartItems) {
            totalItems += item.getQuantity();
        }
        session.setAttribute("totalItems", totalItems);
    }
}

