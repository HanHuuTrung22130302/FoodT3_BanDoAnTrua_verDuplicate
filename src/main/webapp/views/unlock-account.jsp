<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"/>
  <title>Mở khóa tài khoản</title>
  <link href='Images/LOGO_V2.png' rel='icon' type='image/x-icon'/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset_pass.css"/>
</head>
<body>
<div class="container">
  <div class="verification-box">
    <h1>Mở khóa tài khoản</h1>
    <c:if test="${not empty error}">
      <p style="color: red;">${error}</p>
    </c:if>
    <c:if test="${not empty message}">
      <p style="color: green;">${message}</p>
    </c:if>
    <c:if test="${empty sessionScope.accountId}">
      <form action="unlock-account" method="post">
        <input type="hidden" name="action" value="request-otp"/>
        <input type="email" name="email" placeholder="Nhập email của bạn" required/>
        <button type="submit" class="btn">Gửi mã OTP</button>
      </form>
    </c:if>
    <c:if test="${not empty sessionScope.accountId}">
      <form action="unlock-account" method="post">
        <input type="hidden" name="action" value="verify-otp"/>
        <input type="text" name="otp" placeholder="Nhập mã OTP" required/>
        <button type="submit" class="btn">Xác nhận</button>
      </form>
    </c:if>
    <a href="login" class="back-link">Trở về trang đăng nhập</a>
  </div>
</div>
</body>
</html>