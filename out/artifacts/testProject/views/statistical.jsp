<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Thống kê</title>
    <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/statistical.css"/>

    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
</head>
<body>
<div class="container">

    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <div class="content">
        <div class="header">
            <form action="statistical" method="get">
                <input value="${search}" name="text" type="text" placeholder="Tìm tên món ăn..."/>
                <button type="submit">
                    <i class="fa-solid fa-search"></i>
                </button>
            </form>
        </div>
        <div class="summary">
            <div class="card">
                <i class="fas fa-utensils"> </i>
                <div class="text">
                    <p>Sản phẩm được bán ra</p>
                    <p class="number">${totalProducts}</p>
                </div>
            </div>
            <div class="card">
                <i class="fas fa-file-alt"> </i>
                <div class="text">
                    <p>Số lượng bán ra</p>
                    <p class="number">${totalQuantity}</p>
                </div>
            </div>
            <div class="card">
                <i class="fas fa-dollar-sign"> </i>
                <div class="text">
                    <p>Doanh thu</p>
                    <p class="number">${totalRevenue} đ</p>
                </div>
            </div>
        </div>

        <table>
            <thead>
            <tr>
                <th>STT</th>
                <th>TÊN MÓN</th>
                <th>SỐ LƯỢNG BÁN</th>
                <th>DOANH THU</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="detail" items="${invoiceDetails}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td class="product_name">
                        <img alt="${detail.food.foodName}" height="50"
                             src="${pageContext.request.contextPath}/${detail.food.img}"/>
                            ${detail.food.foodName}
                    </td>
                    <td>${detail.quantity}</td>
                    <td>${detail.totalAmount} đ</td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
    </div>
</div>
</body>
</html>
