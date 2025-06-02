document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".status-button");

    // Khởi tạo: Thêm class disabled cho nút active ban đầu
    buttons.forEach(button => {
        if (button.classList.contains("active")) {
            button.classList.add("disabled");
        }
    });

    buttons.forEach(button => {
        button.addEventListener("click", function () {
            // Xóa class 'active' và 'disabled' khỏi tất cả button
            buttons.forEach(btn => {
                btn.classList.remove("active");
                btn.classList.remove("disabled");
            });
            // Thêm class 'active' và 'disabled' vào button vừa click
            this.classList.add("active");
            this.classList.add("disabled");
        });
    });
});