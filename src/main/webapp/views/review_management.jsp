<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý đánh giá sản phẩm</title>
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
                    <th>Khách hàng</th>
                    <th>Sản phẩm</th>
                    <th>Số sao</th>
                    <th>Nội dung</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty reviews}">
                    <tr>
                        <td colspan="6" style="text-align: center">Không có dữ liệu đánh giá</td>
                    </tr>
                </c:if>
                <c:forEach var="review" items="${reviews}">
                    <tr>
                        <td><fmt:formatDate value="${review.date}" pattern="dd-MM-yyyy HH:mm:ss" /></td>
                        <td>${review.name}</td>
                        <td>${review.foodName}</td>
                        <td>${review.rating}</td>
                        <td>${review.comment}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/review-management" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="reviewId" value="${review.reviewId}" />
                                <button type="submit" class="delete-btn" onclick="return confirm('Bạn có chắc chắn muốn xóa đánh giá này?')">Xóa</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- Phân trang -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/review-management?page=${currentPage - 1}&filterDate=${selectedDate}&filterProduct=${selectedProduct}">« Trước</a>
                </c:if>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/review-management?page=${i}&filterDate=${selectedDate}&filterProduct=${selectedProduct}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/review-management?page=${currentPage + 1}&filterDate=${selectedDate}&filterProduct=${selectedProduct}">Tiếp »</a>
                </c:if>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>