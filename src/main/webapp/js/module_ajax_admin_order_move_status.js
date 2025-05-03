function movestatus(invoiceId,option, page) {
    // Tiếp tục thực hiện AJAX
    $.ajax({
        url: "sendmovestatusajaxordermanagement",
        type: "get",
        data: {
            invoiceId: invoiceId,
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
