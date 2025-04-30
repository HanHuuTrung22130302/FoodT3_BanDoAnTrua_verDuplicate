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

  attachEventListeners();
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
      // Giữ lại giá trị hiện tại nếu không có giá trị mới
      document.getElementById("items_name").value =
        food.foodName || document.getElementById("items_name").value;
      document.getElementById("items_category").value =
        food.categoryId || document.getElementById("items_category").value;
      document.getElementById("items_price").value =
        food.price || document.getElementById("items_price").value;
      document.getElementById("items_details").value =
        food.description || document.getElementById("items_details").value;
      document.getElementById("items_ingredients").value =
        food.ingredients || document.getElementById("items_ingredients").value;
    });
}

// Hàm xử lý AJAX cho tất cả các chức năng
async function handleFilter(option, page = 1, search = "", categoryId = "") {
  try {
    const response = await fetch(
      `foodservice?isAjax=true&option=${option}&page=${page}&text=${search}&categoryId=${categoryId}`
    );
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    const data = await response.json();

    // Cập nhật danh sách món ăn
    updateFoodList(data.foods);

    // Cập nhật phân trang
    updatePagination(
      data.currentPage,
      data.totalPages,
      option,
      search,
      categoryId
    );

    // Cập nhật các select box
    updateSelectBoxes(data.currentCategory, data.currentCategoryId);
  } catch (error) {
    console.error("Error:", error);
    showNotification("Có lỗi xảy ra khi tải dữ liệu!", "error");
  }
}

// Hàm cập nhật danh sách món ăn
function updateFoodList(foods) {
  const menuContainer = document.querySelector(".menu_container");
  menuContainer.innerHTML = "";

  if (!foods || foods.length === 0) {
    // Thêm thông báo không có dữ liệu
    const noDataMessage = `
            <div class="no-data-message">
                <div class="no-data-content">
                    <i class="fas fa-search"></i>
                    <h3>Không tìm thấy món ăn phù hợp</h3>
                    <p>Vui lòng thử lại với từ khóa hoặc bộ lọc khác</p>
                </div>
            </div>
        `;
    menuContainer.innerHTML = noDataMessage;
    return;
  }

  foods.forEach((food) => {
    const foodItem = createFoodItem(food);
    menuContainer.appendChild(foodItem);
  });
}

// Hàm tạo HTML cho một món ăn
function createFoodItem(food) {
  const div = document.createElement("div");
  div.className = "menu-item";
  div.setAttribute("data-food-id", food.foodId);

  div.innerHTML = `
        <img alt="${food.foodName}" height="100" src="${food.image}"/>
        <div class="details">
            <h3>${food.foodName}</h3>
            <p>${food.description}</p>
            <button>${getCategoryName(food.categoryId)}</button>
        </div>
        <div class="price">${food.price}</div>
        <div class="actions">
            <button type="button" class="update_item_btn" onclick="openUpdatePopup('${
              food.foodId
            }')">
                <i class="fas fa-edit"></i>
            </button>
            <button type="button" class="delete_item_btn" onclick="deleteFood('${
              food.foodId
            }')">
                <i class="fas fa-trash"></i>
            </button>
        </div>
    `;

  return div;
}

// Hàm cập nhật phân trang
function updatePagination(currentPage, totalPages, option, search, categoryId) {
  const pagiContainer = document.querySelector(".pagi");
  let paginationHTML = "";

  if (currentPage > 1) {
    paginationHTML += `<a href="#" data-page="${currentPage - 1}">&lt;</a>`;
  }

  for (let i = 1; i <= totalPages; i++) {
    paginationHTML += `<a href="#" data-page="${i}" class="${
      currentPage === i ? "active" : ""
    }">${i}</a>`;
  }

  if (currentPage < totalPages) {
    paginationHTML += `<a href="#" data-page="${currentPage + 1}">&gt;</a>`;
  }

  pagiContainer.innerHTML = paginationHTML;

  // Gắn sự kiện cho các nút phân trang
  pagiContainer.querySelectorAll("a").forEach((link) => {
    link.addEventListener("click", (e) => {
      e.preventDefault();
      const page = e.target.getAttribute("data-page");
      handleFilter(option, page, search, categoryId);
    });
  });
}

// Hàm cập nhật select box
function updateSelectBoxes(currentOption, currentCategoryId) {
  const menuFilter = document.querySelector("#menu_filter");
  const specialFilter = document.querySelector("#special_filter");

  menuFilter.value = `foodservice?option=${currentCategoryId}`;
  specialFilter.value = `foodservice?option=${currentOption}`;
}

// Hàm lấy tên danh mục
function getCategoryName(categoryId) {
  const category = document.querySelector(
    `#menu_filter option[value="foodservice?option=${categoryId}"]`
  );
  return category ? category.textContent : "";
}

// Hàm xử lý tìm kiếm thời gian thực
let searchTimeout;

function handleSearch(event) {
  event.preventDefault();
  clearTimeout(searchTimeout);

  const searchText = document.querySelector('input[name="text"]').value;
  const currentOption = document
    .querySelector("#special_filter")
    .value.split("=")[1];
  const currentCategory = document
    .querySelector("#menu_filter")
    .value.split("=")[1];

  searchTimeout = setTimeout(() => {
    handleFilter(currentOption, 1, searchText, currentCategory);
  }, 300);
}

// Hàm xử lý lọc theo danh mục
function handleCategoryFilter(event) {
  const categoryId = event.target.value.split("=")[1];
  const currentOption = document
    .querySelector("#special_filter")
    .value.split("=")[1];
  const searchText = document.querySelector('input[name="text"]').value;
  handleFilter(currentOption, 1, searchText, categoryId);
}

// Hàm xử lý lọc đặc biệt
function handleSpecialFilter(event) {
  const option = event.target.value.split("=")[1];
  const currentCategory = document
    .querySelector("#menu_filter")
    .value.split("=")[1];
  const searchText = document.querySelector('input[name="text"]').value;
  handleFilter(option, 1, searchText, currentCategory);
}

// Hàm gắn các sự kiện
function attachEventListeners() {
  // Form tìm kiếm
  const searchForm = document.querySelector('form[action="foodservice"]');
  const searchInput = document.querySelector('input[name="text"]');

  searchInput.addEventListener("input", handleSearch);
  searchForm.addEventListener("submit", (e) => {
    e.preventDefault();
    handleSearch(e);
  });

  // Select lọc danh mục
  document
    .querySelector("#menu_filter")
    .addEventListener("change", handleCategoryFilter);

  // Select lọc đặc biệt
  document
    .querySelector("#special_filter")
    .addEventListener("change", handleSpecialFilter);
}
