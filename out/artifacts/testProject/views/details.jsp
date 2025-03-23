<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Đã vận chuyển</title>
    <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/module_header_footer.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signinCssModule.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/details.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />

    <script src="../js/module_dangnhap.js" defer></script>
</head>

<body>
<jsp:include page="header.jsp"></jsp:include>

<div id="containe">
    <div class="address">
        <h2>Địa Chỉ Nhận Hàng</h2>
        <p>${order.recipientName}</p>
        <p>${order.phoneNumber}</p>
        <p>${order.deliveryAddress}</p>
        <p class="inBold">GHI CHÚ:</p>
        <p>${order.note}</p>

        <div class="order-container">
            <div class="order-card">
                <div class="idDonHang">
                    <i class="fa-regular fa-copy"></i> ${String.format("%06d",order.idInvoice)}
                </div>
                <c:forEach var="item" items="${order.orderInvoiceDetail}">
                    <div class="product-item">
                        <img
                                src="${item.img}"
                                class="product-image"
                        />
                        <div class="product-info">
                            <h3 class="product-name">${item.foodName}</h3>
                            <p class="product-quantity">Số lượng: ${item.quantity}</p>
                        </div>
                        <div class="product-total">
                            <div class="money">${String.format("%06d",item.totalAmount)} đ</div>
                        </div>
                    </div>
                </c:forEach>
                <div class="line_end"></div>
                <div class="order-total">
                    <strong>Tổng tiền:</strong>
                    <span class="total-money">${String.format("%06d",order.totalAmount)} đ</span>
                </div>

            </div>
        </div>
    </div>

    <div class="tracking">

        <c:if test="${order.orderSt == 1}">
            <!-- Thẻ "xác nhận" -->
            <div class="tracking-item">
                <div class="status waiti">
                    <i class="fa-solid fa-hourglass-half"></i>
                </div>
                <div class="details">
                    <p class="status-text">Xác nhận đơn hàng</p>
                    <p class="description">Đang chờ xác nhận đơn hàng</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đang chuẩn bị đơn hàng</p>
                    <p class="description">Bếp đang chuẩn bị đơn hàng</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đang giao hàng</p>
                    <p class="description">Đơn của quý khách đang được giao</p>
                    <p class="description">Xin hãy chú ý điện thoại</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đã giao</p>
                    <p class="description">Giao hàng thành công</p>
                    <p class="description">Người nhận hàng: ${order.recipientName}</p>
                </div>
            </div>
        </c:if>

        <c:if test="${order.orderSt == 2}">
            <!-- Thẻ "đang chuẩn bị" -->
            <div class="tracking-item">
                <div class="status completed">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Xác nhận đơn hàng</p>
                    <p class="description">Đang chờ xác nhận đơn hàng</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status waiti">
                    <i class="fa-solid fa-hourglass-half"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đang chuẩn bị đơn hàng</p>
                    <p class="description">Bếp đang chuẩn bị đơn hàng</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đang giao hàng</p>
                    <p class="description">Đơn của quý khách đang được giao</p>
                    <p class="description">Xin hãy chú ý điện thoại</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đã giao</p>
                    <p class="description">Giao hàng thành công</p>
                    <p class="description">Người nhận hàng: ${order.recipientName}</p>
                </div>
            </div>
        </c:if>

        <c:if test="${order.orderSt == 3}">
            <!-- Thẻ "đang giao" -->
            <div class="tracking-item">
                <div class="status completed">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Xác nhận đơn hàng</p>
                    <p class="description">Đang chờ xác nhận đơn hàng</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status completed">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đang chuẩn bị đơn hàng</p>
                    <p class="description">Bếp đang chuẩn bị đơn hàng</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status waiti">
                    <i class="fa-solid fa-hourglass-half"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đang giao hàng</p>
                    <p class="description">Đơn của quý khách đang được giao</p>
                    <p class="description">Xin hãy chú ý điện thoại</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đã giao</p>
                    <p class="description">Giao hàng thành công</p>
                    <p class="description">Người nhận hàng: ${order.recipientName}</p>
                </div>
            </div>
        </c:if>

        <c:if test="${order.orderSt == 4}">
            <!-- Thẻ "đã giao" -->
            <div class="tracking-item">
                <div class="status completed">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Xác nhận đơn hàng</p>
                    <p class="description">Đang chờ xác nhận đơn hàng</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status completed">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đang chuẩn bị đơn hàng</p>
                    <p class="description">Bếp đang chuẩn bị đơn hàng</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status completed">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đang giao hàng</p>
                    <p class="description">Đơn của quý khách đang được giao</p>
                    <p class="description">Xin hãy chú ý điện thoại</p>
                </div>
            </div>
            <div class="tracking-item">
                <div class="status completed">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đã giao</p>
                    <p class="description">Giao hàng thành công</p>
                    <p class="description">Người nhận hàng: ${order.recipientName}</p>
                </div>
            </div>
        </c:if>

        <c:if test="${order.orderSt == 5}">
            <!-- Thẻ "Đã hủy" -->
            <div class="tracking-item">
                <div class="status completed">
                    <i class="fas fa-check"></i>
                </div>
                <div class="details">
                    <p class="status-text">Đã hủy</p>
                    <p class="description">Có gì không vừa ý, mong quý khách hãy liên hệ với chúng tôi</p>
                </div>

            </div>
        </c:if>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/js/home.js"></script>
<script src="${pageContext.request.contextPath}/js/purchase.js"></script>
</body>
</html>
