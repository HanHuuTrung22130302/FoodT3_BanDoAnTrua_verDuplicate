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

// Hàm debounce để giới hạn số lần gọi hàm
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Hàm xử lý tìm kiếm và phân loại
function handleSearchAndFilter() {
    const searchForm = document.getElementById("searchForm");
    const searchInput = searchForm.querySelector('input[name="text"]');
    const roleSelect = document.getElementById("filterRole");
    const searchButton = searchForm.querySelector('button[type="submit"]');

    // Hàm xử lý tìm kiếm
    function performSearch() {
        const searchText = searchInput.value;
        const role = roleSelect.value;

        fetch(
            `customersevice?text=${encodeURIComponent(
                searchText
            )}&filterRole=${role}`,
            {
                method: "GET",
                headers: {
                    "X-Requested-With": "XMLHttpRequest",
                },
            }
        )
            .then((response) => response.text())
            .then((html) => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, "text/html");
                const newTable = doc.querySelector("table");
                const currentTable = document.querySelector("table");
                currentTable.parentNode.replaceChild(newTable, currentTable);
                initializeEventListeners();
            })
            .catch((error) => {
                showNotification("Có lỗi xảy ra khi tìm kiếm", "error");
            });
    }

    // Tạo hàm debounce cho tìm kiếm
    const debouncedSearch = debounce(performSearch, 300);

    // Xử lý sự kiện thay đổi select box
    roleSelect.addEventListener("change", performSearch);

    // Xử lý sự kiện nhập liệu trong ô tìm kiếm
    searchInput.addEventListener("input", debouncedSearch);

    // Xử lý sự kiện Enter trong ô tìm kiếm
    searchInput.addEventListener("keypress", function (e) {
        if (e.key === "Enter") {
            e.preventDefault();
            performSearch();
        }
    });

    // Xử lý sự kiện click button tìm kiếm
    searchButton.addEventListener("click", function (e) {
        e.preventDefault();
        performSearch();
    });

    // Xử lý sự kiện submit form
    searchForm.addEventListener("submit", function (e) {
        e.preventDefault();
        performSearch();
    });
}

// Hàm xử lý chặn tài khoản
function handleLockAccount(accountId, hours) {
    fetch("customersevice", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `action=lock&id=${accountId}&hours=${hours}`,
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                showNotification(data.message, "success");
                // Cập nhật trạng thái ngay lập tức
                const row = document
                    .querySelector(`[data-account-id="${accountId}"]`)
                    .closest("tr");
                const statusCell = row.querySelector("td:nth-child(6)");
                statusCell.innerHTML = '<span style="color: orange;">Đang chặn</span>';
            } else {
                showNotification(
                    data.error || "Có lỗi xảy ra khi chặn tài khoản",
                    "error"
                );
            }
        })
        .catch((error) => {
            showNotification("Có lỗi xảy ra khi chặn tài khoản", "error");
        });
}

// Hàm xử lý hủy chặn tài khoản
function handleUnlockAccount(accountId) {
    fetch("customersevice", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `action=unlock&id=${accountId}`,
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                showNotification(data.message, "success");
                // Cập nhật trạng thái ngay lập tức
                const row = document
                    .querySelector(`[data-account-id="${accountId}"]`)
                    .closest("tr");
                const statusCell = row.querySelector("td:nth-child(6)");
                statusCell.innerHTML = '<span style="color: green;">Hoạt động</span>';
            } else {
                showNotification(
                    data.error || "Có lỗi xảy ra khi hủy chặn tài khoản",
                    "error"
                );
            }
        })
        .catch((error) => {
            showNotification("Có lỗi xảy ra khi hủy chặn tài khoản", "error");
        });
}

// Hàm xử lý vô hiệu hóa tài khoản
function handleDeleteAccount(accountId) {
    fetch("customersevice", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `action=delete&id=${accountId}`,
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                showNotification(data.message, "success");
                // Tìm đúng dòng qua button[data-account-id]
                const row = document
                    .querySelector(`button[data-account-id="${accountId}"]`)
                    .closest("tr");
                const statusCell = row.querySelector("td:nth-child(6)");
                statusCell.innerHTML = '<span style="color: red;">Vô hiệu hóa</span>';

                // Thay thế toàn bộ action-buttons bằng nút kích hoạt
                const actionButtons = row.querySelector(".action-buttons");
                if (actionButtons) {
                    actionButtons.innerHTML = `
            <button class="activate" data-account-id="${accountId}">
              <i class="fas fa-check"></i> Kích hoạt
            </button>
          `;
                    // Gắn lại event listener cho nút kích hoạt mới
                    const activateButton = actionButtons.querySelector(".activate");
                    if (activateButton) {
                        activateButton.addEventListener("click", function (e) {
                            e.preventDefault();
                            e.stopPropagation();
                            if (confirm("Bạn có chắc chắn muốn kích hoạt tài khoản này?")) {
                                handleActivateAccount(accountId);
                            }
                        });
                    }
                }
            } else {
                showNotification(
                    data.error || "Có lỗi xảy ra khi vô hiệu hóa tài khoản",
                    "error"
                );
            }
        })
        .catch((error) => {
            showNotification("Có lỗi xảy ra khi vô hiệu hóa tài khoản", "error");
        });
}

function handleActivateAccount(accountId) {
    fetch("customersevice", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `action=activate&id=${accountId}`,
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                showNotification(data.message, "success");
                // Tìm đúng dòng qua button[data-account-id]
                const row = document
                    .querySelector(`button[data-account-id="${accountId}"]`)
                    .closest("tr");
                const statusCell = row.querySelector("td:nth-child(6)");
                statusCell.innerHTML = '<span style="color: green;">Hoạt động</span>';

                // Thay thế toàn bộ action-buttons bằng dropdown chặn + nút vô hiệu hóa
                const actionButtons = row.querySelector(".action-buttons");
                if (actionButtons) {
                    actionButtons.innerHTML = `
            <div class="dropdown">
              <button class="lock_btn" data-account-id="${accountId}">
                <i class="fas fa-lock"></i> Chặn
              </button>
              <div class="dropdown-content">
                <button class="lock-option" data-hours="24">
                  <i class="fas fa-clock"></i> 24 giờ
                </button>
                <button class="lock-option" data-hours="36">
                  <i class="fas fa-clock"></i> 36 giờ
                </button>
                <button class="lock-option" data-hours="48">
                  <i class="fas fa-clock"></i> 48 giờ
                </button>
                <div class="dropdown-divider"></div>
                <button class="unlock-btn" data-account-id="${accountId}">
                  <i class="fas fa-unlock"></i> Hủy chặn
                </button>
              </div>
            </div>
            <button class="delete" data-account-id="${accountId}">
              <i class="fas fa-trash"></i> Vô hiệu hóa
            </button>
          `;
                    // Gắn lại event listeners cho các nút mới
                    initializeEventListeners();
                }
            } else {
                showNotification(
                    data.error || "Có lỗi xảy ra khi kích hoạt tài khoản",
                    "error"
                );
            }
        })
        .catch((error) => {
            showNotification("Có lỗi xảy ra khi kích hoạt tài khoản", "error");
        });
}

// Hàm khởi tạo các event listener
function initializeEventListeners() {
    // Xử lý nút chặn và dropdown
    document.querySelectorAll(".lock_btn").forEach((button) => {
        button.onclick = null;
        button.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            const dropdownContent = this.nextElementSibling;
            dropdownContent.style.display =
                dropdownContent.style.display === "block" ? "none" : "block";
        });
    });

    // Xử lý các tùy chọn chặn
    document.querySelectorAll(".lock-option").forEach((option) => {
        option.onclick = null;
        option.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            const accountId = this.closest(".dropdown")
                .querySelector(".lock_btn")
                .getAttribute("data-account-id");
            const hours = this.getAttribute("data-hours");
            if (
                confirm(`Bạn có chắc chắn muốn chặn tài khoản này trong ${hours} giờ?`)
            ) {
                handleLockAccount(accountId, hours);
            }
        });
    });

    // Xử lý nút hủy chặn
    document.querySelectorAll(".unlock-btn").forEach((button) => {
        button.onclick = null;
        button.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            const accountId = this.getAttribute("data-account-id");
            if (confirm("Bạn có chắc chắn muốn hủy chặn tài khoản này?")) {
                handleUnlockAccount(accountId);
            }
        });
    });

    // Xử lý nút vô hiệu hóa
    document.querySelectorAll(".delete").forEach((button) => {
        button.onclick = null;
        button.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            const accountId = this.getAttribute("data-account-id");
            if (confirm("Bạn có chắc chắn muốn vô hiệu hóa tài khoản này?")) {
                handleDeleteAccount(accountId);
            }
        });
    });

    // Xử lý nút kích hoạt
    document.querySelectorAll(".activate").forEach((button) => {
        button.onclick = null;
        button.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            const accountId = this.getAttribute("data-account-id");
            if (confirm("Bạn có chắc chắn muốn kích hoạt tài khoản này?")) {
                handleActivateAccount(accountId);
            }
        });
    });

    // Đóng dropdown khi click ra ngoài
    document.addEventListener("click", function (e) {
        if (!e.target.closest(".dropdown")) {
            document.querySelectorAll(".dropdown-content").forEach((dropdown) => {
                dropdown.style.display = "none";
            });
        }
    });

    // Xử lý popup chi tiết
    const detailButtons = document.querySelectorAll(".detail_btn");
    const popup = document.getElementById("popup");
    const closeButton = document.querySelector(".close_btn");
    const popupDetails = document.getElementById("popup_details");

    detailButtons.forEach((button) => {
        button.addEventListener("click", () => {
            const listAcc = JSON.parse(button.getAttribute("data-account"));
            popupDetails.innerHTML = `
        <p><strong>Tên Khách Hàng:</strong> ${
                listAcc.fullName || "Chưa cập nhật Họ và Tên"
            }</p>
        <p><strong>Giới tính:</strong> ${
                listAcc.gender == 1
                    ? "Nam"
                    : listAcc.gender == 0
                        ? "Nữ"
                        : "Chưa xác định"
            }</p>
        <p><strong>Ngày sinh:</strong> ${
                listAcc.birthDate || "Chưa cập nhật ngày sinh"
            }</p>
        <p><strong>Địa chỉ:</strong> ${
                listAcc.address || "Chưa cập nhật địa chỉ"
            }</p>
        <p><strong>Số Điện thoại:</strong> ${
                listAcc.phoneNumber || "Chưa cập nhật SĐT"
            }</p>
        <p><strong>Email:</strong> ${listAcc.email || "chưa cập nhật email"}</p>
      `;
            popup.classList.remove("hidden");
        });
    });

    closeButton.addEventListener("click", () => {
        popup.classList.add("hidden");
    });
}

// Khởi tạo khi trang được tải
document.addEventListener("DOMContentLoaded", function () {
    handleSearchAndFilter();
    initializeEventListeners();
});
