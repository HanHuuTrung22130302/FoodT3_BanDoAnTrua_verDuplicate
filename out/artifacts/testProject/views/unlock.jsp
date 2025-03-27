<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"/>
    <title>Mở khóa tài khoản</title>
    <link href='Images/LOGO_V2.png' rel='icon' type='image/x-icon'/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signin.css"/>
</head>
<body>
<div class="container" id="container">
    <div class="form-container sign-in-container">
        <form action="unlock" method="post">
            <input type="hidden" name="action" value="request"/>
            <a href="${pageContext.request.contextPath}/login" class="back-to-home">
                <i class="fas fa-arrow-left"></i>
            </a>
            <h1>Mở khóa tài khoản</h1>
            <span>Nhập tên đăng nhập để nhận mã OTP qua email</span>
            <input name="username" type="text" placeholder="Tên đăng nhập" required/>
            <div id="messageContainer" style="color: red; margin-top: 10px; text-align: center;">
                <c:if test="${not empty error}">${error}</c:if>
                <c:if test="${not empty message}">${message}</c:if>
            </div>
            <button type="submit">Gửi mã OTP</button>
        </form>
    </div>
    <div class="overlay-container">
        <div class="overlay">
            <div class="overlay-panel overlay-right">
                <h1>Cần trợ giúp?</h1>
                <p>Liên hệ chúng tôi nếu bạn gặp vấn đề khi mở khóa tài khoản!</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>