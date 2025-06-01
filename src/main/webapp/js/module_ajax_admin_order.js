function tableOrder(option, page) {
    // Tiếp tục thực hiện AJAX
    $.ajax({
        url: "ajaxordermanagement",
        type: "get",
        data: {
            option: option,
            page: page
        },
        success: function (data) {
            var row = document.getElementById("ajax-section");
            row.innerHTML = data;
        },
        error: function () {
            alert("Failed to load data!");
        }
    });
}
$('#searchForm').on('submit', function (e) {
    e.preventDefault(); // Chặn submit form

    const searchValue = $('#searchInput').val().trim();
    tableOrder(searchValue, 1); // Gọi hiển thị bảng
    pagi(searchValue, 1);       // Gọi phân trang
});
