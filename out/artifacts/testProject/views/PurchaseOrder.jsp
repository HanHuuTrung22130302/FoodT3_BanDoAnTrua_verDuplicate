<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Đang vận chuyển</title>
    <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/module_header_footer.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signinCssModule.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/module_container_PurchaseOrder.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />

    <script src="${pageContext.request.contextPath}/js/module_dangnhap.js" defer></script>
</head>

<body>
<script src="${pageContext.request.contextPath}/js/module_dangnhap.js"></script>
<jsp:include page="header.jsp"></jsp:include>

<!-- Xử lý container -->
<div id="containe">
    <div class="order-status-container">
        <button class="status-button" onclick="ajaxOrder(0)">Tất cả</button>
        <button class="status-button" onclick="ajaxOrder(1)">Đang vận chuyển</button>
        <button class="status-button" onclick="ajaxOrder(2)">Đã vận chuyển</button>
        <button class="status-button" onclick="ajaxOrder(3)">Đã hủy</button>
    </div>
    <div class="search-box-container">
        <input
                oninput="searchByNameInvoice(this)" value="${txtS}" name="text" type="text"
                placeholder="Tìm kiếm đơn hàng..."
                class="search-input"
        />
        <button class="search-button">Tìm kiếm</button>
    </div>

    <div id="content_section">
        <c:forEach var="iorder" items="${ois}">
            <div class="order-container">
                <div class="order-card">
                    <div class="idDonHang">
                        <i class="fa-regular fa-copy"></i> ${String.format("%06d", iorder.idInvoice)}
                    </div>
                    <div class="line_st"></div>
                    <c:forEach var="item" items="${iorder.orderInvoiceDetail}">
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
                                <div class="money" >${item.totalAmount}&nbsp;đ</div>
                            </div>
                        </div>
                    </c:forEach>

                    <div class="line_end"></div>
                    <div class="order-total">
                        <strong>Tổng tiền:</strong>
                        <span class="total-money" id="totalAmount">${iorder.totalAmount}&nbsp;đ</span>
                    </div>
                    <div class="order-footer">
                        <div class="order-status">
                            Trạng thái:
                            <c:choose>
                                <c:when test="${iorder.orderSt == 1}">
                                    Đang chuẩn bị
                                </c:when>
                                <c:when test="${iorder.orderSt == 2}">
                                    Đang giao
                                </c:when>
                                <c:when test="${iorder.orderSt == 3}">
                                    Đã giao
                                </c:when>
                                <c:when test="${iorder.orderSt == 4}">
                                    Đã hủy
                                </c:when>

                            </c:choose></div>
                        <c:if test="${iorder.orderSt == 1}">
                            <!-- Hiển thị cả thẻ <a> và <button> khi order.orderSt là 1 -->
                            <a class="info-order-button" href="PurchaseOrderDetail?id=${iorder.idInvoice}" style="text-decoration: none">Chi tiết</a>
                            <a class="cancel-order-button" href="javascript:void(0);" onclick="confirmCancel(${iorder.idInvoice})" style="text-decoration: none">Hủy đơn hàng</a>
                        </c:if>

                        <c:if test="${iorder.orderSt == 2 || iorder.orderSt == 3}">
                            <!-- Chỉ hiển thị thẻ <a> khi order.orderSt là 2 hoặc 3 -->
                            <a class="info-order-button" href="PurchaseOrderDetail?id=${iorder.idInvoice}" style="text-decoration: none">Chi tiết</a>
                        </c:if>

                    </div>
                </div>
            </div>
        </c:forEach>
    </div>


</div>

<!-- Xử lý footer -->
<jsp:include page="footer.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/js/module_search_invoice_ajax.js"></script>
<script src="${pageContext.request.contextPath}/js/module_OrderInvoice_ajax.js"></script>
<script src="${pageContext.request.contextPath}/js/purchase.js"></script>
<script src="${pageContext.request.contextPath}/js/details.js"></script>
<script src="${pageContext.request.contextPath}/js/module_popup_purchase.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/isCancelOrder.js"></script>
<script src="${pageContext.request.contextPath}/js/module_search_Invoice.js"></script>

</body>
</html>
