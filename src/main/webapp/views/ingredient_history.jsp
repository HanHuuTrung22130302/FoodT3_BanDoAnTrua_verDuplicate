<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Lịch sử nhập/xuất nguyên liệu</title>
    <link rel="icon" href="${pageContext.request.contextPath}/Images/LOGO_V2.png" type="image/x-icon"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/suppliers.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <style>
        .notification {
            position: fixed;
            top: 20px;
            right: 20px;
            color: #fff;
            padding: 15px 20px;
            border-radius: 8px;
            z-index: 9999;
            display: none;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            table-layout: auto;
        }
        table th, table td {
            padding: 15px;
            text-align: left;
            border: 1px solid #ddd;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        table th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        table td {
            max-width: 200px;
        }
        .header {
            margin-bottom: 20px;
        }
        .header form {
            display: flex;
            gap: 10px;
        }
        .header select, .header input {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .header button {
            padding: 8px 15px;
            background: #28a745;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .header button:hover {
            background: #218838;
        }
        .content {
            overflow-x: auto;
        }
        .charts-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-bottom: 20px;
        }
        .chart-box {
            flex: 1;
            min-width: 300px;
            max-width: 500px;
            background: #fff;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        canvas {
            max-width: 100%;
        }
    </style>
</head>
<body>
<div class="container">
    <jsp:include page="leftAdmin.jsp"></jsp:include>

    <!-- Popup thông báo -->
    <div id="notificationPopup" class="notification"></div>

    <div class="content">
        <div class="header">
            <form action="${pageContext.request.contextPath}/IngredientHistory" method="get">
                <select name="type" onchange="this.form.submit()">
                    <option value="all" ${param.type == 'all' || param.type == null ? 'selected' : ''}>Tất cả</option>
                    <option value="import" ${param.type == 'import' ? 'selected' : ''}>Nhập hàng</option>
                    <option value="export" ${param.type == 'export' ? 'selected' : ''}>Xuất hàng</option>
                </select>
                <input type="text" name="search" value="${param.search}" placeholder="Tìm kiếm theo id, tên nguyên liệu, nhà cung cấp..."/>
                <button type="submit"><i class="fa-solid fa-search"></i></button>
            </form>
        </div>

        <!-- Biểu đồ -->
        <div class="charts-container">
            <div class="chart-box">
                <h3>Nguyên liệu sắp hết hạn</h3>
                <canvas id="nearlyExpiredChart"></canvas>
            </div>
            <div class="chart-box">
                <h3>Số lượng nhập/xuất</h3>
                <canvas id="quantityChart"></canvas>
            </div>
            <div class="chart-box">
                <h3>Giá trị nhập/xuất</h3>
                <canvas id="valueChart"></canvas>
            </div>
        </div>

        <!-- Bảng lịch sử -->
        <table>
            <thead>
            <tr>
                <th>STT</th>
                <th>LOẠI</th>
                <th>TÊN NGUYÊN LIỆU</th>
                <th>TÊN NHÀ CUNG CẤP</th>
                <th>SỐ LƯỢNG (kg)</th>
                <th>GIÁ (triệu)</th>
                <th>NGÀY</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${historyList}">
                <tr>
                    <td>${item.ingredientId}</td>
                    <td>${item.importDate != null ? 'Nhập' : 'Xuất'}</td>
                    <td>${item.ingredientName}</td>
                    <td>${item.supplierName}</td>
                    <td>${item.amount}</td>
                    <td>${item.price}</td>
                    <td>${item.importDate != null ? item.importDate : item.expirationDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    // Hiển thị thông báo
    function showNotification(message, type) {
        const popup = document.getElementById("notificationPopup");
        popup.textContent = message;
        popup.style.display = "block";
        popup.style.backgroundColor = (type === 'success') ? "green" : "red";
        setTimeout(() => {
            popup.style.display = "none";
        }, 3000);
    }

    // Dữ liệu cho biểu đồ
    const nearlyExpiredData = JSON.parse('${nearlyExpiredJson}');
    const importByDate = JSON.parse('${importByDateJson}');
    const exportByDate = JSON.parse('${exportByDateJson}');
    const totalImportValue = ${totalImportValue};
    const totalExportValue = ${totalExportValue};
    const totalImportAmount = ${totalImportAmount};
    const totalExportAmount = ${totalExportAmount};

    // 1. Biểu đồ nguyên liệu sắp hết hạn (biểu đồ cột)
    const nearlyExpiredLabels = nearlyExpiredData.map(item => item.ingredientName);
    const nearlyExpiredAmounts = nearlyExpiredData.map(item => item.amount);
    new Chart(document.getElementById('nearlyExpiredChart'), {
        type: 'bar',
        data: {
            labels: nearlyExpiredLabels,
            datasets: [{
                label: 'Số lượng (kg)',
                data: nearlyExpiredAmounts,
                backgroundColor: 'rgba(255, 99, 132, 0.5)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Số lượng (kg)'
                    }
                }
            },
            plugins: {
                legend: {
                    display: true
                }
            }
        }
    });

    // 2. Biểu đồ số lượng nhập/xuất theo thời gian (biểu đồ đường)
    const dates = [...new Set([...Object.keys(importByDate), ...Object.keys(exportByDate)])].sort();
    const importAmounts = dates.map(date => importByDate[date] || 0);
    const exportAmounts = dates.map(date => exportByDate[date] || 0);
    new Chart(document.getElementById('quantityChart'), {
        type: 'line',
        data: {
            labels: dates,
            datasets: [
                {
                    label: 'Nhập hàng (kg)',
                    data: importAmounts,
                    borderColor: 'rgba(54, 162, 235, 1)',
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    fill: true
                },
                {
                    label: 'Xuất hàng (kg)',
                    data: exportAmounts,
                    borderColor: 'rgba(255, 159, 64, 1)',
                    backgroundColor: 'rgba(255, 159, 64, 0.2)',
                    fill: true
                }
            ]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Số lượng (kg)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Ngày'
                    }
                }
            },
            plugins: {
                legend: {
                    display: true
                }
            }
        }
    });

    // 3. Biểu đồ giá trị nhập/xuất (biểu đồ cột)
    new Chart(document.getElementById('valueChart'), {
        type: 'bar',
        data: {
            labels: ['Nhập hàng', 'Xuất hàng'],
            datasets: [{
                label: 'Giá trị (triệu)',
                data: [totalImportValue, totalExportValue],
                backgroundColor: ['rgba(75, 192, 192, 0.5)', 'rgba(153, 102, 255, 0.5)'],
                borderColor: ['rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)'],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Giá trị (triệu)'
                    }
                }
            },
            plugins: {
                legend: {
                    display: true
                }
            }
        }
    });

    // 4. Biểu đồ tỷ lệ nhập/xuất (biểu đồ tròn)
    new Chart(document.getElementById('ratioChart'), {
        type: 'pie',
        data: {
            labels: ['Nhập hàng', 'Xuất hàng'],
            datasets: [{
                label: 'Số lượng (kg)',
                data: [totalImportAmount, totalExportAmount],
                backgroundColor: ['rgba(255, 206, 86, 0.5)', 'rgba(255, 99, 132, 0.5)'],
                borderColor: ['rgba(255, 206, 86, 1)', 'rgba(255, 99, 132, 1)'],
                borderWidth: 1
            }]
        },
        options: {
            plugins: {
                legend: {
                    display: true
                }
            }
        }
    });

    // Hiển thị thông báo khi tải trang
    window.onload = function () {
        var successMsg = "${param.success}";
        var errorMsg = "${param.error}";
        if (successMsg) showNotification(successMsg, "success");
        if (errorMsg) showNotification("Lỗi: " + errorMsg, "error");
    };
</script>
</body>
</html>