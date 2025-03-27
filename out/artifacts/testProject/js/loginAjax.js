$(document).ready(function () {
    $("#loginForm").on("submit", function (event) {
        event.preventDefault();

        let formData = $(this).serialize();
        let captchaResponse = grecaptcha.getResponse();
        if (captchaResponse) {
            formData += "&g-recaptcha-response=" + captchaResponse;
        }

        $.ajax({
            type: "POST",
            url: "/testProject/login", // Cứng URL để đảm bảo đúng
            data: formData,
            dataType: "json",
            success: function (response) {
                console.log("Login response:", response); // Thêm log để kiểm tra
                $("#login_messageContainer").text(response.message);
                if (response.status === "success") {
                    $("#login_messageContainer").css("color", "green");
                    setTimeout(() => {
                        window.location.href = response.redirect || "/testProject/home";
                    }, 2000);
                } else {
                    $("#login_messageContainer").css("color", "red");
                    if (response.message.includes("CAPTCHA")) {
                        $("#captchaContainer").show();
                    }
                }
            },
            error: function (xhr, status, error) {
                console.log("Login error:", xhr, status, error); // Thêm log để kiểm tra lỗi
                $("#login_messageContainer").css("color", "red").text("Có lỗi xảy ra. Vui lòng thử lại.");
            }
        });
    });
});