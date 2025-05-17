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
    .calculate-shipping-btn { margin-top: 10px; padding: 10px 20px; background-color: #b5292f; color: white; border: none; cursor: pointer; border-radius: 10px; }
    .calculate-shipping-btn:disabled { background-color: #cccccc; cursor: not-allowed; }
    .loading { display: none; margin-left: 10px; color: #b5292f; }
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
  <c:if test="${not empty requestScope.errorMessage}">
    <div class="errorMessage">${requestScope.errorMessage}</div>
    <c:remove var="errorMessage" scope="request" />
  </c:if>
  <form action="${pageContext.request.contextPath}/checkout" method="post" id="checkout-form">
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
              <button type="button" id="calculate-shipping-btn" class="calculate-shipping-btn">Kiểm tra phí ship</button>
              <span class="loading" id="loading-spinner"><i class="fa-solid fa-spinner fa-spin"></i> Đang xử lý...</span>
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
                <span id="shipping-fee">
                  <c:choose>
                    <c:when test="${not empty shippingFee}">
                      <c:out value="${shippingFee == 0 ? 'Miễn phí' : shippingFee}" />
                      <c:if test="${shippingFee != 0}"> đ</c:if>
                    </c:when>
                    <c:otherwise>Chưa tính</c:otherwise>
                  </c:choose>
                </span>
              </div>
            </div>
            <div class="priceFlx chk-delivery-time">
              <div class="text">Dự kiến giao hàng</div>
              <div class="price-detail">
                <span id="estimated-delivery-time">
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
        <input type="hidden" name="totalAmount" id="total-amount" value="${totalAmount}" />
        <button class="complete-checkout-btn" type="submit" disabled>Đặt hàng</button>
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

  document.addEventListener('DOMContentLoaded', () => {
    const calculateShippingBtn = document.getElementById('calculate-shipping-btn');
    const loadingSpinner = document.getElementById('loading-spinner');
    const checkoutForm = document.getElementById('checkout-form');
    const completeCheckoutBtn = document.querySelector('.complete-checkout-btn');
    const errorMessageDiv = document.querySelector('.errorMessage') || document.createElement('div');

    if (!errorMessageDiv.classList.contains('errorMessage')) {
      errorMessageDiv.classList.add('errorMessage');
      checkoutForm.prepend(errorMessageDiv);
    }

    // Validate form inputs
    function validateInputs() {
      const requiredFields = ['sonha', 'phuongxa', 'quan'];
      let isValid = true;
      errorMessageDiv.textContent = '';

      requiredFields.forEach(fieldId => {
        const input = document.getElementById(fieldId);
        const errorSpan = input.nextElementSibling;
        if (!input.value.trim()) {
          errorSpan.textContent = `Vui lòng nhập ${input.placeholder}`;
          isValid = false;
        } else {
          errorSpan.textContent = '';
        }
      });

      return isValid;
    }

    // Update UI with shipping data
    function updateShippingUI(data) {
      const shippingFeeSpan = document.getElementById('shipping-fee');
      const deliveryTimeSpan = document.getElementById('estimated-delivery-time');
      const totalPriceSpan = document.getElementById('checkout-cart-price-final');
      const totalAmountInput = document.getElementById('total-amount');
      const baseTotal = parseInt(totalAmountInput.value) || 0;

      // Kiểm tra xem data.shippingFee có hợp lệ không
      if (typeof data.shippingFee === 'number' && data.shippingFee >= 0) {
        shippingFeeSpan.textContent = data.shippingFee === 0 ? 'Miễn phí' : `${data.shippingFee} đ`;
        deliveryTimeSpan.textContent = data.estimatedDeliveryTime || 'Chưa xác định';
        totalPriceSpan.textContent = `${baseTotal + data.shippingFee} đ`;
        completeCheckoutBtn.disabled = false;
      } else {
        shippingFeeSpan.textContent = 'Chưa tính'; // Thay vì "Miễn phí"
        deliveryTimeSpan.textContent = 'Chưa xác định';
        totalPriceSpan.textContent = `${baseTotal} đ`;
        completeCheckoutBtn.disabled = true;
        errorMessageDiv.textContent = data.errorMessage || 'Không thể tính phí ship cho địa chỉ này.';
      }
    }

    // Handle AJAX request for shipping calculation
    calculateShippingBtn.addEventListener('click', () => {
      if (!validateInputs()) {
        return;
      }

      calculateShippingBtn.disabled = true;
      loadingSpinner.style.display = 'inline-block';
      errorMessageDiv.textContent = '';

      const formData = new FormData(checkoutForm);
      const data = {
        tennguoinhan: formData.get('tennguoinhan'),
        sdtnhan: formData.get('sdtnhan'),
        sonha: formData.get('sonha'),
        phuongxa: formData.get('phuongxa'),
        quan: formData.get('quan'),
        'note-order': formData.get('note-order'),
        paymentMethod: formData.get('paymentMethod')
      };

      fetch('${pageContext.request.contextPath}/calculate-shipping', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      })
              .then(response => response.json())
              .then(data => {
                console.log('AJAX Response:', data); // Debug: Xem dữ liệu trả về
                updateShippingUI(data);
              })
              .catch(error => {
                errorMessageDiv.textContent = 'Lỗi khi tính phí ship. Vui lòng thử lại.';
                console.error('Error:', error);
                updateShippingUI({ shippingFee: -1, estimatedDeliveryTime: 'Chưa xác định' });
              })
              .finally(() => {
                calculateShippingBtn.disabled = false;
                loadingSpinner.style.display = 'none';
              });
    });

    // Prevent form submission if shipping fee is not calculated
    checkoutForm.addEventListener('submit', (e) => {
      const shippingFeeSpan = document.getElementById('shipping-fee').textContent;
      const deliveryTimeSpan = document.getElementById('estimated-delivery-time').textContent;
      if (shippingFeeSpan === 'Chưa tính' || deliveryTimeSpan === 'Chưa xác định') {
        e.preventDefault();
        errorMessageDiv.textContent = 'Vui lòng kiểm tra phí ship trước khi đặt hàng.';
      }
    });
  });
</script>

<script src="${pageContext.request.contextPath}/js/check-out.js"></script>
</body>
</html>