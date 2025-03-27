document.addEventListener('DOMContentLoaded', function () {
  const tienmat = document.getElementById('tienmat');
  const thenganhang = document.getElementById('thenganhang');
  const vidientu = document.getElementById('vidientu');

  const thenganhangGroup = document.getElementById('thenganhang-group');
  const vidientuGroup = document.getElementById('vidientu-group');

  // Xử lý sự kiện khi người dùng chọn "Thanh toán khi nhận hàng"
  tienmat.addEventListener('click', function () {
    // Ẩn các form khác
    thenganhangGroup.style.display = 'none';
    vidientuGroup.style.display = 'none';
  });

  // Xử lý sự kiện khi người dùng chọn "Thanh toán bằng thẻ ngân hàng"
  thenganhang.addEventListener('click', function () {
    // Ẩn form khác và hiển thị form thẻ ngân hàng
    thenganhangGroup.style.display = 'block';
    vidientuGroup.style.display = 'none';
  });

  // Xử lý sự kiện khi người dùng chọn "Thanh toán bằng ví điện tử"
  vidientu.addEventListener('click', function () {
    // Ẩn form khác và hiển thị form ví điện tử
    thenganhangGroup.style.display = 'none';
    vidientuGroup.style.display = 'block';
  });
});


// Xử lý các ví điện tử
let googlepay = document.querySelector("#google-pay-icon");
let applepay = document.querySelector("#apple-pay-icon");
let amazonpay = document.querySelector("#amazon-pay-icon");
let paypal = document.querySelector("#paypal-icon");

function activateEWallet(selectedWallet) {
  // Đặt lại trạng thái của tất cả các ví điện tử
  [googlepay, applepay, amazonpay, paypal].forEach((wallet) => {
    wallet.classList.remove("active");
  });

  // Kích hoạt ví được chọn
  selectedWallet.classList.add("active");
}

googlepay.addEventListener("click", () => {
  activateEWallet(googlepay);
});

applepay.addEventListener("click", () => {
  activateEWallet(applepay);
});

amazonpay.addEventListener("click", () => {
  activateEWallet(amazonpay);
});

paypal.addEventListener("click", () => {
  activateEWallet(paypal);
});



