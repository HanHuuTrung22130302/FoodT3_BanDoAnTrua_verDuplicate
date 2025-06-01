<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Thông tin khách hàng</title>
    <link
      href="${pageContext.request.contextPath}/Images/LOGO_V2.png"
      rel="icon"
      type="image/x-icon"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/module_header_footer.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/module_informationuser.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.32/dist/sweetalert2.min.css"
    />
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.32/dist/sweetalert2.all.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  </head>

  <body>
    <jsp:include page="header.jsp"></jsp:include>

    <div class="container">
      <div class="sidebar">
        <div class="namediv">Thông tin</div>
        <div class="linename"></div>
        <ul>
          <li>
            <a href="#" onclick="showContent('profile')">
              <i class="fas fa-id-card"></i>
              <div class="namemenu">Hồ Sơ</div>
            </a>
          </li>
          <li>
            <a href="PurchaseOrder">
              <i class="fas fa-shopping-cart"></i>
              <div class="namemenu">Đơn Hàng</div>
            </a>
          </li>
          <li>
            <a href="#" onclick="showContent('voucher')">
              <i class="fas fa-ticket-alt"></i>
              <div class="namemenu">Kho Voucher</div>
            </a>
          </li>
        </ul>
      </div>

      <div class="content">
        <div id="profile" class="content-section">
          <h2>Hồ Sơ Của Tôi</h2>
          <div class="linehead"></div>
          <form id="profileForm" onsubmit="return updateProfile(event)">
            <div class="form-group">
              <label>Tên đăng nhập</label>
              <input
                id="name"
                name="name"
                type="text"
                value="${sessionScope.currentUser.name}"
                readonly
              />
            </div>
            <div class="form-group">
              <label>Tên</label>
              <input
                id="fullName"
                name="fullName"
                type="text"
                value="${accDetail != null ? accDetail.fullName : ''}"
                required
              />
            </div>
            <div class="form-group">
              <label>Email</label>
              <input
                id="email"
                name="email"
                type="email"
                value="${sessionScope.currentUser.email}"
                readonly
              />
            </div>
            <div class="form-group">
              <label>Số điện thoại</label>
              <input
                id="phone"
                name="phoneNumber"
                type="text"
                value="${accDetail != null ? accDetail.phoneNumber : ''}"
                required
                pattern="[0-9]{10}"
                title="Vui lòng nhập số điện thoại 10 số"
              />
            </div>
            <div class="form-group">
              <label>Địa chỉ nhận hàng</label>
              <input
                id="address"
                name="address"
                type="text"
                value="${accDetail != null ? accDetail.address : ''}"
                required
              />
            </div>
            <div class="form-group">
              <label>Ngày sinh</label>
              <input
                id="birthdate"
                name="birthDate"
                type="date"
                value="${accDetail != null ? accDetail.birthDate : ''}"
                required
              />
            </div>
            <div class="form-group">
              <label>Giới tính</label>
              <div class="gender-picker">
                <input type="radio" id="male" name="gender" value="0"
                ${accDetail != null ? (accDetail.gender == 0 ? "checked" : "") :
                "checked"}>
                <label for="male">Nam</label>
                <input type="radio" id="female" name="gender" value="1"
                ${accDetail != null && accDetail.gender == 1 ? "checked" : ""}>
                <label for="female">Nữ</label>
                <input type="radio" id="other" name="gender" value="2"
                ${accDetail != null && accDetail.gender == 2 ? "checked" : ""}>
                <label for="other">Khác</label>
              </div>
            </div>
            <div class="form-group">
              <button type="submit" class="save-btn">
                <i class="fas fa-save"></i> Lưu thông tin
              </button>
            </div>
          </form>
        </div>

        <div id="voucher" class="content-section hidden">
          <h2>Kho Voucher</h2>
          <div class="linehead"></div>
          <p>Không có voucher nào!</p>
        </div>
      </div>
    </div>

    <jsp:include page="footer.jsp"></jsp:include>

    <script>
      function showContent(sectionId) {
        const sections = document.querySelectorAll(".content-section");
        sections.forEach((section) => section.classList.add("hidden"));
        document.getElementById(sectionId).classList.remove("hidden");
      }

      function updateProfile(event) {
        event.preventDefault();

        // Validate form trước khi gửi
        const form = document.getElementById("profileForm");
        if (!form.checkValidity()) {
          Swal.fire({
            icon: "error",
            title: "Lỗi!",
            text: "Vui lòng điền đầy đủ thông tin bắt buộc!",
          });
          return false;
        }

        // Kiểm tra số điện thoại
        const phone = document.getElementById("phone").value;
        const phoneRegex = /^[0-9]{10}$/;
        if (!phoneRegex.test(phone)) {
          Swal.fire({
            icon: "error",
            title: "Lỗi!",
            text: "Vui lòng nhập số điện thoại hợp lệ (10 số)",
          });
          return false;
        }

        // Nếu chưa chọn giới tính thì mặc định là Nam (0)
        if (!$('input[name="gender"]:checked').val()) {
          $("input#male").prop("checked", true);
        }

        $.ajax({
          url: "AccDetail",
          type: "POST",
          data: $("#profileForm").serialize(),
          dataType: "json",
          success: function (response) {
            if (response.success) {
              Swal.fire({
                icon: "success",
                title: "Thành công!",
                text: response.message,
                showConfirmButton: false,
                timer: 1500,
              });
            } else {
              Swal.fire({
                icon: "error",
                title: "Lỗi!",
                text:
                  response.message || "Có lỗi xảy ra khi cập nhật thông tin",
              });
            }
          },
          error: function (xhr, status, error) {
            console.error("Error:", error);
            console.log("Response:", xhr.responseText);
            Swal.fire({
              icon: "error",
              title: "Lỗi!",
              text: "Có lỗi xảy ra khi cập nhật thông tin. Vui lòng thử lại sau.",
            });
          },
        });

        return false;
      }

      // Thêm sự kiện validate cho form
      document
        .getElementById("profileForm")
        .addEventListener("submit", function (event) {
          const phone = document.getElementById("phone").value;
          const phoneRegex = /^[0-9]{10}$/;

          if (!phoneRegex.test(phone)) {
            event.preventDefault();
            Swal.fire({
              icon: "error",
              title: "Lỗi!",
              text: "Vui lòng nhập số điện thoại hợp lệ (10 số)",
            });
            return false;
          }
        });
    </script>
  </body>
</html>
