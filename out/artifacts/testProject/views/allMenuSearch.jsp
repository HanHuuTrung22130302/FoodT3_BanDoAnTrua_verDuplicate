<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Menu các món cơm</title>
    <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/allmenu.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signinCssModule.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/module_submenu_catelory.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />
    <script src="${pageContext.request.contextPath}/js/module_dangnhap.js" defer></script>
</head>

<body>

<jsp:include page="header.jsp"></jsp:include>

<div id="container">
    <div class="intro">
        <h2>Khám Phá Thực Đơn Của Chúng Tôi</h2>
        <div class="category-container">
            <a href="allmenu?option=tatca">
                <div class="category-item">Tất cả</div>
            </a>
            <a href="allmenu?option=danhgiacao">
                <div class="category-item">Món được đánh giá cao</div>
            </a>
            <a href="allmenu?option=dexuat">
                <div class="category-item">Món được đề xuất</div>
            </a>
            <a href="allmenu?option=quantam">
                <div class="category-item">Món được quan tâm nhiều</div>
            </a>
            <a href="allmenu?option=banchay">
                <div class="category-item">Món bán chạy</div>
            </a>

            <c:forEach var="category" items="${listC}">
                <a href="allmenu?option=${category.idCategory}">
                    <div class="category-item">${category.nameCategory}</div>
                </a>
            </c:forEach>
        </div>
    </div>


    <div id="content_section" class="content_section">

        <c:if test="${empty list}">
            <h3>Không có món ăn phù hợp!</h3>
        </c:if>
        <c:forEach var="food" items="${list}">
            <div class="card" onclick="showPopup('${food.idFood}')">
                <img src="${food.img}" alt="${food.foodName}"/>
                <div class="card_content">
                    <h3>${food.foodName}</h3>
                    <p>${food.price}đ</p>
                    <c:url value="addtoCart?foodID=${food.idFood}" var="addtoCart"/>
                    <a class="btn" href="${addtoCart}">
                        Thêm vào giỏ hàng
                    </a>
                </div>
            </div>

            <!-- Popup chi tiết món ăn -->
            <div id="${food.idFood}" class="popup">
                <div class="popup-content">
                    <img src="${food.img}" alt="${food.foodName}"/>
                    <h3>${food.foodName}</h3>
                    <p>Giá: ${food.price}đ</p>
                    <span>
                            ${food.description}
                    </span>
                    <button class="button-cart">

                        <a href="${addtoCart}">
                            Thêm vào giỏ hàng
                        </a>
                    </button>
                </div>
                <span class="close" onclick="closePopup('${food.idFood}')">&times;</span>
            </div>
        </c:forEach>
        <div class="pagination" style="width:1200px;margin:0px auto; padding-left:35px ;  text-align:center;">
            <c:if test="${currentPage > 1}">
                <a href="search?text=${param.text}&page=${currentPage - 1}"><</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="search?text=${param.text}&page=${i}" class="${currentPage == i ? 'active' : ''}">${i}</a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="search?text=${param.text}&page=${currentPage + 1}">></a>
            </c:if>
        </div>
    </div>


</div>
<!-- Xử lý footer -->
<jsp:include page="footer.jsp"></jsp:include>
<script>
    document.getElementById("deadbuton").addEventListener("click", function (event) {
        event.preventDefault();
    });
</script>
<script src="${pageContext.request.contextPath}/js/menu.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/module_search_ajax.js"></script>
</body>
</html>
