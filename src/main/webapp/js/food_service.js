// Lấy các popup và button
const addPopup = document.getElementById("add_popup");
const updatePopup = document.getElementById("update_popup");
const addButton = document.querySelector(".add_item_btn");
const updateButtons = document.querySelectorAll(".update_item_btn");
const deleteButtons = document.querySelectorAll(".delete_item_btn");
const closeButtons = document.querySelectorAll(".close_btn");

// Đợi DOM load đầy đủ mới gán sự kiện
document.addEventListener("DOMContentLoaded", () => {
  // Hiển thị popup thêm món
  addButton.addEventListener("click", () => {
    addPopup.classList.remove("hidden");
  });

  // Hiển thị popup cập nhật món
  updateButtons.forEach((button) => {
    button.addEventListener("click", () => {
      updatePopup.classList.remove("hidden");
    });
  });

  // Đóng popup
  closeButtons.forEach((button) => {
    button.addEventListener("click", () => {
      button.closest(".popup").classList.add("hidden");
    });
  });

  // Đóng popup khi click ra ngoài
  [addPopup, updatePopup].forEach((popup) => {
    popup.addEventListener("click", (event) => {
      if (event.target === popup) {
        popup.classList.add("hidden");
      }
    });
  });
});

// Xử lý form thêm món mới
document
  .getElementById("new_item_form")
  .addEventListener("submit", async function (e) {
    e.preventDefault();

    try {
      const formData = new FormData(this);
      formData.append("action", "add");

      const response = await fetch("foodservice", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const result = await response.json();
      console.log("Response from server:", result); // Debug log

      if (result.success) {
        // Hiển thị thông báo thành công
        showNotification("Thêm món thành công!", "success");

        // Đóng popup
        document.getElementById("add_popup").classList.add("hidden");

        // Reset form
        this.reset();

        // Lấy tên danh mục từ select box
        const categoryName = document.querySelector(
          `#item_category option[value="${result.categoryId}"]`
        ).textContent;

        // Tạo HTML cho món mới
        const newFoodHtml = `
                <div class="menu-item" data-food-id="${result.foodId}">
                    <img alt="${result.foodName}" height="100" src="${result.image}"/>
                    <div class="details">
                        <h3>${result.foodName}</h3>
                        <p>${result.description}</p>
                        <button>${categoryName}</button>
                    </div>
                    <div class="price">${result.price}</div>
                    <div class="actions">
                        <button type="button" class="update_item_btn" onclick="openUpdatePopup('${result.foodId}')">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="delete_item_btn" onclick="deleteFood('${result.foodId}')">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            `;

        // Thêm món mới vào đầu danh sách
        const menuContainer = document.querySelector(".menu_container");
        if (menuContainer) {
          menuContainer.insertAdjacentHTML("afterbegin", newFoodHtml);
        }
      } else {
        showNotification(
          result.message || "Có lỗi xảy ra khi thêm món!",
          "error"
        );
      }
    } catch (error) {
      console.error("Error:", error);
      showNotification("Có lỗi xảy ra khi thêm món!", "error");
    }
  });

// Xử lý form cập nhật món
document
  .getElementById("update_item_form")
  .addEventListener("submit", async function (e) {
    e.preventDefault();

    try {
      const formData = new FormData(this);
      formData.append("action", "update");
      const foodId = formData.get("idFood");

      const response = await fetch("foodservice", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const result = await response.json();
      console.log("Response from server:", result); // Debug log

      if (result.success) {
        // Hiển thị thông báo thành công
        showNotification("Cập nhật món thành công!", "success");

        // Đóng popup
        document.getElementById("update_popup").classList.add("hidden");

        // Cập nhật thông tin món trong DOM
        const foodItem = document.querySelector(`[data-food-id="${foodId}"]`);
        if (foodItem) {
          const categoryName = document.querySelector(
            `#items_category option[value="${result.categoryId}"]`
          ).textContent;

          foodItem.querySelector("img").src = result.image;
          foodItem.querySelector("img").alt = result.foodName;
          foodItem.querySelector("h3").textContent = result.foodName;
          foodItem.querySelector("p").textContent = result.description;
          foodItem.querySelector("button").textContent = categoryName;
          foodItem.querySelector(".price").textContent = result.price;
        }
      } else {
        showNotification(
          result.message || "Có lỗi xảy ra khi cập nhật món!",
          "error"
        );
      }
    } catch (error) {
      console.error("Error:", error);
      showNotification("Có lỗi xảy ra khi cập nhật món!", "error");
    }
  });

// Xử lý xóa món
function deleteFood(idFood) {
  if (confirm("Bạn có chắc chắn muốn xóa món này?")) {
    const formData = new FormData();
    formData.append("action", "delete");
    formData.append("idFood", idFood);

    fetch("foodservice", {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((result) => {
        if (result.success) {
          showNotification("Xóa món thành công!", "success");
          // Xóa phần tử khỏi DOM
          const foodItem = document.querySelector(`[data-food-id="${idFood}"]`);
          if (foodItem) {
            foodItem.remove();
          }
        } else {
          showNotification("Có lỗi xảy ra khi xóa món!", "error");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        showNotification("Có lỗi xảy ra khi xóa món!", "error");
      });
  }
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

// Xử lý mở popup cập nhật
function openUpdatePopup(idFood) {
  document.getElementById("update_popup").classList.remove("hidden");
  document.getElementById("update_idFood").value = idFood;

  // Lấy thông tin món ăn hiện tại
  fetch(`foodservice?idFood=${idFood}`)
    .then((response) => response.json())
    .then((food) => {
      document.getElementById("items_name").value = food.foodName;
      document.getElementById("items_category").value = food.categoryId;
      document.getElementById("items_price").value = food.price;
      document.getElementById("items_details").value = food.description;
      document.getElementById("items_ingredients").value = food.ingredients;
      document.getElementById("currentImage").value = food.image;
    });
}
