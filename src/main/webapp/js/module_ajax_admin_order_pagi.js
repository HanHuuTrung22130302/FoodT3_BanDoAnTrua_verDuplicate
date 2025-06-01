function pagi(option, page) {
    // Tiếp tục thực hiện AJAX
    $.ajax({
        url: "ajaxpagiordermanagement",
        type: "get",
        data: {
            option: option,
            page: page
        },
        success: function (data) {
            var row = document.getElementById("pagi-section");
            row.innerHTML = data;
        },
        error: function () {
            alert("Failed to load data!");
        }
    });
}
