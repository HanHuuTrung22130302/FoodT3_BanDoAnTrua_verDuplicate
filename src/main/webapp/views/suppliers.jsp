<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Quản lý nhà cung cấp</title>
  <link href='${pageContext.request.contextPath}/Images/LOGO_V2.png' rel='icon' type='image/x-icon'/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/suppliers.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
</head>

<body>
<div class="container">

  <jsp:include page="leftAdmin.jsp"></jsp:include>

  <div class="content">
    <div class="header">
      <form action="suppliers" method="get" id="searchForm">
        <input value="${search}" name="text" type="text" placeholder="Tìm kiếm theo tên, số điện thoại hoặc email..."/>
        <button type="submit">
          <i class="fa-solid fa-search"></i>
        </button>
      </form>
      <button class="import-btn">
        Nhập hàng
      </button>
    </div>

    <div id="importPopup" class="popup hidden">
      <div class="popup_content">
        <span class="close_btn" onclick="closePopup('importPopup')"><i class="fa-solid fa-xmark"></i></span>
        <h2>NHẬP HÀNG TỪ NHÀ CUNG CẤP</h2>
        <form action="importIngredient" method="post">
          <input type="hidden" name="supplierName" id="popupSupplier"/>
          <input type="hidden" name="ingredientName" id="popupIngredient"/>

          <label>Số lượng nhập (kg):</label>
          <input type="number" name="amount" required min="1"/>

          <label>Giá nhập (triệu):</label>
          <input type="number" step="0.01" name="price" required/>

          <label>Ngày nhập hàng:</label>
          <input type="date" name="importDate" required/>

          <label>Ngày hết hạn:</label>
          <input type="date" name="expirationDate" required/>

          <button type="submit">Xác nhận nhập hàng</button>
        </form>
      </div>
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
        <th>ĐỊA CHỈ</th>
        <th>SỐ ĐIỆN THOẠI</th>
        <th>EMAIL</th>
        <th>TRẠNG THÁI</th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="supplier" items="${supplierList}" varStatus="status">
        <tr>
          <td>${status.index + 1}</td>
          <td>${supplier.supplierName}</td>
          <td>${supplier.address}</td>
          <td>${supplier.phone}</td>
          <td>${supplier.email}</td>
          <td>${supplier.status == 1 ? 'Hoạt động' : 'Ngừng hoạt động'}</td>
          <td>
            <button class="detail_btn"
                    data-supplier='{"supplierName":"${supplier.supplierName}","address":"${supplier.address}",
                                "phone":"${supplier.phone}","email":"${supplier.email}","status":"${supplier.status}"}'>
              <i class="fas fa-eye"></i> CHI TIẾT
            </button>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
</body>
<script>
  document.querySelectorAll('.import-btn').forEach(button => {
    button.addEventListener('click', function () {
      const supplier = this.dataset.supplier;
      const ingredient = this.dataset.ingredient;

      document.getElementById('popupSupplier').value = supplier;
      document.getElementById('popupIngredient').value = ingredient;

      document.getElementById('importPopup').classList.remove('hidden');
    });
  });

  function closePopup(id) {
    document.getElementById(id).classList.add('hidden');
  }

  // Đóng popup khi nhấn dấu X
  document.querySelectorAll('.close_btn').forEach(btn => {
    btn.addEventListener('click', () => {
      btn.closest('.popup').classList.add('hidden');
    });
  });
</script>
</html>