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
      <form action="suppliers" method="post" id="searchForm">
        <input value="${search}" name="text" type="text" placeholder="Tìm kiếm theo tên, số điện thoại hoặc email..."/>
        <button type="submit"><i class="fa-solid fa-search"></i></button>
      </form>
      <button class="add-btn">Thêm nhà cung cấp</button>
    </div>

    <!-- Add Supplier Popup -->
    <div id="addPopup" class="popup hidden">
      <div class="popup_content">
        <span class="close_btn" onclick="closePopup('addPopup')"><i class="fa-solid fa-xmark"></i></span>
        <h2>THÊM NHÀ CUNG CẤP</h2>
        <form action="suppliers" method="post">
          <input type="hidden" name="action" value="add"/>
          <label>Tên nhà cung cấp:</label>
          <input type="text" name="supplierName" required/>
          <label>Địa chỉ:</label>
          <input type="text" name="address" required/>
          <label>Số điện thoại:</label>
          <input type="text" name="phone" required/>
          <label>Email:</label>
          <input type="email" name="email" required/>
          <label>Trạng thái:</label>
          <select name="status" required>
            <option value="1">Hoạt động</option>
            <option value="0">Ngừng hoạt động</option>
          </select>
          <button type="submit">Thêm</button>
        </form>
      </div>
    </div>

    <!-- Edit Supplier Popup -->
    <div id="editPopup" class="popup hidden">
      <div class="popup_content">
        <span class="close_btn" onclick="closePopup('editPopup')"><i class="fa-solid fa-xmark"></i></span>
        <h2>CHỈNH SỬA NHÀ CUNG CẤP</h2>
        <form action="suppliers" method="post">
          <input type="hidden" name="action" value="edit"/>
          <input type="hidden" name="supplierId" id="editSupplierId"/>
          <label>Tên nhà cung cấp:</label>
          <input type="text" name="supplierName" id="editSupplierName" required/>
          <label>Địa chỉ:</label>
          <input type="text" name="address" id="editAddress" required/>
          <label>Số điện thoại:</label>
          <input type="text" name="phone" id="editPhone" required/>
          <label>Email:</label>
          <input type="email" name="email" id="editEmail" required/>
          <label>Trạng thái:</label>
          <select name="status" id="editStatus" required>
            <option value="1">Hoạt động</option>
            <option value="0">Ngừng hoạt động</option>
          </select>
          <button type="submit">Cập nhật</button>
        </form>
      </div>
    </div>

    <!-- Delete Confirmation Popup -->
    <div id="deletePopup" class="popup hidden">
      <div class="popup_content">
        <span class="close_btn" onclick="closePopup('deletePopup')"><i class="fa-solid fa-xmark"></i></span>
        <h2>XÁC NHẬN XÓA</h2>
        <p>Bạn có chắc muốn xóa nhà cung cấp <span id="deleteSupplierName"></span>?</p>
        <form id="deleteForm">
          <input type="hidden" name="supplierId" id="deleteSupplierId"/>
          <button type="submit" class="delete-btn">Xóa</button>
          <button type="button" onclick="closePopup('deletePopup')">Hủy</button>
        </form>
      </div>
    </div>

    <!-- Details Popup -->
    <div id="detailsPopup" class="popup hidden">
      <div class="popup_content">
        <span class="close_btn" onclick="closePopup('detailsPopup')"><i class="fa-solid fa-xmark"></i></span>
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
        <th>THAO TÁC</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="supplier" items="${supplierList}" varStatus="status">
        <tr data-supplier-id="${supplier.supplierId}">
          <td>${status.index + 1}</td>
          <td>${supplier.supplierName}</td>
          <td>${supplier.address}</td>
          <td>${supplier.phone}</td>
          <td>${supplier.email}</td>
          <td>${supplier.status == 1 ? 'Hoạt động' : 'Ngừng hoạt động'}</td>
          <td>
            <button class="detail_btn"
                    data-supplier='{"supplierId":${supplier.supplierId},"supplierName":"${supplier.supplierName}",
                                "address":"${supplier.address}","phone":"${supplier.phone}",
                                "email":"${supplier.email}","status":"${supplier.status}"}'>
              <i class="fas fa-eye"></i> Chi tiết
            </button>
            <button class="edit_btn"
                    data-supplier='{"supplierId":${supplier.supplierId},"supplierName":"${supplier.supplierName}",
                                "address":"${supplier.address}","phone":"${supplier.phone}",
                                "email":"${supplier.email}","status":"${supplier.status}"}'>
              <i class="fas fa-edit"></i> Sửa
            </button>
            <button class="delete_btn"
                    data-supplier='{"supplierId":${supplier.supplierId},"supplierName":"${supplier.supplierName}"}'>
              <i class="fas fa-trash"></i> Xóa
            </button>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
<script>
  // Open add supplier popup
  document.querySelector('.add-btn').addEventListener('click', () => {
    document.getElementById('addPopup').classList.remove('hidden');
  });

  // Close popup
  function closePopup(id) {
    document.getElementById(id).classList.add('hidden');
  }

  // Handle details button
  document.querySelectorAll('.detail_btn').forEach(button => {
    button.addEventListener('click', () => {
      const supplierData = button.dataset.supplier;
      console.log('Raw supplier data:', supplierData); // Debug dữ liệu thô
      let supplier;
      try {
        supplier = JSON.parse(supplierData);
        console.log('Parsed supplier data:', supplier); // Debug dữ liệu sau khi parse
      } catch (e) {
        console.error('Error parsing JSON:', e);
        supplier = {};
      }

      // Lấy giá trị từ supplier và kiểm tra trước khi hiển thị
      const supplierName = supplier.supplierName || 'Không có dữ liệu';
      const address = supplier.address || 'Không có dữ liệu';
      const phone = supplier.phone || 'Không có dữ liệu';
      const email = supplier.email || 'Không có dữ liệu';
      const status = supplier.status === '1' ? 'Hoạt động' : supplier.status === '0' ? 'Ngừng hoạt động' : 'Không xác định';

      console.log('Values before rendering:', { supplierName, address, phone, email, status }); // Debug giá trị trước khi render

      // Tạo HTML string
      const htmlContent = [
        '<p><strong>Tên:</strong> ' + supplierName + '</p>',
        '<p><strong>Địa chỉ:</strong> ' + address + '</p>',
        '<p><strong>Số điện thoại:</strong> ' + phone + '</p>',
        '<p><strong>Email:</strong> ' + email + '</p>',
        '<p><strong>Trạng thái:</strong> ' + status + '</p>'
      ].join('');

      const detailsDiv = document.getElementById('popup_details');
      detailsDiv.innerHTML = htmlContent;
      document.getElementById('detailsPopup').classList.remove('hidden');
    });
  });

  // Handle edit button
  document.querySelectorAll('.edit_btn').forEach(button => {
    button.addEventListener('click', () => {
      const supplier = JSON.parse(button.dataset.supplier);
      document.getElementById('editSupplierId').value = supplier.supplierId || '';
      document.getElementById('editSupplierName').value = supplier.supplierName || '';
      document.getElementById('editAddress').value = supplier.address || '';
      document.getElementById('editPhone').value = supplier.phone || '';
      document.getElementById('editEmail').value = supplier.email || '';
      document.getElementById('editStatus').value = supplier.status || '1';
      document.getElementById('editPopup').classList.remove('hidden');
    });
  });

  // Handle delete button
  document.querySelectorAll('.delete_btn').forEach(button => {
    button.addEventListener('click', () => {
      const supplier = JSON.parse(button.dataset.supplier);
      document.getElementById('deleteSupplierId').value = supplier.supplierId || '';
      document.getElementById('deleteSupplierName').textContent = supplier.supplierName || 'Không xác định';
      document.getElementById('deletePopup').classList.remove('hidden');
    });
  });

  // Handle delete form submission with AJAX
  document.getElementById('deleteForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const supplierId = document.getElementById('deleteSupplierId').value;
    const contextPath = '${pageContext.request.contextPath}';
    const deleteUrl = contextPath + '/suppliers_delete';

    fetch(deleteUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: 'supplierId=' + encodeURIComponent(supplierId)
    })
            .then(response => {
              if (!response.ok) {
                throw new Error('HTTP status ' + response.status);
              }
              return response.json();
            })
            .then(data => {
              if (data.success) {
                const row = document.querySelector(`tr[data-supplier-id="${supplierId}"]`);
                if (row) {
                  row.style.display = 'none';
                }
                closePopup('deletePopup');
              } else {
                alert(data.error || 'Lỗi khi xóa nhà cung cấp');
              }
            })
            .catch(error => {
              console.error('Error:', error);
              alert('Lỗi khi xóa nhà cung cấp: ' + error.message);
            });
  });

  // Close popups when clicking X
  document.querySelectorAll('.close_btn').forEach(btn => {
    btn.addEventListener('click', () => {
      btn.closest('.popup').classList.add('hidden');
    });
  });
</script>
</body>
</html>