-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 07, 2025 at 12:04 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `foodt3`
--

-- --------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `foodt3` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `account_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(150) NOT NULL,
  `role_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `failed_attempts` int(11) DEFAULT 0,
  `is_locked` tinyint(1) DEFAULT 0,
  `lock_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `login_type` varchar(20) DEFAULT 'normal',
  `is_deleted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`account_id`, `name`, `password`, `email`, `role_id`, `created_at`, `updated_at`, `failed_attempts`, `is_locked`, `lock_time`, `login_type`, `is_deleted`) VALUES
(1, 'owner', '81dc9bdb52d04dc20036dbd8313ed055', 'ownert3@gmail.com', 3, '2025-03-27 18:02:41', '2025-03-27 18:02:41', 0, 0, '2025-03-27 11:02:41', 'normal', 0),
(2, 'Tuan11_admin', '81dc9bdb52d04dc20036dbd8313ed055', '22130311@st.hcmuaf.edu.vn', 1, '2025-03-27 18:02:41', '2025-03-27 18:02:41', 0, 0, '2025-03-27 11:02:41', 'normal', 0),
(3, 'Trung21_admin', '81dc9bdb52d04dc20036dbd8313ed055', '22130221@st.hcmuaf.edu.vn', 1, '2025-03-27 18:02:41', '2025-03-27 18:02:41', 0, 0, '2025-03-27 11:02:41', 'normal', 0),
(4, 'Tuan12_admin', '81dc9bdb52d04dc20036dbd8313ed055', '22130312@st.hcmuaf.edu.vn', 1, '2025-03-27 18:02:41', '2025-04-07 02:06:53', 0, 0, '2025-04-06 19:06:53', 'normal', 0),
(5, 'Tuan11_user', '81dc9bdb52d04dc20036dbd8313ed055', 'tuan11@gmail.com', 2, '2025-03-27 18:02:41', '2025-04-03 14:26:44', 0, 0, '2025-04-03 07:26:44', 'normal', 0),
(6, 'Trung21_user', '81dc9bdb52d04dc20036dbd8313ed055', 'trung21@gmail.com', 2, '2025-03-27 18:02:41', '2025-04-03 14:26:44', 0, 0, '2025-04-03 07:26:44', 'normal', 0),
(7, 'Tuan12_user', '81dc9bdb52d04dc20036dbd8313ed055', 'tuan12@gmail.com', 2, '2025-03-27 18:02:41', '2025-04-03 14:26:44', 0, 0, '2025-04-03 07:26:44', 'normal', 0);

-- --------------------------------------------------------

--
-- Table structure for table `account_detail`
--

CREATE TABLE `account_detail` (
  `account_id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `address` varchar(255) NOT NULL,
  `gender` tinyint(1) DEFAULT 0,
  `birth_date` date DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `account_detail`
--

INSERT INTO `account_detail` (`account_id`, `full_name`, `phone_number`, `address`, `gender`, `birth_date`, `created_at`, `updated_at`) VALUES
(1, 'Nguyễn Anh Tuấn ', '0869922778', 'Address 1', 1, '2004-01-01', '2024-12-20 17:02:03', '2024-12-20 17:02:03'),
(2, 'Nguyễn Anh Tuấn ', '0901234568', 'Address 2', 1, '2004-02-01', '2024-12-20 17:02:03', '2024-12-20 17:02:03'),
(3, 'Hán Hữu Trung', '0901234569', 'Address 3', 1, '2004-03-01', '2024-12-20 17:02:03', '2024-12-20 17:02:03');

-- --------------------------------------------------------

--
-- Table structure for table `activity_logs`
--

CREATE TABLE `activity_logs` (
  `log_id` int(11) NOT NULL,
  `timestamp` datetime NOT NULL DEFAULT current_timestamp(),
  `account_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `action` varchar(255) NOT NULL,
  `result` varchar(50) NOT NULL,
  `details` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `activity_logs`
--

INSERT INTO `activity_logs` (`log_id`, `timestamp`, `account_id`, `role_id`, `action`, `result`, `details`) VALUES
(20, '2025-03-28 15:37:06', 3, 1, 'Cập nhật món ăn', 'Thành công', 'Mã món ăn: 81'),
(26, '2025-03-28 15:37:20', 3, 1, 'Xóa món ăn', 'Thất bại', 'Mã món ăn: 81'),
(28, '2025-03-28 15:37:33', 3, 1, 'Đăng xuất', 'Thành công', 'Người dùng đã đăng xuất'),
(30, '2025-03-28 15:37:46', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(32, '2025-03-28 15:48:47', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(33, '2025-03-28 15:49:15', 3, 1, 'Thanh toán', 'Thất bại', 'Địa chỉ giao hàng quá xa (> 40.0km)'),
(34, '2025-03-28 15:49:30', 3, 1, 'Thanh toán', 'Thành công', 'Mã đơn hàng: 4, Phương thức: COD, Tổng tiền: 30000'),
(35, '2025-03-28 15:49:50', 3, 1, 'Đăng xuất', 'Thành công', 'Người dùng đã đăng xuất'),
(41, '2025-03-28 17:16:46', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(42, '2025-03-28 17:17:33', 3, 1, 'Đăng xuất', 'Thành công', 'Người dùng đã đăng xuất'),
(44, '2025-03-28 17:50:42', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(45, '2025-03-28 17:52:12', 3, 1, 'Đăng xuất', 'Thành công', 'Người dùng đã đăng xuất'),
(47, '2025-03-28 17:52:16', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(48, '2025-03-28 17:52:43', 3, 1, 'Thanh toán', 'Thành công', 'Mã đơn hàng: 5, Phương thức: COD, Tổng tiền: 60000'),
(50, '2025-03-28 18:21:00', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(62, '2025-03-30 13:37:14', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(63, '2025-03-30 13:37:33', 3, 1, 'Đăng xuất', 'Thành công', 'Người dùng đã đăng xuất'),
(64, '2025-03-30 13:39:32', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(65, '2025-03-30 13:39:41', 3, 1, 'Đăng xuất', 'Thành công', 'Người dùng đã đăng xuất'),
(66, '2025-03-30 13:39:47', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(67, '2025-03-30 13:49:05', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(68, '2025-03-30 13:49:12', 3, 1, 'Đăng xuất', 'Thành công', 'Người dùng đã đăng xuất'),
(69, '2025-03-30 13:49:16', 3, 1, 'Đăng nhập', 'Thành công', 'Người dùng đã đăng nhập'),
(70, '2025-03-31 02:15:40', 3, 1, 'Xem danh sách đơn hàng', 'Thành công', 'Trang: 1, Lọc theo: all'),
(71, '2025-03-31 02:15:54', 3, 1, 'Xem danh sách đơn hàng', 'Thành công', 'Trang: 1, Lọc theo: all'),
(72, '2025-04-01 17:27:17', 3, 1, 'Thanh toán', 'Thất bại', 'Địa chỉ giao hàng quá xa (> 40.0km)'),
(73, '2025-04-01 17:27:30', 3, 1, 'Thanh toán', 'Đang xử lý', 'Chuyển hướng đến VNPay, Tổng tiền: 28500'),
(74, '2025-04-01 17:35:22', 3, 1, 'Thanh toán', 'Đang xử lý', 'Chuyển hướng đến VNPay, Tổng tiền: 28500'),
(75, '2025-04-01 17:37:40', 3, 1, 'Xem danh sách đơn hàng', 'Thành công', 'Trang: 1, Lọc theo: all'),
(76, '2025-04-01 17:44:20', 3, 1, 'Thanh toán', 'Thành công', 'Mã đơn hàng: 7, Phương thức: COD, Tổng tiền: 57000'),
(77, '2025-04-01 17:46:12', 3, 1, 'Thanh toán', 'Thành công', 'Mã đơn hàng: 8, Phương thức: COD, Tổng tiền: 27000'),
(78, '2025-04-03 14:07:24', 3, 1, 'Xem danh sách đơn hàng', 'Thành công', 'Trang: 1, Lọc theo: all'),
(79, '2025-04-03 14:07:26', 3, 1, 'Xem danh sách đơn hàng', 'Thành công', 'Trang: 1, Lọc theo: all'),
(80, '2025-04-03 14:07:28', 3, 1, 'Xem danh sách đơn hàng', 'Thành công', 'Trang: 1, Lọc theo: all'),
(81, '2025-04-03 14:21:26', 3, 1, 'Xem danh sách đơn hàng', 'Thành công', 'Trang: 1, Lọc theo: all'),
(82, '2025-04-07 00:19:12', 4, 1, 'Thanh toán', 'Thành công', 'Mã đơn hàng: 9, Phương thức: COD, Tổng tiền: 30000');

-- --------------------------------------------------------

--
-- Table structure for table `banner`
--

CREATE TABLE `banner` (
  `banner_id` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `banner`
--

INSERT INTO `banner` (`banner_id`, `url`, `created_at`) VALUES
(1, 'Images/home/qc1.jpg', '2025-01-10 00:00:00'),
(2, 'Images/home/qc2.jpg', '2025-01-10 00:00:00'),
(3, 'Images/home/qc3.jpg', '2025-01-10 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `cart_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`category_id`, `category_name`, `description`) VALUES
(1, 'Món Cơm', NULL),
(2, 'Món Bún', NULL),
(3, 'Món Phở', NULL),
(4, 'Nước', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `contact`
--

CREATE TABLE `contact` (
  `contact_id` int(11) NOT NULL,
  `account_id` int(11) DEFAULT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `title` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `discount_code`
--

CREATE TABLE `discount_code` (
  `discount_code_id` int(11) NOT NULL,
  `code_name` varchar(20) NOT NULL,
  `discount_rate` decimal(5,2) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `is_active` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `discount_code`
--

INSERT INTO `discount_code` (`discount_code_id`, `code_name`, `discount_rate`, `title`, `description`, `start_date`, `end_date`, `is_active`) VALUES
(1, 'MUANGAY', 0.05, 'Lần đầu đặt hàng', 'GIẢM 5% CHO ĐƠN HÀNG ĐẦU TIÊN', '2025-04-01 00:00:00', '2029-12-30 00:00:00', 1),
(2, 'MUANGAY2', 0.10, '111', '111', '2025-04-01 00:00:00', '2029-07-20 00:00:00', 1),
(3, 'MUANGAY3', 0.10, 'MUA NGAY', 'MUA NGAY', '2025-04-03 00:00:00', '2025-05-11 00:00:00', 1);

-- --------------------------------------------------------

--
-- Table structure for table `discount_usage`
--

CREATE TABLE `discount_usage` (
  `usage_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `discount_code_id` int(11) NOT NULL,
  `used_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `discount_usage`
--

INSERT INTO `discount_usage` (`usage_id`, `account_id`, `discount_code_id`, `used_at`) VALUES
(1, 3, 1, '2025-04-01 10:44:20'),
(2, 3, 2, '2025-04-01 10:46:12');

-- --------------------------------------------------------

--
-- Table structure for table `food`
--

CREATE TABLE `food` (
  `food_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `food_name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `discount_price` decimal(10,2) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `ingredients` text DEFAULT NULL,
  `quantity` int(11) DEFAULT 0,
  `sold` int(11) DEFAULT 0,
  `is_deleted` tinyint(1) DEFAULT 0,
  `views` int(11) DEFAULT 0,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `food`
--

INSERT INTO `food` (`food_id`, `category_id`, `food_name`, `price`, `discount_price`, `image`, `description`, `ingredients`, `quantity`, `sold`, `is_deleted`, `views`, `created_at`, `updated_at`) VALUES
(1, 1, 'Cơm thịt kho trứng', 30000.00, NULL, 'Images/Food/Com/Com-thit-kho-trung.png', 'Cơm thịt kho trứng là món ăn quen thuộc với sự kết hợp giữa thịt kho mềm ngọt và trứng kho thơm, đậm đà. Nước kho từ thịt và trứng tạo thành một món ăn tuyệt vời khi ăn cùng cơm.', 'thịt heo, trứng, nước mắm, cơm', NULL, 100, NULL, 3, '2024-12-20 17:02:06', '2025-03-24 00:52:29'),
(2, 1, 'Cơm chiên dưa bò', 30000.00, NULL, 'Images/Food/Com/Com-chien-dua-bo.png', 'Cơm chiên dưa bò là một món ăn kết hợp hoàn hảo giữa vị béo ngậy của cơm chiên, sự tươi mát của dưa chua và vị ngọt mềm của thịt bò.', 'cơm, dưa chua, thịt bò', NULL, 200, NULL, 3, '2024-12-20 17:02:06', '2025-03-24 00:52:29'),
(3, 1, 'Cơm ba rọi rim tôm', 30000.00, NULL, 'Images/Food/Com/Com-ba-roi-rim-tom.png', 'Cơm ba rọi rim tôm là một món ăn đậm đà hương vị, kết hợp giữa thịt ba rọi heo mềm mịn và tôm tươi ngọt, tạo nên sự hòa quyện hoàn hảo.', 'thịt ba rọi heo, tôm, nước mắm, cơm', NULL, 1953, NULL, 5, '2024-12-20 17:02:06', '2025-04-05 14:34:29'),
(4, 1, 'Cơm cá chiên xả ớt', 30000.00, NULL, 'Images/Food/Com/Com-ca-chien-xa-ot.png', 'Cá chiên xả ớt là món ăn mang đậm hương vị đặc trưng của ẩm thực Việt Nam, với sự kết hợp hài hòa giữa vị ngọt tự nhiên của cá và hương thơm đặc trưng từ sả và ớt.', 'cá, sả, ớt, cơm', NULL, 1611, NULL, 3, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(5, 1, 'Cơm canh chua cá hú', 30000.00, NULL, 'Images/Food/Com/Com-canh-chua-ca-hu.png', 'Cơm canh chua cá hú là một món ăn đậm đà và thanh mát, mang đặc trưng của ẩm thực miền Nam Việt Nam.', 'cá hú, rau cải, cà chua, cơm', NULL, 200, NULL, 2, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(6, 1, 'Cơm chiên bò lúc lắc', 30000.00, NULL, 'Images/Food/Com/Com-chien-bo-luc-lac.png', 'Cơm chiên bò lúc lắc là món ăn nổi bật với những miếng thịt bò mềm, thấm đẫm gia vị, kết hợp với cơm chiên vàng ươm và rau củ giòn ngọt.', 'thịt bò, cơm, rau củ', NULL, 137, NULL, 45, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(7, 1, 'Cơm gà xối mỡ', 30000.00, NULL, 'Images/Food/Com/Com-chien-ga-xoi-mo.png', 'Cơm gà xối mỡ là món ăn truyền thống với thịt gà được chiên giòn rụm, kết hợp cùng cơm trắng dẻo thơm và các loại rau sống tươi mát.', 'thịt gà, cơm, rau sống', NULL, 3000, NULL, 2, '2024-12-20 17:02:06', '2025-04-05 14:35:39'),
(8, 1, 'Cơm mực trứng muối', 30000.00, NULL, 'Images/Food/Com/Com-chien-muc-trung-muoi.png', 'Cơm mực trứng muối là món ăn thơm ngon với mực tươi, trứng muối béo ngậy, hòa quyện cùng cơm chiên mềm và gia vị đậm đà.', 'mực, trứng muối, cơm', NULL, 75, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(9, 1, 'Cơm chiên trứng ốp la', 30000.00, NULL, 'Images/Food/Com/Com-chien-trung-op-la.png', 'Cơm chiên trứng ốp la là món ăn đơn giản nhưng cực kỳ ngon miệng, với cơm chiên thơm dẻo và một quả trứng ốp la vàng ươm.', 'cơm, trứng', NULL, 2500, NULL, NULL, '2024-12-20 17:02:06', '2025-04-05 14:35:26'),
(10, 1, 'Cơm gà kho sả ớt', 30000.00, NULL, 'Images/Food/Com/Com-ga-kho-xa-ot.png', 'Cơm gà kho sả ớt là món ăn đậm đà hương vị với thịt gà thơm ngon, thấm đẫm gia vị sả và ớt cay nồng.', 'thịt gà, sả, ớt, cơm', NULL, 200, NULL, NULL, '2024-12-20 17:02:06', '2025-03-24 00:52:29'),
(11, 1, 'Cơm gà nấu Hải Nam', 30000.00, NULL, 'Images/Food/Com/Com-ga-nau-Hai-Nam.png', 'Cơm gà nấu Hải Nam có nguồn gốc từ Trung Quốc, với thịt gà được nấu mềm mịn và cơm thơm lừng.', 'thịt gà, cơm', NULL, 1945, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(12, 1, 'Cơm khổ qua chả cá', 30000.00, NULL, 'Images/Food/Com/Com-kho-qua-cha-ca.png', 'Món cơm khổ qua chả cá độc đáo với chả cá thơm ngon và khổ qua tươi xanh. Vị đắng nhẹ của khổ qua hòa quyện cùng chả cá mềm ngọt tạo nên sự cân bằng hoàn hảo.', 'khổ qua, chả cá, cơm', NULL, 1529, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(13, 1, 'Cơm sườn cọng mật ong', 30000.00, NULL, 'Images/Food/Com/Com-suon-cong-mat-ong.png', 'Cơm sườn cọng mật ong nổi bật với hương vị ngọt ngào của mật ong thấm vào từng miếng sườn nướng. Sườn được chế biến mềm, không khô, và có lớp ngoài giòn nhẹ. Món này kết hợp tuyệt vời với cơm trắng và nước chấm đặc trưng.', 'sườn heo, mật ong, cơm', NULL, 1800, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(14, 1, 'Cơm sườn rim nước dừa', 30000.00, NULL, 'Images/Food/Com/Com-suon-non-rim-nuoc-dua.png', 'Cơm sườn non rim nước dừa mang đến hương vị đậm đà và béo ngậy của nước dừa. Miếng sườn được nấu mềm, ngấm vị ngọt của dừa và nước mắm, tạo nên một món ăn hấp dẫn, ngon miệng.', 'sườn heo, nước dừa, nước mắm, cơm', NULL, 424, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(15, 1, 'Cơm tấm đùi gà chiên', 30000.00, NULL, 'Images/Food/Com/Com-tam-dui-ga-chien.png', 'Cơm tấm đùi gà chiên là món ăn phổ biến với đùi gà giòn rụm, cơm tấm mềm dẻo và chén nước mắm pha đặc trưng. Đùi gà được chiên giòn bên ngoài, giữ nguyên độ mềm ngọt bên trong, tạo cảm giác ngon miệng khi thưởng thức.', 'đùi gà, cơm tấm, nước mắm', NULL, 693, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(16, 1, 'Cơm tấm heo quay', 30000.00, NULL, 'Images/Food/Com/Com-tam-heo-quay.png', 'Cơm tấm heo quay là một món ăn đặc trưng của ẩm thực miền Nam, nổi bật với hương vị đậm đà, dễ ăn và rất được ưa chuộng. Món cơm này được làm từ những hạt gạo tấm mềm dẻo, được xới lên thơm lừng, thường được ăn kèm với miếng thịt heo quay vàng giòn, lớp da heo thơm lừng, giòn tan, còn phần thịt bên trong thì mềm, ngọt và đậm đà.', 'thịt heo quay, cơm tấm, nước mắm', NULL, 192, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(17, 1, 'Cơm tấm sườn bì chả', 30000.00, NULL, 'Images/Food/Com/Com-tam-suon-bi-cha.png', 'Cơm tấm sườn bì chả là món ăn đặc trưng của ẩm thực miền Nam. Sườn nướng thơm ngon, bì giòn và chả lụa béo ngậy được kết hợp với cơm tấm dẻo, tạo nên một món ăn ngon, đậm đà và đầy đủ dinh dưỡng cho bữa ăn.', 'sườn heo, bì, chả lụa, cơm tấm, nước mắm', NULL, 859, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(18, 1, 'Cơm tấm sườn trứng', 30000.00, NULL, 'Images/Food/Com/Com-tam-suon-trung.png', 'Cơm tấm sườn trứng là món ăn phổ biến với sự kết hợp giữa sườn nướng thơm lừng, trứng ốp la mềm và cơm tấm dẻo. Món ăn này không chỉ ngon miệng mà còn rất dễ ăn, thích hợp cho bữa sáng hoặc bữa trưa.', 'sườn heo, trứng, cơm tấm, nước mắm', NULL, 1723, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(19, 1, 'Cơm tấm thịt nướng', 30000.00, NULL, 'Images/Food/Com/Com-tam-thit-nuong.png', 'Cơm tấm thịt nướng là món ăn đậm đà hương vị, với thịt nướng vàng ươm, mềm, thấm đẫm gia vị. Khi ăn kèm với cơm tấm dẻo và nước mắm pha, món ăn này mang lại cảm giác thỏa mãn, đầy đủ cho một bữa ăn.', 'thịt heo, cơm tấm, nước mắm', NULL, 55, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(20, 1, 'Cơm tấm xíu mại', 30000.00, NULL, 'Images/Food/Com/Com-tam-xiu-mai.png', 'Cơm tấm xíu mại là món ăn đặc trưng với sự kết hợp giữa cơm tấm mềm, xíu mại thịt thơm lừng, đậm đà gia vị.', 'xíu mại, cơm tấm, nước mắm', NULL, 1070, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(21, 1, 'Cơm tấm thịt kho đài loan', 30000.00, NULL, 'Images/Food/Com/Com-thit-kho-Dai-Loan.png', 'Cơm tấm thịt kho Đài Loan với thịt kho mềm, thấm đẫm gia vị, nước kho đậm đà, ăn kèm với cơm tấm dẻo, tạo nên một món ăn rất đặc biệt. Đây là món ăn lý tưởng cho những ai yêu thích món ăn đậm đà hương vị.', 'thịt heo, cơm tấm, nước mắm', NULL, 1195, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(22, 1, 'Cơm thịt kho mắm ruốc', 30000.00, NULL, 'Images/Food/Com/Com-thit-kho-mam-ruoc.png', 'Cơm thịt kho mắm ruốc có hương vị đặc trưng của mắm ruốc kết hợp với thịt kho mềm, nước kho đậm đà. Món ăn này mang lại cảm giác thỏa mãn, vừa cay cay vừa mặn mà, ăn kèm với cơm tấm là một sự kết hợp tuyệt vời.', 'thịt heo, mắm ruốc, cơm', NULL, 764, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(23, 1, 'Cơm thịt kho trứng', 30000.00, NULL, 'Images/Food/Com/Com-trung-cuon-thit.png', 'Cơm trứng cuộn thịt là món ăn đơn giản nhưng đầy đủ dinh dưỡng,với trứng cuộn mềm mại, bên trong là thịt xay thơm ngon. Món này ăn cùng cơm tấm dẻo và gia vị đậm đà sẽ mang lại trải nghiệm ẩm thực tuyệt vời.', 'trứng, thịt heo, cơm', NULL, 226, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(24, 1, 'Cơm chiên dương châu', 30000.00, NULL, 'Images/Food/Com/Com-chien-duong-chau.png', 'Cơm chiên dương châu là món ăn đặc trưng của ẩm thực Trung Hoa, với cơm chiên thơm lừng kết hợp với thịt gà, tôm, trứng và rau củ tươi ngon. Món ăn này đầy đủ dinh dưỡng, mang đến một hương vị thanh mát và đậm đà.', 'thịt gà, tôm, trứng, rau củ, cơm', NULL, 818, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(25, 1, 'Cơm thịt kho tiêu', 30000.00, NULL, 'Images/Food/Com/Com-thit-kho-tieu.png', 'Cơm thịt kho tiêu là món ăn quen thuộc với hương vị đặc trưng của thịt heo kho đậm đà, kết hợp với tiêu đen thơm nồng. Thịt kho mềm, thấm gia vị, ăn kèm cơm tấm trắng dẻo tạo nên một món ăn vừa ngon vừa dễ ăn.', 'thịt heo, tiêu, cơm', NULL, 1411, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(26, 1, 'Cơm mực xào', 30000.00, NULL, 'Images/Food/Com/Com-muc-xao.png', 'Cơm mực xào là món ăn ngon với mực tươi xào cùng gia vị đậm đà, tạo ra sự kết hợp hoàn hảo với cơm trắng. Món ăn này không chỉ\nngon mà còn rất bổ dưỡng, mang lại hương vị mới mẻ cho bữa ăn.', 'mực, cơm', NULL, 614, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(27, 1, 'Cơm thịt luộc cà pháo', 30000.00, NULL, 'Images/Food/Com/Com-thit-luoc-ca-phao.png', 'Cơm thịt luộc cà pháo là món ăn dân dã nhưng rất ngon miệng, với thịt luộc tươi ngon ăn kèm với cà pháo muối chua, tạo nên hương vị đậm đà, dễ ăn. Món ăn này phù hợp cho những bữa cơm gia đình.', 'thịt heo, cà pháo, cơm', NULL, 819, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(28, 1, 'Cơm giò heo kho cải chua', 30000.00, NULL, 'Images/Food/Com/Com-gio-heo-kho-cai-chua.png', 'Món cơm giò heo kho cải chua là sự kết hợp hoàn hảo giữa giò heo kho mềm ngon, đậm đà gia vị, ăn kèm với cải chua làm món ăn thêm phần hấp dẫn.', 'giò heo, cải chua, cơm', NULL, 251, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(29, 1, 'Cơm đậu hũ kho sườn chay', 25000.00, NULL, 'Images/Food/Com/Com-dau-hu-kho-suon-chay.png', 'Cơm đậu hũ kho sườn chay là một món ăn thanh đạm, phù hợp cho những ai muốn thưởng thức ẩm thực chay. Đậu hũ mềm mịn kết hợp với sườn chay kho đậm đà tạo nên một món ăn đầy đủ dinh dưỡng và dễ ăn.', 'đậu hũ, sườn chay, cơm', NULL, 779, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(30, 1, 'Cơm đậu hũ kho cà ri', 25000.00, NULL, 'Images/Food/Com/Com-dau-hu-kho-ca-ri.png', 'Cơm đậu hũ kho cà ri là món ăn vừa ngon lại vừa dễ ăn với đậu hũ mềm được kho thấm gia vị cà ri đặc trưng. Món ăn này mang lại sự nhẹ nhàng nhưng vẫn đầy đủ hương vị, phù hợp với những ai thích món chay hoặc ít gia vị.', 'đậu hũ, cà ri, cơm', NULL, 1141, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(31, 2, 'Bún bò Huế', 40000.00, NULL, 'Images/Food/Bun/Bun-Bo-Hue.png', 'Bún bò Huế là một món ăn đặc trưng của miền Trung, nổi bật với hương vị đậm đà, cay nồng. Sợi bún mềm mại được kết hợp với nước dùng hầm từ xương bò và các loại gia vị như sả, ớt, tạo nên một món ăn vừa thơm ngon vừa đầy đủ chất dinh dưỡng. Thịt bò thái mỏng, giò heo và huyết bò làm tăng thêm sự phong phú, đặc biệt khi ăn kèm với rau sống tươi mát.', 'thịt bò, giò heo, sả, ớt, bún', NULL, 1368, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(32, 2, 'Bún chả ghẹ', 45000.00, NULL, 'Images/Food/Bun/Bun-cha-ghe.png', 'Bún chả ghẹ là một món ăn đặc biệt kết hợp giữa chả ghẹ tươi ngon và sợi bún mềm mại. Chả ghẹ được chế biến từ thịt ghẹ tươi, giòn ngọt và đậm đà gia vị, khi ăn kèm với bún và nước mắm chua ngọt sẽ tạo nên sự hòa quyện tuyệt vời.', 'ghẹ, bún, nước mắm', NULL, 1417, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(33, 2, 'Bún chả Hà Nội', 30000.00, NULL, 'Images/Food/Bun/Bun-cha-ha-noi.png', 'Bún chả Hà Nội là món ăn nổi tiếng với sự kết hợp hoàn hảo giữa bún tươi và chả nướng thơm lừng. Chả được làm từ thịt heo xay nhuyễn, nêm gia vị rồi nướng trên than hồng, mang lại hương vị đặc trưng. Khi ăn, bạn sẽ cảm nhận được sự giòn ngọt của chả, kết hợp với nước mắm pha chua ngọt, và rau sống tươi mát.', 'thịt heo, bún, nước mắm', NULL, 984, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(34, 2, 'Bún gà sa tế', 35000.00, NULL, 'Images/Food/Bun/Bun-ga-sa-te.png', 'Bún gà sa tế là món ăn đậm đà và cay nồng, đặc trưng với hương vị sa tế thơm lừng. Thịt gà được nấu mềm, ngấm gia vị cay nồng của sa tế, hòa quyện với sợi bún mềm mại. Nước dùng có màu đỏ hấp dẫn, có độ cay vừa phải và vị ngọt từ gà. Món ăn này được ăn kèm với rau sống tươi ngon, tạo nên một sự kết hợp hoàn hảo giữa cay, ngọt và chua.', 'thịt gà, sa tế, bún', NULL, 660, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(35, 2, 'Bún heo quay tóp mỡ', 30000.00, NULL, 'Images/Food/Bun/Bun-heo-quay-cha-gio-top-mo.png', 'Bún heo quay tóp mỡ là sự kết hợp tuyệt vời giữa các món ăn yêu thích của người Việt. Heo quay vàng giòn, thấm đẫm gia vị được ăn kèm với chả giò giòn rụm và tóp mỡ béo ngậy, tạo nên một món ăn đầy hương vị. Sợi bún mềm mại kết hợp với nước mắm pha chua ngọt, mang lại một sự hòa quyện thơm ngon, vừa béo vừa giòn.', 'thịt heo quay, tóp mỡ, bún, nước mắm', NULL, 336, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(36, 2, 'Bún Huế chay', 30000.00, NULL, 'Images/Food/Bun/Bun-Hue-chay.png', 'Bún Huế chay là món ăn thuần Việt mang đậm hương vị miền Trung, nhưng không sử dụng thịt mà thay vào đó là những nguyên liệu chay thanh đạm. Nước dùng đậm đà, được chế biến từ rau củ, gia vị truyền thống như sả, ớt, tạo nên hương vị cay nồng đặc trưng. Món ăn này có sự kết hợp tuyệt vời giữa bún mềm, rau tươi và các loại nấm, đậu hũ, mang đến cảm giác thanh mát, đầy đủ chất dinh dưỡng mà vẫn giữ được hương vị đặc trưng của bún Huế.', 'nấm, đậu hũ, sả, ớt, bún', NULL, 1683, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(37, 2, 'Bún măng gà', 30000.00, NULL, 'Images/Food/Bun/Bun-mang-ga.png', 'Món bún măng gà hấp dẫn với thịt gà mềm, măng tươi giòn, hòa quyện trong nước dùng thanh ngọt, đậm đà. Được trang trí với hành phi và rau sống tươi ngon, tạo nên một bữa ăn vừa bổ dưỡng vừa thơm ngon.', 'thịt gà, măng, bún', NULL, 1425, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(38, 2, 'Bún nem thịt nướng', 30000.00, NULL, 'Images/Food/Bun/Bun-nem-thit-nuong.png', 'Bún nem thịt nướng là sự kết hợp hoàn hảo giữa nem nướng thơm lừng và thịt heo nướng xém vàng, chín mềm. Món ăn được ăn kèm với bún tươi, rau sống và nước mắm chua ngọt, mang đến hương vị đậm đà khó quên.', 'nem, thịt heo, bún, nước mắm', NULL, 75, NULL, 2, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(39, 2, 'Bún thịt luộc mắm nêm', 30000.00, NULL, 'Images/Food/Bun/Bun-thit-luoc-rau-song-mam-nem.png', 'Bún thịt luộc mắm nêm là món ăn dân dã với thịt heo luộc thái mỏng, ăn kèm rau sống tươi ngon và mắm nêm đậm đà. Món ăn này mang lại sự hòa quyện giữa vị ngọt của thịt, vị mặn của mắm nêm và độ tươi mát của rau sống.', 'thịt heo, mắm nêm, bún, rau sống', NULL, 74, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(40, 2, 'Bún thịt nướng', 25000.00, NULL, 'Images/Food/Bun/Bun-thit-nuong.png', 'Bún thịt nướng là món ăn quen thuộc với thịt heo nướng thơm ngon, giòn rụm, kết hợp với bún tươi, rau sống và đậu phộng rang. Đặc biệt, nước mắm pha chua ngọt sẽ làm tăng thêm vị đậm đà cho món ăn, mang đến trải nghiệm ẩm thực đậm chất Việt.', 'thịt heo, bún, nước mắm, đậu phộng', NULL, 135, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(41, 3, 'Phở bò chín', 35000.00, NULL, 'Images/Food/Pho/Pho-bo-chin.png', 'Phở bò chín là món ăn phổ biến với thịt bò được ninh mềm, tạo ra hương vị đậm đà kết hợp cùng nước dùng thanh, ngọt từ xương bò. Món ăn thường được ăn kèm với bánh phở mềm, hành lá, rau thơm và thêm chút chanh, ớt để tăng hương vị.', 'thịt bò, bánh phở, nước dùng', NULL, 445, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(42, 3, 'Phở bò tái lăn', 35000.00, NULL, 'Images/Food/Pho/Pho-bo-tai-lan.png', 'Phở bò tái lăn khác biệt nhờ thịt bò được xào nhanh qua lửa trước khi cho vào nước dùng. Cách chế biến này tạo nên độ mềm mại nhưng vẫn giữ được vị ngọt tự nhiên của thịt bò. Nước dùng nóng kết hợp với bánh phở trắng, hành lá, rau thơm làm tăng thêm sự hấp dẫn của món ăn.', 'thịt bò, bánh phở, nước dùng', NULL, 1808, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(43, 3, 'Phở bò tái', 35000.00, NULL, 'Images/Food/Pho/Pho-bo-tai.png', 'Phở bò tái là món phở được yêu thích nhờ sự hòa quyện của thịt bò tươi tái và nước dùng thanh, ngọt từ xương bò. Khi ăn, thịt bò được làm chín nhẹ bởi nước dùng nóng, giữ nguyên độ mềm, mọng nước và hương vị tự nhiên. Bánh phở dai, mềm, cùng rau thơm, hành lá, giá đỗ và chút chanh ớt.', 'thịt bò, bánh phở, nước dùng', NULL, 1725, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(44, 3, 'Phở bò viên', 40000.00, NULL, 'Images/Food/Pho/Pho-bo-vien.png', 'Phở bò viên là sự kết hợp giữa nước dùng đậm đà, thanh ngọt và những viên bò được làm từ thịt bò xay nhuyễn, gia vị, tạo độ dai, giòn đặc trưng. Bánh phở mềm, thơm kết hợp với hành lá, ngò gai, giá đỗ và chút chanh, ớt, mang đến hương vị đầy lôi cuốn.', 'bò viên, bánh phở, nước dùng', NULL, 1203, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(45, 3, 'Phở chua Lạng Sơn', 45000.00, NULL, 'Images/Food/Pho/Pho-chua-lang-son.png', 'Phở chua Lạng Sơn là món ăn đặc trưng của vùng núi phía Bắc với vị chua thanh nhẹ, hòa quyện cùng bánh phở mềm và các nguyên liệu như thịt gà xé, thịt lợn quay giòn, lạc rang, và rau thơm. Nước sốt chua ngọt làm từ giấm, đường, và các gia vị truyền thống tạo nên hương vị độc đáo, khác biệt so với phở nước.', 'thịt gà, thịt lợn quay, lạc, bánh phở', NULL, 832, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(46, 3, 'Phở cuốn chay', 25000.00, NULL, 'Images/Food/Pho/Pho-cuon-chay.png', 'Phở cuốn chay là sự lựa chọn nhẹ nhàng cho người yêu thích món chay. Bánh phở mềm, cuộn chặt với các loại rau củ tươi như xà lách, cà rốt, bún, đậu phụ và gia vị. Món ăn được chấm với nước tương hoặc nước chấm chua ngọt, tạo ra sự hài hòa giữa vị ngọt thanh của rau và vị mặn nhẹ từ nước chấm.', 'đậu phụ, rau củ, bánh phở', NULL, 539, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(47, 3, 'Phở cuốn chiên phồng', 30000.00, NULL, 'Images/Food/Pho/Pho-cuon-chien-phong.png', 'Phở cuốn chiên phồng mang hương vị lạ miệng khi bánh phở được cuốn chặt và chiên vàng giòn bên ngoài, giữ lại nhân thịt hoặc tôm, rau củ bên trong. Khi ăn, vỏ phở giòn tan kết hợp với nhân mềm và nước chấm chua ngọt tạo nên món ăn hấp dẫn, giòn rụm.', 'thịt, tôm, rau củ, bánh phở', NULL, 192, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(48, 3, 'Phở cuốn ngũ xá', 30000.00, NULL, 'Images/Food/Pho/Pho-cuon-ngu-xa.png', 'Phở cuốn Ngũ Xá nổi tiếng với những lát bánh phở mềm, cuốn chặt thịt bò xào thơm ngon cùng rau sống. Điểm nhấn là phần thịt bò được xào chín vừa, mềm, thơm mà không bị dai, kết hợp cùng vị mát của rau sống. Phở cuốn thường ăn kèm với nước chấm chua ngọt đậm đà, làm tăng thêm độ ngon miệng.', 'thịt bò, rau sống, bánh phở', NULL, 1321, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(49, 3, 'Phở cuốn thịt heo', 30000.00, NULL, 'Images/Food/Pho/Pho-cuon-thit-heo.png', 'Phở cuốn thịt heo là sự kết hợp giữa bánh phở mềm và thịt heo luộc hoặc nướng, tạo nên hương vị thơm ngon, dễ ăn. Nhân cuốn thường đi kèm với các loại rau sống như xà lách, rau thơm và dưa leo. Món ăn này khi chấm với nước mắm pha chua ngọt mang lại hương vị đậm đà, tươi mát và thanh nhẹ.', 'thịt heo, rau sống, bánh phở', NULL, 50, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(50, 3, 'Phở cuốn tôm chua', 35000.00, NULL, 'Images/Food/Pho/Pho-cuon-tom-chua.png', 'Phở cuốn tôm chua mang đậm hương vị đặc trưng của miền Trung với tôm chua, bánh phở mềm mịn và các loại rau sống. Nhân cuốn có vị chua nhẹ của tôm lên men, cùng rau thơm, xà lách, tạo nên món ăn lạ miệng, hấp dẫn. Khi ăn, cuốn được chấm với nước mắm pha đậm đà, vừa có vị chua, ngọt, cay, hòa quyện tạo thành trải nghiệm ẩm thực đặc biệt.', 'tôm, rau sống, bánh phở', NULL, 258, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(51, 3, 'Phở gà', 35000.00, NULL, 'Images/Food/Pho/Pho-ga.png', 'Phở gà là món ăn truyền thống với nước dùng trong, ngọt thanh được ninh từ xương gà, kết hợp cùng thịt gà thơm, mềm. Món ăn có bánh phở mềm, hành lá, rau mùi và chanh, ớt tạo nên hương vị tươi mát, nhẹ nhàng.', 'thịt gà, bánh phở, nước dùng', NULL, 1131, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(52, 3, 'Phở hải sản', 40000.00, NULL, 'Images/Food/Pho/Pho-hai-san.png', 'Phở hải sản là sự kết hợp độc đáo giữa nước dùng thơm ngọt từ hải sản như tôm, mực và cua. Bánh phở mềm, ăn kèm các loại hải sản tươi ngon, hành lá, rau mùi, cùng chút chanh ớt tạo nên món phở đầy đủ vị ngọt của biển.', 'tôm, mực, cua, bánh phở, nước dùng', NULL, 892, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(53, 3, 'Phở heo', 35000.00, NULL, 'Images/Food/Pho/Pho-heo.png', 'Phở heo là biến tấu từ phở truyền thống với nước dùng được ninh từ xương heo, tạo độ ngọt thanh tự nhiên. Thịt heo mềm hoặc sườn non thường được dùng làm nhân, kết hợp với bánh phở trắng mềm, hành lá, rau sống và các loại gia vị.', 'thịt heo, bánh phở, nước dùng', NULL, 1055, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(54, 3, 'Phở xào giòn', 40000.00, NULL, 'Images/Food/Pho/Pho-xao-gion.png', 'Phở xào giòn là món ăn với bánh phở được chiên giòn rụm, kết hợp cùng thịt bò, hải sản hoặc rau củ xào. Nước sốt sệt đậm đà chan lên lớp phở giòn tan, tạo nên sự cân bằng giữa độ giòn và độ mềm trong từng miếng ăn.', 'thịt bò, rau củ, bánh phở', NULL, 601, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(55, 3, 'Phở xào hải sản', 35000.00, NULL, 'Images/Food/Pho/Pho-xao-hai-san.png', 'Phở xào hải sản là món ăn được yêu thích nhờ sự kết hợp giữa bánh phở dai, mềm và các loại hải sản tươi ngon như tôm, mực, và cá. Tất cả được xào nhanh tay cùng rau củ và gia vị, tạo nên món ăn đầy màu sắc và hương vị đậm đà.', 'tôm, mực, cá, bánh phở', NULL, 1821, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(56, 3, 'Phở xài kiểu Thái', 40000.00, NULL, 'Images/Food/Pho/Pho-xao-kieu-Thai.png', 'Phở xào kiểu Thái mang nét đặc trưng với hương vị đậm đà, chua cay nhờ nước sốt được pha từ gia vị truyền thống Thái như nước mắm, ớt, nước cốt chanh và thảo mộc. Bánh phở mềm được xào đều với thịt hoặc hải sản, rau củ, tạo nên món ăn thơm lừng, đậm chất Thái.', 'thịt, bánh phở, ớt, nước mắm', NULL, 1321, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(57, 3, 'Phở xào rau cải', 30000.00, NULL, 'Images/Food/Pho/Pho-xao-rau-cai.png', 'Phở xào rau cải là món ăn đơn giản nhưng không kém phần hấp dẫn với bánh phở xào cùng rau cải xanh tươi. Rau cải được xào nhanh tay giữ độ giòn và màu sắc xanh mướt, kết hợp cùng bánh phở dai mềm, gia vị đậm đà.', 'rau cải, bánh phở', NULL, 1134, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(58, 3, 'Phở xào thịt băm', 35000.00, NULL, 'Images/Food/Pho/pho-xao-thit-bam.png', 'Phở xào thịt băm là sự kết hợp hài hòa giữa bánh phở dai mềm và thịt băm nhỏ, được xào cùng gia vị thơm phức. Món này thường được thêm hành lá, rau mùi và các loại gia vị đặc trưng như nước tương, tiêu, ớt, tạo nên hương vị đậm đà, dễ ăn.', 'thịt heo, bánh phở', NULL, 1695, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(59, 3, 'Phở xốt vang', 45000.00, NULL, 'Images/Food/Pho/Pho-xot-vang.png', 'Phở xốt vang là món ăn độc đáo kết hợp giữa phở truyền thống và thịt bò hầm xốt vang, một kiểu hầm bò mang hương vị phương Tây. Thịt bò mềm, thấm gia vị, hòa quyện với nước dùng xốt vang đậm đà, thơm lừng vị rượu vang và các loại thảo mộc.', 'thịt bò, rượu vang, bánh phở', NULL, 1085, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(60, 3, 'Phở gà trộn', 35000.00, NULL, 'Images/Food/Pho/pho-ga-tron.png', 'Phở gà trộn là món ăn hấp dẫn với bánh phở mềm trộn cùng thịt gà xé dai ngon, thường được thêm hành phi, lạc rang, rau thơm và các loại gia vị. Nước sốt chua ngọt được pha chế đặc biệt, giúp hòa quyện các nguyên liệu lại với nhau, tạo nên hương vị đậm đà, thơm lừng.', 'thịt gà, bánh phở, lạc', NULL, 330, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(61, 4, 'Chanh muối', 15000.00, NULL, 'Images/Food/nuoc/chanh-muoi.png', 'Chanh muối là thức uống giải khát truyền thống với vị chua ngọt và chút mặn nhẹ, giúp thanh lọc cơ thể và giải nhiệt nhanh chóng. Đồ uống này còn mang đến cảm giác sảng khoái, giúp giảm mệt mỏi.', 'chanh, muối', NULL, 377, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(62, 4, 'Espresso', 25000.00, NULL, 'Images/Food/nuoc/espresso.png', 'Espresso là loại cà phê đậm đà, được pha chế từ máy với áp suất cao, tạo nên lớp bọt mịn trên bề mặt. Đây là thức uống yêu thích cho những ai cần sự tỉnh táo và thích hương vị mạnh mẽ của cà phê nguyên chất.', 'cà phê', NULL, 885, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(63, 4, 'Latte', 30000.00, NULL, 'Images/Food/nuoc/latte.png', 'Latte là sự kết hợp hài hòa giữa cà phê espresso và sữa tươi được đánh nóng, tạo nên lớp bọt sữa mềm mịn, thơm béo. Đây là thức uống lý tưởng cho những ai yêu thích cà phê với vị nhẹ nhàng, êm dịu.', 'cà phê, sữa', NULL, 1292, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(64, 4, 'Matcha Latte', 35000.00, NULL, 'Images/Food/nuoc/matcha-latte.png', 'Matcha Latte là sự kết hợp giữa bột trà xanh nguyên chất và sữa tươi, tạo nên vị ngọt nhẹ, thơm mát và hơi đắng đặc trưng của matcha. Món này không chỉ thơm ngon mà còn giàu chất chống oxy hóa, tốt cho sức khỏe.', 'matcha, sữa', NULL, 1808, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(65, 4, 'Nước cam', 20000.00, NULL, 'Images/Food/nuoc/Nuoc-cam.png', 'Nước cam tươi ép là thức uống bổ dưỡng, giàu vitamin C, giúp tăng cường miễn dịch và cung cấp năng lượng tức thì. Hương vị cam ngọt mát, thơm ngon, là lựa chọn hoàn hảo cho những ngày hè oi bức hoặc khi bạn cần sự tươi mới.', 'cam', NULL, 1175, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(66, 4, 'Nước chanh', 15000.00, NULL, 'Images/Food/nuoc/Nuoc-chanh.png', 'Nước chanh tươi mát là thức uống quen thuộc giúp giải khát hiệu quả, thanh lọc cơ thể và tăng cường sức đề kháng nhờ lượng vitamin C dồi dào. Đồ uống này mang vị chua ngọt nhẹ, dễ uống và sảng khoái.', 'chanh', NULL, 441, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(67, 4, 'Nước dưa hấu', 18000.00, NULL, 'Images/Food/nuoc/Nuoc-dua-hau.png', 'Nước dưa hấu tươi mát mang lại cảm giác sảng khoái tức thì nhờ vị ngọt tự nhiên và giàu nước. Đây là thức uống lý tưởng để giải nhiệt trong những ngày nắng nóng, bổ sung vitamin và khoáng chất cần thiết.', 'dưa hấu', NULL, 659, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(68, 4, 'Nước ép bưởi cà rốt', 25000.00, NULL, 'Images/Food/nuoc/Nuoc-ep-buoi-ca-rot.png', 'Nước ép bưởi cà rốt kết hợp hai thành phần giàu vitamin C và beta-carotene, giúp tăng cường sức khỏe và đẹp da. Vị ngọt dịu từ cà rốt hòa quyện với chút chua nhẹ của bưởi, mang lại hương vị tươi ngon, bổ dưỡng.', 'bưởi, cà rốt', NULL, 1962, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(69, 4, 'Nước ép dứa cam', 28000.00, NULL, 'Images/Food/nuoc/nuoc-ep-dua-cam.png', 'Nước ép dứa cam kết hợp vị ngọt đậm đà của dứa với vị chua thanh của cam, tạo nên thức uống vừa ngon miệng vừa giàu vitamin. Đồ uống này giúp tăng cường sức đề kháng và hỗ trợ tiêu hóa hiệu quả.', 'dứa, cam', NULL, 1853, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(70, 4, 'Nước ép táo', 22000.00, NULL, 'Images/Food/nuoc/Nuoc-ep-tao.png', 'Nước ép táo tươi mát với hương vị ngọt thanh nhẹ nhàng, giúp cung cấp năng lượng và bổ sung vitamin A, C. Thức uống này mang lại sự tươi mới và lành mạnh, thích hợp cho mọi lứa tuổi.', 'táo', NULL, 1381, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(71, 4, 'Sinh tố dâu sữa chua', 30000.00, NULL, 'Images/Food/nuoc/sinh-to-dau-sua-chua.png', 'Sinh tố dâu tươi kết hợp sữa chua mịn màng, tạo nên thức uống chua ngọt độc đáo, giàu vitamin C và tốt cho hệ tiêu hóa. Phù hợp với những ai yêu thích hương vị trái cây tự nhiên.', 'dâu, sữa chua', NULL, 1337, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(72, 4, 'Sinh tố xoài', 28000.00, NULL, 'Images/Food/nuoc/Sinh-to-xoai.png', 'Sinh tố xoài đậm đà, thơm lừng vị xoài chín, giàu chất xơ và vitamin C, mang lại cảm giác sảng khoái và giải nhiệt cho cơ thể trong những ngày nắng nóng.', 'xoài', NULL, 540, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(73, 4, 'Sữa chua việt quất', 32000.00, NULL, 'Images/Food/nuoc/Sua-chua-viet-quat.png', 'Sữa chua việt quất với vị chua ngọt tự nhiên, bổ sung lợi khuẩn và chất chống oxy hóa từ việt quất, mang đến thức uống tốt cho sức khỏe và làn da.', 'việt quất, sữa chua', NULL, 672, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(74, 4, 'Trà chanh giã tay', 20000.00, NULL, 'Images/Food/nuoc/Tra_chanh_gia_tay.png', 'Trà chanh giã tay có hương vị chua thanh của chanh kết hợp với vị trà đậm đà, giúp thanh lọc cơ thể và giải khát hiệu quả. Thức uống này rất thích hợp trong ngày hè.', 'trà, chanh', NULL, 1730, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(75, 4, 'Trà bạc hà', 25000.00, NULL, 'Images/Food/nuoc/Tra-bac-ha.png', 'Trà bạc hà thơm mát, giúp thư giãn và giảm căng thẳng. Vị mát lạnh từ bạc hà làm dịu đi sự oi bức của ngày hè, đồng thời hỗ trợ tiêu hóa hiệu quả.', 'trà, bạc hà', NULL, 654, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(76, 4, 'Trà đào', 28000.00, NULL, 'Images/Food/nuoc/Tra-dao.png', 'Trà đào ngọt ngào, kết hợp vị chua nhẹ của đào chín mọng, mang đến hương vị tươi mát và sảng khoái. Thích hợp cho những ai yêu thích vị trà trái cây tự nhiên.', 'trà, đào', NULL, 59, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(77, 4, 'Trà hoa nhài', 14000.00, NULL, 'Images/Food/nuoc/Tra-hoa-nhai.png', 'Trà hoa nhài dịu nhẹ, mang hương thơm thanh khiết giúp thư giãn và làm dịu tinh thần. Thức uống thanh mát này là lựa chọn lý tưởng cho những buổi tối thư thái.', 'trà, hoa nhài', NULL, 314, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(78, 4, 'Trà sữa cookie', 25000.00, NULL, 'Images/Food/nuoc/Tra-sua-cookie.png', 'Trà sữa cookie là sự hòa quyện giữa vị trà sữa thơm béo và hương vị giòn tan của cookie. Một thức uống ngọt ngào và thú vị dành cho các bạn trẻ.', 'trà, sữa, cookie', NULL, 1385, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18'),
(79, 4, 'Trà sữa kem trứng cháy', 35000.00, NULL, 'Images/Food/nuoc/Tra-sua-kem-trung-chay.png', 'Trà sữa kem trứng cháy độc đáo với lớp kem trứng béo ngậy và vị trà thơm đậm đà. Sự kết hợp tuyệt vời này mang lại trải nghiệm mới mẻ và hấp dẫn cho người thưởng thức.', 'trà, sữa, trứng', NULL, 1992, NULL, 2, '2024-12-20 17:02:06', '2025-04-07 00:21:32'),
(80, 4, 'Trà trân châu khoai lang', 28000.00, NULL, 'Images/Food/nuoc/Tra-tran-chau-khoai-lang.png', 'Trà trân châu khoai lang với trân châu dẻo thơm vị khoai lang, hòa quyện với trà thanh mát. Đây là món uống đặc biệt, hấp dẫn và phù hợp cho mọi lứa tuổi.', 'trà, trân châu, khoai lang', NULL, 1817, NULL, NULL, '2024-12-20 17:02:06', '2025-03-29 21:23:18');

-- --------------------------------------------------------

--
-- Table structure for table `food_ingredients`
--

CREATE TABLE `food_ingredients` (
  `id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `ingredient_id` int(11) NOT NULL,
  `required_amount` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `food_ingredients`
--

INSERT INTO `food_ingredients` (`id`, `food_id`, `ingredient_id`, `required_amount`) VALUES
(1, 1, 3, 0.2),
(2, 1, 4, 1),
(3, 1, 5, 0.02),
(4, 1, 6, 0.3),
(5, 2, 6, 0.3),
(6, 2, 7, 0.1),
(7, 2, 1, 0.2),
(8, 3, 3, 0.2),
(9, 3, 8, 0.1),
(10, 3, 5, 0.02),
(11, 3, 6, 0.3),
(12, 4, 9, 0.2),
(13, 4, 10, 0.05),
(14, 4, 11, 0.01),
(15, 4, 6, 0.3),
(16, 5, 9, 0.2),
(17, 5, 12, 0.1),
(18, 5, 13, 0.05),
(19, 5, 6, 0.3),
(20, 6, 1, 0.2),
(21, 6, 6, 0.3),
(22, 6, 14, 0.1),
(23, 7, 15, 0.2),
(24, 7, 6, 0.3),
(25, 7, 16, 0.05),
(26, 8, 17, 0.2),
(27, 8, 18, 1),
(28, 8, 6, 0.3),
(29, 9, 6, 0.3),
(30, 9, 4, 1),
(31, 10, 15, 0.2),
(32, 10, 10, 0.05),
(33, 10, 11, 0.01),
(34, 10, 6, 0.3),
(35, 11, 15, 0.2),
(36, 11, 6, 0.3),
(37, 12, 46, 0.1),
(38, 12, 47, 0.2),
(39, 12, 6, 0.3),
(40, 13, 48, 0.2),
(41, 13, 19, 0.02),
(42, 13, 6, 0.3),
(43, 14, 48, 0.2),
(44, 14, 20, 0.1),
(45, 14, 5, 0.02),
(46, 14, 6, 0.3),
(47, 15, 21, 0.2),
(48, 15, 22, 0.3),
(49, 15, 5, 0.02),
(50, 16, 3, 0.2),
(51, 16, 22, 0.3),
(52, 16, 5, 0.02),
(53, 17, 48, 0.2),
(54, 17, 23, 0.1),
(55, 17, 24, 0.1),
(56, 17, 22, 0.3),
(57, 17, 5, 0.02),
(58, 18, 48, 0.2),
(59, 18, 4, 1),
(60, 18, 22, 0.3),
(61, 18, 5, 0.02),
(62, 19, 3, 0.2),
(63, 19, 22, 0.3),
(64, 19, 5, 0.02),
(65, 20, 25, 0.2),
(66, 20, 22, 0.3),
(67, 20, 5, 0.02),
(68, 21, 3, 0.2),
(69, 21, 22, 0.3),
(70, 21, 5, 0.02),
(71, 22, 3, 0.2),
(72, 22, 26, 0.02),
(73, 22, 6, 0.3),
(74, 23, 4, 1),
(75, 23, 3, 0.2),
(76, 23, 6, 0.3),
(77, 24, 15, 0.1),
(78, 24, 8, 0.1),
(79, 24, 4, 1),
(80, 24, 14, 0.1),
(81, 24, 6, 0.3),
(82, 25, 3, 0.2),
(83, 25, 11, 0.01),
(84, 25, 6, 0.3),
(85, 26, 17, 0.2),
(86, 26, 6, 0.3),
(87, 27, 3, 0.2),
(88, 27, 42, 0.1),
(89, 27, 6, 0.3),
(90, 28, 43, 0.2),
(91, 28, 44, 0.1),
(92, 28, 6, 0.3),
(93, 29, 33, 0.2),
(94, 29, 48, 0.2),
(95, 29, 6, 0.3),
(96, 30, 33, 0.2),
(97, 30, 45, 0.02),
(98, 30, 6, 0.3),
(99, 31, 1, 0.2),
(100, 31, 43, 0.1),
(101, 31, 10, 0.05),
(102, 31, 11, 0.01),
(103, 31, 29, 0.3),
(104, 32, 30, 0.2),
(105, 32, 29, 0.3),
(106, 32, 5, 0.02),
(107, 33, 3, 0.2),
(108, 33, 29, 0.3),
(109, 33, 5, 0.02),
(110, 34, 15, 0.2),
(111, 34, 31, 0.02),
(112, 34, 29, 0.3),
(113, 35, 3, 0.2),
(114, 35, 27, 0.1),
(115, 35, 28, 0.1),
(116, 35, 29, 0.3),
(117, 35, 5, 0.02),
(118, 36, 32, 0.1),
(119, 36, 33, 0.2),
(120, 36, 10, 0.05),
(121, 36, 11, 0.01),
(122, 36, 29, 0.3),
(123, 37, 15, 0.2),
(124, 37, 34, 0.1),
(125, 37, 29, 0.3),
(126, 38, 35, 0.2),
(127, 38, 3, 0.2),
(128, 38, 29, 0.3),
(129, 38, 5, 0.02),
(130, 39, 3, 0.2),
(131, 39, 26, 0.02),
(132, 39, 29, 0.3),
(133, 39, 16, 0.05),
(134, 40, 3, 0.2),
(135, 40, 29, 0.3),
(136, 40, 5, 0.02),
(137, 40, 36, 0.05),
(138, 41, 1, 0.2),
(139, 41, 37, 0.3),
(140, 41, 38, 0.5),
(141, 42, 1, 0.2),
(142, 42, 37, 0.3),
(143, 42, 38, 0.5),
(144, 43, 1, 0.2),
(145, 43, 37, 0.3),
(146, 43, 38, 0.5),
(147, 44, 39, 0.2),
(148, 44, 37, 0.3),
(149, 44, 38, 0.5),
(150, 45, 15, 0.1),
(151, 45, 40, 0.2),
(152, 45, 41, 0.05),
(153, 45, 37, 0.3),
(154, 46, 33, 0.2),
(155, 46, 14, 0.1),
(156, 46, 37, 0.3),
(157, 47, 15, 0.2),
(158, 47, 37, 0.3),
(159, 47, 38, 0.5),
(160, 48, 1, 0.2),
(161, 48, 37, 0.3),
(162, 49, 11, 0.01),
(163, 50, 14, 0.05),
(164, 51, 36, 0.05),
(165, 52, 14, 0.1),
(166, 53, 14, 0.05),
(167, 54, 11, 0.01),
(168, 55, 14, 0.05),
(169, 56, 20, 0.5),
(170, 57, 14, 0.1),
(171, 58, 13, 0.1),
(172, 59, 14, 0.1),
(173, 60, 14, 0.1),
(216, 61, 14, 0.1),
(217, 62, 14, 0.1),
(218, 63, 14, 0.1),
(219, 64, 14, 0.05),
(220, 65, 14, 0.1),
(221, 66, 14, 0.1),
(222, 67, 14, 0.1),
(223, 68, 14, 0.1),
(224, 69, 14, 0.1),
(225, 70, 14, 0.1),
(226, 71, 14, 0.1),
(227, 72, 14, 0.1),
(228, 73, 14, 0.1),
(229, 74, 14, 0.1),
(230, 75, 14, 0.1),
(231, 76, 14, 0.1),
(232, 77, 14, 0.1),
(233, 78, 14, 0.05),
(234, 79, 6, 0.1),
(235, 80, 33, 0.1);

-- --------------------------------------------------------

--
-- Table structure for table `ingredients`
--

CREATE TABLE `ingredients` (
  `ingredient_id` int(11) NOT NULL,
  `ingredient_name` varchar(100) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `supplier_name` varchar(100) DEFAULT NULL,
  `import_date` date NOT NULL,
  `expiration_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ingredients`
--

INSERT INTO `ingredients` (`ingredient_id`, `ingredient_name`, `amount`, `price`, `supplier_id`, `supplier_name`, `import_date`, `expiration_date`) VALUES
(1, 'Thịt bò', 100.00, 20000000.00, 1, 'CP Food', '2025-04-05', '2026-04-04'),
(3, 'Thịt heo', 100.00, 15.00, 1, 'CP Food', '2025-04-05', '2025-04-12'),
(4, 'Trứng', 100.00, 0.50, 2, 'VinaEggs', '2025-04-05', '2025-04-20'),
(5, 'Nước mắm', 50.00, 5.00, 3, 'Nam Ngư', '2025-03-01', '2026-03-01'),
(6, 'Cơm', 200.00, 2.00, 4, 'Rice Company', '2025-04-05', '2025-04-06'),
(7, 'Dưa chua', 50.00, 3.00, 5, 'Vegetable Supplier', '2025-04-01', '2025-04-15'),
(8, 'Tôm', 50.00, 25.00, 6, 'Seafood Company', '2025-04-04', '2025-04-11'),
(9, 'Cá', 50.00, 20.00, 6, 'Seafood Company', '2025-04-04', '2025-04-11'),
(10, 'Sả', 20.00, 1.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-10'),
(11, 'Ớt', 10.00, 2.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-10'),
(12, 'Rau cải', 30.00, 3.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-08'),
(13, 'Cà chua', 30.00, 4.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-08'),
(14, 'Rau củ', 100.00, 5.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-10'),
(15, 'Thịt gà', 100.00, 18.00, 1, 'CP Food', '2025-04-05', '2025-04-12'),
(16, 'Rau sống', 30.00, 3.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-08'),
(17, 'Mực', 30.00, 30.00, 6, 'Seafood Company', '2025-04-04', '2025-04-11'),
(18, 'Trứng muối', 20.00, 2.00, 2, 'VinaEggs', '2025-04-01', '2025-05-01'),
(19, 'Mật ong', 10.00, 8.00, 7, 'Honey Farm', '2025-03-15', '2026-03-15'),
(20, 'Nước dừa', 50.00, 4.00, 8, 'Tropical Drinks', '2025-04-01', '2025-04-15'),
(21, 'Đùi gà', 50.00, 20.00, 1, 'CP Food', '2025-04-05', '2025-04-12'),
(22, 'Cơm tấm', 100.00, 3.00, 4, 'Rice Company', '2025-04-05', '2025-04-06'),
(23, 'Bì heo', 30.00, 12.00, 1, 'CP Food', '2025-04-05', '2025-04-12'),
(24, 'Chả lụa', 30.00, 15.00, 9, 'Meat Deli', '2025-04-03', '2025-04-10'),
(25, 'Xíu mại', 30.00, 18.00, 9, 'Meat Deli', '2025-04-03', '2025-04-10'),
(26, 'Mắm ruốc', 10.00, 6.00, 3, 'Nam Ngư', '2025-03-01', '2026-03-01'),
(27, 'Tóp mỡ', 20.00, 10.00, 1, 'CP Food', '2025-04-05', '2025-04-12'),
(28, 'Chả giò', 30.00, 12.00, 9, 'Meat Deli', '2025-04-03', '2025-04-10'),
(29, 'Bún', 100.00, 4.00, 10, 'Noodle Factory', '2025-04-03', '2025-04-10'),
(30, 'Ghẹ', 20.00, 35.00, 6, 'Seafood Company', '2025-04-04', '2025-04-11'),
(31, 'Sa tế', 10.00, 7.00, 11, 'Spice Company', '2025-03-15', '2026-03-15'),
(32, 'Nấm', 20.00, 8.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-10'),
(33, 'Đậu hũ', 50.00, 6.00, 12, 'Tofu Factory', '2025-04-03', '2025-04-10'),
(34, 'Măng', 20.00, 5.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-10'),
(35, 'Nem', 30.00, 15.00, 9, 'Meat Deli', '2025-04-03', '2025-04-10'),
(36, 'Đậu phộng', 20.00, 5.00, 13, 'Nut Supplier', '2025-03-20', '2025-09-20'),
(37, 'Bánh phở', 100.00, 5.00, 10, 'Noodle Factory', '2025-04-03', '2025-04-10'),
(38, 'Nước dùng', 200.00, 10.00, 14, 'Soup Base', '2025-04-01', '2025-04-08'),
(39, 'Bò viên', 30.00, 18.00, 1, 'CP Food', '2025-04-05', '2025-04-12'),
(40, 'Thịt lợn quay', 30.00, 22.00, 1, 'CP Food', '2025-04-05', '2025-04-12'),
(41, 'Lạc', 20.00, 5.00, 13, 'Nut Supplier', '2025-03-20', '2025-09-20'),
(42, 'Cà pháo', 20.00, 4.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-08'),
(43, 'Giò heo', 30.00, 15.00, 1, 'CP Food', '2025-04-05', '2025-04-12'),
(44, 'Cải chua', 20.00, 3.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-10'),
(45, 'Cà ri', 10.00, 6.00, 11, 'Spice Company', '2025-03-15', '2026-03-15'),
(46, 'Khổ qua', 20.00, 4.00, 5, 'Vegetable Supplier', '2025-04-03', '2025-04-08'),
(47, 'Chả cá', 30.00, 25.00, 6, 'Seafood Company', '2025-04-04', '2025-04-11'),
(48, 'Sườn heo', 50.00, 17.00, 1, 'CP Food', '2025-04-05', '2025-04-12');

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE `invoice` (
  `invoice_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `recipient_name` varchar(100) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `delivery_address` varchar(255) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `order_date` datetime DEFAULT current_timestamp(),
  `total_amount` decimal(10,2) NOT NULL,
  `discount_code_id` int(11) DEFAULT NULL,
  `payment_method` tinyint(1) NOT NULL,
  `is_paid` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `invoice`
--

INSERT INTO `invoice` (`invoice_id`, `account_id`, `recipient_name`, `phone_number`, `delivery_address`, `note`, `order_date`, `total_amount`, `discount_code_id`, `payment_method`, `is_paid`) VALUES
(1, 3, 'Nguyễn Anh Tuấn', '09090909', 'trường đại học Quốc Tế đại học quốc gia, Linh Trung, Thủ Đức, Việt Nam', '', '2025-03-27 23:46:44', 30000.00, NULL, 1, NULL),
(2, 3, 'Nguyễn Anh Tuấn', '09090909', 'trường đại học Quốc Tế đại học quốc gia, Linh Trung, Thủ Đức, Việt Nam', 'test', '2025-03-27 23:48:54', 120000.00, NULL, 1, NULL),
(3, 3, 'Nguyễn Anh Tuấn', '09090909', 'trường đại học Quốc Tế đại học quốc gia, Linh Trung, Thủ Đức, Việt Nam', '', '2025-03-28 15:38:14', 60000.00, NULL, 1, NULL),
(4, 3, 'Nguyễn Anh Tuấn', '09090909', 'trường đại học Quốc Tế đại học quốc gia, Linh Trung, Thủ Đức, Việt Nam', '', '2025-03-28 15:49:30', 30000.00, NULL, 1, NULL),
(5, 3, 'Nguyễn Anh Tuấn', '09090909', 'trường đại học Quốc Tế đại học quốc gia, Linh Trung, Thủ Đức, Việt Nam', '', '2025-03-28 17:52:43', 60000.00, NULL, 1, NULL),
(6, 3, 'Nguyễn Anh Tuấn', '09090909', 'trường đại học Quốc Tế đại học quốc gia, Linh Trung, Thủ Đức, Việt Nam', '', '2025-04-01 17:36:05', 28500.00, NULL, 3, NULL),
(7, 3, 'Nguyễn Anh Tuấn', '09090909', 'trường đại học Quốc Tế đại học quốc gia, Linh Trung, Thủ Đức, Việt Nam', '', '2025-04-01 17:44:20', 57000.00, NULL, 1, NULL),
(8, 3, 'Nguyễn Anh Tuấn', '09090909', 'trường đại học Quốc Tế đại học quốc gia, Linh Trung, Thủ Đức, Việt Nam', '', '2025-04-01 17:46:12', 27000.00, NULL, 1, NULL),
(9, 4, 'Nguyễn Anh Tuấn', '09090909', 'trường đại học Quốc Tế đại học quốc gia, Linh Trung, Thủ Đức, Việt Nam', '', '2025-04-07 00:19:12', 30000.00, NULL, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `invoice_detail`
--

CREATE TABLE `invoice_detail` (
  `detail_id` int(11) NOT NULL,
  `invoice_id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `invoice_detail`
--

INSERT INTO `invoice_detail` (`detail_id`, `invoice_id`, `food_id`, `quantity`, `total_amount`) VALUES
(1, 1, 1, 1, 30000.00),
(2, 2, 38, 4, 120000.00),
(3, 3, 2, 2, 60000.00),
(4, 4, 3, 1, 30000.00),
(5, 5, 1, 1, 30000.00),
(6, 5, 2, 1, 30000.00),
(7, 6, 3, 1, 30000.00),
(8, 7, 1, 2, 60000.00),
(9, 8, 1, 1, 30000.00),
(10, 9, 9, 1, 30000.00);

-- --------------------------------------------------------

--
-- Table structure for table `order_status`
--

CREATE TABLE `order_status` (
  `order_id` int(11) NOT NULL,
  `invoice_id` int(11) NOT NULL,
  `order_status` tinyint(1) NOT NULL,
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `order_status`
--

INSERT INTO `order_status` (`order_id`, `invoice_id`, `order_status`, `updated_at`) VALUES
(1, 1, 1, '2025-03-27 23:46:44'),
(2, 2, 1, '2025-03-27 23:48:54'),
(3, 3, 1, '2025-03-28 15:38:14'),
(4, 4, 1, '2025-03-28 15:49:30'),
(5, 5, 1, '2025-03-28 17:52:43'),
(6, 6, 1, '2025-04-01 17:36:05'),
(7, 7, 1, '2025-04-01 17:44:20'),
(8, 8, 1, '2025-04-01 17:46:12'),
(9, 9, 2, '2025-04-07 02:07:35');

-- --------------------------------------------------------

--
-- Table structure for table `password_reset_tokens`
--

CREATE TABLE `password_reset_tokens` (
  `token_id` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `expiry_time` datetime NOT NULL,
  `is_used` tinyint(1) NOT NULL DEFAULT 0,
  `account_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pending_accounts`
--

CREATE TABLE `pending_accounts` (
  `pending_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(150) NOT NULL,
  `token` varchar(255) NOT NULL,
  `expiry_time` datetime NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `product_view`
--

CREATE TABLE `product_view` (
  `view_id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `view_time` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `product_view`
--

INSERT INTO `product_view` (`view_id`, `food_id`, `account_id`, `view_time`) VALUES
(1, 1, 1, '2025-01-13 11:30:44'),
(2, 1, 1, '2025-01-13 11:49:21'),
(3, 3, 1, '2025-01-13 11:49:26'),
(4, 3, 1, '2025-01-13 11:49:34'),
(5, 2, 1, '2025-01-13 11:50:00'),
(6, 38, 1, '2025-03-27 23:48:39'),
(7, 3, 1, '2025-04-05 14:21:23'),
(8, 3, 1, '2025-04-05 14:34:29'),
(9, 7, 1, '2025-04-05 14:35:39'),
(10, 79, 1, '2025-04-07 00:21:32');

-- --------------------------------------------------------

--
-- Table structure for table `revenue`
--

CREATE TABLE `revenue` (
  `revenue_id` int(11) NOT NULL,
  `date_month` date NOT NULL,
  `total_revenue` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

CREATE TABLE `review` (
  `review_id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `invoice_id` int(11) DEFAULT NULL,
  `rating` tinyint(1) NOT NULL CHECK (`rating` between 1 and 5),
  `created_at` datetime DEFAULT current_timestamp(),
  `comment` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`review_id`, `food_id`, `account_id`, `invoice_id`, `rating`, `created_at`, `comment`) VALUES
(1, 1, 1, NULL, 5, '2025-03-30 00:46:22', 'Món ăn rất ngon, sẽ quay lại!'),
(2, 1, 2, NULL, 4, '2025-03-30 00:46:22', 'Hương vị tuyệt vời, 5 sao!'),
(3, 2, 2, NULL, 5, '2025-03-30 00:46:22', 'Ngon nhưng hơi mắc!'),
(4, 2, 3, NULL, 4, '2025-03-30 00:46:22', 'Chất lượng tốt, đáng tiền.'),
(5, 3, 1, NULL, 5, '2025-03-30 00:46:22', 'Ăn hoài không chán!'),
(6, 3, 3, NULL, 4, '2025-03-30 00:46:22', 'Món này hợp khẩu vị của tôi!'),
(7, 4, 2, NULL, 5, '2025-03-30 00:46:22', 'Tuyệt vời, chắc chắn sẽ mua tiếp!'),
(8, 4, 1, NULL, 4, '2025-03-30 00:46:22', 'Giá hơi cao nhưng chất lượng xứng đáng.'),
(9, 5, 3, NULL, 5, '2025-03-30 00:46:22', 'Món này ổn nhưng phục vụ hơi lâu.'),
(10, 5, 1, NULL, 4, '2025-03-30 00:46:22', 'Rất hài lòng, đáng giá 5 sao!'),
(11, 6, 2, NULL, 5, '2025-03-30 00:46:22', 'Ngon lắm, sẽ giới thiệu bạn bè.'),
(12, 6, 3, NULL, 4, '2025-03-30 00:46:22', 'Món ăn hợp khẩu vị, đáng thử!'),
(13, 7, 1, NULL, 5, '2025-03-30 00:46:22', 'Phần ăn đầy đủ, ngon miệng.'),
(14, 7, 2, NULL, 4, '2025-03-30 00:46:22', 'Chất lượng món ăn tuyệt vời.'),
(15, 8, 3, NULL, 5, '2025-03-30 00:46:22', 'Sẽ quay lại vào lần sau!'),
(16, 8, 1, NULL, 4, '2025-03-30 00:46:22', 'Phục vụ tốt, món ăn ngon.'),
(17, 9, 2, NULL, 5, '2025-03-30 00:46:22', 'Hương vị đặc biệt, không thể quên!'),
(18, 9, 3, NULL, 4, '2025-03-30 00:46:22', 'Đáng đồng tiền, món ăn xuất sắc!'),
(19, 10, 1, NULL, 5, '2025-03-30 00:46:22', 'Món này tuyệt vời, đáng thử.'),
(20, 10, 2, NULL, 4, '2025-03-30 00:46:22', 'Ăn một lần là ghiền ngay!'),
(21, 11, 3, NULL, 5, '2025-03-30 00:46:22', 'Sẽ quay lại nhiều lần nữa.'),
(22, 11, 1, NULL, 4, '2025-03-30 00:46:22', 'Ngon nhưng hơi cay một chút!'),
(23, 12, 2, NULL, 5, '2025-03-30 00:46:22', 'Chất lượng đảm bảo, món ăn tuyệt hảo.'),
(24, 12, 3, NULL, 4, '2025-03-30 00:46:22', 'Đáng thử, giá hợp lý.'),
(25, 13, 1, NULL, 5, '2025-03-30 00:46:22', 'Món ăn được trình bày đẹp mắt.'),
(26, 13, 2, NULL, 4, '2025-03-30 00:46:22', 'Vị đậm đà, rất hợp khẩu vị.'),
(27, 14, 3, NULL, 5, '2025-03-30 00:46:22', 'Món này ăn rất ngon.'),
(28, 14, 1, NULL, 4, '2025-03-30 00:46:22', 'Chất lượng và giá cả hợp lý.'),
(29, 15, 2, NULL, 5, '2025-03-30 00:46:22', 'Rất hài lòng, món ăn ngon.'),
(30, 15, 3, NULL, 4, '2025-03-30 00:46:22', 'Hương vị đặc trưng, không lẫn vào đâu được!'),
(31, 16, 1, NULL, 5, '2025-03-30 00:46:22', 'Món ăn hợp khẩu vị, sẽ ủng hộ tiếp.'),
(32, 16, 2, NULL, 4, '2025-03-30 00:46:22', 'Giá ổn, đồ ăn chất lượng.'),
(33, 17, 3, NULL, 5, '2025-03-30 00:46:22', 'Tuyệt vời, sẽ giới thiệu bạn bè.'),
(34, 17, 1, NULL, 4, '2025-03-30 00:46:22', 'Phục vụ nhanh, món ăn ngon.'),
(35, 18, 2, NULL, 5, '2025-03-30 00:46:22', 'Chất lượng tốt, rất đáng tiền.'),
(36, 18, 3, NULL, 4, '2025-03-30 00:46:22', 'Phần ăn đầy đủ, đáng thử.'),
(37, 19, 1, NULL, 5, '2025-03-30 00:46:22', 'Hương vị khó quên, chắc chắn quay lại.'),
(38, 19, 2, NULL, 4, '2025-03-30 00:46:22', 'Món ăn ngon, giá hợp lý.'),
(39, 20, 3, NULL, 5, '2025-03-30 00:46:22', 'Món này không thể bỏ qua!'),
(40, 20, 1, NULL, 4, '2025-03-30 00:46:22', 'Đáng thử, rất đáng tiền!'),
(41, 21, 1, NULL, 5, '2025-03-30 01:47:34', 'Món này rất ngon, tôi sẽ quay lại!'),
(42, 21, 2, NULL, 4, '2025-03-30 01:47:34', 'Chất lượng tốt, giá hợp lý.'),
(43, 21, 3, NULL, 5, '2025-03-30 01:47:34', 'Ăn không ngán, tuyệt vời!'),
(44, 22, 1, NULL, 4, '2025-03-30 01:47:34', 'Món ăn ngon nhưng hơi đắt!'),
(45, 22, 2, NULL, 5, '2025-03-30 01:47:34', 'Món ăn tuyệt vời, hương vị đặc biệt!'),
(46, 22, 3, NULL, 4, '2025-03-30 01:47:34', 'Chất lượng tốt, phục vụ nhanh chóng.'),
(47, 23, 1, NULL, 5, '2025-03-30 01:47:34', 'Sẽ giới thiệu cho bạn bè!'),
(48, 23, 2, NULL, 4, '2025-03-30 01:47:34', 'Phần ăn đầy đủ, rất ngon!'),
(49, 23, 3, NULL, 5, '2025-03-30 01:47:34', 'Rất hài lòng, chắc chắn quay lại!'),
(50, 24, 1, NULL, 5, '2025-03-30 01:47:34', 'Chất lượng tuyệt vời, sẽ thử lại!'),
(51, 24, 2, NULL, 4, '2025-03-30 01:47:34', 'Món ăn hợp khẩu vị của tôi!'),
(52, 24, 3, NULL, 5, '2025-03-30 01:47:34', 'Món này rất ngon, giá cả hợp lý!'),
(53, 25, 1, NULL, 4, '2025-03-30 01:47:34', 'Món ăn này khá ổn, nhưng không quá xuất sắc.'),
(54, 25, 2, NULL, 5, '2025-03-30 01:47:34', 'Ngon tuyệt vời, sẽ quay lại lần sau!'),
(55, 25, 3, NULL, 4, '2025-03-30 01:47:34', 'Phục vụ tốt, hương vị rất ngon!'),
(56, 26, 1, NULL, 5, '2025-03-30 01:47:34', 'Một trong những món ăn yêu thích của tôi!'),
(57, 26, 2, NULL, 4, '2025-03-30 01:47:34', 'Món ăn ngon, nhưng có thể cải thiện chút xíu về thời gian phục vụ.'),
(58, 26, 3, NULL, 5, '2025-03-30 01:47:34', 'Tuyệt vời, nhất định sẽ quay lại!'),
(59, 27, 1, NULL, 4, '2025-03-30 01:47:34', 'Món ăn rất ngon, nhưng giá hơi cao.'),
(60, 27, 2, NULL, 5, '2025-03-30 01:47:34', 'Món này ăn rất ngon, phục vụ cũng tốt.'),
(61, 27, 3, NULL, 4, '2025-03-30 01:47:34', 'Rất hài lòng, nhưng phục vụ có thể nhanh hơn!'),
(62, 28, 1, NULL, 5, '2025-03-30 01:47:34', 'Sẽ quay lại vào lần sau, món ăn rất tuyệt vời!'),
(63, 28, 2, NULL, 4, '2025-03-30 01:47:34', 'Chất lượng món ăn rất ổn, đáng thử.'),
(64, 28, 3, NULL, 5, '2025-03-30 01:47:34', 'Rất hài lòng với món ăn này!'),
(65, 29, 1, NULL, 4, '2025-03-30 01:47:34', 'Món ăn ngon, nhưng hơi nhạt so với tôi.'),
(66, 29, 2, NULL, 5, '2025-03-30 01:47:34', 'Món này tuyệt vời, tôi rất thích!'),
(67, 29, 3, NULL, 4, '2025-03-30 01:47:34', 'Chất lượng khá ổn, sẽ quay lại nếu có dịp.'),
(68, 30, 1, NULL, 5, '2025-03-30 01:47:34', 'Ngon tuyệt vời, sẽ giới thiệu cho bạn bè!'),
(69, 30, 2, NULL, 4, '2025-03-30 01:47:34', 'Món ăn này rất ổn, nhưng tôi nghĩ có thể cải thiện một chút về gia vị.'),
(70, 30, 3, NULL, 5, '2025-03-30 01:47:34', 'Món này quá ngon, đáng thử!'),
(71, 31, 1, NULL, 5, '2025-03-30 01:47:34', 'Món ăn cực kỳ ngon, tôi sẽ quay lại!'),
(72, 31, 2, NULL, 4, '2025-03-30 01:47:34', 'Chất lượng tuyệt vời, nhưng giá hơi cao.'),
(73, 31, 3, NULL, 5, '2025-03-30 01:47:34', 'Rất hài lòng, chắc chắn sẽ quay lại!'),
(74, 32, 1, NULL, 5, '2025-03-30 01:47:34', 'Món ăn rất ngon, tôi yêu thích món này!'),
(75, 32, 2, NULL, 4, '2025-03-30 01:47:34', 'Chất lượng ổn, tuy nhiên tôi thấy hơi ít gia vị.'),
(76, 32, 3, NULL, 5, '2025-03-30 01:47:34', 'Món ăn rất đáng thử, tôi rất thích!'),
(77, 33, 1, NULL, 4, '2025-03-30 01:47:34', 'Món này khá ngon nhưng có thể đậm đà hơn.'),
(78, 33, 2, NULL, 5, '2025-03-30 01:47:34', 'Rất ngon, tôi sẽ quay lại lần sau!'),
(79, 33, 3, NULL, 4, '2025-03-30 01:47:34', 'Món này rất hợp khẩu vị của tôi.'),
(80, 34, 1, NULL, 5, '2025-03-30 01:47:34', 'Tuyệt vời, sẽ giới thiệu cho bạn bè!'),
(81, 34, 2, NULL, 4, '2025-03-30 01:47:34', 'Món ăn này ổn, nhưng có thể cải thiện chút xíu.'),
(82, 34, 3, NULL, 5, '2025-03-30 01:47:34', 'Rất ngon, món này sẽ được tôi thử lại lần sau!'),
(83, 35, 1, NULL, 4, '2025-03-30 01:47:34', 'Món ăn rất ngon nhưng cần cải thiện chút về phần gia vị.'),
(84, 35, 2, NULL, 5, '2025-03-30 01:47:34', 'Món này quá tuyệt, tôi rất thích!'),
(85, 35, 3, NULL, 4, '2025-03-30 01:47:34', 'Rất ngon, chắc chắn sẽ quay lại.'),
(86, 36, 1, NULL, 5, '2025-03-30 01:47:34', 'Sẽ quay lại vào lần sau, món ăn rất tuyệt vời!'),
(87, 36, 2, NULL, 4, '2025-03-30 01:47:34', 'Chất lượng rất ổn, giá hợp lý!'),
(88, 36, 3, NULL, 5, '2025-03-30 01:47:34', 'Ngon tuyệt vời, món ăn đáng thử!'),
(89, 37, 1, NULL, 4, '2025-03-30 01:47:34', 'Món này khá ngon, nhưng tôi nghĩ cần cải thiện về thời gian phục vụ.'),
(90, 37, 2, NULL, 5, '2025-03-30 01:47:34', 'Món ăn này rất tuyệt, tôi rất hài lòng!'),
(91, 37, 3, NULL, 4, '2025-03-30 01:47:34', 'Chất lượng khá tốt, tôi sẽ quay lại.'),
(92, 38, 1, NULL, 5, '2025-03-30 01:47:34', 'Rất ngon, món ăn tuyệt vời!'),
(93, 38, 2, NULL, 4, '2025-03-30 01:47:34', 'Món này ổn, tôi sẽ thử lại nếu có dịp.'),
(94, 38, 3, NULL, 5, '2025-03-30 01:47:34', 'Một trong những món ăn tuyệt vời nhất tôi từng thử!'),
(95, 39, 1, NULL, 4, '2025-03-30 01:47:34', 'Món ăn rất ngon nhưng hơi đắt.'),
(96, 39, 2, NULL, 5, '2025-03-30 01:47:34', 'Món này tuyệt vời, tôi rất thích!'),
(97, 39, 3, NULL, 4, '2025-03-30 01:47:34', 'Tuyệt vời, nhưng cần cải thiện về phần phục vụ.'),
(98, 40, 1, NULL, 5, '2025-03-30 01:47:34', 'Món ăn rất ngon, tôi rất hài lòng!'),
(99, 40, 2, NULL, 4, '2025-03-30 01:47:34', 'Chất lượng rất ổn, tôi thích món này!'),
(100, 40, 3, NULL, 5, '2025-03-30 01:47:34', 'Một trong những món ăn tuyệt vời nhất tôi từng thử!'),
(101, 41, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, sẽ quay lại!'),
(102, 41, 2, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng ổn, nhưng hơi đắt.'),
(103, 41, 3, NULL, 5, '2025-03-30 01:49:56', 'Rất hài lòng, món ăn tuyệt vời!'),
(104, 42, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon nhưng cần cải thiện gia vị một chút.'),
(105, 42, 2, NULL, 5, '2025-03-30 01:49:56', 'Món này rất ngon, chắc chắn sẽ quay lại.'),
(106, 42, 3, NULL, 4, '2025-03-30 01:49:56', 'Rất hợp khẩu vị, nhưng giá hơi cao.'),
(107, 43, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn ngon tuyệt vời, tôi rất thích!'),
(108, 43, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn tốt, nhưng thời gian phục vụ có thể nhanh hơn.'),
(109, 43, 3, NULL, 5, '2025-03-30 01:49:56', 'Chất lượng tuyệt vời, tôi sẽ quay lại!'),
(110, 44, 1, NULL, 4, '2025-03-30 01:49:56', 'Món này khá ngon, nhưng không có gì đặc biệt.'),
(111, 44, 2, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi rất thích!'),
(112, 44, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng ổn, sẽ thử lại nếu có dịp.'),
(113, 45, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn này rất ngon, chắc chắn quay lại!'),
(114, 45, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ổn, nhưng có thể cải thiện chút về phần gia vị.'),
(115, 45, 3, NULL, 5, '2025-03-30 01:49:56', 'Món này rất tuyệt, đáng thử!'),
(116, 46, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon, nhưng có thể cải thiện về phần phục vụ.'),
(117, 46, 2, NULL, 5, '2025-03-30 01:49:56', 'Sẽ giới thiệu cho bạn bè, món ăn tuyệt vời!'),
(118, 46, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng ổn, sẽ quay lại nếu có dịp.'),
(119, 47, 1, NULL, 5, '2025-03-30 01:49:56', 'Ngon tuyệt vời, món ăn rất đáng thử!'),
(120, 47, 2, NULL, 4, '2025-03-30 01:49:56', 'Rất ngon, nhưng giá hơi cao.'),
(121, 47, 3, NULL, 5, '2025-03-30 01:49:56', 'Tôi rất thích món này, sẽ quay lại!'),
(122, 48, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn này khá ngon, nhưng chưa đạt được sự xuất sắc.'),
(123, 48, 2, NULL, 5, '2025-03-30 01:49:56', 'Món này tuyệt vời, sẽ quay lại lần sau!'),
(124, 48, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng ổn, nhưng có thể cải thiện chút về gia vị.'),
(125, 49, 1, NULL, 5, '2025-03-30 01:49:56', 'Một trong những món ăn tuyệt vời nhất tôi từng thử!'),
(126, 49, 2, NULL, 4, '2025-03-30 01:49:56', 'Món này rất ngon, nhưng tôi nghĩ có thể cải thiện chút xíu về phần phục vụ.'),
(127, 49, 3, NULL, 5, '2025-03-30 01:49:56', 'Tôi rất hài lòng, món ăn rất ngon!'),
(128, 50, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn này khá ngon, nhưng hơi nhạt so với tôi.'),
(129, 50, 2, NULL, 5, '2025-03-30 01:49:56', 'Món này rất ngon, chắc chắn tôi sẽ quay lại!'),
(130, 50, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng rất ổn, nhưng có thể cải thiện thêm gia vị.'),
(131, 51, 1, NULL, 5, '2025-03-30 01:49:56', 'Sẽ quay lại lần sau, món ăn tuyệt vời!'),
(132, 51, 2, NULL, 4, '2025-03-30 01:49:56', 'Rất ngon, nhưng thời gian phục vụ có thể nhanh hơn.'),
(133, 51, 3, NULL, 5, '2025-03-30 01:49:56', 'Tuyệt vời, sẽ giới thiệu cho bạn bè!'),
(134, 52, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon, nhưng có thể đậm đà hơn một chút.'),
(135, 52, 2, NULL, 5, '2025-03-30 01:49:56', 'Món này rất tuyệt vời, tôi rất thích!'),
(136, 52, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng ổn, tôi sẽ quay lại lần sau.'),
(137, 53, 1, NULL, 5, '2025-03-30 01:49:56', 'Ngon tuyệt vời, sẽ quay lại lần sau!'),
(138, 53, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn này khá ngon, nhưng tôi nghĩ có thể cải thiện chút xíu.'),
(139, 53, 3, NULL, 5, '2025-03-30 01:49:56', 'Chắc chắn sẽ quay lại, món ăn tuyệt vời!'),
(140, 54, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn khá ngon nhưng chưa thật sự đặc biệt.'),
(141, 54, 2, NULL, 5, '2025-03-30 01:49:56', 'Tuyệt vời, món ăn rất ngon, tôi rất hài lòng!'),
(142, 54, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng tốt, nhưng có thể cải thiện chút về gia vị.'),
(143, 55, 1, NULL, 5, '2025-03-30 01:49:56', 'Món này rất ngon, chắc chắn sẽ quay lại!'),
(144, 55, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn khá ngon, nhưng hơi thiếu gia vị.'),
(145, 55, 3, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi rất thích!'),
(146, 56, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ổn nhưng cần cải thiện một chút về thời gian phục vụ.'),
(147, 56, 2, NULL, 5, '2025-03-30 01:49:56', 'Rất ngon, món ăn tuyệt vời!'),
(148, 56, 3, NULL, 4, '2025-03-30 01:49:56', 'Món ăn khá ổn, sẽ quay lại lần sau.'),
(149, 57, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi rất thích!'),
(150, 57, 2, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng tốt, nhưng có thể cải thiện chút về gia vị.'),
(151, 57, 3, NULL, 5, '2025-03-30 01:49:56', 'Ngon tuyệt vời, tôi sẽ quay lại!'),
(152, 58, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn này khá ngon, nhưng không quá đặc biệt.'),
(153, 58, 2, NULL, 5, '2025-03-30 01:49:56', 'Tuyệt vời, món ăn rất ngon!'),
(154, 58, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng ổn, tôi sẽ thử lại lần sau.'),
(155, 59, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn rất tuyệt vời, tôi rất hài lòng!'),
(156, 59, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon, nhưng giá hơi cao.'),
(157, 59, 3, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, chắc chắn tôi sẽ quay lại!'),
(158, 60, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn này khá ngon, nhưng chưa thật sự xuất sắc.'),
(159, 60, 2, NULL, 5, '2025-03-30 01:49:56', 'Rất ngon, tôi rất thích món này!'),
(160, 60, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng khá tốt, nhưng phục vụ có thể nhanh hơn.'),
(161, 61, 1, NULL, 5, '2025-03-30 01:49:56', 'Món này ngon tuyệt vời, tôi rất hài lòng!'),
(162, 61, 2, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng ổn, nhưng thời gian phục vụ hơi lâu.'),
(163, 61, 3, NULL, 5, '2025-03-30 01:49:56', 'Món ăn rất ngon, chắc chắn quay lại!'),
(164, 62, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn rất ngon nhưng có thể đậm đà hơn một chút.'),
(165, 62, 2, NULL, 5, '2025-03-30 01:49:56', 'Món này tuyệt vời, chắc chắn tôi sẽ quay lại!'),
(166, 62, 3, NULL, 4, '2025-03-30 01:49:56', 'Rất ngon, nhưng có thể cải thiện chút xíu về gia vị.'),
(167, 63, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, sẽ quay lại lần sau!'),
(168, 63, 2, NULL, 4, '2025-03-30 01:49:56', 'Món này rất ngon, nhưng có thể cải thiện chút về thời gian phục vụ.'),
(169, 63, 3, NULL, 5, '2025-03-30 01:49:56', 'Tuyệt vời, sẽ giới thiệu cho bạn bè!'),
(170, 64, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn này khá ngon, nhưng có thể đậm đà hơn một chút.'),
(171, 64, 2, NULL, 5, '2025-03-30 01:49:56', 'Món ăn rất tuyệt, tôi rất thích!'),
(172, 64, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng tốt, nhưng phục vụ có thể nhanh hơn.'),
(173, 65, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi rất hài lòng!'),
(174, 65, 2, NULL, 4, '2025-03-30 01:49:56', 'Món này khá ngon, nhưng không quá đặc biệt.'),
(175, 65, 3, NULL, 5, '2025-03-30 01:49:56', 'Ngon tuyệt vời, tôi rất thích món này!'),
(176, 66, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon nhưng hơi đắt.'),
(177, 66, 2, NULL, 5, '2025-03-30 01:49:56', 'Tuyệt vời, món ăn rất ngon, tôi sẽ quay lại!'),
(178, 66, 3, NULL, 4, '2025-03-30 01:49:56', 'Món ăn rất ngon, nhưng giá có thể hợp lý hơn.'),
(179, 67, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi rất thích!'),
(180, 67, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon nhưng hơi đắt.'),
(181, 67, 3, NULL, 5, '2025-03-30 01:49:56', 'Tuyệt vời, sẽ giới thiệu cho bạn bè!'),
(182, 68, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon nhưng phục vụ hơi lâu.'),
(183, 68, 2, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi sẽ quay lại lần sau!'),
(184, 68, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng rất tốt, sẽ quay lại lần sau.'),
(185, 69, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn rất ngon, tôi rất hài lòng!'),
(186, 69, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn khá ngon, nhưng thời gian phục vụ có thể cải thiện.'),
(187, 69, 3, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi sẽ quay lại!'),
(188, 70, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn rất ngon, nhưng có thể cải thiện phần gia vị.'),
(189, 70, 2, NULL, 5, '2025-03-30 01:49:56', 'Món này rất tuyệt vời, tôi rất thích!'),
(190, 70, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng rất tốt, sẽ quay lại lần sau.'),
(191, 71, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi sẽ quay lại lần sau!'),
(192, 71, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn này khá ngon, nhưng hơi thiếu gia vị.'),
(193, 71, 3, NULL, 5, '2025-03-30 01:49:56', 'Rất ngon, món ăn tuyệt vời!'),
(194, 72, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn rất ngon nhưng giá hơi cao.'),
(195, 72, 2, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi sẽ quay lại!'),
(196, 72, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng khá ổn, nhưng phục vụ có thể nhanh hơn.'),
(197, 73, 1, NULL, 5, '2025-03-30 01:49:56', 'Sẽ quay lại vào lần sau, món ăn tuyệt vời!'),
(198, 73, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon nhưng hơi đắt.'),
(199, 73, 3, NULL, 5, '2025-03-30 01:49:56', 'Rất hài lòng, món ăn tuyệt vời!'),
(200, 74, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn rất ngon nhưng cần cải thiện về gia vị.'),
(201, 74, 2, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, chắc chắn tôi sẽ quay lại!'),
(202, 74, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng rất ổn, nhưng phục vụ có thể nhanh hơn.'),
(203, 75, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi rất hài lòng!'),
(204, 75, 2, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng ổn, nhưng giá hơi cao.'),
(205, 75, 3, NULL, 5, '2025-03-30 01:49:56', 'Ngon tuyệt vời, tôi sẽ quay lại!'),
(206, 76, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn rất ngon nhưng có thể đậm đà hơn một chút.'),
(207, 76, 2, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, tôi rất thích!'),
(208, 76, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng khá tốt, tôi sẽ quay lại.'),
(209, 77, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, sẽ giới thiệu cho bạn bè!'),
(210, 77, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn rất ngon, nhưng giá hơi cao.'),
(211, 77, 3, NULL, 5, '2025-03-30 01:49:56', 'Tuyệt vời, tôi rất thích món ăn này!'),
(212, 78, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon nhưng phục vụ hơi lâu.'),
(213, 78, 2, NULL, 5, '2025-03-30 01:49:56', 'Món ăn tuyệt vời, chắc chắn tôi sẽ quay lại!'),
(214, 78, 3, NULL, 4, '2025-03-30 01:49:56', 'Chất lượng rất ổn, tôi sẽ quay lại lần sau.'),
(215, 79, 1, NULL, 5, '2025-03-30 01:49:56', 'Món ăn rất ngon, tôi rất hài lòng!'),
(216, 79, 2, NULL, 4, '2025-03-30 01:49:56', 'Món ăn này khá ngon, nhưng thời gian phục vụ có thể nhanh hơn.'),
(217, 79, 3, NULL, 5, '2025-03-30 01:49:56', 'Tuyệt vời, món ăn này rất đáng thử!'),
(218, 80, 1, NULL, 4, '2025-03-30 01:49:56', 'Món ăn ngon nhưng giá hơi cao.'),
(219, 80, 2, NULL, 5, '2025-03-30 01:49:56', 'Tuyệt vời, món ăn rất ngon, tôi sẽ quay lại!'),
(220, 80, 3, NULL, 4, '2025-03-30 01:49:56', 'Món này rất hợp khẩu vị, nhưng có thể cải thiện về phần phục vụ.'),
(221, 3, 1, NULL, 5, '2025-04-05 14:34:06', 'Món này ngon tuyệt vời, thịt rim đậm đà, tôm tươi ngon!'),
(222, 3, 2, NULL, 5, '2025-04-05 14:34:06', 'Cơm dẻo, thịt ba rọi mềm, tôm rim vừa miệng. Rất hài lòng!'),
(223, 3, 3, NULL, 5, '2025-04-05 14:34:06', 'Món này ăn hoài không chán, đặc biệt thích nước sốt rim.'),
(224, 3, 4, NULL, 4, '2025-04-05 14:34:06', 'Ngon nhưng hơi mặn một chút, có lẽ do khẩu vị cá nhân.'),
(225, 3, 5, NULL, 5, '2025-04-05 14:34:06', 'Tôm rim thơm lừng, thịt ba rọi béo ngậy. Đáng giá!'),
(226, 3, 6, NULL, 5, '2025-04-05 14:34:06', 'Món này hợp khẩu vị gia đình tôi, sẽ đặt lại.'),
(227, 3, 7, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu thêm rau sống sẽ tuyệt hơn.'),
(228, 3, 1, NULL, 5, '2025-04-05 14:34:06', 'Thịt rim mềm, tôm tươi, cơm nóng hổi. 10/10!'),
(229, 3, 2, NULL, 5, '2025-04-05 14:34:06', 'Món này đúng chuẩn vị miền Nam, rất thích!'),
(230, 3, 3, NULL, 4, '2025-04-05 14:34:06', 'Ngon nhưng hơi ít tôm, giá cả hợp lý.'),
(231, 3, 4, NULL, 5, '2025-04-05 14:34:06', 'Ăn một lần là nhớ mãi, đặc biệt thích vị ngọt của tôm.'),
(232, 3, 5, NULL, 5, '2025-04-05 14:34:06', 'Cơm nóng, thịt mềm, tôm giòn. Xuất sắc!'),
(233, 3, 6, NULL, 4, '2025-04-05 14:34:06', 'Ngon nhưng hơi nhiều dầu, có lẽ do cách chế biến.'),
(234, 3, 7, NULL, 5, '2025-04-05 14:34:06', 'Món này đáng đồng tiền, sẽ giới thiệu bạn bè.'),
(235, 3, 1, NULL, 5, '2025-04-05 14:34:06', 'Thịt ba rọi rim tôm là sự kết hợp hoàn hảo!'),
(236, 3, 2, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu thêm chút rau thơm sẽ tốt hơn.'),
(237, 3, 3, NULL, 5, '2025-04-05 14:34:06', 'Món này ăn kèm dưa leo rất hợp.'),
(238, 3, 4, NULL, 5, '2025-04-05 14:34:06', 'Tôm rim thấm gia vị, thịt ba rọi mềm. Tuyệt!'),
(239, 3, 5, NULL, 5, '2025-04-05 14:34:06', 'Món này làm tôi nhớ quê, hương vị đậm đà.'),
(240, 3, 6, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu giảm bớt đường sẽ phù hợp hơn.'),
(241, 3, 7, NULL, 5, '2025-04-05 14:34:06', 'Thịt rim tôm là món tủ của tôi, luôn ủng hộ!'),
(242, 3, 1, NULL, 5, '2025-04-05 14:34:06', 'Cơm dẻo, thịt mềm, tôm ngọt. Quá hài lòng!'),
(243, 3, 2, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng hơi ít nước sốt.'),
(244, 3, 3, NULL, 5, '2025-04-05 14:34:06', 'Món này ăn kèm nước mắm ớt rất hợp.'),
(245, 3, 4, NULL, 5, '2025-04-05 14:34:06', 'Thịt ba rọi rim tôm là món không thể bỏ qua!'),
(246, 3, 5, NULL, 5, '2025-04-05 14:34:06', 'Món này đúng vị, thịt mềm, tôm tươi.'),
(247, 3, 6, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu thêm chút đậu phộng sẽ ngon hơn.'),
(248, 3, 7, NULL, 5, '2025-04-05 14:34:06', 'Món này làm tôi muốn ăn mỗi ngày!'),
(249, 3, 1, NULL, 5, '2025-04-05 14:34:06', 'Thịt rim tôm là sự kết hợp hoàn hảo, không thể chê!'),
(250, 7, 1, NULL, 5, '2025-04-05 14:34:06', 'Gà xối mỡ giòn rụm, cơm dẻo thơm. Xuất sắc!'),
(251, 7, 2, NULL, 5, '2025-04-05 14:34:06', 'Món này ăn kèm nước mắm chua ngọt rất hợp.'),
(252, 7, 3, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng hơi nhiều dầu.'),
(253, 7, 4, NULL, 5, '2025-04-05 14:34:06', 'Gà chiên giòn, thơm lừng. Đáng đồng tiền!'),
(254, 7, 5, NULL, 5, '2025-04-05 14:34:06', 'Món này làm tôi nhớ quán gà rán nổi tiếng.'),
(255, 7, 6, NULL, 5, '2025-04-05 14:34:06', 'Gà xối mỡ giòn bên ngoài, mềm bên trong. Tuyệt!'),
(256, 7, 7, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu thêm rau sống sẽ tốt hơn.'),
(257, 7, 1, NULL, 5, '2025-04-05 14:34:06', 'Gà chiên vàng ươm, cơm nóng hổi. 10/10!'),
(258, 7, 2, NULL, 5, '2025-04-05 14:34:06', 'Món này hợp khẩu vị cả nhà tôi.'),
(259, 7, 3, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng hơi mặn một chút.'),
(260, 7, 4, NULL, 5, '2025-04-05 14:34:06', 'Gà xối mỡ đúng chuẩn, giòn tan.'),
(261, 7, 5, NULL, 5, '2025-04-05 14:34:06', 'Món này ăn hoài không ngán.'),
(262, 7, 6, NULL, 5, '2025-04-05 14:34:06', 'Gà chiên thơm, cơm dẻo. Rất hài lòng!'),
(263, 7, 7, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu giảm bớt dầu sẽ tốt hơn.'),
(264, 7, 1, NULL, 5, '2025-04-05 14:34:06', 'Gà xối mỡ là món tủ của tôi!'),
(265, 7, 2, NULL, 5, '2025-04-05 14:34:06', 'Món này đáng giá 5 sao, không thể chê.'),
(266, 7, 3, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng hơi ít nước chấm.'),
(267, 7, 4, NULL, 5, '2025-04-05 14:34:06', 'Gà chiên giòn, không ngấy. Tuyệt vời!'),
(268, 7, 5, NULL, 5, '2025-04-05 14:34:06', 'Món này ăn kèm dưa leo rất hợp.'),
(269, 7, 6, NULL, 5, '2025-04-05 14:34:06', 'Gà xối mỡ thơm lừng, cơm nóng. Xuất sắc!'),
(270, 7, 7, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu thêm chút rau thơm sẽ tốt hơn.'),
(271, 7, 1, NULL, 5, '2025-04-05 14:34:06', 'Gà chiên giòn, không bị khô. Rất ngon!'),
(272, 7, 2, NULL, 5, '2025-04-05 14:34:06', 'Món này đúng vị, sẽ đặt lại.'),
(273, 7, 3, NULL, 5, '2025-04-05 14:34:06', 'Gà xối mỡ làm tôi muốn ăn mỗi ngày!'),
(274, 7, 4, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng hơi nhiều mỡ.'),
(275, 7, 5, NULL, 5, '2025-04-05 14:34:06', 'Gà chiên vàng ươm, giòn rụm. Tuyệt!'),
(276, 7, 6, NULL, 5, '2025-04-05 14:34:06', 'Món này hợp khẩu vị cả gia đình.'),
(277, 7, 7, NULL, 5, '2025-04-05 14:34:06', 'Gà xối mỡ là món không thể bỏ qua!'),
(278, 7, 1, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu giảm bớt muối sẽ tốt hơn.'),
(279, 18, 1, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng thơm lừng, cơm tấm dẻo. Xuất sắc!'),
(280, 18, 2, NULL, 5, '2025-04-05 14:34:06', 'Món này ăn kèm nước mắm tỏi ớt rất hợp.'),
(281, 18, 3, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng hơi ít sườn.'),
(282, 18, 4, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng đậm đà, cơm thơm. Đáng giá!'),
(283, 18, 5, NULL, 5, '2025-04-05 14:34:06', 'Món này làm tôi nhớ quán cơm tấm Sài Gòn.'),
(284, 18, 6, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng mềm, không bị khô. Tuyệt!'),
(285, 18, 7, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu thêm chút bì sẽ tốt hơn.'),
(286, 18, 1, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng thấm gia vị, cơm dẻo. 10/10!'),
(287, 18, 2, NULL, 5, '2025-04-05 14:34:06', 'Món này hợp khẩu vị cả nhà tôi.'),
(288, 18, 3, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng hơi mặn một chút.'),
(289, 18, 4, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng đúng chuẩn, thơm ngon.'),
(290, 18, 5, NULL, 5, '2025-04-05 14:34:06', 'Món này ăn hoài không ngán.'),
(291, 18, 6, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng giòn, cơm dẻo. Rất hài lòng!'),
(292, 18, 7, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu giảm bớt đường sẽ tốt hơn.'),
(293, 18, 1, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng là món tủ của tôi!'),
(294, 18, 2, NULL, 5, '2025-04-05 14:34:06', 'Món này đáng giá 5 sao, không thể chê.'),
(295, 18, 3, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng hơi ít nước mắm.'),
(296, 18, 4, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng thơm, không ngấy. Tuyệt vời!'),
(297, 18, 5, NULL, 5, '2025-04-05 14:34:06', 'Món này ăn kèm dưa leo rất hợp.'),
(298, 18, 6, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng thơm lừng, cơm nóng. Xuất sắc!'),
(299, 18, 7, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu thêm chút rau thơm sẽ tốt hơn.'),
(300, 18, 1, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng mềm, không bị khô. Rất ngon!'),
(301, 18, 2, NULL, 5, '2025-04-05 14:34:06', 'Món này đúng vị, sẽ đặt lại.'),
(302, 18, 3, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng làm tôi muốn ăn mỗi ngày!'),
(303, 18, 4, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng hơi nhiều mỡ.'),
(304, 18, 5, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng vàng ươm, giòn rụm. Tuyệt!'),
(305, 18, 6, NULL, 5, '2025-04-05 14:34:06', 'Món này hợp khẩu vị cả gia đình.'),
(306, 18, 7, NULL, 5, '2025-04-05 14:34:06', 'Sườn nướng là món không thể bỏ qua!'),
(307, 18, 1, NULL, 4, '2025-04-05 14:34:06', 'Ngon, nhưng nếu giảm bớt muối sẽ tốt hơn.');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`role_id`, `role_name`) VALUES
(1, 'admin'),
(3, 'owner'),
(2, 'user');

-- --------------------------------------------------------

--
-- Table structure for table `shipping`
--

CREATE TABLE `shipping` (
  `shipping_id` int(11) NOT NULL,
  `invoice_id` int(11) NOT NULL,
  `delivery_method` tinyint(1) NOT NULL,
  `distance` decimal(5,2) DEFAULT NULL,
  `cost` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int(11) NOT NULL,
  `supplier_name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `supplier_name`, `address`, `phone`, `email`, `status`) VALUES
(1, 'CP Food', 'Số 363, Nguyễn Tri Phương, Phường 5, Quận 10, TP.HCM', '0923389893', 'cpfood@gmail.com', 1),
(2, 'VinaEggs', 'Số 45, đường Lê Lợi, Quận 1, TP.HCM', '0987654321', 'vinaeggs@gmail.com', 1),
(3, 'Nam Ngư', 'Khu công nghiệp Biên Hòa, Đồng Nai', '0251389456', 'namngu@gmail.com', 1),
(4, 'Rice Company', 'Số 12, đường 3/2, Quận 10, TP.HCM', '0912345678', 'ricecompany@gmail.com', 1),
(5, 'Vegetable Supplier', 'Chợ đầu mối Bình Điền, Quận 8, TP.HCM', '0967891234', 'vegetable.supplier@gmail.com', 1),
(6, 'Seafood Company', 'Cảng cá Vũng Tàu, Bà Rịa - Vũng Tàu', '0254369871', 'seafood.company@gmail.com', 1),
(7, 'Honey Farm', 'Lâm Đồng', '0912987456', 'honey.farm@gmail.com', 1),
(8, 'Tropical Drinks', 'Số 78, đường Nguyễn Văn Linh, Quận 7, TP.HCM', '0978123456', 'tropical.drinks@gmail.com', 1),
(9, 'Meat Deli', 'Số 56, đường Cộng Hòa, Tân Bình, TP.HCM', '0934567890', 'meat.deli@gmail.com', 1),
(10, 'Noodle Factory', 'Khu công nghiệp Tân Bình, TP.HCM', '0285432198', 'noodle.factory@gmail.com', 1),
(11, 'Spice Company', 'Số 23, đường Lê Văn Việt, Quận 9, TP.HCM', '0912876543', 'spice.company@gmail.com', 1),
(12, 'Tofu Factory', 'Số 34, đường Nguyễn Thị Minh Khai, Quận 3, TP.HCM', '0976543210', 'tofu.factory@gmail.com', 1),
(13, 'Nut Supplier', 'Chợ đầu mối Thủ Đức, TP.HCM', '0987123456', 'nut.supplier@gmail.com', 1),
(14, 'Soup Base', 'Số 67, đường Võ Văn Kiệt, Quận 1, TP.HCM', '0912348765', 'soup.base@gmail.com', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`account_id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `role_id` (`role_id`);

--
-- Indexes for table `account_detail`
--
ALTER TABLE `account_detail`
  ADD PRIMARY KEY (`account_id`);

--
-- Indexes for table `activity_logs`
--
ALTER TABLE `activity_logs`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `account_id` (`account_id`),
  ADD KEY `role_id` (`role_id`);

--
-- Indexes for table `banner`
--
ALTER TABLE `banner`
  ADD PRIMARY KEY (`banner_id`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`cart_id`),
  ADD KEY `account_id` (`account_id`),
  ADD KEY `food_id` (`food_id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `contact`
--
ALTER TABLE `contact`
  ADD PRIMARY KEY (`contact_id`),
  ADD KEY `account_id` (`account_id`);

--
-- Indexes for table `discount_code`
--
ALTER TABLE `discount_code`
  ADD PRIMARY KEY (`discount_code_id`),
  ADD UNIQUE KEY `code_name` (`code_name`);

--
-- Indexes for table `discount_usage`
--
ALTER TABLE `discount_usage`
  ADD PRIMARY KEY (`usage_id`),
  ADD KEY `account_id` (`account_id`),
  ADD KEY `discount_code_id` (`discount_code_id`);

--
-- Indexes for table `food`
--
ALTER TABLE `food`
  ADD PRIMARY KEY (`food_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `food_ingredients`
--
ALTER TABLE `food_ingredients`
  ADD PRIMARY KEY (`id`),
  ADD KEY `food_id` (`food_id`),
  ADD KEY `ingredient_id` (`ingredient_id`);

--
-- Indexes for table `ingredients`
--
ALTER TABLE `ingredients`
  ADD PRIMARY KEY (`ingredient_id`);

--
-- Indexes for table `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`invoice_id`),
  ADD KEY `account_id` (`account_id`),
  ADD KEY `discount_code_id` (`discount_code_id`);

--
-- Indexes for table `invoice_detail`
--
ALTER TABLE `invoice_detail`
  ADD PRIMARY KEY (`detail_id`),
  ADD KEY `invoice_id` (`invoice_id`),
  ADD KEY `food_id` (`food_id`);

--
-- Indexes for table `order_status`
--
ALTER TABLE `order_status`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `invoice_id` (`invoice_id`);

--
-- Indexes for table `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`token_id`),
  ADD UNIQUE KEY `token` (`token`),
  ADD KEY `account_id` (`account_id`);

--
-- Indexes for table `pending_accounts`
--
ALTER TABLE `pending_accounts`
  ADD PRIMARY KEY (`pending_id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `token` (`token`);

--
-- Indexes for table `product_view`
--
ALTER TABLE `product_view`
  ADD PRIMARY KEY (`view_id`),
  ADD KEY `food_id` (`food_id`),
  ADD KEY `account_id` (`account_id`);

--
-- Indexes for table `revenue`
--
ALTER TABLE `revenue`
  ADD PRIMARY KEY (`revenue_id`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `food_id` (`food_id`),
  ADD KEY `account_id` (`account_id`),
  ADD KEY `invoice_id` (`invoice_id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`role_id`),
  ADD UNIQUE KEY `role_name` (`role_name`);

--
-- Indexes for table `shipping`
--
ALTER TABLE `shipping`
  ADD PRIMARY KEY (`shipping_id`),
  ADD KEY `invoice_id` (`invoice_id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `account_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `activity_logs`
--
ALTER TABLE `activity_logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=84;

--
-- AUTO_INCREMENT for table `banner`
--
ALTER TABLE `banner`
  MODIFY `banner_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `contact`
--
ALTER TABLE `contact`
  MODIFY `contact_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `discount_code`
--
ALTER TABLE `discount_code`
  MODIFY `discount_code_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `discount_usage`
--
ALTER TABLE `discount_usage`
  MODIFY `usage_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `food`
--
ALTER TABLE `food`
  MODIFY `food_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=82;

--
-- AUTO_INCREMENT for table `food_ingredients`
--
ALTER TABLE `food_ingredients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=238;

--
-- AUTO_INCREMENT for table `ingredients`
--
ALTER TABLE `ingredients`
  MODIFY `ingredient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT for table `invoice`
--
ALTER TABLE `invoice`
  MODIFY `invoice_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `invoice_detail`
--
ALTER TABLE `invoice_detail`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `order_status`
--
ALTER TABLE `order_status`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  MODIFY `token_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pending_accounts`
--
ALTER TABLE `pending_accounts`
  MODIFY `pending_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `product_view`
--
ALTER TABLE `product_view`
  MODIFY `view_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `revenue`
--
ALTER TABLE `revenue`
  MODIFY `revenue_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `review`
--
ALTER TABLE `review`
  MODIFY `review_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=308;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `shipping`
--
ALTER TABLE `shipping`
  MODIFY `shipping_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`);

--
-- Constraints for table `account_detail`
--
ALTER TABLE `account_detail`
  ADD CONSTRAINT `account_detail_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE;

--
-- Constraints for table `activity_logs`
--
ALTER TABLE `activity_logs`
  ADD CONSTRAINT `activity_logs_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `activity_logs_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`);

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`) ON DELETE CASCADE;

--
-- Constraints for table `contact`
--
ALTER TABLE `contact`
  ADD CONSTRAINT `contact_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`);

--
-- Constraints for table `discount_usage`
--
ALTER TABLE `discount_usage`
  ADD CONSTRAINT `discount_usage_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`),
  ADD CONSTRAINT `discount_usage_ibfk_2` FOREIGN KEY (`discount_code_id`) REFERENCES `discount_code` (`discount_code_id`);

--
-- Constraints for table `food`
--
ALTER TABLE `food`
  ADD CONSTRAINT `food_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`);

--
-- Constraints for table `food_ingredients`
--
ALTER TABLE `food_ingredients`
  ADD CONSTRAINT `food_ingredients_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`),
  ADD CONSTRAINT `food_ingredients_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`ingredient_id`);

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`),
  ADD CONSTRAINT `invoice_ibfk_2` FOREIGN KEY (`discount_code_id`) REFERENCES `discount_code` (`discount_code_id`);

--
-- Constraints for table `invoice_detail`
--
ALTER TABLE `invoice_detail`
  ADD CONSTRAINT `invoice_detail_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`invoice_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `invoice_detail_ibfk_2` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`);

--
-- Constraints for table `order_status`
--
ALTER TABLE `order_status`
  ADD CONSTRAINT `order_status_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`invoice_id`) ON DELETE CASCADE;

--
-- Constraints for table `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD CONSTRAINT `password_reset_tokens_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE;

--
-- Constraints for table `product_view`
--
ALTER TABLE `product_view`
  ADD CONSTRAINT `product_view_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`),
  ADD CONSTRAINT `product_view_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`);

--
-- Constraints for table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `review_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`),
  ADD CONSTRAINT `review_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`),
  ADD CONSTRAINT `review_ibfk_3` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`invoice_id`);

--
-- Constraints for table `shipping`
--
ALTER TABLE `shipping`
  ADD CONSTRAINT `shipping_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`invoice_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
