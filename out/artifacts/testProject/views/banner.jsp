<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Admin</title>
    <link href="Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/banner_management.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
</head>
<body>
<div class="container">

    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <div class="content">
        <div class="header">QUẢN LÝ BANNER</div>
        <div class="banner-management">
            <h2>Danh sách Banner</h2>
            <table>
                <thead>
                <tr>
                    <th>#</th>
                    <th>Hình ảnh</th>
                    <th>Hành động</th>
                    <th>Ngày tạo</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="bann" items="${bans}">
                    <tr>
                        <td>${bann.id}</td>
                        <td><img src="${pageContext.request.contextPath}/${bann.url}"/></td>

                        <td>
                            <form action="banner" method="post" style="display: inline">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${bann.id}">
                                <button class="delete"
                                        onclick="return confirm('Bạn có chắc chắn muốn xóa banner này?')"><i
                                        class="fas fa-trash"></i>
                                    Xóa
                                </button>
                            </form>
                        </td>
                        <td>
                                ${bann.date}
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
            <button class="add-banner"><i class="fas fa-plus"></i> Thêm Banner Mới</button>
        </div>

        <div id="popup" class="popup hidden">
            <div class="popup_content">
          <span class="close_btn">
            <i class="fa-solid fa-xmark"></i> 
          </span>
                <h2>THÊM BANNER MỚI</h2>
                <form id="new_item_form" action="banner" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add">
                    <label for="item_image">Hình ảnh:</label>
                    <input type="file" name="image" id="item_image" required>

                    <button type="submit">Lưu</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/admin_popup.js"></script>
</body>
</html>
