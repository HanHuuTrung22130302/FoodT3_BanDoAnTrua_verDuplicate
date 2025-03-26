let slideIndex = 0;
const slides = document.getElementsByClassName("mySlides");
const dots = document.getElementsByClassName("dot");
const totalSlides = slides.length;

let slideInterval;

function showSlides() {
    const offset = -slideIndex * 100;
    document.querySelector('.w3-content').style.transform = `translateX(${offset}%)`;

    for (let i = 0; i < totalSlides; i++) {
        dots[i].className = dots[i].className.replace(" active", ""); 
    }
    dots[slideIndex].className += " active";
}

function plusDivs(n) {
  slideIndex += n;

  if (slideIndex < 0) {
    slideIndex = totalSlides - 1;
  } else if (slideIndex >= totalSlides) {
    slideIndex = 0;
  }

  showSlides();

  resetSlideInterval();
}

function currentSlide(n) {
    slideIndex = n;
    showSlides();

    resetSlideInterval();
}

function startSlideShow() {
    slideInterval = setInterval(() => {
        slideIndex++;
        if (slideIndex >= totalSlides) {
            slideIndex = 0;
        }
        showSlides();
    }, 8000);
}

function resetSlideInterval() {
    clearInterval(slideInterval); 
    startSlideShow(); 
}

startSlideShow();
showSlides();
