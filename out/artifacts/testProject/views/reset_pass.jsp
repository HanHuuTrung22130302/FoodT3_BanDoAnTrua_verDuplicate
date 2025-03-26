<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Đặt lại mật khẩu</title>
    <link href="Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset_pass.css"/>
</head>
<body>
<div class="container">
    <h1>Đặt lại mật khẩu</h1>
    <form action="resetpassword" method="post">
        <input type="email" class="form-control" value="${email}" name="email" id="email" placeholder="Email address"
               required/>
        <input type="password" class="form-control" value="" name="password" id="password"
               placeholder="Nhập mật khẩu mới" required/>
        <input type="password" class="form-control" value="" id="confirm_password" name="confirm_password"
               placeholder="Xác nhận mật khẩu mới" required/>
        <p class="text-danger">${mess}</p>
        <button class="btn" type="submit">Cập nhật mật khẩu</button>
    </form>
</div>
</body>
</html>
