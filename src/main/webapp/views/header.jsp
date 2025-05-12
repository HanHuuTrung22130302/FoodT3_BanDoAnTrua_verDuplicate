<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                                    <a href="AccDetail" id="user-link">Thông tin</a>
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
                        <a href="cart" class="count" id="order-count">
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
                            <a href="allmenu?option=tatca" class="tabbar">
                                <i class="fa-solid fa-bars"></i>Thực đơn
                            </a>
                            <ul class="submenu">
                                <li>
                                    <a href="allmenu?option=tatca">
                                        <i class="fa-solid fa-bowl-rice"></i>Tất cả
                                    </a>
                                </li>

                                <c:forEach var="category" items="${listC}">
                                    <c:set var="iconClass" value="fa-solid fa-bowl-rice"/>

                                    <c:choose>
                                        <c:when test="${category.categoryName.contains('Bún') || category.categoryName.contains('Phở')}">
                                            <c:set var="iconClass" value="fa-solid fa-bowl-food"/>
                                        </c:when>
                                        <c:when test="${category.categoryName.contains('Nước')}">
                                            <c:set var="iconClass" value="fa-solid fa-glass-water"/>
                                        </c:when>
                                    </c:choose>

                                    <li>
                                        <a href="allmenu?option=${category.categoryId}">
                                            <i class="${iconClass}"></i>${category.categoryName}
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </li>

                        <li class="menu-item"><a href="home">Trang chủ</a></li>

                        <li class="menu-item"><a href="about">Giới thiệu</a></li>
                        <li class="menu-item"><a href="contact">Liên hệ</a></li>
                    </ul>
                </div>
                <div class="search">
                    <form id="searchForm" action="search" method="get">
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