function preventBodyScroll(e) {
    e.preventDefault();
}

function showPopup(popupId) {
    console.log("Opening popup:", popupId); // Xác nhận hàm được gọi

    var popups = document.querySelectorAll(".popup");
    popups.forEach(function (popup) {
        popup.style.display = "none";
    });

    const popupElement = document.getElementById(popupId);
    if (!popupElement) {
        console.error("Không tìm thấy popup có ID:", popupId);
        return;
    }
    popupElement.style.display = "flex";

    document.body.classList.add('no-scroll');
}


function closePopup(popupId) {
    document.getElementById(popupId).style.display = "none";
    document.body.classList.remove('no-scroll');

    document.removeEventListener('wheel', preventBodyScroll, { passive: false });
    document.removeEventListener('touchmove', preventBodyScroll, { passive: false });
}

function showCancelPopup(invoiceId) {
    const cancelPopup = document.getElementById("cancelPopup" + invoiceId);
    if (cancelPopup) {
        cancelPopup.style.display = "block";
    }
}



const typeSelect = document.getElementById('typeSelect');
const inputGroup = document.getElementById('inputGroup');

function updateInput() {
    const type = typeSelect.value;
    inputGroup.innerHTML = '';

    const input = document.createElement('input');
    if (type === 'day') {
        input.type = 'date';
        input.name = 'date';
    } else if (type === 'month') {
        input.type = 'month';
        input.name = 'month';
    }

    input.id = "filterInput"; // Đặt ID để dễ lấy giá trị khi gửi AJAX
    inputGroup.appendChild(input);
}

typeSelect.addEventListener('change', updateInput);
updateInput(); // gọi lần đầu khi load trang

inputGroup.addEventListener("change", function (e) {
    if (e.target && (e.target.name === "date" || e.target.name === "month")) {
        const value = e.target.value; // "yyyy-MM-dd" hoặc "yyyy-MM"
        const page = 1;

        tableOrder(value, page);
        pagi(value, page);
    }
});
