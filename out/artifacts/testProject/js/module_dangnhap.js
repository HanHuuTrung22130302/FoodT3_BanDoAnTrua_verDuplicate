// Kiểm tra trạng thái đăng nhập và cập nhật giao diện
function checkLoginStatus() {
    const loggedInUser = JSON.parse(localStorage.getItem("loggedInUser"));

    if (loggedInUser) {
        // Hiển thị tên người dùng và menu sau khi đăng nhập
        document.getElementById("login-link").style.display = "none";
        document.getElementById("user-menu").style.display = "block";

        // Hiển thị tên người dùng
        const userName = document.getElementById("user-name");
        userName.textContent = loggedInUser.username;

        // Kiểm tra xem người dùng có phải là admin không
        const adminLink = document.getElementById("admin-link");
        const userLink = document.getElementById("user-link");
        if (loggedInUser.role === "admin") {
            adminLink.style.display = "block";
            userLink.style.display = "block";
        } else {
            adminLink.style.display = "none";
            userLink.style.display = "block";
        }
    } else {
        // Nếu người dùng chưa đăng nhập, hiển thị nút đăng nhập
        document.getElementById("login-link").style.display = "block";
        document.getElementById("user-menu").style.display = "none";
    }
}


checkLoginStatus();
