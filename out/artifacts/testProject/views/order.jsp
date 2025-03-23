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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/food_service.css"/>
</head>
<body>
<div class="container">

    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <div class="content">
        <div class="header">
            <select style="max-width: 150px" onchange="window.location.href=this.value">
                <option value="ordermanagement?option=all"
                ${currentCategory == null || currentCategory == 'all' ? 'selected' : ''}>
                    Tất cả
                </option>
                <option value="ordermanagement?option=shipping"
                ${currentCategory == null || currentCategory == 'shipping' ? 'selected' : ''}>
                    Đang giao
                </option>
                <option value="ordermanagement?option=delivered"
                ${currentCategory == null || currentCategory == 'delivered' ? 'selected' : ''}>
                    Đã giao
                </option>
                <option value="ordermanagement?option=canceled"
                ${currentCategory == null || currentCategory == 'canceled' ? 'selected' : ''}>
                    Đã hủy
                </option>
                <option value="ordermanagement?option=yearmonth"
                ${currentCategory == null || currentCategory == 'yearmonth' ? 'selected' : ''}>
                    Theo tháng
                </option>
                <option value="ordermanagement?option=year"
                ${currentCategory == null || currentCategory == 'year' ? 'selected' : ''}>
                    Theo năm
                </option>
            </select>

            <form action="SearchInvoice" method="get">
                <input style="width: 800px" name="text" type="text" placeholder="Tìm kiếm mã đơn hoặc khách hàng"/>
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
            </tr>
            </thead>
            <tbody>
            <c:forEach var="oi" items="${ois}">
                <tr>
                    <td>${String.format("%06d",oi.idInvoice)}</td>
                    <td>${oi.recipientName}</td>
                    <td>${oi.orderDate}</td>
                    <td class="money">${oi.totalAmount}</td>
                    <td>
                        <button class="details-button">
                            <c:choose>
                                <c:when test="${oi.orderSt == 1}">
                                    Đang giao
                                </c:when>
                                <c:when test="${oi.orderSt == 2}">
                                    Đã giao
                                </c:when>
                                <c:when test="${oi.orderSt == 3}">
                                    Đã hủy
                                </c:when>

                            </c:choose>
                        </button>
                    </td>
                </tr>

            </c:forEach>
            </tbody>
        </table>
        <div class="pagi"
             style="width:800px; height:50px; margin:20px auto; padding-left:35px; text-align:center; z-index: 999">
            <c:if test="${currentPage > 1}">
                <a onclick="" href="ordermanagement?option=${param.option}&page=${currentPage - 1}"><</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="ordermanagement?option=${param.option}&page=${i}"
                   class="${currentPage == i ? 'active' : ''}">${i}</a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="ordermanagement?option=${param.option}&page=${currentPage + 1}">></a>
            </c:if>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/purchase.js"></script>
</body>
</html>
