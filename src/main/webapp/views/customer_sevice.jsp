<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Admin Service</title>
    <link href="Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customer_manage.css"/>
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
            <form action="customersevice" method="get" id="searchForm">
                <select name="filterRole" id="filterRole">
                    <option value="user" ${selectedRole == 'user' ? 'selected' : ''}>Người dùng</option>
                    <option value="admin" ${selectedRole == 'admin' ? 'selected' : ''}>Admin</option>
                </select>
                <input value="${search}" name="text" type="text" placeholder="Tìm kiếm theo tên, số điện thoại hoặc email..."/>
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
                <th>LOẠI ĐĂNG NHẬP</th>
                <th>TRẠNG THÁI</th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="listAcc" items="${listAcc}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${listAcc.fullName != null ? listAcc.fullName : '<span style="color: red;">Chưa cập nhật Họ và Tên</span>'}</td>
                    <td>${listAcc.phoneNumber != null ? listAcc.phoneNumber : '<span style="color: red;">Chưa cập nhật SĐT</span>'}</td>
                    <td>${listAcc.email != "" ? listAcc.email : '<span style="color: red;">chưa cập nhật email</span>'}</td>
                    <td>
                        <c:choose>
                            <c:when test="${listAcc.loginType == 'normal'}">
                                <span style="color: blue;">Thông thường</span>
                            </c:when>
                            <c:when test="${listAcc.loginType == 'google'}">
                                <span style="color: green;">Google</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: gray;">${listAcc.loginType}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${listAcc.deleted}">
                                <span style="color: red;">Vô hiệu hóa</span>
                            </c:when>
                            <c:when test="${listAcc.locked}">
                                <span style="color: orange;">Đang chặn</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: green;">Hoạt động</span>
                            </c:otherwise>
                        </c:choose>
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
                    <td>
                        <div class="action-buttons">
                            <c:if test="${(currentUser.roleId == 3) || (currentUser.roleId == 1 && listAcc.roleId == 2)}">
                                <c:if test="${!listAcc.deleted}">
                                    <div class="dropdown">
                                        <button class="lock_btn" data-account-id="${listAcc.accountId}">
                                            <i class="fas fa-lock"></i> Chặn
                                        </button>
                                        <div class="dropdown-content">
                                            <button class="lock-option" data-hours="24">
                                                <i class="fas fa-clock"></i> 24 giờ
                                            </button>
                                            <button class="lock-option" data-hours="36">
                                                <i class="fas fa-clock"></i> 36 giờ
                                            </button>
                                            <button class="lock-option" data-hours="48">
                                                <i class="fas fa-clock"></i> 48 giờ
                                            </button>
                                            <div class="dropdown-divider"></div>
                                            <button class="unlock-btn" data-account-id="${listAcc.accountId}">
                                                <i class="fas fa-unlock"></i> Hủy chặn
                                            </button>
                                        </div>
                                    </div>
                                    <button class="delete" data-account-id="${listAcc.accountId}">
                                        <i class="fas fa-trash"></i> Vô hiệu hóa
                                    </button>
                                </c:if>
                                <c:if test="${listAcc.deleted}">
                                    <button class="activate" data-account-id="${listAcc.accountId}">
                                        <i class="fas fa-check"></i> Kích hoạt
                                    </button>
                                </c:if>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/admin_custom_management.js"></script>
</body>
</html>