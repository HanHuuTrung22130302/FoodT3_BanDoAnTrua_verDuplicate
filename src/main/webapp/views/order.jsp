<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Đơn hàng</title>
    <link href="Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/module_css_adminDonHang.css"/>
</head>
<body>
<div class="container">

    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <div class="content">
        <div class="header">
            <select style="max-width: 150px" onchange="handleSelectChange(this.value)">
                <option value="all|1" ${currentCategory == null || currentCategory == 'all' ? 'selected' : ''}>Tất cả</option>
                <option value="waitingConfirm|1" ${currentCategory == 'waitingConfirm' ? 'selected' : ''}>Chờ xác nhận</option>
                <option value="preparing|1" ${currentCategory == 'preparing' ? 'selected' : ''}>Đang chuẩn bị</option>
                <option value="shipping|1" ${currentCategory == 'shipping' ? 'selected' : ''}>Đang giao hàng</option>
                <option value="delivered|1" ${currentCategory == 'delivered' ? 'selected' : ''}>Đã hoàn thành</option>
                <option value="canceled|1" ${currentCategory == 'canceled' ? 'selected' : ''}>Đã hủy</option>

            </select>
            <select id="typeSelect">
                <option value="day">Ngày</option>
                <option value="month">Tháng</option>
            </select>

            <div class="input-group" id="inputGroup">
                <!-- Input sẽ thay đổi tại đây -->
            </div>
            <form action="SearchInvoice" method="get">
                <input style="display: flex; flex: 1;max-width: 1000px" name="text" type="text" placeholder="Tìm kiếm mã đơn hoặc khách hàng"/>
                <button type="submit">
                    <i class="fa-solid fa-search"></i>
                </button>
            </form>
            <div class="icons">
                <a href="ordermanagement"><i class="fas fa-sync-alt"> </i></a>
            </div>
        </div>

        <table>
            <thead>
            <tr>
                <th>MÃ ĐƠN</th>
                <th>KHÁCH HÀNG</th>
                <th>NGÀY ĐẶT</th>
                <th>TỔNG TIỀN</th>
                <th>TRẠNG THÁI</th>
                <th>CHI TIẾT ĐƠN HÀNG</th>
            </tr>
            </thead>
            <tbody id="ajax-section" >
            <c:forEach var="oi" items="${ois}">
                <tr>
                    <td>${String.format("%06d",oi.invoiceId)}</td>
                    <td>${oi.recipientName}</td>
                    <td>${oi.orderDate}</td>
                    <td class="money">${oi.totalAmount}</td>
                    <td>
                        <button class="details-button" onclick="showPopup('check${oi.invoiceId}')"
                                <c:if test="${oi.orderStatus == 4 || oi.orderStatus == 5}">disabled</c:if>>
                            <c:choose>
                                <c:when test="${oi.orderStatus == 1}">
                                    Chờ xác nhận
                                </c:when>
                                <c:when test="${oi.orderStatus == 2}">
                                    Đang chuẩn bị
                                </c:when>
                                <c:when test="${oi.orderStatus == 3}">
                                    Đang giao hàng
                                </c:when>
                                <c:when test="${oi.orderStatus == 4}">
                                    Đã hoàn thành
                                </c:when>
                                <c:when test="${oi.orderStatus == 5}">
                                    Đã hủy
                                </c:when>
                            </c:choose>
                        </button>
                        <div id="check${oi.invoiceId}" class="popup">
                            <div class="popup-content-check">
                                <div class="closeDetail" onclick="closePopup('check${oi.invoiceId}')">&times;</div>
                                <div class="checkText">
                                    Thực hiện hành động kế tiếp cho đơn hàng ID: ${String.format("%06d", oi.invoiceId)}?
                                </div>

                                <div class="popup-actions">
                                    <div class="buttonSubmitCheck" onclick="movestatus('${oi.invoiceId}','${param.option}',${param.page})">
                                        <c:choose>
                                            <c:when test="${oi.orderStatus == 1}">
                                                Xác nhận làm đơn hàng id: ${String.format("%06d",oi.invoiceId)}
                                            </c:when>
                                            <c:when test="${oi.orderStatus == 2}">
                                                Xác nhận chuyển đơn hàng cho shiper
                                            </c:when>
                                            <c:when test="${oi.orderStatus == 3}">
                                                Xác nhận hoàn thành đơn hàng
                                            </c:when>
                                        </c:choose>
                                    </div>

                                    <button class="button-cancel-order" onclick="showCancelPopup(${oi.invoiceId})">Hủy đơn hàng</button>

                                    <div id="cancelPopup${oi.invoiceId}" class="cancel-popup" style="display: none;">
                                        <div class="popup-content-cancel">
                                            <div class="popup-header">Xác nhận hủy đơn hàng ID: ${String.format("%06d", oi.invoiceId)}</div>
                                            <textarea class="cancel-reason" placeholder="Lý do hủy..."></textarea>
                                            <div class="popup-actions-cancel">
                                                <button class="button-confirm-cancel" onclick="confirmCancelOrder(${oi.invoiceId})">Xác nhận</button>
                                                <button class="button-cancel-cancel" onclick="closePopup('cancelPopup${oi.invoiceId}')">Đóng</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </td>
                    <td>
                        <button class="buttonDetailInvoice" onclick="showPopup('detail${oi.invoiceId}');scrollToTop('detail${oi.invoiceId}')">Chi tiết
                        </button>
                        <div id="detail${oi.invoiceId}" class="popup">
                            <div class="popup-content-detail">
                                <div class="closeDetail" onclick="closePopup('detail${oi.invoiceId}');">
                                    &times;
                                </div>
                                <div class="popup-body">
                                    <div class="order-card">
                                        <div class="popup-order-top">
                                            <div class="popup-order-id">
                                                <span class="popup-order-label">ID đơn hàng:</span>
                                                <span class="popup-order-value">#${String.format("%06d", oi.invoiceId)}</span>
                                            </div>
                                            <div class="popup-order-status">
                                                <span class="popup-order-label">Tình trạng:</span>
                                                <span class="popup-order-value">
                                                    <c:choose>
                                                        <c:when test="${oi.orderStatus == 1}">Đang chờ xác nhận</c:when>
                                                        <c:when test="${oi.orderStatus == 2}">Đang chuẩn bị</c:when>
                                                        <c:when test="${oi.orderStatus == 3}">Đang giao hàng</c:when>
                                                        <c:when test="${oi.orderStatus == 4}">Đã hoàn thành</c:when>
                                                        <c:when test="${oi.orderStatus == 5}">Đã hủy</c:when>
                                                    </c:choose>
                                                </span>
                                            </div>
                                        </div>

                                        <div class="line_st"></div>
                                        <div class="popup-order-info">
                                            <div class="popup-order-row">
                                                <span class="popup-order-label">Họ tên người nhận:</span>
                                                <span class="popup-order-value">${oi.recipientName}</span>
                                            </div>
                                            <div class="popup-order-row">
                                                <span class="popup-order-label">Số điện thoại:</span>
                                                <span class="popup-order-value">${oi.phoneNumber}</span>
                                            </div>
                                            <div class="popup-order-row">
                                                <span class="popup-order-label">Địa chỉ nhận hàng:</span>
                                                <span class="popup-order-value">${oi.deliveryAddress}</span>
                                            </div>
                                        </div>
                                        <div class="line_st"></div>
                                        <div class="noteOrder">
                                            <div class="popup-order-row">
                                                <span class="popup-order-label">Ghi chú:</span>
                                                <span class="popup-order-value">${oi.note}</span>
                                            </div>
                                        </div>
                                        <div class="line_st"></div>
                                        <c:forEach var="item" items="${oi.orderInvoiceDetail}">
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
                                                    <div class="money">${item.totalAmount}&nbsp;đ</div>
                                                </div>
                                            </div>
                                        </c:forEach>

                                        <div class="line_end"></div>
                                        <div class="order-total">
                                            Tổng tiền:
                                            <span class="total-money" id="totalAmount"
                                                  style="font-size: 22px">${oi.totalAmount}&nbsp;đ</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>

            </c:forEach>
            </tbody>
        </table>
        <div id="pagi-section" class="pagi" style="width:800px; height:50px; margin:20px auto; padding-left:35px; text-align:center; z-index: 999">
            <c:if test="${currentPage > 1}">
                <div class="pagiOrder" onclick="tableOrder('${param.option}',${currentPage - 1});pagi('${param.option}',${currentPage - 1})"><</div>
            </c:if>

            <c:if test="${currentPage > 3}">
                <div class="pagiOrder" onclick="tableOrder('${param.option}',1);pagi('${param.option}',1)">1</div>
                <div class="pagiOrder">..</div>
            </c:if>

            <c:forEach begin="${currentPage - 1}" end="${currentPage + 1}" var="i">
                <c:if test="${i > 0 && i <= totalPages}">
                    <div onclick="tableOrder('${param.option}',${i});pagi('${param.option}',${i})" class="pagiOrder ${currentPage == i ? 'active' : ''}">${i}</div>
                </c:if>
            </c:forEach>

            <c:if test="${currentPage < totalPages - 2}">
                <div class="pagiOrder">..</div>
                <div class="pagiOrder" onclick="tableOrder('${param.option}',${totalPages});pagi('${param.option}',${totalPages})">${totalPages}</div>
            </c:if>

            <c:if test="${currentPage < totalPages}">
                <div class="pagiOrder" onclick="tableOrder('${param.option}',${currentPage + 1});pagi('${param.option}',${currentPage + 1})">></div>
            </c:if>
        </div>

    </div>
</div>
<script src="${pageContext.request.contextPath}/js/module_popup_adminDonHang.js"></script>
<script src="${pageContext.request.contextPath}/js/module_stopPopup.js"></script>
<script src="${pageContext.request.contextPath}/js/purchase.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/module_ajax_admin_order.js"></script>
<script src="${pageContext.request.contextPath}/js/module_ajax_admin_order_pagi.js"></script>
<script src="${pageContext.request.contextPath}/js/module_ajax_admin_order_move_status.js"></script>
<script src="${pageContext.request.contextPath}/js/module_ajax_admin_order_cancel_status.js"></script>
<script>
    function handleSelectChange(value) {
        const [option, page] = value.split("|");
        tableOrder(option, page);
        pagi(option, page);
    }

</script>
</body>
</html>
