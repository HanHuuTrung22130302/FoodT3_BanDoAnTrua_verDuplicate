document.addEventListener("DOMContentLoaded", function () {
  // Xử lý popup
  const addCategoryBtn = document.querySelector(".add-category");
  const popup = document.getElementById("popup");
  const closeBtn = document.querySelector(".close_btn");
  const form = document.getElementById("new_item_form");

  addCategoryBtn.addEventListener("click", function () {
    popup.classList.remove("hidden");
  });

  closeBtn.addEventListener("click", function () {
    popup.classList.add("hidden");
  });

  // Xử lý form thêm category
  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const formData = new URLSearchParams();
    formData.append("action", "add");
    formData.append(
      "categoryName",
      document.getElementById("category_name").value
    );
    formData.append(
      "description",
      document.getElementById("category_description").value
    );

    fetch("category", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: formData,
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          const tbody = document.querySelector("table tbody");
          const newRow = document.createElement("tr");
          const currentRowCount = tbody.children.length;

          newRow.innerHTML = `
                <td>${currentRowCount + 1}</td>
                <td>${data.category.categoryName}</td>
                <td>
                    <form action="category" method="post" style="display: inline">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${
                          data.category.categoryId
                        }">
                        <button class="delete" type="button">
                            <i class="fas fa-trash"></i> Xóa
                        </button>
                    </form>
                </td>
            `;
          tbody.appendChild(newRow);
          popup.classList.add("hidden");
          form.reset();
          showNotification("Thêm danh mục thành công!", "success");
        } else {
          showNotification(data.message || "Thêm danh mục thất bại!", "error");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        showNotification("Có lỗi xảy ra!", "error");
      });
  });

  // Xử lý xóa category
  document.addEventListener("click", function (e) {
    if (e.target.closest(".delete")) {
      e.preventDefault();
      const form = e.target.closest("form");
      const categoryId = form.querySelector('input[name="id"]').value;

      if (confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
        fetch("category", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: `action=delete&id=${categoryId}`,
        })
          .then((response) => response.json())
          .then((data) => {
            if (data.success) {
              // Xóa dòng khỏi bảng
              const row = form.closest("tr");
              row.remove();
              showNotification("Xóa danh mục thành công!", "success");
            } else {
              showNotification(
                data.message || "Xóa danh mục thất bại!",
                "error"
              );
            }
          })
          .catch((error) => {
            console.error("Error:", error);
            showNotification("Có lỗi xảy ra!", "error");
          });
      }
    }
  });

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
