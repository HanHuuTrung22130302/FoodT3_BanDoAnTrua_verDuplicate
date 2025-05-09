function cancelstatus(reason,invoiceId,option, page) {

    $.ajax({
        url: "sendcancelstatusajaxordermanagement",
        type: "get",
        data: {
            reason: reason,
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
function confirmCancelOrder(invoiceId,option,page) {
    // Lấy lý do từ textarea
    const popup = document.getElementById('cancelPopup' + invoiceId);
    const reason = popup.querySelector('.cancel-reason').value.trim();

    if (!reason) {
        alert("Vui lòng nhập lý do hủy đơn hàng!");
        return;
    }

    // Lấy option và page từ URL
    // const urlParams = new URLSearchParams(window.location.search);
    // const option = urlParams.get("option");
    // const page = urlParams.get("page");

    // Gọi AJAX đã có sẵn
    cancelstatus(reason, invoiceId, option, page);

    // Đóng popup
    closePopup('cancelPopup' + invoiceId);
}
