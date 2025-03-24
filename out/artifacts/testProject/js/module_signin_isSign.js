// Tạo các tài khoản giả
const accounts = {
    "admin": { email: "admin@gmail.com", password: "123", role: "admin" },
    "user": { id: 1, email: "user@gmail.com", password: "123", role: "user" },
    "user2": { id: 2, email: "user2@gmail.com", password: "123", role: "user" },
    "user3": { id: 3, email: "user3@gmail.com", password: "123", role: "user" },
};

// Hàm xử lý đăng nhập
function login() {
    const inputField = document.querySelector('.sign-in-container input[type="text"]');
    const passwordInput = document.querySelector('.sign-in-container input[type="password"]');

    const input = inputField ? inputField.value.trim() : '';
    const password = passwordInput ? passwordInput.value : '';

    // Tìm tài khoản phù hợp dựa trên name hoặc email
    const userAccount = Object.keys(accounts).find(
        (name) => (name === input || accounts[name].email === input) && accounts[name].password === password
    );

    if (userAccount) {
        const account = accounts[userAccount];

        // Lưu trạng thái đăng nhập vào localStorage
        const loggedInUser = {
            name: userAccount,
            email: account.email,
            role: account.role,
            password: account.password,
            ...(account.role !== "admin" && { id: account.id }), // Thêm id nếu không phải admin
        };

        // Lưu trạng thái đăng nhập
        localStorage.setItem("loggedInUser", JSON.stringify(loggedInUser));

        // Chuyển hướng về trang chủ
        window.location.href = "../html/newhome.jsp";
    } else {
        alert("Email, tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
    }
}

// Gán sự kiện cho nút Đăng nhập
document.querySelector('.sign-in-container form').addEventListener("submit", function(event) {
    event.preventDefault(); // Ngăn gửi form
    login();
});
