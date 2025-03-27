function getU(idFood){
    $.ajax({
        url: "ViewUController",
        type:"get",
        data: {
            idFood: idFood
        },
        success: function (data){
        }
    })
}