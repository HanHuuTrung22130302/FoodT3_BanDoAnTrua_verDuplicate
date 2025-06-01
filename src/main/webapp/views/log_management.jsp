<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý log</title>
    <link
      href="${pageContext.request.contextPath}/Images/LOGO_V2.png"
      rel="icon"
      type="image/x-icon"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/log_management.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  </head>

  <body>
    <div class="container">
      <jsp:include page="leftAdmin.jsp"></jsp:include>

      <div class="content">
        <div class="header">
          <form id="searchForm">
            <select name="filterRoleId" id="filterRoleId">
              <option value="all" ${selectedRoleId == 'all' ? 'selected' : ''}>Tất cả vai trò</option>
              <option value="1" ${selectedRoleId == '1' ? 'selected' : ''}>Admin</option>
              <option value="2" ${selectedRoleId == '2' ? 'selected' : ''}>Người dùng</option>
            </select>
            <input type="date" name="filterDate" id="filterDate" value="${selectedDate}" />
            <input
              type="text"
              name="filterAction"
              id="filterAction"
              value="${selectedAction}"
              placeholder="Tìm kiếm hành động"
            />
            <button type="button" id="searchButton"><i class="fa-solid fa-search"></i></button>
          </form>
        </div>

        <div id="loadingIndicator" style="display: none; text-align: center; margin: 20px;">
          <i class="fa-solid fa-spinner fa-spin fa-2x"></i>
          <p>Đang tải dữ liệu...</p>
        </div>

        <div id="logTable">
          <table>
            <thead>
              <tr>
                <th>Thời gian</th>
                <th>ID Người dùng</th>
                <th>Vai trò</th>
                <th>Hành động</th>
                <th>Kết quả</th>
                <th>Chi tiết</th>
              </tr>
            </thead>
            <tbody id="logTableBody">
              <c:if test="${empty logs}">
                <tr>
                  <td colspan="6" style="text-align: center">
                    Không có dữ liệu log
                  </td>
                </tr>
              </c:if>
              <c:forEach var="log" items="${logs}">
                <tr>
                  <td>${log.timestamp}</td>
                  <td>${log.accountId}</td>
                  <td>${log.roleName}</td>
                  <td>${log.action}</td>
                  <td>${log.result}</td>
                  <td>${log.details}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
          <c:if test="${not empty logs and logs.size() >= 20 and showAll != 'true'}">
            <div style="text-align: center; margin-top: 20px;">
              <button id="loadMoreBtn" class="load-more-btn">Xem thêm</button>
            </div>
          </c:if>
        </div>
      </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/log_management.js"> </script>
  </body>
</html>
