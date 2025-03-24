$(document).ready(function() {
    $("#signupForm").submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: "${pageContext.request.contextPath}/signup",
            type: "POST",
            data: $(this).serialize(),
            success: function(response) {
                $("#messageContainer").text(response.message);
                if (response.status === "success") {
                    $("#messageContainer").css("color", "green");
                    setTimeout(() => {
                        window.location.href = "${pageContext.request.contextPath}/signin.jsp";
                    }, 2000);
                } else {
                    $("#messageContainer").css("color", "red");
                }
            },
            error: function() {
                $("#messageContainer").text("Có lỗi xảy ra, vui lòng thử lại.");
                $("#messageContainer").css("color", "red");
            }
        });
    });
});