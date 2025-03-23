// Xử lý hiển thị form khi nhấn vào sidebar
function showContent(sectionId) {
  const sections = document.querySelectorAll(".content-section");
  sections.forEach((section) => section.classList.add("hidden"));
  document.getElementById(sectionId).classList.remove("hidden");
}


// Lắng nghe sự kiện nút Lưu trong form Hồ Sơ (nếu cần)
document.getElementById("save-btn")?.addEventListener("click", function (e) {
  e.preventDefault();
  // Bạn có thể thêm hành động khi nhấn Lưu tại đây nếu cần
  alert("Dữ liệu đã được lưu!");
});


// Khôi phục dữ liệu từ localStorage khi trang được tải (phần này sẽ bị xóa)
window.onload = function () {
  // Xóa phần lưu trữ thông tin người dùng từ localStorage
  // Chỉ giữ lại phần hiển thị các form khi trang tải


  // Đảm bảo các mục được hiển thị khi người dùng truy cập trang
  const sections = document.querySelectorAll(".content-section");
  sections.forEach((section) => section.classList.add("hidden"));
  document.getElementById("profile").classList.remove("hidden"); // Hiển thị form Hồ Sơ mặc định
};
