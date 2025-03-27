
const popup = document.getElementById("popup");
const addButton = document.querySelector(".add-banner");
const closeButton =  document.querySelector(".close_btn");

addButton.addEventListener("click", () => {
  popup.classList.remove("hidden");
});

closeButton.addEventListener("click", () =>{
  popup.classList.add("hidden");
});

popup.addEventListener("click", (event) => {
  if (event.target === popup) {
    popup.classList.add("hidden");
  }
});

