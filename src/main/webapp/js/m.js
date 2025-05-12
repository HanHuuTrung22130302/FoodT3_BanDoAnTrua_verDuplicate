// Kiểm tra trạng thái đăng nhập và cập nhật giao diện
function checkLoginStatus() {
    const loggedInUser = JSON.parse(localStorage.getItem("loggedInUser"));
    const loginLink = document.getElementById("login-link");
    const userMenu = document.getElementById("user-menu");
    const userName = document.getElementById("user-name");
    const adminLink = document.getElementById("admin-link");
    const userLink = document.getElementById("user-link");

    // Kiểm tra xem các phần tử có tồn tại không
    if (!loginLink || !userMenu || !userName || !adminLink || !userLink) {
        console.log("Một số phần tử DOM chưa sẵn sàng");
        return;
    }

    if (loggedInUser) {
        // Hiển thị tên người dùng và menu sau khi đăng nhập
        loginLink.style.display = "none";
        userMenu.style.display = "block";

        // Hiển thị tên người dùng
        userName.textContent = loggedInUser.username;

        // Kiểm tra xem người dùng có phải là admin không
        if (loggedInUser.role === "admin") {
            adminLink.style.display = "block";
            userLink.style.display = "block";
        } else {
            adminLink.style.display = "none";
            userLink.style.display = "block";
        }
    } else {
        // Nếu người dùng chưa đăng nhập, hiển thị nút đăng nhập
        loginLink.style.display = "block";
        userMenu.style.display = "none";
    }
}

// Đợi cho DOM được tải hoàn toàn trước khi chạy script
document.addEventListener("DOMContentLoaded", function () {
    checkLoginStatus();
});
