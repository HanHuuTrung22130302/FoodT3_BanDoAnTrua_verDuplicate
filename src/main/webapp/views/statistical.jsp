<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Thống kê và phân tích</title>
    <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/statisticals.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
</head>
<body>
<div class="container">
    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <div class="content">
        <div class="header">
            <form action="statistical" method="get">
                <label>
                    <select name="timeFilter" onchange="this.form.submit()">
                        <option value="day" ${timeFilter == 'day' ? 'selected' : ''}>Hôm nay</option>
                        <option value="week" ${timeFilter == 'week' ? 'selected' : ''}>Tuần này</option>
                        <option value="month" ${timeFilter == 'month' ? 'selected' : ''}>Tháng này</option>
                    </select>
                </label>
                <label>
                    <input value="${search}" name="text" type="text" placeholder="Tìm tên món ăn..."/>
                </label>
                <button type="submit">
                    <i class="fa-solid fa-search"></i>
                </button>
            </form>
        </div>

        <div class="dashboard">
            <!-- Phần tổng quan -->
            <div class="summary">
                <div class="card">
                    <i class="fas fa-utensils"></i>
                    <div class="text">
                        <p>Sản phẩm được bán ra</p>
                        <p class="number">${totalProducts}</p>
                    </div>
                </div>
                <div class="card">
                    <i class="fas fa-file-alt"></i>
                    <div class="text">
                        <p>Số lượng bán ra</p>
                        <p class="number">${totalQuantity}</p>
                    </div>
                </div>
                <div class="card">
                    <i class="fas fa-dollar-sign"></i>
                    <div class="text">
                        <p>Doanh thu</p>
                        <p class="number">
                            <fmt:formatNumber value="${totalRevenue}" type="number" pattern="#,###"/> đ
                        </p>
                    </div>
                </div>
                <div class="card">
                    <i class="fas fa-shopping-cart"></i>
                    <div class="text">
                        <p>Số đơn hàng</p>
                        <p class="number">${totalOrders}</p>
                    </div>
                </div>
            </div>

            <!-- Phần xu hướng -->
            <div class="trends">
                <div class="trend-section revenue-trends">
                    <div class="section-header">
                        <h3><i class="fas fa-chart-line"></i>Xu hướng doanh thu</h3>
                        <div class="time-period">12 tháng gần nhất</div>
                    </div>
                    <div class="trend-cards">
                        <c:forEach var="month" items="${last12Months}" varStatus="status">
                            <div class="trend-card">
                                <div class="trend-header">
                                    <div class="period">
                                        <i class="far fa-calendar-alt"></i>
                                        <span>Tháng ${month.monthValue}/${month.year}</span>
                                    </div>
                                    <c:set var="monthChange" value="${revenueStats[month] - revenueStats[month.minusMonths(1)]}"/>
                                    <span class="trend-indicator ${monthChange >= 0 ? 'up' : 'down'}">
                                        <i class="fas fa-${monthChange >= 0 ? 'arrow-up' : 'arrow-down'}"></i>
                                        <fmt:formatNumber value="${Math.abs(monthChange)}" type="number" pattern="#,###"/> đ
                                    </span>
                                </div>
                                <div class="trend-value">
                                    <fmt:formatNumber value="${revenueStats[month]}" type="number" pattern="#,###"/> đ
                                </div>
                                <div class="trend-percentage">
                                    <c:if test="${revenueStats[month.minusMonths(1)] != 0}">
                                        <span class="${monthChange >= 0 ? 'up' : 'down'}">
                                            <fmt:formatNumber value="${(Math.abs(monthChange) / revenueStats[month.minusMonths(1)]) * 100}" maxFractionDigits="1"/>%
                                            so với tháng trước
                                        </span>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <div class="trend-section order-trends">
                    <div class="section-header">
                        <h3><i class="fas fa-shopping-cart"></i>Xu hướng đơn hàng</h3>
                        <div class="time-period">12 tháng gần nhất</div>
                    </div>
                    <div class="trend-cards">
                        <c:forEach var="month" items="${last12Months}" varStatus="status">
                            <div class="trend-card">
                                <div class="trend-header">
                                    <div class="period">
                                        <i class="far fa-calendar-alt"></i>
                                        <span>Tháng ${month.monthValue}/${month.year}</span>
                                    </div>
                                    <c:set var="monthOrderChange" value="${orderStats[month] - orderStats[month.minusMonths(1)]}"/>
                                    <span class="trend-indicator ${monthOrderChange >= 0 ? 'up' : 'down'}">
                                        <i class="fas fa-${monthOrderChange >= 0 ? 'arrow-up' : 'arrow-down'}"></i>
                                        ${Math.abs(monthOrderChange)} đơn
                                    </span>
                                </div>
                                <div class="trend-value">
                                    ${orderStats[month]} đơn
                                </div>
                                <div class="trend-percentage">
                                    <c:if test="${orderStats[month.minusMonths(1)] != 0}">
                                        <span class="${monthOrderChange >= 0 ? 'up' : 'down'}">
                                            <fmt:formatNumber value="${(Math.abs(monthOrderChange) / orderStats[month.minusMonths(1)]) * 100}" maxFractionDigits="1"/>%
                                            so với tháng trước
                                        </span>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <!-- Phần phân tích sản phẩm -->
            <div class="analysis">
                <div class="performance-section">
                    <h3>Sản phẩm bán chạy</h3>
                    <table>
                        <thead>
                        <tr>
                            <th>STT</th>
                            <th>TÊN MÓN</th>
                            <th>SỐ LƯỢNG BÁN</th>
                            <th>DOANH THU</th>
                            <th>TỶ LỆ BÁN</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty bestSellingProducts}">
                            <tr>
                                <td colspan="5" style="text-align: center">Không có dữ liệu</td>
                            </tr>
                        </c:if>
                        <c:forEach var="product" items="${bestSellingProducts}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td class="product_name">
                                    <img alt="${product.food.foodName}" height="50"
                                         src="${pageContext.request.contextPath}/${product.food.image}"/>
                                    ${product.food.foodName}
                                </td>
                                <td>${product.quantity}</td>
                                <td><fmt:formatNumber value="${product.totalAmount}" type="number" pattern="#,###"/> đ</td>
                                <td>${product.salesPercentage}%</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="performance-section">
                    <h3>Sản phẩm bán chậm</h3>
                    <table>
                        <thead>
                        <tr>
                            <th>STT</th>
                            <th>TÊN MÓN</th>
                            <th>SỐ LƯỢNG BÁN</th>
                            <th>DOANH THU</th>
                            <th>TỶ LỆ BÁN</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty slowSellingProducts}">
                            <tr>
                                <td colspan="5" style="text-align: center">Không có dữ liệu</td>
                            </tr>
                        </c:if>
                        <c:forEach var="product" items="${slowSellingProducts}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td class="product_name">
                                    <img alt="${product.food.foodName}" height="50"
                                         src="${pageContext.request.contextPath}/${product.food.image}"/>
                                    ${product.food.foodName}
                                </td>
                                <td>${product.quantity}</td>
                                <td><fmt:formatNumber value="${product.totalAmount}" type="number" pattern="#,###"/> đ</td>
                                <td>${product.salesPercentage}%</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="performance-section">
                    <h3>Sản phẩm không bán được</h3>
                    <table>
                        <thead>
                        <tr>
                            <th>STT</th>
                            <th>TÊN MÓN</th>
                            <th>GIÁ</th>
                            <th>DANH MỤC</th>
                            <th>TRẠNG THÁI</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty unsoldProducts}">
                            <tr>
                                <td colspan="5" style="text-align: center">Không có sản phẩm nào không bán được</td>
                            </tr>
                        </c:if>
                        <c:forEach var="product" items="${unsoldProducts}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td class="product_name">
                                    <img alt="${product.foodName}" height="50"
                                         src="${pageContext.request.contextPath}/${product.image}"/>
                                    ${product.foodName}
                                </td>
                                <td><fmt:formatNumber value="${product.price}" type="number" pattern="#,###"/> đ</td>
                                <td>${product.category.categoryName}</td>
                                <td>${product.status == 1 ? 'Đang bán' : 'Ngừng bán'}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/statistical.js"></script>
</body>
</html>
