$(document).ready(function () {
    $("form[action='login']").on("submit", function (event) {
        event.preventDefault(); // Ngăn chặn hành động submit mặc định

        $.ajax({
            type: "POST",
            url: $(this).attr('action'),
            data: $(this).serialize(),
            success: function (response) {
                // Nếu đăng nhập thành công, chuyển hướng ngay lập tức
                if (response.status === "success") {
                    window.location.href = "home"; // Chuyển hướng đến trang home
                } else {
                    // Hiển thị thông báo lỗi nếu đăng nhập thất bại
                    $("#messageContainer").css("color", "red").text(response.message);
                }
            },
            error: function () {
                // Hiển thị thông báo lỗi nếu có lỗi xảy ra
                $("#messageContainer").css("color", "red").text("Có lỗi xảy ra. Vui lòng thử lại.");
            }
        });
    });
});

$(document).ready(function () {
    $("form[action='signup']").on("submit", function (event) {
        event.preventDefault(); // Ngăn chặn hành động submit mặc định

        $.ajax({
            type: "POST",
            url: $(this).attr('action'),
            data: $(this).serialize(),
            success: function (response) {
                // Nếu đăng ký thành công, chuyển hướng đến trang đăng nhập
                if (response.status === "success") {
                    window.location.href = "login"; // Chuyển hướng đến trang login
                } else {
                    // Hiển thị thông báo lỗi nếu đăng ký thất bại
                    $("#messageContainer").css("color", "red").text(response.message);
                }
            },
            error: function () {
                // Hiển thị thông báo lỗi nếu có lỗi xảy ra
                $("#messageContainer").css("color", "red").text("Có lỗi xảy ra. Vui lòng thử lại.");
            }
        });
    });
});
