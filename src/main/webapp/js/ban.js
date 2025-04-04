document.addEventListener("DOMContentLoaded", function () {
  const dropdowns = document.querySelectorAll(".dropdown");

  dropdowns.forEach((dropdown) => {
    const button = dropdown.querySelector(".lock_btn");
    const content = dropdown.querySelector(".dropdown-content");
    let timeoutId;

    dropdown.addEventListener("mouseenter", function (e) {
      clearTimeout(timeoutId);
      dropdowns.forEach((d) => d.classList.remove("show"));
      dropdown.classList.add("show");

      // Tính toán vị trí cho dropdown
      const rect = button.getBoundingClientRect();
      content.style.top = rect.bottom + 5 + "px";
      content.style.left = rect.left + "px";
    });

    dropdown.addEventListener("mouseleave", function (e) {
      timeoutId = setTimeout(() => {
        dropdown.classList.remove("show");
      }, 100);
    });

    content.addEventListener("mouseenter", function () {
      clearTimeout(timeoutId);
    });

    content.addEventListener("mouseleave", function () {
      timeoutId = setTimeout(() => {
        dropdown.classList.remove("show");
      }, 100);
    });
  });
});
