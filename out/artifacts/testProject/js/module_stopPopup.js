// Chặn sự kiện lan truyền từ các nút "Thêm vào giỏ hàng"
document.addEventListener('DOMContentLoaded', () => {
    const addToCartButtons = document.querySelectorAll('.add-to-cart');
    addToCartButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            event.stopPropagation();
            // Có thể thêm xử lý khác tại đây nếu cần
        });
    });
});
