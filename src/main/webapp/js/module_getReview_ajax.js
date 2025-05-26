function ajaxGetReviewFID(fid, option = null) {
    const reviewContainer = document.getElementById("review-list" + fid);
    const currentOption = parseInt(reviewContainer.dataset.option || "0");

    // Nếu không truyền option, mặc định giữ nguyên option hiện tại
    if (option === null) {
        option = currentOption;
        const loadMoreBtn = document.getElementById("loadMoreBtn" + fid);
        if (loadMoreBtn) loadMoreBtn.style.display = "inline-block";
    }

    // Tính số lượng review hiện có để gửi lên server (dùng để tính offset)
    const amount = document.getElementsByClassName("countFragmentReview" + fid).length;
    const optioncount = document.getElementsByClassName("option" + option).length;


    $.ajax({
        url: "AjaxControllerReviewFID",
        type: "get",
        data: {
            text1: fid,
            text2: option,
            exits: amount,
            curOption: currentOption
        },
        success: function (data) {
            if (option !== currentOption) {
                reviewContainer.innerHTML = data;
                reviewContainer.dataset.option = option;
            } else {
                reviewContainer.innerHTML += data;
            }

            const loadMoreBtn = document.getElementById("loadMoreBtn" + fid);
            if (data.includes("endOfReviewFlag")) {
                if (loadMoreBtn) loadMoreBtn.style.display = "none";
            } else {
                if (loadMoreBtn) loadMoreBtn.style.display = "inline-block";
            }

        }
    });
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

function setOption(fid, option) {
    const reviewContainer = document.getElementById("review-list" + fid);
    reviewContainer.dataset.option = option;
    ajaxGetReviewFID(fid, option);
}
