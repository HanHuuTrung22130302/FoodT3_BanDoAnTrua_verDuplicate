function cancelstatus(reason,invoiceId,option, page) {

    $.ajax({
        url: "sendcancelstatusajaxordermanagement",
        type: "post",
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
    // Gọi AJAX đã có sẵn
    cancelstatus('5',reason, invoiceId, option, page);

    // Đóng popup
    closePopup('cancelPopup' + invoiceId);
}
function confirmCancelBombOrder(invoiceId,option,page) {

    const popup = document.getElementById('cancelPopup' + invoiceId);
    const reason = popup.querySelector('.cancel-reason').value.trim();

    if (!reason) {
        alert("Vui lòng nhập lý do hủy đơn hàng!");
        return;
    }
    // Gọi AJAX đã có sẵn
    cancelstatus('6',reason, invoiceId, option, page);

    // Đóng popup
    closePopup('cancelPopup' + invoiceId);
}
