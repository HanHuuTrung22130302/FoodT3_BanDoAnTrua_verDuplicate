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

function showCancelPopup(id) {
    closePopup('check' + id);
    showPopup('cancelPopup' + id);
}
function confirmCancelOrder(invoiceId) {
    // Ẩn popup hủy
    const cancelPopup = document.getElementById("cancelPopup" + invoiceId);
    if (cancelPopup) {
        cancelPopup.style.display = "none";
    }

    const mainPopup = document.getElementById("check" + invoiceId);
    if (mainPopup) {
        mainPopup.style.display = "none";
    }

}

const typeSelect = document.getElementById('typeSelect');
const inputGroup = document.getElementById('inputGroup');

function updateInput() {
    const type = typeSelect.value;
    inputGroup.innerHTML = '';

    if (type === 'day') {
        const input = document.createElement('input');
        input.type = 'date';
        input.name = 'date';
        inputGroup.appendChild(input);
    } else if (type === 'month') {
        const input = document.createElement('input');
        input.type = 'month';
        input.name = 'month';
        inputGroup.appendChild(input);
    } else if (type === 'year') {
        const input = document.createElement('input');
        input.type = 'number';
        input.name = 'year';
        input.min = '1900';
        input.max = new Date().getFullYear();
        input.placeholder = 'Nhập năm (VD: 2025)';
        inputGroup.appendChild(input);
    }
}

typeSelect.addEventListener('change', updateInput);
updateInput(); // Khởi tạo lần đầu