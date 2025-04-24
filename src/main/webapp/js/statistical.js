function clearSearch() {
  const searchForm = document.getElementById("searchForm");
  const searchInput = searchForm.querySelector('input[name="text"]');
  searchInput.value = "";
  searchForm.submit();
}

function updateStatisticalData(data) {
  console.log("Updating UI with data:", data);
  // Cập nhật tổng quan
  document.querySelector(".card:nth-child(1) .number").textContent =
    data.totalProducts;
  document.querySelector(".card:nth-child(2) .number").textContent =
    data.totalQuantity;
  document.querySelector(".card:nth-child(3) .number").textContent =
    data.totalRevenue.toLocaleString() + " đ";
  document.querySelector(".card:nth-child(4) .number").textContent =
    data.totalOrders;

  // Cập nhật bảng sản phẩm bán chạy
  updateProductTable("bestSellingProducts", data.bestSellingProducts);

  // Cập nhật bảng sản phẩm bán chậm
  updateProductTable("slowSellingProducts", data.slowSellingProducts);

  // Cập nhật bảng sản phẩm chưa bán được
  updateUnsoldProductsTable(data.unsoldProducts);
}

function updateProductTable(tableId, products) {
  const tbody = document.querySelector(`#${tableId} tbody`);
  tbody.innerHTML = "";

  if (products.length === 0) {
    tbody.innerHTML =
      '<tr><td colspan="4" style="text-align: center">Không có dữ liệu</td></tr>';
    return;
  }

  products.forEach((product, index) => {
    const row = document.createElement("tr");
    row.innerHTML = `
            <td>${index + 1}</td>
            <td class="product_name">
                <img alt="${product.food.foodName}" height="50" src="${
      product.food.image
    }"/>
                ${product.food.foodName}
            </td>
            <td>${product.quantity}</td>
            <td>${product.totalAmount.toLocaleString()} đ</td>
        `;
    tbody.appendChild(row);
  });
}

function updateUnsoldProductsTable(products) {
  const tbody = document.querySelector("#unsoldProducts tbody");
  tbody.innerHTML = "";

  if (products.length === 0) {
    tbody.innerHTML =
      '<tr><td colspan="3" style="text-align: center">Không có dữ liệu</td></tr>';
    return;
  }

  products.forEach((product, index) => {
    const row = document.createElement("tr");
    row.innerHTML = `
            <td>${index + 1}</td>
            <td class="product_name">
                <img alt="${product.foodName}" height="50" src="${
      product.image
    }"/>
                ${product.foodName}
            </td>
            <td>${product.isDeleted == 0 ? "Đang bán" : "Ngừng bán"}</td>
        `;
    tbody.appendChild(row);
  });
}

// Xử lý sự kiện khi thay đổi bộ lọc thời gian
document.addEventListener("DOMContentLoaded", function () {
  const timeFilter = document.getElementById("timeFilter");
  console.log("timeFilter element:", timeFilter);

  if (timeFilter) {
    timeFilter.addEventListener("change", function () {
      console.log("timeFilter changed to:", this.value);
      const timeFilter = this.value;
      const searchText = document.querySelector('input[name="text"]').value;
      fetchStatisticalData(timeFilter, searchText);
    });
  } else {
    console.error("timeFilter element not found");
  }

  // Xử lý sự kiện input cho ô tìm kiếm
  const searchInput = document.querySelector('input[name="text"]');
  if (searchInput) {
    let searchTimeout;
    searchInput.addEventListener("input", function () {
      // Xóa timeout cũ nếu có
      if (searchTimeout) {
        clearTimeout(searchTimeout);
      }

      // Đặt timeout mới để tránh gửi quá nhiều request
      searchTimeout = setTimeout(() => {
        const timeFilter = document.getElementById("timeFilter").value;
        const searchText = this.value;
        console.log("Searching for:", searchText);
        fetchStatisticalData(timeFilter, searchText);
      }, 300); // Đợi 300ms sau khi người dùng ngừng gõ
    });
  }
});

// Xử lý sự kiện khi submit form tìm kiếm
document.getElementById("searchForm").addEventListener("submit", function (e) {
  e.preventDefault();
  const timeFilter = document.getElementById("timeFilter").value;
  const searchText = document.querySelector('input[name="text"]').value;
  fetchStatisticalData(timeFilter, searchText);
});

// Hàm gửi yêu cầu AJAX
function fetchStatisticalData(timeFilter, searchText) {
  console.log(
    "Fetching data with timeFilter:",
    timeFilter,
    "searchText:",
    searchText
  );
  const url = new URL(contextPath + "/statistical", window.location.origin);
  url.searchParams.append("timeFilter", timeFilter);
  if (searchText) {
    url.searchParams.append("text", searchText);
  }

  fetch(url, {
    headers: {
      "X-Requested-With": "XMLHttpRequest",
    },
  })
    .then((response) => {
      console.log("Response status:", response.status);
      return response.json();
    })
    .then((data) => {
      console.log("Received data:", data);
      updateStatisticalData(data);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}
