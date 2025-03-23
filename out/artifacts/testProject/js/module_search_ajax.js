function searchByName(param){
    var txtSearch= param.value;
    $.ajax({
        url: "AjaxSearchController",
        type:"get",
        data: {
            text: txtSearch
        },
        success: function (data){
            var row = document.getElementById("content_section");
            row.innerHTML = data;

        }
    })
}