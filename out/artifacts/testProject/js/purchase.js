function formatNumber(number) {
    return number.toLocaleString('vi-VN'); // Định dạng số kiểu Việt Nam
}

// Lấy tất cả các thẻ có class "total-money"
const moneyElements = document.querySelectorAll('.money, .total-money');

moneyElements.forEach(element => {
    // Lấy giá trị số từ textContent và loại bỏ ký tự không phải số
    const number = parseInt(element.textContent.replace(/[^0-9]/g, ''), 10);
    // Định dạng lại số và cập nhật vào thẻ
    element.textContent = formatNumber(number) + ' đ';
});
