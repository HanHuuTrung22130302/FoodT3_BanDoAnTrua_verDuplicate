<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Food Service</title>
    <link href='${pageContext.request.contextPath}/Images/LOGO_V2.png' rel='icon' type='image/x-icon'/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/food_service.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
</head>

<body>
<div class="container">

    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <div class="content">
        <div class="header">
            <select id="menu_filter" onchange="location.href=this.value;">
                <option value="foodservice?option=tatca"
                ${currentCategory == null || currentCategory == 'tatca' ? 'selected' : ''}>
                    Tất cả
                </option>
                <c:forEach var="category" items="${listC}">
                    <option value="foodservice?option=${category.idCategory}"
                        ${currentCategory == category.idCategory.toString() ? 'selected' : ''}>
                            ${category.nameCategory}
                    </option>
                </c:forEach>
            </select>
            <select id="special_filter" onchange="location.href=this.value;">
                <option value="foodservice?option=danhgiacao"
                ${currentCategory == null || currentCategory == 'danhgiacao' ? 'selected' : ''}>
                    Món được đánh giá cao
                </option>
                <option value="foodservice?option=dexuat"
                ${currentCategory == null || currentCategory == 'dexuat' ? 'selected' : ''}>
                    Món được đề xuất
                </option>
                <option value="foodservice?option=quantam"
                ${currentCategory == null || currentCategory == 'quantam' ? 'selected' : ''}>
                    Món được quan tâm nhiều
                </option>
                <option value="foodservice?option=banchay"
                ${currentCategory == null || currentCategory == 'banchay' ? 'selected' : ''}>
                    Món được bán chạy
                </option>
            </select>

            <form action="foodservice" method="get">
                <input value="${search}" name="text" type="text" placeholder="Tìm kiếm theo món..."/>
                <button type="submit">
                    <i class="fa-solid fa-search"></i>
                </button>
            </form>

            <button class="add_item_btn">
                + Thêm món mới
            </button>
        </div>

        <%-- Thêm món mới --%>
        <div id="add_popup" class="popup hidden">
            <div class="popup_content">
          <span class="close_btn">
            <i class="fa-solid fa-xmark"></i>
          </span>
                <h2>THÊM MÓN MỚI</h2>
                <form id="new_item_form" action="foodservice" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add">
                    <label for="item_name">Tên Món:</label>
                    <input type="text" id="item_name" name="foodName" placeholder="Nhập tên món ăn..." required>

                    <label for="item_category">Loại Món:</label>
                    <select id="item_category" name="idCategory">
                        <c:forEach var="category" items="${listC}">
                            <option value="${category.idCategory}">${category.nameCategory}</option>
                        </c:forEach>
                    </select>

                    <label for="item_price"> Giá:</label>
                    <input type="number" id="item_price" name="price" placeholder="Nhập giá của món ăn:" required>

                    <label for="item_details">Chi tiết món ăn:</label>
                    <textarea id="item_details" name="description" placeholder="Nhập chi tiết món ăn:"
                              required></textarea>

                    <label for="item_image">Hình ảnh:</label>
                    <input type="file" id="item_image" name="img" required>

                    <button type="submit">Lưu</button>
                </form>
            </div>
        </div>

        <%-- Cập nhật thông tin món --%>
        <div id="update_popup" class="popup hidden">
            <div class="popup_content">
          <span class="close_btn">
            <i class="fa-solid fa-xmark"></i>
          </span>
                <h2>CẬP NHẬT MÓN</h2>
                <form id="update_item_form" action="foodservice" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="idFood" value="${food.idFood}">
                    <label for="items_name">Tên Món:</label>
                    <input type="text" id="items_name" name="foodName" placeholder="Nhập tên món ăn..." required>

                    <label for="items_category">Loại Món:</label>
                    <select id="items_category" name="idCategory">
                        <c:forEach var="category" items="${listC}">
                            <option value="${category.idCategory}">${category.nameCategory}</option>
                        </c:forEach>
                    </select>

                    <label for="items_price"> Giá:</label>
                    <input type="number" id="items_price" name="price" placeholder="Nhập giá của món ăn:" required>

                    <label for="items_details">Chi tiết món ăn:</label>
                    <textarea id="items_details" name="description" placeholder="Nhập chi tiết món ăn:"
                              required>
                    </textarea>

                    <label for="items_image">Hình ảnh:</label>
                    <input type="file" id="items_image" name="img" required>

                    <button type="submit">Lưu</button>
                </form>
            </div>
        </div>

        <div class="menu_container">
            <c:forEach var="food" items="${list}">
                <div class="menu-item">
                    <img alt="${food.foodName}" height="100" src="${food.img}"/>
                    <div class="details">
                        <h3>
                                ${food.foodName}
                        </h3>
                        <p>
                                ${food.description}
                        </p>
                        <button>
                            <c:forEach var="category" items="${listC}">
                                <c:if test="${category.idCategory == food.idCategory}">
                                    ${category.nameCategory}
                                </c:if>
                            </c:forEach>
                        </button>
                    </div>
                    <div class="price">
                            ${food.price}
                    </div>
                    <div class="actions">
                        <!-- Nút cập nhật mở popup bằng JavaScript -->
                        <form action="foodservice" method="post" style="display: inline">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="idFood" value="${food.idFood}">
                            <button type="button" class="update_item_btn" onclick="openUpdatePopup(${food.idFood})">
                                <i class="fas fa-edit"></i>
                            </button>
                        </form>

                        <!-- Nút xóa có xác nhận -->
                        <form action="foodservice" method="post" style="display: inline">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="idFood" value="${food.idFood}">
                            <button type="submit" class="delete_item_btn"
                                    onclick="return confirm('Bạn có chắc chắn muốn xóa món này?')">
                                <i class="fas fa-trash"></i>
                            </button>
                        </form>
                    </div>

                </div>
            </c:forEach>
        </div>
        <div class="pagi" style="width:1200px;margin:0px auto; padding-left:35px; text-align:center;">
            <c:if test="${currentPage > 1}">
                <a onclick="" href="foodservice?option=${param.option}&page=${currentPage - 1}"><</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="foodservice?option=${param.option}&page=${i}"
                   class="${currentPage == i ? 'active' : ''}">${i}</a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="foodservice?option=${param.option}&page=${currentPage + 1}">></a>
            </c:if>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/food_service.js"></script>
</body>
</html>