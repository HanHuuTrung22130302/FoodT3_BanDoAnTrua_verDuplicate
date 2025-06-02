function confirmCancel(invoiceId) {
    const textarea = document.querySelector('.cancel-reason' + invoiceId);
    const reason = textarea.value.trim();

    if (!reason) {
        alert("Vui lòng nhập lý do hủy đơn hàng!");
        return;
    }

    // Tạo form động
    const form = document.createElement('form');
    form.method = 'post';
    form.action = 'CancelBtnController';

    // Tạo input invoiceId
    const inputId = document.createElement('input');
    inputId.type = 'hidden';
    inputId.name = 'invoiceId';
    inputId.value = invoiceId;
    form.appendChild(inputId);

    // Tạo input reason
    const inputReason = document.createElement('input');
    inputReason.type = 'hidden';
    inputReason.name = 'reason';
    inputReason.value = reason;
    form.appendChild(inputReason);

    document.body.appendChild(form);
    form.submit();
}
