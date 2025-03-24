<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><html>
<head>
  <meta charset="UTF-8"/>
  <meta
          name="viewport"
          content="width=
    , initial-scale=1.0"
  />
  <link
          rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
  />
  <link
          rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
  />
  <title>Xác thực email</title>
  <link href='Images/LOGO_V2.png' rel='icon' type='image/x-icon'/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signin.css"/>
</head>
<body>
  <h1>Xác thực email</h1>
  <p>${message}</p>
  <a href=login>Quay lại đăng nhập</a>
</body>
</html>
