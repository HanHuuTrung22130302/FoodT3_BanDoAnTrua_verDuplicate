window.onload = function() {
    var successMessage = document.getElementById("success-message");
    if (successMessage) {
        setTimeout(function() {
            successMessage.style.display = "none";
        }, 2000);
    }

    var emptyCartMessage = document.getElementById("empty-cart-message");
    if (emptyCartMessage) {
        setTimeout(function() {
            emptyCartMessage.style.display = "none";
        }, 2000);
    }
};
