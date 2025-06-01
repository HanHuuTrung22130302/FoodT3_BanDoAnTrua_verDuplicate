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
                        popup.classList.add("hidden");
                        showNotification("Thêm banner thành công!", "success");
                        fetchAndRenderBanners();
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
                    showNotification("Xóa banner thành công!", "success");
                    fetchAndRenderBanners();
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

function renderBannerTable(banners) {
    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = "";
    banners.forEach((bann, index) => {
        const row = document.createElement("tr");
        row.setAttribute("data-banner-id", bann.bannerId);
        row.innerHTML = `
      <td>${index + 1}</td>
      <td><img src="${bann.url}"/></td>
      <td>
        <button class="delete" onclick="deleteBanner(${bann.bannerId})">
          <i class="fas fa-trash"></i> Xóa
        </button>
      </td>
      <td>${bann.date}</td>
    `;
        tbody.appendChild(row);
    });
}

function fetchAndRenderBanners() {
    fetch("banner", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "action=list",
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                renderBannerTable(data.banners);
            }
        });
}
