<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Quản lý thực phẩm</title>
    <link rel="icon" href="${pageContext.request.contextPath}/Images/LOGO_V2.png" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/s.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
    <script>
        var ingredientsBySupplier = JSON.parse('${ingredientsBySupplierJson}');
    </script>
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
    </style>
</head>

<body>
<div class="container">
    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <!-- Popup thông báo -->
    <div id="notificationPopup" class="notification"></div>

    <div class="content">
        <div class="header">
            <form action="${pageContext.request.contextPath}/Ingredients" method="get" id="searchForm">
                <select name="filter" onchange="this.form.submit()">
                    <option value="all" ${param.filter == 'all' || param.filter == null ? 'selected' : ''}>Tất cả</option>
                    <option value="nearlyExpired" ${param.filter == 'nearlyExpired' ? 'selected' : ''}>Sắp hết hạn</option>
                </select>
                <input type="text" name="search" value="${param.search}" placeholder="Tìm kiếm theo id hàng, tên nhà cung cấp, tên hàng nhập..."/>
                <button type="submit"><i class="fa-solid fa-search"></i></button>
                <button type="button" id="openImportPopup">Nhập hàng</button>
            </form>
        </div>

        <!-- Popup nhập hàng -->
        <div id="importPopup" class="popup hidden">
            <div class="popup_content">
                <span class="close_import_btn"><i class="fa-solid fa-xmark"></i></span>
                <h2>Nhập Hàng Mới</h2>
                <form id="importForm" method="post"
                      action="${pageContext.request.contextPath}/ImportIngredientController">
                    <label>Chọn nhà cung cấp:</label>
                    <select id="supplierSelect" name="supplierId" required onchange="updateIngredients()">
                        <option value="">Chọn nhà cung cấp</option>
                        <c:forEach var="s" items="${supplierList}">
                            <option value="${s.supplierId}">${s.supplierName}</option>
                        </c:forEach>
                    </select>

                    <label>Chọn nguyên liệu:</label>
                    <select id="ingredientSelect" name="ingredientId" required>
                        <option value="">Vui lòng chọn nhà cung cấp trước</option>
                    </select>

                    <label>Số lượng (kg):</label>
                    <input type="number" name="amount" min="1" required/>

                    <label>Giá nhập (triệu):</label>
                    <input type="number" name="price" step="0.01" required/>

                    <label>Ngày nhập:</label>
                    <input type="date" name="importDate" required/>

                    <label>Ngày hết hạn:</label>
                    <input type="date" name="expirationDate" required/>

                    <button type="submit">Xác nhận nhập hàng</button>
                </form>
            </div>
        </div>

        <!-- Bảng dữ liệu nguyên liệu -->
        <table>
            <thead>
            <tr>
                <th>STT</th>
                <th>TÊN NHÀ CUNG CẤP</th>
                <th>HÀNG NHẬP</th>
                <th>SỐ LƯỢNG NHẬP</th>
                <th>GIÁ NHẬP</th>
                <th>NGÀY NHẬP HÀNG</th>
                <th>NGÀY HẾT HẠN</th>
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

<script>
    // Hiển thị popup thông báo
    function showNotification(message, type) {
        const popup = document.getElementById("notificationPopup");
        popup.textContent = message;
        popup.style.display = "block";
        popup.style.backgroundColor = (type === 'success') ? "green" : "red";

        setTimeout(() => {
            popup.style.display = "none";
        }, 3000);
    }

    function updateIngredients() {
        const supplierId = document.getElementById("supplierSelect").value;
        const select = document.getElementById("ingredientSelect");
        select.innerHTML = '<option value="">Chọn nguyên liệu</option>';

        if (!supplierId) {
            select.innerHTML = '<option value="">Vui lòng chọn nhà cung cấp trước</option>';
            return;
        }

        const ingredients = ingredientsBySupplier[supplierId] || [];
        ingredients.forEach(item => {
            const opt = document.createElement("option");
            opt.value = item.ingredientId;
            opt.textContent = item.ingredientName;
            select.appendChild(opt);
        });
    }

    document.getElementById("openImportPopup").addEventListener("click", () => {
        document.getElementById("importPopup").classList.remove("hidden");
    });

    document.querySelector(".close_import_btn").addEventListener("click", () => {
        document.getElementById("importPopup").classList.add("hidden");
    });

    window.onload = function () {
        updateIngredients();
        
        // Gọi thông báo nếu có param
        var successMsg = "${param.success}";
        var errorMsg = "${param.error}";
        
        if (successMsg) showNotification(successMsg, "success");
        if (errorMsg) showNotification("Lỗi: " + errorMsg, "error");
    };
</script>
</body>
</html>
