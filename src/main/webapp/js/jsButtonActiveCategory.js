document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".status-button");

    buttons.forEach(button => {
        button.addEventListener("click", function () {
            // Xóa class 'active' khỏi tất cả button
            buttons.forEach(btn => btn.classList.remove("active"));
            // Thêm class 'active' vào button vừa click
            this.classList.add("active");
        });
    });
});
