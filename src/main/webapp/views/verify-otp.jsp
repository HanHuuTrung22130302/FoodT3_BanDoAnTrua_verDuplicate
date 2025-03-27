<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"/>
    <title>Xác minh OTP</title>
    <link href='Images/LOGO_V2.png' rel='icon' type='image/x-icon'/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signin.css"/>
</head>
<body>
<div class="container" id="container">
    <div class="form-container sign-in-container">
        <form action="unlock" method="post">
            <input type="hidden" name="action" value="verify"/>
            <input type="hidden" name="username" value="${username}"/>
            <a href="${pageContext.request.contextPath}/login" class="back-to-home">
                <i class="fas fa-arrow-left"></i>
            </a>
            <h1>Xác minh OTP</h1>
            <span>Nhập mã OTP đã được gửi đến email của bạn</span>
            <input name="otp" type="text" placeholder="Mã OTP" required/>
            <div id="messageContainer" style="color: red; margin-top: 10px; text-align: center;">
                <c:if test="${not empty error}">${error}</c:if>
                <c:if test="${not empty message}">${message}</c:if>
            </div>
            <button type="submit">Xác minh</button>
            <a href="unlock?action=request">Gửi lại mã OTP</a>
        </form>
    </div>
    <div class="overlay-container">
        <div class="overlay">
            <div class="overlay-panel overlay-right">
                <h1>Xác minh nhanh</h1>
                <p>Mã OTP có hiệu lực trong 5 phút. Kiểm tra email của bạn!</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>