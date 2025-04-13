
    document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.increase-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const foodId = btn.getAttribute('data-food-id');
            updateQuantity(foodId, 'increment');
        });
    });

    document.querySelectorAll('.decrease-btn').forEach(btn => {
    btn.addEventListener('click', () => {
    const foodId = btn.getAttribute('data-food-id');
    updateQuantity(foodId, 'decrement');
});
});

    document.querySelectorAll('.delete-btn').forEach(btn => {
    btn.addEventListener('click', () => {
    const foodId = btn.getAttribute('data-food-id');
    updateQuantity(foodId, 'remove');
});
});
        document.getElementById('deleteAll-btn').addEventListener('click', () => {
            fetch('AjaxCartController', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'action=removeAll'
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        document.getElementById('cart-items-container').innerHTML = '';
                        document.getElementById('subtotal').textContent = '0₫';
                        document.getElementById('total').textContent = '0₫';
                        document.getElementById('order-count').textContent = '0';
                    }
                })
                .catch(error => console.error('AJAX error:', error));
        });



        function updateQuantity(foodId, action) {
    fetch('AjaxCartController', {
    method: 'POST',
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    body: `foodId=${foodId}&action=${action}`
})
    .then(response => response.json())
    .then(data => {
    if (data.success) {
    if (data.newQuantity === 0) {
    const row = document.querySelector(`.delete-btn[data-food-id="${foodId}"]`).closest('tr');
    if (row) row.remove();
} else {
    document.querySelector(`#quantity-${foodId} span`).textContent = data.newQuantity;
    const itemTotal = data.newQuantity * data.unitPrice;
    document.querySelector(`#item-total-${foodId}`).textContent = itemTotal+'₫';
}

    document.getElementById('subtotal').textContent = data.subtotal+'₫';
    document.getElementById('total').textContent = data.total+'₫';
    document.getElementById('order-count').textContent = data.totalItems;

    }
})
    .catch(error => console.error('AJAX error:', error));
}
});

