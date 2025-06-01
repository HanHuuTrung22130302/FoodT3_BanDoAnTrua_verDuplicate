$(document).ready(function () {
    $("form[action='signup']").on("submit", function (event) {
        event.preventDefault(); // Ngăn chặn hành động submit mặc định

        // Xóa thông báo cũ nếu có
        $("#messageContainer").text("").removeClass("error success");

        $.ajax({
            type: "POST",
            url: $(this).attr("action"),
            data: $(this).serialize(),
            dataType: "json", // Chỉ định kiểu dữ liệu trả về là JSON
            success: function (response) {
                if (response.status === "success") {
                    // Hiển thị thông báo thành công
                    $("#messageContainer")
                        .css("color", "red")
                        .addClass("success")
                        .text(
                            "Yêu cầu của bạn đã được tiếp nhận. Vui lòng kiểm tra email để tiếp tục quá trình đăng ký!"
                        );
                    // Xóa form sau khi đăng ký thành công
                    $("form[action='signup']")[0].reset();
                } else {
                    // Hiển thị thông báo lỗi
                    $("#messageContainer")
                        .css("color", "red")
                        .addClass("error")
                        .text(response.message);
                }
            },
            error: function (xhr, status, error) {
                // Hiển thị thông báo lỗi chi tiết hơn
                let errorMessage = "Có lỗi xảy ra. Vui lòng thử lại.";
                try {
                    const response = JSON.parse(xhr.responseText);
                    if (response.message) {
                        errorMessage = response.message;
                    }
                } catch (e) {
                    console.error("Lỗi parse JSON:", e);
                }
                $("#messageContainer")
                    .css("color", "red")
                    .addClass("error")
                    .text(errorMessage);
            },
        });
    });
});
