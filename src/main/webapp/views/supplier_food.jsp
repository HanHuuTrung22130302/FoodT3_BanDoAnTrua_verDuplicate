<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Quản lý thực phẩm</title>
  <link href='${pageContext.request.contextPath}/Images/LOGO_V2.png' rel='icon' type='image/x-icon'/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/suppliers.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
</head>

<body>
<div class="container">

  <jsp:include page="leftAdmin.jsp"></jsp:include>

  <div class="content">
    <div class="header">
      <form action="" method="get" id="searchForm">
        <input value="${search}" name="text" type="text" placeholder="Tìm kiếm theo tên, số điện thoại hoặc email..."/>
        <button type="submit">
          <i class="fa-solid fa-search"></i>
        </button>

      </form>
    </div>


    <div id="popup" class="popup hidden">
      <div class="popup_content">
        <span class="close_btn"><i class="fa-solid fa-xmark"></i></span>
        <h2>CHI TIẾT NHÀ CUNG CẤP</h2>
        <div id="popup_details"></div>
      </div>
    </div>

    <table>
      <thead>
      <tr>
        <th>STT</th>
        <th>TÊN NHÀ CUNG CẤP</th>
        <th>HÀNG NHẬP</th>
        <th>SỐ LƯỢNG NHẬP</th>
        <th>GIÁ NHẬP</th>
        <th>NGÀY NHẬP HÀNG</th>
        <th>NGÀY HẾT HÀNG</th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="supplier" items="${ingredientsList}">
        <tr>
          <td>${supplier.ingredientId}</td>
          <td>${supplier.supplierName}</td>
          <td>${supplier.ingredientName}</td>
          <td>${supplier.amount} kg</td>
          <td>${supplier.price} triệu</td>
          <td>${supplier.importDate}</td>
          <td>${supplier.expirationDate}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>