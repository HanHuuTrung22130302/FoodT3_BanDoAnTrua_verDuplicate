<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Food T^3</title>
    <link href="Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/about.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signinCssModule.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/module_header_footer.css"/>
    <script src="${pageContext.request.contextPath}/js/module_dangnhap.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
</head>

<body>
<jsp:include page="header.jsp"></jsp:include>

<!-- Xử lý content\  -->
<div class="container">
    <div class="content_dis">
        <div class="left_content">
            <p>
                Chào mừng bạn đến với Food T^3 nơi mang đến những bữa trưa thơm
                ngon, đầy đủ dinh dưỡng và tràn đầy năng lượng cho một ngày làm việc
                hiệu quả!
            </p>
        </div>
        <div class="right_content">
            <video autoplay muted loop>

                <source src="${pageContext.request.contextPath}/Videos/%5Bappsgolem.com%5D%5B00-00-00%5D%5B00-00-16%5D_Miami_s_Best_New_Chef_is_Mak.mp4">
                type="video/mp4"

                Trình duyệt của bạn không hỗ trợ video.
            </video>
        </div>
    </div>

    <div class="allInfo">
        <div class="text2">Quản Lý Cửa Hàng</div>
        <div class="card-container">
            <div class="card-info">
                <img src="${pageContext.request.contextPath}/Images/team/trung_han.jpg"" alt="Hình ảnh cá nhân"/>
                <div class="info">
                    <h3>Hán Hữu Trung</h3>
                    <p>MSSV: 22130302</p>
                    <p>Lớp: DH22DTC</p>
                </div>
            </div>

            <div class="card-info">
                <img
                        src="<c:url value="/Images/team/tuan_22130311.jpg"/>"
                        alt="Hình ảnh cá nhân"
                />
                <div class="info">
                    <h3>Nguyễn Anh Tuấn</h3>
                    <p>MSSV: 22130311</p>
                    <p>Lớp: DH22DTC</p>
                </div>
            </div>

            <div class="card-info">
                <img
                        src="<c:url value="/Images/team/tuan_22130312.jpg"/> "
                        alt="Hình ảnh cá nhân"
                />
                <div class="info">
                    <h3>Nguyễn Anh Tuấn</h3>
                    <p>MSSV: 22130312</p>
                    <p>Lớp: DH22DTA</p>
                </div>
            </div>
        </div>
    </div>
</div>

</div>
<div id="container-monban">
    <div class="header-mon">

        <div class="tag">Hãy đến với chúng tôi vì chúng tôi có</div>

    </div>
    <div class="content_section_bottom">
        <div class="card_bottom">
            <img src="<c:url value="/Images/home/pic1.jpg"/> "/>
            <div class="text">Nguyên liệu tươi mới</div>
        </div>
        <div class="card_bottom">
            <img src="<c:url value="/Images/home/pic2.jpg"/>"/>
            <div class="text">Món ăn ngon miệng</div>
        </div>
        <div class="card_bottom">
            <img src="<c:url value="/Images/home/pic3.jpg"/>"/>
            <div class="text">Đóng gói tiện lợi</div>
        </div>
        <div class="card_bottom">
            <img src="<c:url value="/Images/home/pic4.jpg"/> "/>
            <div class="text">Giao hàng tận nơi</div>
        </div>

    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>

<script src="${pageContext.request.contextPath}/js/home.js"></script>
<script src="${pageContext.request.contextPath}/js/module_popup_home.js"></script>

</body>
</html>
