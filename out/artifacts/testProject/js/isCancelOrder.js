function confirmCancel(orderId) {
    // Hiển thị hộp thoại xác nhận
    var result = confirm("Bạn có chắc chắn muốn hủy đơn hàng này?");
    if (result) {
        // Tạo liên kết để hủy đơn hàng
        var url = "CancelBtnController?id=" + orderId;
        window.location.href = url;
    } else {
        console.log("Hủy bỏ hành động.");
    }
}