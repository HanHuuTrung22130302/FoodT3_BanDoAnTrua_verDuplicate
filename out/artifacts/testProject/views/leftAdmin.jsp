<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <div class="sidebar">
        <a href="admin">
            <img alt="admin" src="Images/LOGO_V2.png"/>
        </a>
        <h2>T^3 Restaurant</h2>
        <ul>
            <li>
                <a class="active" href="admin">
                    <i class="fas fa-home"> </i>
                    Trang tổng quan
                </a>
            </li>
            <li>
                <a href="foodservice">
                    <i class="fas fa-utensils"> </i>
                    Sản phẩm
                </a>
            </li>
            <li>
                <a href="banner">
                    <i class="fas fa-image">
                    </i>
                    Quản lý Banner
                </a>
            </li>
            <li>
                <a href="customersevice">
                    <i class="fas fa-users"> </i>
                    Khách hàng
                </a>
            </li>
            <li>
                <a href="ordermanagement">
                    <i class="fas fa-shopping-cart"> </i>
                    Đơn hàng
                </a>
            </li>
            <li>
                <a href="statistical">
                    <i class="fas fa-chart-bar"> </i>
                    Thống kê
                </a>
            </li>

            <li>
                <a href="discount">
                    <i class="fas fa-chart-bar"> </i>
                    Quản lý mã giảm giá
                </a>
            </li>

            <li>
                <a id="logout" href="logout">
                    <i class="fas fa-sign-out-alt"> </i>
                    Đăng xuất
                </a>
            </li>
        </ul>
    </div>