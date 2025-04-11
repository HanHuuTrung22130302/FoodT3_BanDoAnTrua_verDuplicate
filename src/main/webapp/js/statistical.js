// Biểu đồ doanh thu
function initRevenueChart(dayRevenue, weekRevenue, monthRevenue) {
  const revenueCtx = document.getElementById("revenueChart").getContext("2d");
  new Chart(revenueCtx, {
    type: "line",
    data: {
      labels: ["Ngày", "Tuần", "Tháng"],
      datasets: [
        {
          label: "Doanh thu",
          data: [dayRevenue, weekRevenue, monthRevenue],
          borderColor: "rgb(75, 192, 192)",
          tension: 0.1,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Doanh thu theo thời gian",
        },
      },
    },
  });
}

// Biểu đồ số đơn hàng
function initOrdersChart(dayOrders, weekOrders, monthOrders) {
  const ordersCtx = document.getElementById("ordersChart").getContext("2d");
  new Chart(ordersCtx, {
    type: "bar",
    data: {
      labels: ["Ngày", "Tuần", "Tháng"],
      datasets: [
        {
          label: "Số đơn hàng",
          data: [dayOrders, weekOrders, monthOrders],
          backgroundColor: "rgb(54, 162, 235)",
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Số đơn hàng theo thời gian",
        },
      },
    },
  });
}

// Biểu đồ sản phẩm bán chạy
function initBestSellingChart(labels, data) {
  const bestSellingCtx = document
    .getElementById("bestSellingChart")
    .getContext("2d");
  new Chart(bestSellingCtx, {
    type: "pie",
    data: {
      labels: labels,
      datasets: [
        {
          data: data,
          backgroundColor: [
            "rgb(255, 99, 132)",
            "rgb(54, 162, 235)",
            "rgb(255, 205, 86)",
            "rgb(75, 192, 192)",
            "rgb(153, 102, 255)",
          ],
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Tỷ lệ bán của sản phẩm bán chạy",
        },
      },
    },
  });
}

// Biểu đồ sản phẩm bán chậm
function initSlowSellingChart(labels, data) {
  const slowSellingCtx = document
    .getElementById("slowSellingChart")
    .getContext("2d");
  new Chart(slowSellingCtx, {
    type: "pie",
    data: {
      labels: labels,
      datasets: [
        {
          data: data,
          backgroundColor: [
            "rgb(255, 99, 132)",
            "rgb(54, 162, 235)",
            "rgb(255, 205, 86)",
            "rgb(75, 192, 192)",
            "rgb(153, 102, 255)",
          ],
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Tỷ lệ bán của sản phẩm bán chậm",
        },
      },
    },
  });
}

// Biểu đồ sản phẩm không bán được
function initUnsoldChart(labels, data) {
  const unsoldCtx = document.getElementById("unsoldChart").getContext("2d");
  new Chart(unsoldCtx, {
    type: "pie",
    data: {
      labels: labels,
      datasets: [
        {
          data: data,
          backgroundColor: [
            "rgb(255, 99, 132)",
            "rgb(54, 162, 235)",
            "rgb(255, 205, 86)",
            "rgb(75, 192, 192)",
            "rgb(153, 102, 255)",
          ],
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Sản phẩm không bán được theo danh mục",
        },
      },
    },
  });
}

document.addEventListener("DOMContentLoaded", function () {
  // Biểu đồ doanh thu
  const revenueCtx = document.getElementById("revenueChart").getContext("2d");
  new Chart(revenueCtx, {
    type: "line",
    data: {
      labels: ["Ngày", "Tuần", "Tháng"],
      datasets: [
        {
          label: "Doanh thu",
          data: [
            parseInt(document.getElementById("dayRevenue").value),
            parseInt(document.getElementById("weekRevenue").value),
            parseInt(document.getElementById("monthRevenue").value),
          ],
          borderColor: "rgb(75, 192, 192)",
          tension: 0.1,
          fill: true,
          backgroundColor: "rgba(75, 192, 192, 0.1)",
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Doanh thu theo thời gian",
          font: {
            size: 16,
          },
        },
        legend: {
          display: false,
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            callback: function (value) {
              return value.toLocaleString("vi-VN") + " đ";
            },
          },
        },
      },
    },
  });

  // Biểu đồ số đơn hàng
  const ordersCtx = document.getElementById("ordersChart").getContext("2d");
  new Chart(ordersCtx, {
    type: "bar",
    data: {
      labels: ["Ngày", "Tuần", "Tháng"],
      datasets: [
        {
          label: "Số đơn hàng",
          data: [
            parseInt(document.getElementById("dayOrders").value),
            parseInt(document.getElementById("weekOrders").value),
            parseInt(document.getElementById("monthOrders").value),
          ],
          backgroundColor: "rgba(54, 162, 235, 0.8)",
          borderColor: "rgb(54, 162, 235)",
          borderWidth: 1,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Số đơn hàng theo thời gian",
          font: {
            size: 16,
          },
        },
        legend: {
          display: false,
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            stepSize: 1,
          },
        },
      },
    },
  });

  // Biểu đồ sản phẩm bán chạy
  const bestSellingCtx = document
    .getElementById("bestSellingChart")
    .getContext("2d");
  const bestSellingData = JSON.parse(
    document.getElementById("bestSellingData").value
  );
  new Chart(bestSellingCtx, {
    type: "pie",
    data: {
      labels: bestSellingData.map((item) => item.name),
      datasets: [
        {
          data: bestSellingData.map((item) => item.quantity),
          backgroundColor: [
            "rgba(255, 99, 132, 0.8)",
            "rgba(54, 162, 235, 0.8)",
            "rgba(255, 206, 86, 0.8)",
            "rgba(75, 192, 192, 0.8)",
            "rgba(153, 102, 255, 0.8)",
          ],
          borderColor: [
            "rgb(255, 99, 132)",
            "rgb(54, 162, 235)",
            "rgb(255, 206, 86)",
            "rgb(75, 192, 192)",
            "rgb(153, 102, 255)",
          ],
          borderWidth: 1,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Tỷ lệ bán của sản phẩm bán chạy",
          font: {
            size: 16,
          },
        },
        legend: {
          position: "right",
        },
      },
    },
  });

  // Biểu đồ sản phẩm bán chậm
  const slowSellingCtx = document
    .getElementById("slowSellingChart")
    .getContext("2d");
  const slowSellingData = JSON.parse(
    document.getElementById("slowSellingData").value
  );
  new Chart(slowSellingCtx, {
    type: "pie",
    data: {
      labels: slowSellingData.map((item) => item.name),
      datasets: [
        {
          data: slowSellingData.map((item) => item.quantity),
          backgroundColor: [
            "rgba(255, 99, 132, 0.8)",
            "rgba(54, 162, 235, 0.8)",
            "rgba(255, 206, 86, 0.8)",
            "rgba(75, 192, 192, 0.8)",
            "rgba(153, 102, 255, 0.8)",
          ],
          borderColor: [
            "rgb(255, 99, 132)",
            "rgb(54, 162, 235)",
            "rgb(255, 206, 86)",
            "rgb(75, 192, 192)",
            "rgb(153, 102, 255)",
          ],
          borderWidth: 1,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Tỷ lệ bán của sản phẩm bán chậm",
          font: {
            size: 16,
          },
        },
        legend: {
          position: "right",
        },
      },
    },
  });

  // Biểu đồ sản phẩm không bán được
  const unsoldCtx = document.getElementById("unsoldChart").getContext("2d");
  const unsoldData = JSON.parse(document.getElementById("unsoldData").value);
  new Chart(unsoldCtx, {
    type: "pie",
    data: {
      labels: unsoldData.map((item) => item.name),
      datasets: [
        {
          data: unsoldData.map((item) => 1),
          backgroundColor: [
            "rgba(255, 99, 132, 0.8)",
            "rgba(54, 162, 235, 0.8)",
            "rgba(255, 206, 86, 0.8)",
            "rgba(75, 192, 192, 0.8)",
            "rgba(153, 102, 255, 0.8)",
          ],
          borderColor: [
            "rgb(255, 99, 132)",
            "rgb(54, 162, 235)",
            "rgb(255, 206, 86)",
            "rgb(75, 192, 192)",
            "rgb(153, 102, 255)",
          ],
          borderWidth: 1,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: "Sản phẩm không bán được theo danh mục",
          font: {
            size: 16,
          },
        },
        legend: {
          position: "right",
        },
      },
    },
  });
});
