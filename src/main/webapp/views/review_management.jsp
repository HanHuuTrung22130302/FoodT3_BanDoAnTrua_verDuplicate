<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý đánh giá khách hàng</title>
    <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/review_management.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
</head>
<body>
<div class="container">
    <jsp:include page="leftAdmin.jsp"></jsp:include>
    <div class="content">
        <div class="header">
            <form action="${pageContext.request.contextPath}/review-management" method="get">
                <select name="filterStatus" id="filterStatus">
                    <option value="all" ${selectedStatus == 'all' ? 'selected' : ''}>Tất cả trạng thái</option>
                    <option value="approved" ${selectedStatus == 'approved' ? 'selected' : ''}>Đã duyệt</option>
                    <option value="pending" ${selectedStatus == 'pending' ? 'selected' : ''}>Chưa duyệt</option>
                </select>
                <input type="date" name="filterDate" id="filterDate" value="${selectedDate}" />
                <input type="text" name="filterProduct" id="filterProduct" value="${selectedProduct}" placeholder="Tìm kiếm sản phẩm" />
                <button type="submit"><i class="fa-solid fa-search"></i></button>
            </form>
        </div>
        <div id="reviewTable">
            <table>
                <thead>
                <tr>
                    <th>Thời gian</th>
                    <th>ID Khách hàng</th>
                    <th>Sản phẩm</th>
                    <th>Số sao</th>
                    <th>Nội dung</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty reviews}">
                    <tr>
                        <td colspan="7" style="text-align: center">Không có dữ liệu đánh giá</td>
                    </tr>
                </c:if>
                <c:forEach var="review" items="${reviews}">
                    <tr>
                        <td>${review.timestamp}</td>
                        <td>${review.customerId}</td>
                        <td>${review.productName}</td>
                        <td>${review.rating}</td>
                        <td>${review.content}</td>
                        <td>${review.status}</td>
                        <td>
                            <c:if test="${review.status == 'pending'}">
                                <form action="${pageContext.request.contextPath}/review-management" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="approve" />
                                    <input type="hidden" name="reviewId" value="${review.id}" />
                                    <button type="submit" class="approve-btn">Duyệt</button>
                                </form>
                            </c:if>
                            <form action="${pageContext.request.contextPath}/review-management" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="reviewId" value="${review.id}" />
                                <button type="submit" class="delete-btn" onclick="return confirm('Bạn có chắc chắn muốn xóa đánh giá này?')">Xóa</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>