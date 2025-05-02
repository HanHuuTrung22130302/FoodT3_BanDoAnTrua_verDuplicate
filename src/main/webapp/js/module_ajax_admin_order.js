function tableOrder(option, page) {
    // Tiếp tục thực hiện AJAX
    $.ajax({
        url: "ajaxordermanagement",
        type: "get",
        data: {
            option: option,
            page: page
        },
        success: function (data) {
            var row = document.getElementById("ajax-section");
            row.innerHTML = data;
        },
        error: function () {
            alert("Failed to load data!");
        }
    });
}
