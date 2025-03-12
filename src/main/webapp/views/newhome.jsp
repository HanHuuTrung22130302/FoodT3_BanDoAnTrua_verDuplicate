<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Food T^3</title>
    <link href='Images/LOGO_V2.png' rel='icon' type='image/x-icon'/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/newhome.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/module_home_n.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
    <script src="${pageContext.request.contextPath}/js/module_dangnhap.js" defer></script>
    <script src="${pageContext.request.contextPath}/js/module_dangnhap.js"></script>
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
                                <c:if test="${sessionScope.currentUser.idRole == 1}">
                                    <a href="admin" id="admin-link">Quản trị</a>
                                </c:if>
                                <c:if test="${sessionScope.currentUser.idRole == 2}">
                                    <a href="AccDetail" id="user-link">Thông tin</a>
<%--                                    <a href="user?idAcc=${sessionScope.currentUser.idAcc}" id="user-link">Thông tin</a>--%>
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
                    <form action="search" method="get">
                        <input value="${txtS}" name="text" type="text" placeholder="Tìm kiếm món ăn"/>
                        <button type="submit">
                            <i class="fa-solid fa-search"></i>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="container">
    <div class="w3-content w3-display-container">
        <c:forEach var ="bann" items="${bans}" >
            <img class="mySlides" src="${pageContext.request.contextPath}/${bann.url}" style="width:100%">
        </c:forEach>
    </div>

    <button class="w3-button w3-black left" onclick="plusDivs(-1)">&#10094;</button>
    <button class="w3-button w3-black right" onclick="plusDivs(1)">&#10095;</button>

    <div class="dot-container">
        <c:forEach var ="cd" items="${countDot}" >
            <span class="dot" onclick="currentSlide(${cd})"></span>
        </c:forEach>
    </div>
</div>
<div id="container-monban">
    <div class="header-mon">
        <div class="tag">Món Bán Chạy</div>
    </div>
    <div id="content_section lst4Sold" class="content_section">
        <c:forEach var="food" items="${lst4Sold}">
            <div class="card" onclick="showPopup('${food.idFood}');getU('${food.idFood}')">
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
            <div id="${food.idFood}" class="popup">
                <div class="popup-content">
                    <img src="${food.img}" alt="${food.foodName}"/>
                    <h3>${food.foodName}</h3>
                    <p>Giá: ${food.price}đ</p>
                    <span>
                            ${food.description}
                    </span>
                    <button class="button-cart">

                        <a class="linktocart"  href="${addtoCart}">
                            Thêm vào giỏ hàng
                        </a>
                    </button>
                </div>
                <span class="close" onclick="closePopup('${food.idFood}')">&times;</span>
            </div>
        </c:forEach>
    </div>
    <div class="xemtatca">
        <a href="allmenu?option=banchay">Xem Tất Cả</a>
    </div>
</div>
<!-- Mon duoc quan tam-->
<div id="container-monban">
    <div class="header-mon">
        <div class="tag">Món được quan tâm nhiều</div>
    </div>
    <div id="content_section lst4View" class="content_section">
        <c:forEach var="food" items="${lst4View}">
            <div class="card" onclick="showPopup('${food.idFood}');getU('${food.idFood}')">
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
            <div id="${food.idFood}" class="popup">
                <div class="popup-content">
                    <img src="${food.img}" alt="${food.foodName}"/>
                    <h3>${food.foodName}</h3>
                    <p>Giá: ${food.price}đ</p>
                    <span>
                            ${food.description}
                    </span>
                    <button class="button-cart">

                        <a class="linktocart"  href="${addtoCart}">
                            Thêm vào giỏ hàng
                        </a>
                    </button>
                </div>
                <span class="close" onclick="closePopup('${food.idFood}')">&times;</span>
            </div>
        </c:forEach>
    </div>
    <div class="xemtatca">
        <a href="allmenu?option=quantam">Xem Tất Cả</a>
    </div>
</div>

<div id="container-monban">
    <div class="header-mon">
        <div class="tag">Món được đè xuất</div>
    </div>
    <div id="content_section lst4Propose" class="content_section">
        <c:forEach var="food" items="${lst4Propose}">
            <div class="card" onclick="showPopup('${food.idFood}');getU('${food.idFood}')">
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
            <div id="${food.idFood}" class="popup">
                <div class="popup-content">
                    <img src="${food.img}" alt="${food.foodName}"/>
                    <h3>${food.foodName}</h3>
                    <p>Giá: ${food.price}đ</p>
                    <span>
                            ${food.description}
                    </span>
                    <button class="button-cart">

                        <a class="linktocart"  href="${addtoCart}">
                            Thêm vào giỏ hàng
                        </a>
                    </button>
                </div>
                <span class="close" onclick="closePopup('${food.idFood}')">&times;</span>
            </div>
        </c:forEach>
    </div>
    <div class="xemtatca">
        <a href="allmenu?option=dexuat">Xem Tất Cả</a>
    </div>
</div>

<div id="container-monban">
    <div class="header-mon">
        <div class="tag">Món được đánh giá cao</div>
    </div>
    <div id="content_section" class="content_section">
        <c:forEach var="food" items="${lst4Rate}">
            <div class="card" onclick="showPopup('${food.idFood}');getU('${food.idFood}')">
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
            <div id="${food.idFood}" class="popup">
                <div class="popup-content">
                    <img src="${food.img}" alt="${food.foodName}"/>
                    <h3>${food.foodName}</h3>
                    <p>Giá: ${food.price}đ</p>
                    <span>
                            ${food.description}
                    </span>
                    <button class="button-cart">

                        <a class="linktocart"  href="${addtoCart}">
                            Thêm vào giỏ hàng
                        </a>
                    </button>
                </div>
                <span class="close" onclick="closePopup('${food.idFood}')">&times;</span>
            </div>
        </c:forEach>
    </div>
    <div class="xemtatca">
        <a href="allmenu?option=danhgiacao">Xem Tất Cả</a>
    </div>
</div>

<div id="container-monban">
    <div class="header-mon">

        <div class="tag">Tại sao nên chọn Food T^3</div>

    </div>
    <div class="content_section_bottom">
        <div class="card_bottom">
            <img src="Images/home/pic1.jpg"/>
            <div class="text">Nguyên liệu tươi mới</div>
        </div>
        <div class="card_bottom">
            <img src="Images/home/pic2.jpg"/>
            <div class="text">Món ăn mgon miệng</div>
        </div>
        <div class="card_bottom">
            <img src="Images/home/pic3.jpg"/>
            <div class="text">Đóng gói tiện lợi</div>
        </div>
        <div class="card_bottom">
            <img src="Images/home/pic4.jpg"/>
            <div class="text">Giao hàng tận nơi</div>
        </div>

    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>

<script src="js/home.js">
</script>
<script src="js/module_popup_home.js"></script>
<script src="${pageContext.request.contextPath}/js/ViewU.js"></script>
</body>

</html>