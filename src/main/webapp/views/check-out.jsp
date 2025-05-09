<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Thanh toán</title>
  <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/check-out.css" />
  <link rel="stylesheet" href="https://site-assets.fontawesome.com/releases/v6.6.0/css/all.css"/>
  <style>
    a { text-decoration: none; color: white; }
    .form-group { margin-bottom: 15px; }
    .form-message { color: red; font-size: 12px; }
    .errorMessage { color: white; margin-bottom: 10px; }
    .calculate-shipping-btn { margin-top: 10px; padding: 10px 20px; background-color: #b5292f; color: white; border: none; cursor: pointer;border-radius: 10px; }
  </style>
</head>
<body>
<div class="checkout-page">
  <div class="checkout-header">
    <div class="checkout-return">
      <button>
        <a href="cart" style="text-decoration: none">
          <i class="fa-regular fa-chevron-left"></i>
        </a>
      </button>
    </div>
    <h2 class="checkout-title">Thanh toán</h2>
  </div>
  <c:if test="${not empty errorMessage}">
    <div class="errorMessage">${errorMessage}</div>
  </c:if>
  <form action="${pageContext.request.contextPath}/checkout" method="post">
    <main class="checkout-section container">
      <div class="checkout-col-left">
        <div class="checkout-row">
          <div class="checkout-col-title">Thông tin đơn hàng</div>
          <div class="checkout-col-content">
            <div class="content-group">
              <p class="checkout-content-label">Phương thức thanh toán</p>
              <div class="checkout-payment-type">
                <div class="payment-btn active" id="tienmat">
                  <input type="radio" id="nhanhang" name="paymentMethod" value="1" <c:if test="${empty formData.paymentMethod || formData.paymentMethod == '1'}">checked</c:if>>
                  <label for="nhanhang">
                    <i class="fa-duotone fa-money-bill"></i>
                    Thanh toán khi nhận hàng
                  </label>
                </div>
                <div class="payment-btn" id="vidientu">
                  <input type="radio" id="dientu" name="paymentMethod" value="3" <c:if test="${formData.paymentMethod == '3'}">checked</c:if>>
                  <label for="dientu">
                    <i class="fa-duotone fa-solid fa-wallet"></i>
                    Thanh toán bằng VNPay
                  </label>
                </div>
              </div>
            </div>
            <div class="content-group">
              <p class="checkout-content-label">Ghi chú đơn hàng</p>
              <textarea type="text" class="note-order" name="note-order" placeholder="Nhập ghi chú"><c:out value="${formData['note-order']}" /></textarea>
            </div>
          </div>
        </div>
        <div class="checkout-row">
          <div class="checkout-col-title">Thông tin người nhận</div>
          <div class="checkout-col-content">
            <div class="content-group">
              <div class="form-group">
                <input id="tennguoinhan" name="tennguoinhan" type="text" placeholder="Tên người nhận" class="form-control" value="${formData.tennguoinhan}" required />
                <span class="form-message"></span>
              </div>
              <div class="form-group">
                <input id="sdtnhan" name="sdtnhan" type="text" placeholder="Số điện thoại nhận hàng" class="form-control" value="${formData.sdtnhan}" required />
                <span class="form-message"></span>
              </div>
              <div class="form-group">
                <input id="sonha" name="sonha" type="text" placeholder="Số nhà, tên đường" class="form-control" value="${formData.sonha}" required />
                <span class="form-message"></span>
              </div>
              <div class="form-group">
                <input id="phuongxa" name="phuongxa" type="text" placeholder="Phường/Xã" class="form-control" value="${formData.phuongxa}" required />
                <span class="form-message"></span>
              </div>
              <div class="form-group">
                <input id="quan" name="quan" type="text" placeholder="Quận/Huyện" class="form-control" value="${formData.quan}" required />
                <span class="form-message"></span>
              </div>
              <button type="submit" formaction="${pageContext.request.contextPath}/calculate-shipping" class="calculate-shipping-btn">Kiểm tra phí ship</button>
            </div>
          </div>
        </div>
      </div>
      <div class="checkout-col-right">
        <p class="checkout-content-label">Đơn hàng</p>
        <div class="bill-total" id="list-order-checkout">
          <c:forEach var="item" items="${order.items}">
            <div class="food-total">
              <div class="count">${item.quantity}</div>
              <div class="info-food">
                <div class="name-food">${item.food.foodName}</div>
              </div>
            </div>
          </c:forEach>
        </div>
        <div class="bill-payment">
          <div class="total-bill-order">
            <div class="priceFlx">
              <div class="text">
                Tiền hàng
                <span class="count">${order.items.size()} món</span>
              </div>
              <div class="price-detail">
                <span id="checkout-cart-total">
                  <c:choose>
                    <c:when test="${not empty totalAmount}"><c:out value="${totalAmount}" /> đ</c:when>
                    <c:otherwise>0 đ</c:otherwise>
                  </c:choose>
                </span>
              </div>
            </div>
            <div class="priceFlx chk-ship">
              <div class="text">Phí vận chuyển</div>
              <div class="price-detail chk-free-ship">
                <span>
                  <c:choose>
                    <c:when test="${empty shippingFee || shippingFee == 0}">Miễn phí</c:when>
                    <c:otherwise><c:out value="${shippingFee}" /> đ</c:otherwise>
                  </c:choose>
                </span>
              </div>
            </div>
            <div class="priceFlx chk-delivery-time">
              <div class="text">Dự kiến giao hàng</div>
              <div class="price-detail">
        <span>
            <c:choose>
              <c:when test="${not empty estimatedDeliveryTime}"><c:out value="${estimatedDeliveryTime}" /></c:when>
              <c:otherwise>Chưa xác định</c:otherwise>
            </c:choose>
        </span>
              </div>
            </div>
          </div>
          <div class="policy-note">
            Bằng việc bấm vào nút “Đặt hàng”, tôi đồng ý với
            <a href="#" target="_blank">chính sách hoạt động</a>
            của chúng tôi.
          </div>
        </div>
        <div class="total-checkout">
          <div class="text">Tổng tiền</div>
          <div class="price-bill" id="tongtiengiao">
            <div class="price-final" id="checkout-cart-price-final">
              <c:choose>
                <c:when test="${not empty totalAmount && not empty shippingFee}"><c:out value="${totalAmount + shippingFee}" /> đ</c:when>
                <c:when test="${not empty totalAmount}"><c:out value="${totalAmount}" /> đ</c:when>
                <c:otherwise>0 đ</c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>
        <input type="hidden" name="totalAmount" value="${totalAmount}" />
        <button class="complete-checkout-btn" type="submit">Đặt hàng</button>
      </div>
    </main>
  </form>
</div>

<div id="order-success-modal" class="modal" style="display: ${not empty paymentSuccessMessage ? 'block' : 'none'}">
  <div class="modal-content">
    <div class="modal-check">
      <i class="fa-solid fa-check fa-2xl"></i>
    </div>
    <p>
      <c:out value="${paymentSuccessMessage != null ? paymentSuccessMessage : 'Đặt hàng thành công!'}" /><br />
      Đơn hàng của bạn đang được xử lý
    </p>
    <form action="cart" method="get">
      <button type="submit">Đóng</button>
    </form>
  </div>
</div>

<script>
  <c:if test="${not empty paymentSuccessMessage}">
  setTimeout(() => {
    document.getElementById("order-success-modal").style.display = "none";
    <c:remove var="paymentSuccessMessage" scope="session" />
  }, 5000);
  </c:if>
</script>

<script src="${pageContext.request.contextPath}/js/check-out.js"></script>
</body>
</html>