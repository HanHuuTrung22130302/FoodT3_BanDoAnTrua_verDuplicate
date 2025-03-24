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
    updateButtons.forEach(button => {
        button.addEventListener("click", () => {
            updatePopup.classList.remove("hidden");
        });
    });

    // Đóng popup
    closeButtons.forEach(button => {
        button.addEventListener("click", () => {
            button.closest(".popup").classList.add("hidden");
        });
    });

    // Đóng popup khi click ra ngoài
    [addPopup, updatePopup].forEach(popup => {
        popup.addEventListener("click", (event) => {
            if (event.target === popup) {
                popup.classList.add("hidden");
            }
        });
    });
});

function openUpdatePopup(idFood) {
    document.getElementById('update_popup').classList.remove('hidden');
    document.getElementById('update_item_form').elements['idFood'].value = idFood;
}


