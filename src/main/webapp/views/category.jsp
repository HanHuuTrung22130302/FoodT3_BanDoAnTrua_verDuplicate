<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Admin</title>
  <link href="Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/category_management.css"/>
  <link
          rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
  />
</head>
<body>
<div class="container">

  <jsp:include page="leftAdmin.jsp"></jsp:include>

  <div class="content">
    <div class="header">QUẢN LÝ DANH MỤC</div>
    <div class="category-management">
      <h2>Danh sách danh mục</h2>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Tên danh mục</th>
          <th>Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="category" items="${categories}">
          <tr>
            <td>${category.categoryId}</td>
            <td>${category.categoryName}</td>
            <td>
              <form action="category" method="post" style="display: inline">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${category.categoryId}">
                <button class="delete"
                        onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục này?')"><i
                        class="fas fa-trash"></i>
                  Xóa
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <button class="add-category"><i class="fas fa-plus"></i> Thêm Danh Mục Mới</button>
    </div>

    <div id="popup" class="popup hidden">
      <div class="popup_content">
          <span class="close_btn">
            <i class="fa-solid fa-xmark"></i>
          </span>
        <h2>THÊM DANH MỤC MỚI</h2>
        <form id="new_item_form" action="category" method="post">
          <input type="hidden" name="action" value="add">
          <label for="category_name">Tên danh mục:</label>
          <input type="text" name="categoryName" id="category_name" required>
          
          <label for="category_description">Mô tả:</label>
          <textarea name="description" id="category_description" rows="3"></textarea>

          <button type="submit">Lưu</button>
        </form>
      </div>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/js/admin_popup_category.js"></script>
</body>
</html>
