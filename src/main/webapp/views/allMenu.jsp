<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


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
                                <c:if test="${sessionScope.currentUser.roleId == 1}">
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
                        <a href="<%= (session.getAttribute("currentUser") != null) ? "PurchaseOrder" : "login" %>" class="nav-item">
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
                <a href="allmenu?option=${category.categoryId}">
                    <div class="category-item">${category.categoryName}</div>
                </a>
            </c:forEach>
        </div>
    </div>


    <div id="content_section">
        <div class="content_section">
        <c:forEach var="food" items="${list}">
            <div class="card" onclick="showPopup('${food.foodId}');getU('${food.foodId}')">
                <img src="${food.img}" alt="${food.foodName}"/>
                <div class="card_content">
                    <h3>${food.foodName}</h3>
                    <p>${food.price}đ</p>
                    <c:url value="addtoCart?foodID=${food.idFood}" var="addtoCart"/>
                    <a class="btn" onclick="event.stopPropagation()" href="${addtoCart}">
                        Thêm vào giỏ hàng
                    </a>
                </div>
            </div>

            <!-- Popup chi tiết món ăn -->
            <div id="${food.foodId}" class="popup">
                <div class="popup-content">
                    <img src="${food.image}" alt="${food.foodName}"/>
                    <h3>${food.foodName}</h3>
                    <p>Giá: ${food.price}đ</p>
                    <span>
                            ${food.description} <br>
                    </span>
                    <button class="button-cart">

                        <a class="linktocart" href="${addtoCart}" >
                            Thêm vào giỏ hàng
                        </a>
                    </button>
                </div>
                <span class="close" onclick="closePopup('${food.foodId}')">&times;</span>
            </div>
        </c:forEach>
        </div>
        <div class="pagination" style="width:1200px;margin:0px auto; padding-left:35px; text-align:center;">

            <c:forEach begin="1" end="${totalPages}" var="i">
                <button onclick="loadSP('${param.option}', ${i})" class="${currentPage == i ? 'active' : ''}">${i}</button>
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
</body>
</html>
