document.addEventListener("DOMContentLoaded", function () {
  // Xử lý popup
  const addDiscountBtn = document.querySelector(".add-banner");
  const popup = document.getElementById("popup");
  const closeBtn = document.querySelector(".close_btn");
  const form = document.getElementById("new_item_form");

  addDiscountBtn.addEventListener("click", function () {
    popup.classList.remove("hidden");
  });

  closeBtn.addEventListener("click", function () {
    popup.classList.add("hidden");
  });

  // Xử lý form thêm discount
  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const formData = new URLSearchParams();
    formData.append("action", "add");
    formData.append("codeName", document.getElementById("dis-code").value);
    formData.append("discountRate", document.getElementById("discount-rate").value);
    formData.append("title", document.getElementById("title").value);
    formData.append("description", document.getElementById("description").value);
    formData.append("startDate", document.getElementById("start-date").value);
    formData.append("endDate", document.getElementById("end-date").value);

    // Log dữ liệu gửi đi
    console.log("Dữ liệu gửi đi:", Object.fromEntries(formData));

    fetch("discount", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: formData,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        // Log dữ liệu nhận về
        console.log("Dữ liệu nhận về:", data);
        
        if (data.success) {
          const tbody = document.querySelector("table tbody");
          const newRow = document.createElement("tr");
          const currentRowCount = tbody.children.length;

          // Format số thứ tự
          const formattedIndex = String(currentRowCount + 1).padStart(3, '0');
          
          // Format tỉ lệ giảm giá
          const discountRate = (data.discount.discountRate * 100).toFixed(2);

          newRow.innerHTML = `
                <td>${formattedIndex}</td>
                <td>${data.discount.codeName}</td>
                <td>${discountRate}%</td>
                <td>${data.discount.title}</td>
                <td>${data.discount.description}</td>
                <td>${data.discount.startDate}</td>
                <td>${data.discount.endDate}</td>
                <td>
                    <form action="discount" method="post" style="display: inline">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${data.discount.discountCodeId}">
                        <button class="delete" type="button">
                            <i class="fas fa-trash"></i>
                        </button>
                    </form>
                </td>
            `;
          tbody.appendChild(newRow);
          popup.classList.add("hidden");
          form.reset();
          showNotification("Thêm mã giảm giá thành công!", "success");
        } else {
          showNotification(data.message || "Thêm mã giảm giá thất bại!", "error");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        showNotification("Có lỗi xảy ra: " + error.message, "error");
      });
  });

  // Xử lý xóa discount
  document.addEventListener("click", function (e) {
    if (e.target.closest(".delete")) {
      e.preventDefault();
      const form = e.target.closest("form");
      const discountId = form.querySelector('input[name="id"]').value;

      if (confirm("Bạn có chắc chắn muốn xóa mã giảm giá này?")) {
        const formData = new URLSearchParams();
        formData.append("action", "delete");
        formData.append("id", discountId);
        
        // Log dữ liệu gửi đi để debug
        console.log("Dữ liệu xóa gửi đi:", Object.fromEntries(formData));

        fetch("discount", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: formData,
        })
          .then((response) => {
            if (!response.ok) {
              throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
          })
          .then((data) => {
            // Log dữ liệu nhận về để debug
            console.log("Dữ liệu xóa nhận về:", data);
            
            if (data.success) {
              // Xóa dòng khỏi bảng
              const row = form.closest("tr");
              row.remove();
              showNotification("Xóa mã giảm giá thành công!", "success");
              
              // Cập nhật lại số thứ tự các hàng
              updateRowNumbers();
            } else {
              showNotification(data.message || "Xóa mã giảm giá thất bại!", "error");
            }
          })
          .catch((error) => {
            console.error("Error:", error);
            showNotification("Có lỗi xảy ra khi xóa: " + error.message, "error");
          });
      }
    }
  });
  
  // Cập nhật lại số thứ tự các hàng sau khi xóa
  function updateRowNumbers() {
    const rows = document.querySelectorAll("table tbody tr");
    rows.forEach((row, index) => {
      const idCell = row.cells[0];
      idCell.textContent = String(index + 1).padStart(3, '0');
    });
  }

  // Hàm hiển thị thông báo
  function showNotification(message, type) {
    const notification = document.createElement("div");
    notification.className = `notification ${type}`;
    notification.textContent = message;

    document.body.appendChild(notification);

    // Animation
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
});
