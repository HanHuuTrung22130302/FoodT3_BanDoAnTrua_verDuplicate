package hcmuaf.nlu.edu.vn.testproject.controllers.user; import hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO;
import hcmuaf.nlu.edu.vn.testproject.models.Food;
import hcmuaf.nlu.edu.vn.testproject.models.Item;
import hcmuaf.nlu.edu.vn.testproject.models.Order;
import hcmuaf.nlu.edu.vn.testproject.services.FoodService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
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
        int quantity = 1;
        int id;
        String removeFoodID = request.getParameter("removeFoodID");
        String removeAll = request.getParameter("removeAll");
        String increment = request.getParameter("increment");
        String decrement = request.getParameter("decrement");


        if (removeAll != null) {
            // Xóa tất cả món ăn khỏi giỏ
            HttpSession session = request.getSession();
            Order order = (Order) session.getAttribute("order");


            if (order != null) {
                order.setItems(new ArrayList<Item>());
                session.setAttribute("order", order);

            }
            response.sendRedirect("cart");
        } else if (removeFoodID != null) {
            // Xóa món ăn cụ thể khỏi giỏ
            int foodIDToRemove = Integer.parseInt(removeFoodID);
            HttpSession session = request.getSession();
            Order order = (Order) session.getAttribute("order");


            if (order != null) {
                List<Item> list = order.getItems();
                list.removeIf(item -> item.getFood().getFoodId() == foodIDToRemove);
                order.setItems(list);
                session.setAttribute("order", order);
            }
            response.sendRedirect("cart");


        }else if(increment != null) {
            int foodIdToIncrement = Integer.parseInt(increment);
            HttpSession session = request.getSession();
            Order order = (Order) session.getAttribute("order");
            if (order != null) {
                List<Item> list = order.getItems();
                for (Item item : list) {
                    if (item.getFood().getFoodId() == foodIdToIncrement) {
                        item.setQuantity(item.getQuantity() + 1);
                    }
                }
                session.setAttribute("order", order);
            }
            response.sendRedirect("cart");
        }else if(decrement != null) {
            int foodIdDecrement = Integer.parseInt(decrement);
            HttpSession session = request.getSession();
            Order order = (Order) session.getAttribute("order");
            if (order != null) {
                List<Item> list = order.getItems();
                for (Item item : list) {
                    if (item.getFood().getFoodId() == foodIdDecrement) {
                        if (item.getQuantity() > 1) {
                            item.setQuantity(item.getQuantity() - 1);
                        } else {
                            list.remove(item);
                        }
                        break;
                    }
                }
                order.setItems(list);
                session.setAttribute("order", order);
            }
            response.sendRedirect("cart");

        } else if (request.getParameter("foodID") != null) {
            // Xử lý thêm món ăn vào giỏ hàng
            id = Integer.parseInt(request.getParameter("foodID"));
            Food food = foodService.getFoodByID(id);
            if (food != null) {
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }
                HttpSession session = request.getSession();
                if (session.getAttribute("order") == null) {
                    Order order = new Order();
                    List<Item> list = new ArrayList<Item>();
                    Item item = new Item();
                    item.setQuantity(quantity);
                    item.setFood(food);
                    item.setPrice(food.getPrice());
                    list.add(item);
                    order.setItems(list);
                    session.setAttribute("order", order);
                } else {
                    Order order = (Order) session.getAttribute("order");
                    List<Item> list = order.getItems();
                    boolean found = false;
                    for (Item item : list) {
                        if (item.getFood().getFoodId() == food.getFoodId()) {
                            item.setQuantity(item.getQuantity() + quantity);
                            found = true;
                        }
                    }
                    if (!found) {
                        Item item = new Item();
                        item.setQuantity(quantity);
                        item.setFood(food);
                        item.setPrice(food.getPrice());
                        list.add(item);
                    }
                    session.setAttribute("order", order);
                }
            }
            response.sendRedirect("cart");
        } else {
            response.sendRedirect("home");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
