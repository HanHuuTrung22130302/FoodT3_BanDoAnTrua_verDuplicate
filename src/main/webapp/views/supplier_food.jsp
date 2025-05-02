<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Quản lý thực phẩm</title>
  <link href='${pageContext.request.contextPath}/Images/LOGO_V2.png' rel='icon' type='image/x-icon'/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/suppliers_food.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
  <script>
    // Sử dụng JSON đã được chuyển đổi trong servlet
    var ingredientsBySupplier = ${ingredientsBySupplierJson};
  </script>
</head>

<body>
<div class="container">

  <jsp:include page="leftAdmin.jsp"></jsp:include>

  <div class="content">
    <div class="header">
      <form action="" method="get" id="searchForm">
        <input value="${search}" name="text" type="text" placeholder="Tìm kiếm theo tên, số điện thoại hoặc email..."/>
        <select name="filter" onchange="this.form.submit()">
          <option value="all" ${param.filter == 'all' || param.filter == null ? 'selected' : ''}>Tất cả</option>
          <option value="nearlyExpired" ${param.filter == 'nearlyExpired' ? 'selected' : ''}>Sắp hết hạn</option>
        </select>
        <button type="submit">
          <i class="fa-solid fa-search"></i>
        </button>
        <button type="button" id="openImportPopup">Nhập hàng</button>
      </form>
    </div>

    <div id="importPopup" class="popup hidden">
      <div class="popup_content">
        <span class="close_import_btn"><i class="fa-solid fa-xmark"></i></span>
        <h2>Nhập Hàng Mới</h2>

        <form id="importForm" method="post" action="ImportIngredientController">
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
<script>
  function updateIngredients() {
    const supplierId = document.getElementById("supplierSelect").value;
    console.log("Supplier ID gửi đi:", supplierId);
    const select = document.getElementById("ingredientSelect");
    select.innerHTML = '<option value="">Chọn nguyên liệu</option>';

    if (!supplierId) {
      select.innerHTML = '<option value="">Vui lòng chọn nhà cung cấp trước</option>';
      return;
    }

    const ingredients = ingredientsBySupplier[supplierId] || [];
    console.log("Dữ liệu nguyên liệu cho supplierId " + supplierId + ": ", ingredients);
    if (ingredients.length === 0) {
      console.log("Không có nguyên liệu nào cho supplierId: " + supplierId);
    }
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

  window.onload = function() {
    updateIngredients();
  };
</script>
</html>