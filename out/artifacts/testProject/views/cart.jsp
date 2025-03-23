<%@ page import="hcmuaf.nlu.edu.vn.testproject.models.Food" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="hcmuaf.nlu.edu.vn.testproject.daos.FoodCartDAO" %>
<%@ page import="hcmuaf.nlu.edu.vn.testproject.models.Item" %>
<%@ page import="hcmuaf.nlu.edu.vn.testproject.models.Order" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Giỏ hàng</title>
    <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signinCssModule.css"/>
    <script src="${pageContext.request.contextPath}/js/module_dangnhap.js" defer></script>
</head>
<style>
    .congtrubutton{
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;
    }
    a{
        color: white;
    }
</style>
<body>
<script src="${pageContext.request.contextPath}/js/module_dangnhap.js"></script>
<jsp:include page="header.jsp"></jsp:include>


<div id="content_section">
    <div class="cart-container">
        <div class="cart-left">


            <c:if test="${not empty sessionScope.paymentSuccessMessage}">
                <div class="success-message" id="success-message">
                    <p>${sessionScope.paymentSuccessMessage}</p>
                </div>
                <c:remove var="paymentSuccessMessage"/>
            </c:if>

            <c:if test="${not empty sessionScope.cartEmptyMessage}">
                <div class="empty-cart-message" id="empty-cart-message">
                    <p>${sessionScope.cartEmptyMessage}</p>
                </div>
                <c:remove var="cartEmptyMessage"/>
            </c:if>

                <table class="cart-table">
                    <thead>
                    <tr>
                        <th></th>
                        <th>Tên sản phẩm</th>
                        <th>Hình ảnh</th>
                        <th>Số lượng</th>
                        <th>Giá</th>
                        <th>Thành tiền</th>
                        <th>
                            <a href="${pageContext.request.contextPath}/addtoCart?removeAll=true">
                                <i class="fa-regular fa-trash-can"></i>
                            </a>
                        </th>
                    </tr>
                    </thead>
                    <tbody id="cart-items-container" class="cart-body">
                    <c:forEach items="${sessionScope.order.items}" var="item">
                        <tr>
                            <td>
                                <button class="delete-btn">
                                    <a href="${pageContext.request.contextPath}/addtoCart?removeFoodID=${item.food.idFood}" class="delete-btn">
                                        <i class="fa-regular fa-trash-can"></i>
                                    </a>
                                </button>
                            </td>
                            <td>${item.food.foodName}</td>
                            <td>
                                <img src="${item.food.img}" alt="${item.food.foodName}" class="product-img"/>
                            </td>
                            <td>
                                <div class="congtrubutton">
                                    <a href="${pageContext.request.contextPath}/addtoCart?decrement=${item.food.idFood}">
                                        <button>-</button>
                                    </a>
                                    <span>${item.quantity}</span>
                                    <a href="${pageContext.request.contextPath}/addtoCart?increment=${item.food.idFood}">
                                        <button>+</button>
                                    </a>
                                </div>
                            </td>
                            <td>${item.food.price}₫</td>
                            <td>${item.quantity * item.food.price}₫</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                    </tbody>


                </table>


            <div class="cart-buttons">
                <button
                        class="continue-btn">
                    <a href="home">← Tiếp tục xem sản phẩm</a>
                </button>
            </div>
        </div>


        <div class="cart-right">
            <h2>Tổng Sản Phẩm</h2>
            <div class="cart-summary">
                <div class="summary-item">
                    <span>Tạm tính:</span>
                    <c:forEach var="item" items="${sessionScope.order.items}">
                        <c:set var="subtotal" value="${subtotal + item.quantity * item.food.price}" />
                    </c:forEach>
                    ${subtotal}₫
                </div>
            </div>
            <div class="summary-item total">
                <span>Tổng cộng:</span>
                <span id="total"> ${subtotal}₫</span>
            </div>
            <button
                    class="checkout-btn">
                    <a href="checkout" style="text-decoration: none">Tiến hành thanh toán</a>
            </button>


            <div class="voucher">
                <label for="voucher">Phiếu ưu đãi</label>
                <input type="text" id="voucher" placeholder="Mã ưu đãi"/>
                <button class="apply-btn" onclick="applyVoucher()">Áp dụng</button>
            </div>
        </div>
    </div>
</div>


<jsp:include page="footer.jsp"></jsp:include>


<script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
