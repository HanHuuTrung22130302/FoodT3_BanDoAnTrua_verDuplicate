<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Quản lý nguyên liệu</title>
    <link rel="icon" href="${pageContext.request.contextPath}/Images/LOGO_V2.png" type="image/png"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/suppliers.css"/>
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
        .popup {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }
        .popup_content {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            width: 400px;
            position: relative;
        }
        .close_import_btn, .close_export_btn {
            position: absolute;
            top: 10px;
            right: 10px;
            cursor: pointer;
            font-size: 20px;
        }
        .popup_content h2 {
            margin-top: 0;
        }
        .popup_content label {
            display: block;
            margin: 10px 0 5px;
        }
        .popup_content input, .popup_content select {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .popup_content button {
            width: 100%;
            padding: 10px;
            background: #28a745;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .popup_content button:hover {
            background: #218838;
        }
        .hidden {
            display: none;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            table-layout: auto;
        }
        table th, table td {
            padding: 15px;
            text-align: left;
            border: 1px solid #ddd;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        table th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        table td {
            max-width: 200px;
        }
        .content {
            overflow-x: auto;
        }
        .pagination {
            margin-top: 20px;
            display: flex;
            justify-content: center;
            gap: 10px;
        }
        .pagination a {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            text-decoration: none;
            color: #333;
        }
        .pagination a:hover {
            background-color: #f2f2f2;
        }
        .pagination a.active {
            background-color: #b5292f;
            color: #fff;
            border-color: #b5292f;
        }
        .pagination a.disabled {
            color: #ccc;
            cursor: not-allowed;
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
                <button type="button" id="openExportPopup">Xuất hàng</button>
            </form>
        </div>

        <!-- Popup xuất hàng -->
        <div id="exportPopup" class="popup hidden">
            <div class="popup_content">
                <span class="close_export_btn"><i class="fa-solid fa-xmark"></i></span>
                <h2>Xuất Nguyên Liệu</h2>
                <form id="exportForm" method="post" action="${pageContext.request.contextPath}/ExportIngredientController">
                    <label>Chọn nguyên liệu:</label>
                    <select id="ingredientSelectExport" name="ingredientId" required>
                        <option value="">Chọn nguyên liệu</option>
                        <c:forEach var="ingredient" items="${ingredientsList}">
                            <option value="${ingredient.ingredientId}">${ingredient.ingredientName} (Còn: ${ingredient.amount} kg)</option>
                        </c:forEach>
                    </select>
                    <label>Số lượng xuất (kg):</label>
                    <input type="number" name="usedAmount" min="0.01" step="0.01" required/>
                    <label>Ngày xuất:</label>
                    <input type="date" name="usedDate" required/>
                    <button type="submit">Xác nhận xuất hàng</button>
                </form>
            </div>
        </div>

        <!-- Popup nhập hàng -->
        <div id="importPopup" class="popup hidden">
            <div class="popup_content">
                <span class="close_import_btn"><i class="fa-solid fa-xmark"></i></span>
                <h2>Nhập Hàng Mới</h2>
                <form id="importForm" method="post" action="${pageContext.request.contextPath}/ImportIngredientController">
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

        <!-- Phân trang -->
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/Ingredients?page=${currentPage - 1}&filter=${filter}&search=${search}">« Trước</a>
            </c:if>
            <c:if test="${currentPage <= 1}">
                <a class="disabled">« Trước</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage == i}">
                        <a class="active">${i}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/Ingredients?page=${i}&filter=${filter}&search=${search}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/Ingredients?page=${currentPage + 1}&filter=${filter}&search=${search}">Sau »</a>
            </c:if>
            <c:if test="${currentPage >= totalPages}">
                <a class="disabled">Sau »</a>
            </c:if>
        </div>
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
    document.getElementById("openExportPopup").addEventListener("click", () => {
        document.getElementById("exportPopup").classList.remove("hidden");
    });
    document.querySelector(".close_export_btn").addEventListener("click", () => {
        document.getElementById("exportPopup").classList.add("hidden");
    });
    window.onload = function () {
        updateIngredients();
        var successMsg = "${param.success}";
        var errorMsg = "${param.error}";
        if (successMsg) showNotification(successMsg, "success");
        if (errorMsg) showNotification("Lỗi: " + errorMsg, "error");
    };
</script>
</body>
</html>