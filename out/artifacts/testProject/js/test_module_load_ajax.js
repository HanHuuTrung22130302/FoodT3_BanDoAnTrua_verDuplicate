function loadSP(option, page) {
    // Tiếp tục thực hiện AJAX
    $.ajax({
        url: "menuajax",
        type: "get",
        data: {
            option: option,
            page: page
        },
        success: function (data) {
            var row = document.getElementById("content_section");
            row.innerHTML = data;
        },
        error: function () {
            alert("Failed to load data!");
        }
    });
}
