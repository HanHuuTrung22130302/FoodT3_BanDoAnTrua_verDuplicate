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
          <select id="supplierSelect" name="supplierId" required>
            <option value="">-- Chọn nhà cung cấp --</option>
            <c:forEach var="s" items="${supplierList}">
              <option value="${s.supplierId}">${s.supplierName}</option>
            </c:forEach>
          </select>

          <label>Chọn nguyên liệu:</label>
          <select id="ingredientSelect" name="ingredientId" required>
            <option value="">-- Vui lòng chọn nhà cung cấp trước --</option>
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
  document.getElementById("openImportPopup").addEventListener("click", () => {
    document.getElementById("importPopup").classList.remove("hidden");
  });

  document.querySelector(".close_import_btn").addEventListener("click", () => {
    document.getElementById("importPopup").classList.add("hidden");
  });

  // AJAX load nguyên liệu theo nhà cung cấp
  document.getElementById("supplierSelect").addEventListener("change", function () {
    const supplierId = this.value;
    const ingredientSelect = document.getElementById("ingredientSelect");
    ingredientSelect.innerHTML = '<option>Đang tải...</option>';

    fetch(`getIngredientsBySupplier?supplierId=${supplierId}`)
            .then(res => res.json())
            .then(data => {
              let options = '<option value="">-- Chọn nguyên liệu --</option>';
              data.forEach(item => {
                options += `<option value="${item.ingredientId}">${item.ingredientName}</option>`;
              });
              ingredientSelect.innerHTML = options;
            })
            .catch(() => {
              ingredientSelect.innerHTML = '<option value="">Không có dữ liệu</option>';
            });
  });
</script>
</html>