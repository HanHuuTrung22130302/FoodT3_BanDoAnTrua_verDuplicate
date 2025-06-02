function getCurrentOption() {
    const orderContainer = document.querySelector('.order-container');
    if (orderContainer) {
        const classes = orderContainer.classList;
        for (let className of classes) {
            if (className.startsWith('currentOption')) {
                return className.replace('currentOption', '') || '0';
            }
        }
    }
    return '0';
}

function ajaxOrder(option) {
    const currentOption = getCurrentOption();
    const currentCount = document.querySelectorAll('.countOrder').length;
    const loadmoreButton = document.querySelector('.loadmoreorder');

    // Bật nút loadmore trước khi gửi AJAX
    if (loadmoreButton) {
        loadmoreButton.style.removeProperty('display');
        console.log("Showing loadmore button before AJAX");
    } else {
        console.error("Loadmore button not found");
    }
    $.ajax({
        url: "PurchaseOrderAjaxControler",
        type: "get",
        data: {
            text: option,
            offset: currentCount,
            currentOption: currentOption
        },
        success: function(data) {
            var row = document.getElementById("content_section");
            if (option === currentOption) {
                row.innerHTML += data; // Thêm dữ liệu nếu option giống currentOption
            } else {
                row.innerHTML = data; // Ghi đè dữ liệu nếu option khác currentOption
            }
            formatCurrency();
            checkEndOfFlag();
        }
    });
}
function ajaxOrderLoadMore(param){
    const currentCount = document.querySelectorAll('.countOrder').length;
    const loadmoreButton = document.querySelector('.loadmoreorder');

    // Bật nút loadmore trước khi gửi AJAX
    if (loadmoreButton) {
        loadmoreButton.style.removeProperty('display');
        console.log("Showing loadmore button before AJAX");
    } else {
        console.error("Loadmore button not found");
    }
    $.ajax({
        url: "PurchaseOrderAjaxControler",
        type:"get",
        data: {
            text: param,
            offset: currentCount
        },
        success: function (data){
            var row = document.getElementById("content_section");
            row.innerHTML += data;
            formatCurrency();
            checkEndOfFlag();
        }
    })
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
function preventBodyScroll(e) {
    e.preventDefault();
}

function showPopup(popupId) {
    console.log("Opening popup:", popupId);

    // Lấy thẻ popup và wrapper
    const popup = document.getElementById(popupId);
    const wrapper = document.getElementById("popupWrapper" + popupId.replace("cancelPopup", ""));

    if (!popup || !wrapper) {
        console.error("Không tìm thấy popup hoặc wrapper:", popupId);
        return;
    }

    wrapper.style.display = "block";
    document.body.classList.add('no-scroll');
}

function closePopup(popupId) {
    const wrapper = document.getElementById("popupWrapper" + popupId.replace("cancelPopup", ""));
    if (wrapper) {
        wrapper.style.display = "none";
    }

    document.body.classList.remove('no-scroll');
}


function checkEndOfFlag() {
    if (document.querySelector('.endofflag')) {
        document.querySelector('.loadmoreorder').style.display = 'none';
    }
}

function checkOrderCount() {
    const orderCount = document.querySelectorAll('.countOrder').length;
    if (orderCount < 10) {
        document.querySelector('.loadmoreorder').style.display = 'none';
    }
}

// Gọi kiểm tra khi trang tải
document.addEventListener('DOMContentLoaded', () => {
    checkEndOfFlag();
    checkOrderCount();
});