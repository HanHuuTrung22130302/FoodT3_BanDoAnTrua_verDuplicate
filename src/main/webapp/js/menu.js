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
