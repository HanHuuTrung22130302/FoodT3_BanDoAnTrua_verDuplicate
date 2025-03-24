<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Admin</title>
    <link href="Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/discount.css">
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
</head>
<body>
<div class="container">

    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <div class="content">
        <div class="header">QUẢN LÝ MÃ GIẢM GIÁ</div>
        <div class="banner-management">
            <h2>Danh sách các mã giảm giá</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Mã giảm giá</th>
                    <th>Tỉ lệ giảm giá</th>
                    <th>Tiêu đề</th>
                    <th>Mô tả</th>
                    <th>Ngày bắt đầu</th>
                    <th>Ngày hết hạn</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="discount" items="${discounts}" varStatus="status">
                    <tr>
                        <td><fmt:formatNumber value="${status.index + 1}" pattern="000"/></td>
                        <td>${discount.codeName}</td>
                        <td><fmt:formatNumber value="${discount.discountRate * 100}" pattern="0.##"/>%</td>
                        <td>${discount.title}</td>
                        <td>${discount.description}</td>
                        <td>${discount.startDate}</td>
                        <td>${discount.endDate}</td>
                        <td>
                            <form action="discount" method="post" style="display: inline">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${discount.idCode}">
                                <button class="delete"
                                        onclick="return confirm('Bạn có chắc chắn muốn xóa discount này?')">
                                    <i class="fas fa-trash"
                                    ></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <button class="add-banner"><i class="fas fa-plus"></i> Thêm Mã Giảm Giá Mới</button>
        </div>

        <%-- Thêm mã giảm giá --%>
        <div id="popup" class="popup hidden">
            <div class="popup_content">
          <span class="close_btn">
            <i class="fa-solid fa-xmark"></i> 
          </span>
                <h2>THÊM MÃ GIẢM GIÁ MỚI</h2>
                <form id="new_item_form" method="POST" action="discount">
                    <input type="hidden" name="action" value="add">
                    <label for="dis-code">Mã giảm giá:</label>
                    <input type="text" id="dis-code" name="codeName" placeholder="Hãy nhập mã giảm giá..." required>

                    <label for="discount-rate">Tỉ lệ giảm giá (%):</label>
                    <input type="number" id="discount-rate" name="discountRate" placeholder="Hãy nhập tỉ lệ giảm giá..."
                           min="0" max="100" required>

                    <label for="title">Tiêu đề:</label>
                    <input type="text" id="title" name="title" placeholder="Hãy nhập tiêu đề..." required>

                    <label for="description">Mô tả:</label>
                    <textarea id="description" name="description" placeholder="Hãy nhập mô tả..." rows="3"
                              required></textarea>

                    <label for="start-date">Ngày bắt đầu:</label>
                    <input type="date" id="start-date" name="startDate" required>

                    <label for="end-date">Ngày hết hạn:</label>
                    <input type="date" id="end-date" name="endDate" required>

                    <button type="submit">Lưu</button>
                </form>

            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/admin_popup.js"></script>
</body>
</html>
