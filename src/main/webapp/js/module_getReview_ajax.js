function ajaxGetReview(fid,option){

    $.ajax({
        url: "AjaxControllerReview",
        type:"get",
        data: {
            text1: fid,
            text2: option
        },
        success: function (data){
            var row = document.getElementById("review-list");
            row.innerHTML = data;
        }
    })
}