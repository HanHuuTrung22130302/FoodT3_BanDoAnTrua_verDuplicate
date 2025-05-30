<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Lịch sử nhập/xuất nguyên liệu</title>
    <link rel="icon" href="${pageContext.request.contextPath}/Images/LOGO_V2.png" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/suppliers.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
    <style>
        .notification {
            position: fixed;
            top: 20px;
            right: 20px;
            color: #fff;
            padding: 15px 20px;
            border-radius: 8px;
            z-index: 9999;
            display: none;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table th, table td {
            padding: 15px;
            text-align: left;
            border: 1px solid #ddd;
        }
        table th {
            background-color: #f2f2f2;
        }
        .header {
            margin-bottom: 20px;
        }
        .header form {
            display: flex;
            gap: 10px;
        }
        .header select, .header input {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .header button {
            padding: 8px 15px;
            background: #28a745;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .header button:hover {
            background: #218838;
        }
    </style>
</head>
<body>
<div class="container">
    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <!-- Popup thông báo -->
    <div id="notificationPopup" class="notification"></div>

    <div class="content">
        <div class="header">
            <form action="${pageContext.request.contextPath}/IngredientHistory" method="get">
                <select name="type" onchange="this.form.submit()">
                    <option value="all" ${param.type == 'all' || param.type == null ? 'selected' : ''}>Tất cả</option>
                    <option value="import" ${param.type == 'import' ? 'selected' : ''}>Nhập hàng</option>
                    <option value="export" ${param.type == 'export' ? 'selected' : ''}>Xuất hàng</option>
                </select>
                <input type="text" name="search" value="${param.search}" placeholder="Tìm kiếm theo id, tên nguyên liệu, nhà cung cấp..."/>
                <button type="submit"><i class="fa-solid fa-search"></i></button>
            </form>
        </div>

        <!-- Bảng lịch sử -->
        <table>
            <thead>
            <tr>
                <th>STT</th>
                <th>LOẠI</th>
                <th>TÊN NGUYÊN LIỆU</th>
                <th>TÊN NHÀ CUNG CẤP</th>
                <th>SỐ LƯỢNG (kg)</th>
                <th>GIÁ (triệu)</th>
                <th>NGÀY</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${historyList}">
                <tr>
                    <td>${item.ingredientId}</td>
                    <td>${item.importDate != null ? 'Nhập' : 'Xuất'}</td>
                    <td>${item.ingredientName}</td>
                    <td>${item.supplierName}</td>
                    <td>${item.amount}</td>
                    <td>${item.price}</td>
                    <td>${item.importDate != null ? item.importDate : item.expirationDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    function showNotification(message, type) {
        const popup = document.getElementById("notificationPopup");
        popup.textContent = message;
        popup.style.display = "block";
        popup.style.backgroundColor = (type === 'success') ? "green" : "red";
        setTimeout(() => {
            popup.style.display = "none";
        }, 3000);
    }

    window.onload = function () {
        var successMsg = "${param.success}";
        var errorMsg = "${param.error}";
        if (successMsg) showNotification(successMsg, "success");
        if (errorMsg) showNotification("Lỗi: " + errorMsg, "error");
    };
</script>
</body>
</html>