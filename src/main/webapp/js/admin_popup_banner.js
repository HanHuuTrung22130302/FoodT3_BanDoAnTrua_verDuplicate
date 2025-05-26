document.addEventListener("DOMContentLoaded", function () {
  // Xử lý popup
  const addBannerBtn = document.querySelector(".add-banner");
  const popup = document.getElementById("popup");
  const closeBtn = document.querySelector(".close_btn");

  if (addBannerBtn) {
    addBannerBtn.addEventListener("click", function () {
      popup.classList.remove("hidden");
    });
  }

  if (closeBtn) {
    closeBtn.addEventListener("click", function () {
      popup.classList.add("hidden");
    });
  }

  // Xử lý thêm banner mới
  const addBannerForm = document.getElementById("new_item_form");
  if (addBannerForm) {
    addBannerForm.addEventListener("submit", function (e) {
      e.preventDefault();
      const formData = new FormData(this);

      fetch("banner", {
        method: "POST",
        body: formData,
      })
        .then((response) => response.json())
        .then((data) => {
          if (data.success) {
            // Thêm banner mới vào bảng
            const tbody = document.querySelector("table tbody");
            const newRow = document.createElement("tr");
            newRow.setAttribute("data-banner-id", data.bannerId);
            newRow.innerHTML = `
                            <td>${tbody.children.length + 1}</td>
                            <td><img src="${data.url}"/></td>
                            <td>
                                <button class="delete" onclick="deleteBanner(${
                                  data.bannerId
                                })">
                                    <i class="fas fa-trash"></i> Xóa
                                </button>
                            </td>
                            <td>${data.date}</td>
                        `;
            tbody.appendChild(newRow);

            // Đóng popup và hiển thị thông báo
            popup.classList.add("hidden");
            showNotification("Thêm banner thành công!", "success");
          } else {
            showNotification("Thêm banner thất bại!", "error");
          }
        })
        .catch((error) => {
          console.error("Error:", error);
          showNotification("Có lỗi xảy ra!", "error");
        });
    });
  }
});

// Xử lý xóa banner
function deleteBanner(id) {
  if (confirm("Bạn có chắc chắn muốn xóa banner này?")) {
    fetch("banner", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: `action=delete&id=${id}`,
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          // Xóa dòng khỏi bảng
          const row = document.querySelector(`tr[data-banner-id="${id}"]`);
          if (row) {
            row.remove();
            // Cập nhật lại số thứ tự
            const rows = document.querySelectorAll("table tbody tr");
            rows.forEach((row, index) => {
              row.cells[0].textContent = index + 1;
            });
          }
          showNotification("Xóa banner thành công!", "success");
        } else {
          showNotification("Xóa banner thất bại!", "error");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        showNotification("Có lỗi xảy ra!", "error");
      });
  }
}

// Hàm hiển thị thông báo
function showNotification(message, type) {
  const notification = document.createElement("div");
  notification.className = `notification ${type}`;
  notification.textContent = message;
  document.body.appendChild(notification);

  // Hiển thị thông báo
  setTimeout(() => {
    notification.classList.add("show");
  }, 100);

  // Tự động ẩn sau 3 giây
  setTimeout(() => {
    notification.classList.remove("show");
    setTimeout(() => {
      notification.remove();
    }, 300);
  }, 3000);
}
