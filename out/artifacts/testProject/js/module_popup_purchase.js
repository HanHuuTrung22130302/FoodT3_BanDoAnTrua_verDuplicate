// Lấy các phần tử cần thao tác
const ratingPopup = document.getElementById("rating-popup");
const commentButtons = document.querySelectorAll(".comment-button");
const closeButton = document.querySelector(".close-button");

// Hiển thị popup khi nhấn vào nút "Đánh giá"
commentButtons.forEach((button) => {
  button.addEventListener("click", () => {
    ratingPopup.style.display = "block";
  });
});

// Đóng popup khi nhấn vào nút đóng
closeButton.addEventListener("click", () => {
  ratingPopup.style.display = "none";
});

// Đóng popup khi nhấn ra ngoài
window.addEventListener("click", (event) => {
  if (event.target === ratingPopup) {
    ratingPopup.style.display = "none";
  }
});
