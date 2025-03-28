$(document).ready(function () {
    $("form[action='login']").on("submit", function (event) {
        event.preventDefault(); // Ngăn chặn hành động submit mặc định

        $.ajax({
            type: "POST",
            url: $(this).attr('action'),
            data: $(this).serialize(),
            success: function (response) {
                if (response.status === "success") {
                    window.location.href = "home"; // Chuyển hướng đến trang home
                } else if (response.status === "locked") {
                    $("#login_messageContainer").css("color", "red").text(response.message);
                } else {
                    $("#login_messageContainer").css("color", "red").text(response.message);
                }
            },
            error: function () {
                $("#login_messageContainer").css("color", "red").text("Có lỗi xảy ra. Vui lòng thử lại.");
            }
        });
    });
});