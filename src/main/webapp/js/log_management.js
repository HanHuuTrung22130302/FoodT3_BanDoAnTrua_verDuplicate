$(document).ready(function() {
    // Hàm xử lý tìm kiếm
    function searchLogs() {
        const roleId = $('#filterRoleId').val();
        const date = $('#filterDate').val();
        const action = $('#filterAction').val();

        // Hiển thị loading indicator
        $('#loadingIndicator').show();
        $('#logTable').hide();

        $.ajax({
            url: 'LogManagement',
            type: 'GET',
            data: {
                filterRoleId: roleId,
                filterDate: date,
                filterAction: action
            },
            success: function(response) {
                // Cập nhật URL mà không reload trang
                const newUrl = new URL(window.location.href);
                newUrl.searchParams.set('filterRoleId', roleId);
                newUrl.searchParams.set('filterDate', date);
                newUrl.searchParams.set('filterAction', action);
                window.history.pushState({}, '', newUrl);

                // Cập nhật nội dung bảng
                const $newContent = $(response).find('#logTableBody');
                $('#logTableBody').html($newContent.html());

                // Ẩn loading indicator và hiển thị bảng
                $('#loadingIndicator').hide();
                $('#logTable').show();
            },
            error: function(xhr, status, error) {
                console.error('Lỗi khi tải dữ liệu:', error);
                $('#loadingIndicator').hide();
                $('#logTable').show();
                alert('Có lỗi xảy ra khi tải dữ liệu. Vui lòng thử lại sau.');
            }
        });
    }

    // Xử lý sự kiện khi thay đổi vai trò
    $('#filterRoleId').change(function() {
        searchLogs();
    });

    // Xử lý sự kiện khi thay đổi ngày
    $('#filterDate').change(function() {
        searchLogs();
    });

    // Xử lý sự kiện khi nhấn nút tìm kiếm
    $('#searchButton').click(function() {
        searchLogs();
    });

    // Xử lý sự kiện khi nhập vào ô tìm kiếm hành động
    let searchTimeout;
    $('#filterAction').on('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(function() {
            searchLogs();
        }, 500); // Đợi 500ms sau khi người dùng ngừng nhập
    });
});