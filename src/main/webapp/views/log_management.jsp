<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Quản lý log</title>
  <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/log.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
</head>

<body>
<div class="container">
  <jsp:include page="leftAdmin.jsp"></jsp:include>

  <div class="content">
    <div class="header">
      <form action="LogManagement" method="get">
        <select name="filterRole">
          <option value="all">Tất cả vai trò</option>
          <option value="admin">Admin</option>
          <option value="user">Người dùng</option>
        </select>
        <input type="date" name="filterDate" placeholder="Chọn ngày"/>
        <input type="text" name="filterAction" placeholder="Tìm kiếm hành động"/>
        <button type="submit"><i class="fa-solid fa-search"></i></button>
      </form>
    </div>

    <table>
      <thead>
      <tr>
        <th>Thời gian</th>
        <th>ID Người dùng</th>
        <th>Vai trò</th>
        <th>Hành động</th>
        <th>Kết quả</th>
        <th>Chi tiết</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="log" items="${logs}">
        <tr>
          <td>${log.timestamp}</td>
          <td>${log.userId}</td>
          <td>${log.role}</td>
          <td>${log.action}</td>
          <td>${log.result}</td>
          <td>${log.details}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>
