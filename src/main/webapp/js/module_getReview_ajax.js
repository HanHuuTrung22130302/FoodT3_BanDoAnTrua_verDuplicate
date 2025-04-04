
function ajaxGetReviewFID(fid,option){

    $.ajax({
        url: "AjaxControllerReviewFID",
        type:"get",
        data: {
            text1: fid,
            text2: option
        },
        success: function (data){
            var row = document.getElementById("review-list"+fid);
            row.innerHTML = data;
        }
    })
}
function scrollToTop(foodId) {
    const popup = document.getElementById(foodId);
    const popupBody = popup.querySelector(".popup-body");

    popupBody.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}
function scrollToReviewList(foodId) {
    const reviewList = document.getElementById("scrollbody" + foodId);
    reviewList.scrollIntoView({
        behavior: "smooth",
        block: "start"  // Đảm bảo phần tử nằm ở trên cùng của cửa sổ khi cuộn
    });
}
