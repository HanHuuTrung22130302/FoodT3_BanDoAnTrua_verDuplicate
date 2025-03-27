<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Admin Service</title>
    <link href="Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customer_service.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
</head>
<body>
<div class="container">

    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <div class="content">
        <div class="header">
            <form action="customersevice" method="get">
                <input value="${search}" name="text" type="text" placeholder="Tìm kiếm khách hàng..."/>
                <button type="submit">
                    <i class="fa-solid fa-search"></i>
                </button>
            </form>
        </div>

        <div id="popup" class="popup hidden">
            <div class="popup_content"
                 style="padding: 20px; border-radius: 10px; box-shadow: 0 0 15px rgba(0,0,0,0.2); background: #fff; max-width: 500px; margin: auto;">
            <span class="close_btn" style="float: right; cursor: pointer; font-size: 20px;">
              <i class="fa-solid fa-xmark"></i>
            </span>
                <h2 style="text-align: center;">CHI TIẾT KHÁCH HÀNG</h2>
                <div id="popup_details"></div>
            </div>
        </div>

        <table>
            <thead>
            <tr>
                <th>STT</th>

                <th>HỌ VÀ TÊN</th>

                <th>SỐ ĐIỆN THOẠI</th>

                <th>EMAIL</th>

                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="listAcc" items="${listAcc}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${listAcc.fullName != null ? listAcc.fullName : '<span style="color: red;">Chưa cập nhật Họ và Tên</span>'}</td>

                    <td>${listAcc.phoneNumber != null ? listAcc.phoneNumber : '<span style="color: red;">Chưa cập nhật SĐT</span>'}</td>

                    <td>
                            ${listAcc.email != "" ? listAcc.email : '<span style="color: red;">chưa cập nhật email</span>'}
                    </td>


                    <td>
                        <button class="detail_btn"
                                data-account='{"fullName":"${listAcc.fullName}","gender":
                            "${listAcc.gender}","birthDate":"${listAcc.birthDate}","address":
                            "${listAcc.address}","phoneNumber":"${listAcc.phoneNumber}","email":"${listAcc.email}"}'>
                            <i class="fas fa-eye"></i>
                            CHI TIẾT
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/cus_service.js"></script>
</body>
</html>