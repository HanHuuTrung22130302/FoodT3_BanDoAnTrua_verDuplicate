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
        <h2 style="border-bottom: solid 1px #dddddd; margin-right: 5px">Địa Chỉ Nhận Hàng</h2>
        <p>${order.recipientName}</p>
        <p>${order.phoneNumber}</p>
        <p>${order.deliveryAddress}</p>
        <div class="aroundNote">
            <p class="inBold">GHI CHÚ:</p>
            <p>${order.note}</p>
        </div>
        <div class="order-container">
            <div class="order-card">
                <div class="idDonHang">
                    <i class="fa-regular fa-copy"></i> ${String.format("%06d",order.invoiceId)}
                </div>
                <c:forEach var="item" items="${order.orderInvoiceDetail}">
                    <div class="product-item">
                        <img
                                src="${item.image}"
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
                    <span style="font-size: 16px; font-weight: bold">Thành tiền:</span>
                    <span class="total-money">${String.format("%06d",order.totalAmount)} đ</span>
                </div>


            </div>
        </div>
        <div class="order-footer">

            <c:if test="${order.orderStatus == 4 && order.isReview==0}">
                <div class="cancel-order-button" href="javascript:void(0);"
                     onclick="showPopup('cancelPopup${order.invoiceId}')" style="text-decoration: none">Đánh giá đơn
                    hàng
                </div>

                <div id="popupWrapper${order.invoiceId}" style="display: none;">
                    <div class="overlay" onclick="closePopup('cancelPopup${order.invoiceId}')"></div>

                    <div id="cancelPopup${order.invoiceId}" class="cancel-popup">
                        <div class="popup-content-cancel review-popup">
                            <div class="popup-header">Đánh giá đơn hàng #${String.format("%06d", order.invoiceId)}</div>

                            <form action="reviewbyusercontroller" method="post" onsubmit="return showThankYouAndSubmit(this)">
                                <input type="hidden" name="invoiceId" value="${order.invoiceId}"/>

                                <div class="review-items">
                                    <c:forEach var="item" items="${order.orderInvoiceDetail}">
                                        <div class="review-item">
                                            <input type="hidden" name="foodId[]" value="${item.foodId}"/>

                                            <img src="${item.image}" class="review-image"/>
                                            <div class="review-info">
                                                <div class="review-name">${item.foodName}</div>

                                                <label class="review-label">Đánh giá:</label>
                                                <select name="rating[]" class="review-rating">
                                                    <option value="1">1 sao</option>
                                                    <option value="2">2 sao</option>
                                                    <option value="3">3 sao</option>
                                                    <option value="4">4 sao</option>
                                                    <option value="5">5 sao</option>
                                                </select>

                                                <label class="review-label">Bình luận:</label>
                                                <textarea name="comment[]" class="review-comment"
                                                          placeholder="Nhận xét của bạn..."></textarea>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <div class="popup-actions-cancel">
                                    <button type="submit" class="button-confirm-cancel">
                                        Gửi đánh giá
                                    </button>
                                    <button type="button" class="button-cancel-cancel"
                                            onclick="closePopup('cancelPopup${order.invoiceId}')">
                                        Đóng
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </c:if>


            <c:if test="${order.orderStatus == 1}">
                <div class="cancel-order-button" href="javascript:void(0);"
                     onclick="showPopup('cancelPopup${order.invoiceId}')" style="text-decoration: none">Hủy đơn
                    hàng
                </div>
                <%--                            confirmCancel(${iorder.invoiceId})--%>
                <div id="popupWrapper${order.invoiceId}" style="display: none;">
                    <div class="overlay" onclick="closePopup('cancelPopup${order.invoiceId}')"></div>

                    <div id="cancelPopup${order.invoiceId}" class="cancel-popup">
                        <div class="popup-content-cancel">
                            <div class="popup-header">Xác nhận hủy đơn hàng
                                ID: ${String.format("%06d", order.invoiceId)}</div>
                            <textarea class="cancel-reason cancel-reason${order.invoiceId}"
                                      placeholder="Lý do hủy..."></textarea>
                            <div class="popup-actions-cancel">
                                <button class="button-confirm-cancel" onclick="confirmCancel(${order.invoiceId})">
                                    Xác nhận
                                </button>
                                <button class="button-cancel-cancel"
                                        onclick="closePopup('cancelPopup${order.invoiceId}')">
                                    Đóng
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

            </c:if>


        </div>
    </div>

    <div class="tracking">
        <div class="tagOrder">
            <div class="nameorder">
                <strong>Mã vận đơn:</strong> ${String.format("%06d", order.invoiceId)}
            </div>
            <div class="orderDate">
                <strong>Ngày đặt:</strong> ${order.orderDate}
            </div>
            <div class="ordercodbank">
                <strong>Phương thức thanh toán:</strong>
                <span class="codBanking">
                    <c:choose>
                        <c:when test="${order.paymentMethod == 2}">Tài khoản ngân hàng</c:when>
                        <c:when test="${order.paymentMethod == 1}">Nhận hàng thanh toán</c:when>
                    </c:choose>
                </span>
            </div>
        </div>

        <c:if test="${order.orderStatus == 1}">
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

        <c:if test="${order.orderStatus == 2}">
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

        <c:if test="${order.orderStatus == 3}">
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

        <c:if test="${order.orderStatus == 4}">
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

        <c:if test="${order.orderStatus == 5}">
            <!-- Thẻ "Đã hủy" -->
            <div class="tracking-item">

                <div class="details">
                    <p class="status-text" style="color: #b5292f ; font-size: 30px ">Đơn hàng đã hủy</p>
                    <p class="description">Có gì không vừa ý, mong quý khách hãy liên hệ với chúng tôi</p>

                </div>

            </div>
        </c:if>
        <c:if test="${order.orderStatus == 6}">
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
                    <i class="fas fa-times"></i>
                </div>
                <div class="details">
                    <p class="status-text">Giao hàng thất bại</p>
                    <p class="description">Không liên hệ được với khách hàng</p>
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/js/home.js"></script>
<script src="${pageContext.request.contextPath}/js/purchase.js"></script>
<script src="${pageContext.request.contextPath}/js/module_OrderInvoice_ajax.js"></script>
<script src="${pageContext.request.contextPath}/js/isCancelOrder.js"></script>
<script>
    function showThankYouAndSubmit(form) {
        // Tạo overlay nền mờ, khóa toàn bộ tương tác
        const overlay = document.createElement('div');
        overlay.style.position = 'fixed';
        overlay.style.top = '0';
        overlay.style.left = '0';
        overlay.style.width = '100vw';
        overlay.style.height = '100vh';
        overlay.style.backgroundColor = 'rgba(0, 0, 0, 0.3)';
        overlay.style.zIndex = '9998';
        overlay.style.pointerEvents = 'all'; // Ngăn tương tác phía sau
        overlay.style.cursor = 'not-allowed';
        document.body.appendChild(overlay);

        // Tạo popup cảm ơn
        const popup = document.createElement('div');
        popup.innerText = "Cảm ơn vì đã đánh giá sản phẩm của chúng tôi!";
        popup.style.position = 'fixed';
        popup.style.top = '50%';
        popup.style.left = '50%';
        popup.style.transform = 'translate(-50%, -50%)';
        popup.style.backgroundColor = '#b5292f';
        popup.style.color = 'white';
        popup.style.padding = '20px 40px';
        popup.style.fontSize = '20px';
        popup.style.borderRadius = '10px';
        popup.style.zIndex = '9999';
        popup.style.boxShadow = '0 0 15px rgba(0, 0, 0, 0.5)';
        popup.style.textAlign = 'center';
        popup.style.pointerEvents = 'none'; // Cho phép bấm overlay nhưng không gì xảy ra
        document.body.appendChild(popup);

        // Sau 3 giây xóa popup và overlay rồi submit form
        setTimeout(() => {
            document.body.removeChild(popup);
            document.body.removeChild(overlay);
            form.submit();
        }, 3000);

        return false; // Ngăn submit ngay lập tức
    }
</script>



</body>
</html>
