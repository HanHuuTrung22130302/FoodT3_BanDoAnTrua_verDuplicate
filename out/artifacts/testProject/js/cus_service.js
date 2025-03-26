const detailButtons = document.querySelectorAll(".detail_btn");
const popup = document.getElementById("popup");
const closeButton = document.querySelector(".close_btn");
const popupDetails = document.getElementById("popup_details");

detailButtons.forEach(button => {
    button.addEventListener("click", () => {
        const listAcc = JSON.parse(button.getAttribute("data-account"));
        popupDetails.innerHTML = `
            <p><strong>Tên Khách Hàng:</strong> ${listAcc.fullName != null ? listAcc.fullName : "Chưa cập nhật Họ và Tên"}</p>
            <p><strong>Giới tính:</strong> ${listAcc.gender == 1 ? 'Nam' : (listAcc.gender == 0 ? 'Nữ' : 'Chưa xác định')}</p>
            <p><strong>Ngày sinh:</strong> ${listAcc.birthDate != null ? listAcc.birthDate : "Chưa cập nhật ngày sinh"}</p>
            <p><strong>Địa chỉ:</strong> ${listAcc.address != null ? listAcc.address : "Chưa cập nhật địa chỉ"}</p>
            <p><strong>Số Điện thoại:</strong> ${listAcc.phoneNumber != null ? listAcc.phoneNumber : "Chưa cập nhật SĐT"}</p>
            <p><strong>Email:</strong> ${listAcc.email != null ? listAcc.email : 'chưa cập nhật email'}</p>
        `;
        popup.classList.remove("hidden");
    });
});

closeButton.addEventListener("click", () => {
    popup.classList.add("hidden");
});