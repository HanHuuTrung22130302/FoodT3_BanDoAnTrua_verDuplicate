<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />
    <title>Đăng nhập</title>
    <link href="Images/LOGO_V2.png" rel="icon" type="image/x-icon" />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/signin.css"
    />
  </head>
  <body>
    <div class="container" id="container">
      <div class="form-container sign-up-container">
        <form action="signup" method="post">
          <a href="home" class="back-to-home"
            ><i class="fas fa-arrow-left"></i
          ></a>
          <h1>Tạo tài khoản</h1>
          <div class="social-container">
            <a href="#" class="social"> <i class="fab fa-facebook-f"></i></a>
            <a
              href="https://accounts.google.com/o/oauth2/auth?scope=email profile openid&redirect_uri=http://foodt3.online/loginGoogle&response_type=code&client_id=165264526065-7cooqsg6h3rda6ouocf2aq0fvqi5fcjh.apps.googleusercontent.com&approval_prompt=force"
              class="social"
            >
              <i class="fab fa-google-plus-g"></i
            ></a>
            <a href="#" class="social"><i class="fa-brands fa-twitter"></i></a>
          </div>
          <span>hoặc sử dụng email của bạn</span>
          <input name="name" type="text" placeholder="Tên đăng nhập" required />
          <input name="email" type="email" placeholder="Email" required />
          <input name="pass" type="password" placeholder="Mật khẩu" required />
          <div
            id="messageContainer"
            style="color: red; margin-top: 10px; text-align: center"
          ></div>
          <button type="submit">Đăng ký</button>
        </form>
      </div>
      <div class="form-container sign-in-container">
        <form action="login" method="post">
          <a href="${pageContext.request.contextPath}/home" class="back-to-home"
            ><i class="fas fa-arrow-left"></i
          ></a>
          <h1>Đăng nhập</h1>
          <div class="social-container">
            <a href="#" class="social"> <i class="fab fa-facebook-f"></i></a>
            <a
              href="https://accounts.google.com/o/oauth2/auth?scope=email profile openid&redirect_uri=http://foodt3.online/loginGoogle&response_type=code&client_id=165264526065-7cooqsg6h3rda6ouocf2aq0fvqi5fcjh.apps.googleusercontent.com&approval_prompt=force"
              class="social"
            >
              <i class="fab fa-google-plus-g"></i
            ></a>
            <a href="#" class="social"><i class="fa-brands fa-twitter"></i></a>
          </div>
          <span>Hoặc sử dụng tài khoản</span>
          <input
            name="user"
            type="text"
            placeholder="Tên đăng nhập hoặc Email"
            required
          />
          <input name="pass" type="password" placeholder="Mật khẩu" required />
          <div
            id="login_messageContainer"
            style="color: red; margin-top: 10px; text-align: center"
          ></div>
          <a href="forgotpass">Bạn quên mật khẩu?</a>
          <button type="submit">Đăng nhập</button>
        </form>
      </div>
      <div class="overlay-container">
        <div class="overlay">
          <div class="overlay-panel overlay-left">
            <h1>Chào mừng trở lại!</h1>
            <p>Để không bỏ lỡ những ưu đãi hấp dẫn, đăng nhập ngay!</p>
            <button class="ghost" id="signIn">Đăng nhập</button>
          </div>
          <div class="overlay-panel overlay-right">
            <h1>Hello, Friends!</h1>
            <p>
              Đăng ký và bắt đầu hành trình khai phá vị giác ngay cùng chúng
              tôi!
            </p>
            <button class="ghost" id="signUp">Đăng ký</button>
          </div>
        </div>
      </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/signin.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/loginAjax.js"></script>
    <script src="${pageContext.request.contextPath}/js/signUpAjax.js"></script>
  </body>
</html>
