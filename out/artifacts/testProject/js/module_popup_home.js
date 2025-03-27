function showPopup(popupId) {
    // Ẩn tất cả các pop-up
    var popups = document.querySelectorAll(".popup");
    popups.forEach(function (popup) {
      popup.style.display = "none";
    });
  
    // Hiển thị pop-up của sản phẩm được chọn
    document.getElementById(popupId).style.display = "flex";
  }
  
  function closePopup(popupId) {
    document.getElementById(popupId).style.display = "none";
  }
  