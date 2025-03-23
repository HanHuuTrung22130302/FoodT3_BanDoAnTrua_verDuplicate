// Hiển thị/Ẩn chatbox
function toggleChatbox() {
    const chatbox = document.getElementById('chatbox');
    chatbox.style.display = chatbox.style.display === 'flex' ? 'none' : 'flex';
}

// Gửi tin nhắn
function sendMessage() {
    const input = document.getElementById('user-input');
    const message = input.value.trim();
    if (!message) return;

    const chatBody = document.getElementById('chatbox-body');
    const userMessage = document.createElement('div');
    userMessage.className = 'message user-message';
    userMessage.textContent = message;
    chatBody.appendChild(userMessage);

    input.value = '';
    chatBody.scrollTop = chatBody.scrollHeight;

    fetch(contextPath + '/webhook', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            queryResult: {
                queryText: message,
                parameters: {
                    taste: extractTaste(message),
                    restriction: extractRestriction(message),
                    category: extractCategory(message)
                }
            }
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            const botMessage = document.createElement('div');
            botMessage.className = 'message bot-message';
            botMessage.innerHTML = formatResponse(data.fulfillmentText);
            chatBody.appendChild(botMessage);
            chatBody.scrollTop = chatBody.scrollHeight;
        })
        .catch(error => {
            console.error('Error details:', error.message);
            const botMessage = document.createElement('div');
            botMessage.className = 'message bot-message';
            botMessage.textContent = 'Có lỗi xảy ra, vui lòng thử lại!';
            chatBody.appendChild(botMessage);
            chatBody.scrollTop = chatBody.scrollHeight;
        });
}

// Trích xuất sở thích về vị
function extractTaste(message) {
    const tastes = ['ngọt', 'mặn', 'cay', 'chua', 'nhạt'];
    return tastes.find(taste => message.includes(taste)) || null;
}

// Trích xuất hạn chế (restriction)
function extractRestriction(message) {
    const restrictions = [
        { keyword: 'thịt bò', value: 'thịt bò' },
        { keyword: 'hải sản', value: 'hải sản' },
        { keyword: 'cay', value: 'cay' },
        { keyword: 'dầu mỡ', value: 'dầu mỡ' },
        { keyword: 'mặn', value: 'mặn' },
        { keyword: 'chay', value: 'chay' }
    ];
    return restrictions.find(r =>
        (message.includes('không ăn được') || message.includes('dị ứng')) &&
        message.includes(r.keyword)
    )?.value || null;
}

// Trích xuất danh mục (category)
function extractCategory(message) {
    const categories = [
        { keyword: 'cơm', value: 'cơm' },
        { keyword: 'bún', value: 'bún' },
        { keyword: 'phở', value: 'phở' },
        { keyword: 'nước', value: 'nước' },
        { keyword: 'khát nước', value: 'nước' } // Thêm "khát nước" để gợi ý món nước
    ];
    return categories.find(category => message.includes(category.keyword))?.value || null;
}

// Định dạng phản hồi với link sản phẩm
function formatResponse(text) {
    const foodNames = text.match(/([A-ZĐÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴ][a-zàáảãạâầấẩẫậăắằẳẵặèéẻẽẹêếềểễệìíỉĩịòóỏõọôốồổỗộơớờởỡợùúủũụưứừửữựỳýỷỹỵ\s]+)(?=\s*\()/g);
    if (foodNames) {
        foodNames.forEach(food => {
            const link = `<a href="/food?foodName=${encodeURIComponent(food.trim())}" target="_blank">${food.trim()}</a>`;
            text = text.replace(food, link);
        });
    }
    return text;
}