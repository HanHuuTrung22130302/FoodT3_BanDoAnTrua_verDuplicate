// Danh sách lưu trữ tất cả các hạn chế trong phiên trò chuyện
let restrictionsList = [];

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

    const newRestriction = extractRestriction(message);
    if (newRestriction && !restrictionsList.includes(newRestriction)) {
        restrictionsList.push(newRestriction);
    }

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
                    restrictions: restrictionsList,
                    category: extractCategory(message),
                    product: extractProduct(message),
                    ingredients: extractIngredients(message)
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

// Trích xuất sở thích (taste)
function extractTaste(message) {
    const tastes = [
        { keyword: 'ngọt', value: 'ngọt' },
        { keyword: 'mặn', value: 'mặn' },
        { keyword: 'cay', value: 'cay' },
        { keyword: 'chua', value: 'chua' },
        { keyword: 'nhạt', value: 'nhạt' },
        { keyword: 'thịt gà', value: 'thịt gà' },
        { keyword: 'gà', value: 'gà' },
        { keyword: 'thịt heo', value: 'thịt heo' },
        { keyword: 'heo', value: 'heo' },
        { keyword: 'thịt bò', value: 'thịt bò' },
        { keyword: 'bò', value: 'bò' },
        { keyword: 'hải sản', value: 'hải sản' },
        { keyword: 'tôm', value: 'tôm' }
    ];
    return tastes.find(t =>
        (message.includes('muốn ăn') || message.includes('thích') || message.includes('thèm')) || message.includes('gợi ý') &&
        message.includes(t.keyword)
    )?.value || null;
}

// Trích xuất hạn chế (restriction)
function extractRestriction(message) {
    const restrictions = [
        { keyword: 'thịt bò', value: 'thịt bò' },
        { keyword: 'bò', value: 'bò' },
        { keyword: 'hải sản', value: 'hải sản' },
        { keyword: 'tôm', value: 'tôm' },
        { keyword: 'cay', value: 'cay' },
        { keyword: 'dầu mỡ', value: 'dầu mỡ' },
        { keyword: 'mặn', value: 'mặn' },
        { keyword: 'chay', value: 'chay' }
    ];
    return restrictions.find(r =>
        (message.includes('không ăn được') || message.includes('dị ứng') || message.includes('ghét')) &&
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
        { keyword: 'khát nước', value: 'nước' }
    ];
    return categories.find(category => message.includes(category.keyword))?.value || null;
}

// Trích xuất sản phẩm (product) để hỏi chi tiết
function extractProduct(message) {
    const keywords = ['thông tin', 'chi tiết', 'có gì', 'giá', 'là bao nhiêu', 'món này', 'thành phần', 'nguyên liệu'];
    if (keywords.some(keyword => message.includes(keyword))) {
        // Chuẩn hóa chuỗi: loại bỏ các từ không cần thiết
        let normalizedMessage = message.toLowerCase()
            .replace("có những thành phần gì", "")
            .replace("có những nguyên liệu nào", "")
            .replace("có nguyên liệu gì", "")
            .replace("có gì", "")
            .replace("thành phần của", "")
            .replace("nguyên liệu của", "")
            .replace("thành phần", "")
            .replace("nguyên liệu", "")
            .trim();

        // Tách tên món bằng regex
        let productMatch = normalizedMessage.match(/(món\s+)?([a-zàáảãạâầấẩẫậăắằẳẵặèéẻẽẹêếềểễệìíỉĩịòóỏõọôốồổỗộơớờởỡợùúủũụưứừửữựỳýỷỹỵ\s]+)/i);
        return productMatch ? productMatch[2].trim() : null;
    }
    return null;
}

// Trích xuất thành phần (ingredients)
function extractIngredients(message) {
    const ingredients = [
        { keyword: 'gà', value: 'gà' },
        { keyword: 'bò', value: 'bò' },
        { keyword: 'tôm', value: 'tôm' },
        { keyword: 'cá', value: 'cá' },
        { keyword: 'rau', value: 'rau' },
        { keyword: 'heo', value: 'heo' },
        { keyword: 'hải sản', value: 'hải sản' }
    ];
    const foundIngredients = ingredients.filter(i =>
        (message.includes('có') || message.includes('chứa')) && message.includes(i.keyword)
    ).map(i => i.value);
    return foundIngredients.length > 0 ? foundIngredients : null;
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