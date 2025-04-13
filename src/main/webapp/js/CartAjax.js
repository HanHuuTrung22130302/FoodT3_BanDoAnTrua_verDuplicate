document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.increase-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const foodId = btn.getAttribute('data-food-id');
            updateQuantity(foodId, 'increment');
        });
    });

    // Giảm số lượng
    document.querySelectorAll('.decrease-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const foodId = btn.getAttribute('data-food-id');
            updateQuantity(foodId, 'decrement');
        });
    });

    document.querySelectorAll('final_total').forEach(

    )

    function updateQuantity(foodId, action) {
        fetch('AjaxCartController', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: `foodId=${foodId}&action=${action}`
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Cập nhật lại số lượng và tổng
                    document.querySelector(`#quantity-${foodId} span`).textContent = data.newQuantity;
                    document.getElementById('subtotal').textContent = data.subtotal + '₫';
                    document.getElementById('total').textContent = data.total + '₫';
                    document.getElementById('finalTotal').textContent = data.total + '₫';
                }
            })
            .catch(error => console.error('AJAX error:', error));
    }
});
