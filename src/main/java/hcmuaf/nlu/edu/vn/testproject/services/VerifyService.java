package hcmuaf.nlu.edu.vn.testproject.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerifyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyService.class);
    private final String from = "anhtuanwork0925@gmail.com";
    private final String password = "hbnw mtjx zikp icnw"; // App password từ Gmail
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(5); // Tăng lên 5 phút
    }

    public boolean isExpireTime(LocalDateTime expiryTime) {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    public boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public String generateOtpCode() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo số ngẫu nhiên 6 chữ số
        return String.valueOf(otp);
    }

    private boolean sendEmail(String to, String subject, String content) {
        if (!isValidEmail(to)) {
            LOGGER.warn("Email không hợp lệ: {}", to);
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };

        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);

        try {
            msg.addHeader("content-type", "text/html; charset=UTF-8");
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject, "UTF-8");
            msg.setContent(content, "text/html; charset=UTF-8");

            Transport.send(msg);
            LOGGER.info("Gửi email thành công tới: {}", to);
            return true;
        } catch (MessagingException e) {
            LOGGER.error("Lỗi gửi email tới {}: {}", to, e.getMessage());
            return false;
        }
    }

    public boolean sendVerificationEmail(String to, String token, String name) {
        String link = "http://localhost:8080/testProject/verify?token=" + token;
        String content = "<h1>Xin chào " + name + "</h1>" +
                "<p>Đây là email tự động từ hệ thống đăng ký của chúng tôi.</p>" +
                "<p>Click vào liên kết sau để xác thực tài khoản: <a href=\"" + link + "\">Click here</a></p>" +
                "<p>Liên kết này sẽ hết hạn sau 5 phút.</p>";
        return sendEmail(to, "Xác thực email đăng ký", content);
    }

    public boolean sendOtpEmail(String to, String otpCode, String name) {
        String content = "<h1>Xin chào " + name + "</h1>" +
                "<p>Mã OTP của bạn để mở khóa tài khoản là:</p>" +
                "<h2>" + otpCode + "</h2>" +
                "<p>Mã này sẽ hết hạn sau 5 phút.</p>";
        return sendEmail(to, "Mã OTP để mở khóa tài khoản", content);
    }
}