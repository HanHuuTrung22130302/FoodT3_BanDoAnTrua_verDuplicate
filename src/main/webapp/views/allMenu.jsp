<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Menu các món cơm</title>
    <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/allmenu_n.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signinCssModule.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/module_submenu_catelory.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/module_home_n.css"/>
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
<div id="header">
    <div class="containerheaderAll">
        <div class="containers">
            <div class="containerss">
                <div class="left">
                    <div class="logo">
                        <a href="home">
                            <img src="Images/LOGO_V2.png" alt="Food store của Trung, Atuan, Atuan">
                        </a>
                    </div>
                </div>
                <div class="right">
                    <c:if test="${sessionScope.currentUser == null}">
                        <a href="login" class="nav-item" id="login-link">
                            <i class="fa-solid fa-user"></i> Đăng Nhập
                        </a>
                    </c:if>

                    <c:if test="${sessionScope.currentUser != null}">
                        <div class="user-menu" id="user-menu">
                            <i class="fa-solid fa-user"></i>
                            <a href="#" class=" user-name" id="user-name">
                                    ${sessionScope.currentUser.name}
                            </a>
                            <div class="submenu" id="submenu">
                                <c:if test="${sessionScope.currentUser.roleId == 1 || sessionScope.currentUser.roleId == 3}">
                                    <a href="admin" id="admin-link">Quản trị</a>
                                </c:if>
                                <c:if test="${sessionScope.currentUser.roleId == 2}">
                                    <a href="user" id="user-link">Thông tin</a>
                                </c:if>
                                <a href="logout" id="logout">Đăng xuất</a>
                            </div>
                        </div>
                    </c:if>
                    <div class="nav_item_shop">
                        <a href="<%= (session.getAttribute("currentUser") != null) ? "PurchaseOrder" : "login" %>"
                           class="nav-item">
                            <i class="fa-solid fa-truck-fast"></i> Đơn hàng
                        </a>

                        <a href="order" class="count">
                            <c:if test="${not empty sessionScope.totaldh}">
                                ${sessionScope.totaldh}
                            </c:if>
                            <c:if test="${empty sessionScope.totaldh}">
                                0
                            </c:if>
                        </a>
                    </div>
                    <div class="nav_item_shop">
                        <a href="cart" class="nav-item">
                            <i class="fa-solid fa-basket-shopping"></i> Giỏ hàng
                        </a>
                        <a href="cart" class="count">
                            <c:if test="${not empty sessionScope.totalItems}">
                                ${sessionScope.totalItems}
                            </c:if>
                            <c:if test="${empty sessionScope.totalItems}">
                                0
                            </c:if>
                        </a>
                    </div>
                </div>

            </div>
            <div class="bottom">
                <div class="menu">
                    <ul class="menu-list">

                        <li class="menu-item">
                            <a href="allmenu?option=tatca" class="tabbar"> <i class="fa-solid fa-bars"></i>Thực đơn</a>
                            <ul class="submenu">
                                <li><a href="allmenu?option=tatca"><i class="fa-solid fa-bowl-rice"></i>Tất cả</a>
                                </li>
                                <li><a href="allmenu?option=1"><i class="fa-solid fa-bowl-rice"></i>Món cơm</a>
                                </li>
                                <li><a href="allmenu?option=2"><i class="fa-solid fa-bowl-food"></i>Món bún</a>
                                </li>
                                <li><a href="allmenu?option=3"><i class="fa-solid fa-bowl-food"></i>Món phở</a>
                                </li>
                                <li><a href="allmenu?option=4"><i class="fa-solid fa-glass-water"></i>Nước</a>
                                </li>
                            </ul>
                        </li>
                        <li class="menu-item"><a href="home">Trang chủ</a></li>

                        <li class="menu-item"><a href="about">Giới thiệu</a></li>
                        <li class="menu-item"><a href="contactcontrolle">Liên hệ</a></li>
                    </ul>
                </div>
                <div class="search">
                    <form action="AjaxSearchController" method="get">
                        <input oninput="searchByName(this)" value="${txtS}" name="text" type="text"
                               placeholder="Tìm kiếm món ăn"/>
                        <button id="deadbuton" type="submit">
                            <i class="fa-solid fa-search"></i>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="container">
    <div class="intro">
        <h2>Khám Phá Thực Đơn Của Chúng Tôi</h2>
        <%--        <div class="category-container">--%>
        <%--            <a href="allmenu?option=tatca">--%>
        <%--                <div class="category-item status-button active">Tất cả</div>--%>
        <%--            </a>--%>
        <%--            <a href="allmenu?option=danhgiacao">--%>
        <%--                <div class="category-item status-button">Món được đánh giá cao</div>--%>
        <%--            </a>--%>
        <%--            <a href="allmenu?option=dexuat">--%>
        <%--                <div class="category-item status-button">Món được đề xuất</div>--%>
        <%--            </a>--%>
        <%--            <a href="allmenu?option=quantam">--%>
        <%--                <div class="category-item status-button">Món được quan tâm nhiều</div>--%>
        <%--            </a>--%>
        <%--            <a href="allmenu?option=banchay">--%>
        <%--                <div class="category-item status-button">Món bán chạy</div>--%>
        <%--            </a>--%>

        <%--            <c:forEach var="category" items="${listC}">--%>
        <%--                <a href="allmenu?option=${category.categoryId}">--%>
        <%--                    <div class="category-item status-button">${category.categoryName}</div>--%>
        <%--                </a>--%>
        <%--            </c:forEach>--%>
        <%--        </div>--%>

        <c:set var="currentOption" value="${param.option}"/>

        <div class="category-container">
            <div onclick="findCategory('tatca','1')">
                <div class="category-item status-button ${currentOption == 'tatca' || currentOption == null ? 'active' : ''}">
                    Tất cả
                </div>
            </div>
            <div onclick="findCategory('danhgiacao','1')">
                <div class="category-item status-button ${currentOption == 'danhgiacao' ? 'active' : ''}">Món được đánh
                    giá cao
                </div>
            </div>
            <div onclick="findCategory('dexuat','1')">
                <div class="category-item status-button ${currentOption == 'dexuat' ? 'active' : ''}">Món được đề xuất
                </div>
            </div>
            <div onclick="findCategory('quantam','1')">
                <div class="category-item status-button ${currentOption == 'quantam' ? 'active' : ''}">Món được quan tâm
                    nhiều
                </div>
            </div>
            <div onclick="findCategory('banchay','1')">
                <div class="category-item status-button ${currentOption == 'banchay' ? 'active' : ''}">Món bán chạy
                </div>
            </div>

            <c:forEach var="category" items="${listC}">
                <div onclick="findCategory('${category.categoryId}','1')">
                    <div class="category-item status-button ${currentOption == category.categoryId.toString() ? 'active' : ''}">
                            ${category.categoryName}
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>


    <div id="content_section">
        <div class="content_section">
            <c:forEach var="food" items="${list}">
                <div class="card"
                     onclick="showPopup('${food.foodId}');scrollToTop(${food.foodId});getU('${food.foodId}');ajaxGetReviewFID(${food.foodId},0)">
                    <img src="${food.image}" alt="${food.foodName}"/>
                    <div class="card_content">
                        <div class="nameFood">${food.foodName}</div>
                        <div class="priceFood">
                            <fmt:formatNumber value="${food.price}" type="number" groupingUsed="true"/>đ
                        </div>
                        <div class="card_footer">
                            <c:url value="addtoCart?foodID=${food.foodId}" var="addtoCart"/>
                            <a class="btn" onclick="event.stopPropagation()" href="${addtoCart}">
                                Thêm vào giỏ
                            </a>
                            <div class="reviewFood">
                                <div class="ratingFood">
                                    <i class="fas fa-star"></i>
                                    <span class="rating-value">${food.rating}</span>
                                </div>
                                <c:set var="soldValue" value="${food.sold}"/>
                                <div class="soldFood">
                                    <span class="sales-text">Đã bán</span>
                                    <span class="sales-value">
                                <c:choose>
                                    <c:when test="${soldValue >= 1000}">
                                        <fmt:formatNumber value="${soldValue / 1000}" maxFractionDigits="1"/>k
                                    </c:when>
                                    <c:otherwise>
                                        ${soldValue}
                                    </c:otherwise>
                                </c:choose>
                                </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Popup chi tiết món ăn -->
                <div id="${food.foodId}" class="popup">

                    <div class="popup-content">
                        <div class="close" onclick="scrollToTop(${food.foodId});closePopup('${food.foodId}');">&times;
                        </div>

                        <div class="popup-body">
                            <img src="${food.image}" alt="${food.foodName}"/>
                            <div class="containePopup">

                                <div class="nameAndSold">
                                    <div class="nameFoodPopup">${food.foodName}</div>
                                    <div class="ratingAndSold">
                                        <c:set var="soldValuePopup" value="${food.sold}"/>
                                        <div class="soldFoodPopup">
                                            <span class="sales-textPopup">Đã bán</span>
                                            <span class="sales-valuePopup">
                                <c:choose>
                                    <c:when test="${soldValue >= 1000}">
                                        <fmt:formatNumber value="${soldValue / 1000}" maxFractionDigits="1"/>k
                                    </c:when>
                                    <c:otherwise>
                                        ${soldValue}
                                    </c:otherwise>
                                </c:choose>
                                </span>
                                        </div>
                                        <div class="ratingFoodPopup">
                                            <i class="fas fa-star"></i>
                                            <span class="rating-valuePopup">${food.rating}</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="priceFoodPopup"><span style="color: black;font-size: 15px">Giá: </span>
                                    <fmt:formatNumber value="${food.price}" type="number" groupingUsed="true"/>đ
                                </div>
                                <div class="descriptionFoodPopup">${food.description}</div>

                                <div id="scrollbody${food.foodId}" class="danhgiasanpham">Đánh giá sản phẩm</div>
                                <div class="rating-filter">
                                    <button onclick="scrollToReviewList(${food.foodId});ajaxGetReviewFID(${food.foodId},0)">
                                        Tất cả
                                    </button>
                                    <button onclick="scrollToReviewList(${food.foodId});ajaxGetReviewFID(${food.foodId},5)">
                                        5⭐
                                    </button>
                                    <button onclick="scrollToReviewList(${food.foodId});ajaxGetReviewFID(${food.foodId},4)">
                                        4⭐
                                    </button>
                                    <button onclick="scrollToReviewList(${food.foodId});ajaxGetReviewFID(${food.foodId},3)">
                                        3⭐
                                    </button>
                                    <button onclick="scrollToReviewList(${food.foodId});ajaxGetReviewFID(${food.foodId},2)">
                                        2⭐
                                    </button>
                                    <button onclick="scrollToReviewList(${food.foodId});ajaxGetReviewFID(${food.foodId},1)">
                                        1⭐
                                    </button>
                                </div>
                                <div class="user-reviews">
                                    <div id="review-list${food.foodId}">
                                        <button class="next10cmt" onclick="ajaxGetReviewFID(${food.foodId})"></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <button class="scrollToTop" onclick="scrollToTop(${food.foodId})">^</button>
                        <div class="popup-footer">
                            <button class="button-cart">
                                <a class="linktocart" href="${addtoCart}">
                                    Thêm vào giỏ hàng
                                </a>
                            </button>
                        </div>
                    </div>


                </div>
            </c:forEach>
        </div>
        <div class="pagination" style="width:1200px;margin:0px auto; padding-left:35px; text-align:center;">
            <c:forEach begin="1" end="${totalPages}" var="i">
                <button onclick="loadSP('${param.option}', ${i})"
                        class="${currentPage == i ? 'active' : ''}">${i}</button>
            </c:forEach>
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
<script src="${pageContext.request.contextPath}/js/module_search_ajax.js"></script>
<script src="${pageContext.request.contextPath}/js/ViewU.js"></script>
<script src="${pageContext.request.contextPath}/js/test_module_load_ajax.js"></script>
<script src="${pageContext.request.contextPath}/js/menu.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/module_getReview_ajax.js"></script>
<script src="${pageContext.request.contextPath}/js/jsButtonActiveCategory.js"></script>
<script src="${pageContext.request.contextPath}/js/module_category_ajax.js"></script>
</body>
</html>
