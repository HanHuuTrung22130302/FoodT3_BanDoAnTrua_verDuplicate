<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Food T^3</title>
    <link href="${pageContext.request.contextPath}/Images/LOGO_V2.png" rel="icon" type="image/x-icon"/>


    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/newhome.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/contact.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signinCssModule.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
    <script src="../js/module_dangnhap.js" defer></script>
    <style>
        .input-field::placeholder {
            color: #ccc;
        }

        .input-field.error::placeholder {
            color: red;
        }

    </style>
</head>

<body>

<jsp:include page="header.jsp"></jsp:include>

<!-- Xử lý content\  -->

<div class="container">
    <div class="form-section">
        <h2>Liên hệ với chúng tôi</h2>

        <form id="contactForm" action="contactcontrolle" method="post">

            <div class="form-group">
                <label for="name">Họ và Tên *</label>
                <input
                        type="text"
                        id="name"
                        name="name"
                        placeholder="Nhập tên của bạn."
                        class="input-field"
                />
            </div>
            <div class="form-group">
                <label for="email">Email *</label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="Nhập địa chỉ email của bạn."
                        class="input-field"
                />
            </div>
            <div class="form-group">
                <label for="title">Tiêu đề *</label>
                <input
                        type="text"
                        id="title"
                        name="title"
                        placeholder="Nhập chủ đề."
                        class="input-field"
                />
            </div>
            <div class="form-group">
                <label for="message">Nội dung *</label>
                <textarea
                        id="message"
                        name="message"
                        placeholder="Nhập yêu cầu của bạn."
                        class="input-field"
                ></textarea>
            </div>
            <button type="submit" class="submit-btn">Gửi</button>
        </form>
    </div>
    <div class="containerright">
        <iframe
                src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3918.214525515973!2d106.78918677573684!3d10.871281657435551!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3175276398969f7b%3A0x9672b7efd0893fc4!2zVHLGsOG7nW5nIMSQ4bqhaSBo4buNYyBOw7RuZyBMw6JtIFRQLiBI4buTIENow60gTWluaA!5e0!3m2!1svi!2s!4v1704294427338!5m2!1svi!2s"
                height="600" width="620px" style="border: none;" allowfullscreen="" aria-hidden="false"
                tabindex="0"></iframe>
    </div>

</div>
<div id="container-monban">
    <div class="header-mon">

        <div class="tag">Hãy đến với chúng tôi vì chúng tôi có</div>

    </div>
    <div class="content_section_bottom">
        <div class="card_bottom">
            <img src="Images/home/pic1.jpg"/>
            <div class="text">Nguyên liệu tươi mới</div>
        </div>
        <div class="card_bottom">
            <img src="Images/home/pic2.jpg"/>
            <div class="text">Món ăn ngon miệng</div>
        </div>
        <div class="card_bottom">
            <img src="Images/home/pic3.jpg"/>
            <div class="text">Đóng gói tiện lợi</div>
        </div>
        <div class="card_bottom">
            <img src="Images/home/pic4.jpg"/>
            <div class="text">Giao hàng tận nơi</div>
        </div>

    </div>
</div>

<!-- Xử lý footer -->
<jsp:include page="footer.jsp"></jsp:include>

<script src="${pageContext.request.contextPath}/js/home.js">
</script>
</body>

</html>