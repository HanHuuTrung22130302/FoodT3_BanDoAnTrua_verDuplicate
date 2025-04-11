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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/statistical.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="container">
    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <div class="content">
        <div class="header">
            <h2>Thống kê và phân tích hiệu suất bán hàng</h2>
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

        <!-- Input ẩn để truyền dữ liệu cho JavaScript -->
        <input type="hidden" id="dayRevenue" value="${revenueStats['day']}">
        <input type="hidden" id="weekRevenue" value="${revenueStats['week']}">
        <input type="hidden" id="monthRevenue" value="${revenueStats['month']}">
        <input type="hidden" id="dayOrders" value="${orderStats['day']}">
        <input type="hidden" id="weekOrders" value="${orderStats['week']}">
        <input type="hidden" id="monthOrders" value="${orderStats['month']}">
        <input type="hidden" id="bestSellingData" value='${bestSellingProductsJson}'>
        <input type="hidden" id="slowSellingData" value='${slowSellingProductsJson}'>
        <input type="hidden" id="unsoldData" value='${unsoldProductsJson}'>

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
                            <fmt:formatNumber value="${revenueStats[timeFilter]}" type="number" pattern="#,###"/> đ
                        </p>
                    </div>
                </div>
                <div class="card">
                    <i class="fas fa-shopping-cart"></i>
                    <div class="text">
                        <p>Số đơn hàng</p>
                        <p class="number">${orderStats[timeFilter]}</p>
                    </div>
                </div>
            </div>

            <!-- Phần biểu đồ -->
            <div class="charts">
                <div class="chart-section">
                    <h3>Biểu đồ doanh thu theo thời gian</h3>
                    <canvas id="revenueChart"></canvas>
                </div>
                <div class="chart-section">
                    <h3>Biểu đồ số đơn hàng theo thời gian</h3>
                    <canvas id="ordersChart"></canvas>
                </div>
            </div>

            <!-- Phần phân tích sản phẩm -->
            <div class="analysis">
                <div class="performance-section">
                    <h3>Sản phẩm bán chạy</h3>
                    <div class="chart-container">
                        <canvas id="bestSellingChart"></canvas>
                    </div>
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
                    <div class="chart-container">
                        <canvas id="slowSellingChart"></canvas>
                    </div>
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
                    <div class="chart-container">
                        <canvas id="unsoldChart"></canvas>
                    </div>
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
