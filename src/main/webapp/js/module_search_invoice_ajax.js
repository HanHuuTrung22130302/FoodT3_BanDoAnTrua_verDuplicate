function searchInvoice(option) {
    let searchText = '';
    let statusOption = '0'; // Mặc định là "Tất cả"
    const currentOption = getCurrentOption();
    const currentCount = document.querySelectorAll('.countOrder').length || 0;
    const loadmoreButton = document.querySelector('.loadmoreorder');

    // Bật nút loadmore
    if (loadmoreButton) {
        loadmoreButton.style.removeProperty('display');
        console.log("Showing loadmore button before AJAX (using default CSS)");
    } else {
        console.error("Loadmore button not found");
    }

    // Xử lý tham số option
    if (option && option.tagName === 'INPUT') {
        searchText = option.value.trim() || ''; // Lấy giá trị tìm kiếm
    } else if (typeof option === 'number' || typeof option === 'string') {
        statusOption = option.toString(); // Lấy trạng thái đơn hàng
    }

    console.log("ajaxOrder - searchText:", searchText, "statusOption:", statusOption, "currentOption:", currentOption, "currentCount:", currentCount);
    $.ajax({
        url: "PurchaseOrderAjaxControler",
        type: "get",
        data: {
            text: searchText || statusOption, // Ưu tiên searchText nếu có
            offset: currentCount,
            currentOption: currentOption
        },
        success: function(data) {
            var row = document.getElementById("content_section");
            if (statusOption === currentOption && !searchText) {
                row.innerHTML += data;
            } else {
                row.innerHTML = data;
            }
            formatCurrency();
            checkEndOfFlag();
        },
        error: function(xhr, status, error) {
            console.error("AJAX error:", status, error);
        }
    });
}
function formatNumber(number) {
    return number.toLocaleString('vi-VN'); // Định dạng số kiểu Việt Nam
}

function formatCurrency() {
    // Lấy tất cả các thẻ có class "money" hoặc "total-money"
    const moneyElements = document.querySelectorAll('.money, .total-money');

    moneyElements.forEach(element => {
        // Lấy giá trị số từ textContent và loại bỏ ký tự không phải số
        const number = parseInt(element.textContent.replace(/[^0-9]/g, ''), 10);
        if (!isNaN(number)) {
            // Định dạng lại số và cập nhật vào thẻ
            element.textContent = formatNumber(number) + ' đ';
        }
    });
}