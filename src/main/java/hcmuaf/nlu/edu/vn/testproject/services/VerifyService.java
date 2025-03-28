package hcmuaf.nlu.edu.vn.testproject.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class VerifyService {
    private final String from = "anhtuanwork0925@gmail.com";
    private final String password = "hbnw mtjx zikp icnw"; // App password từ Gmail
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(20); // Thời gian hết hạn là 5 phút
    }

    public boolean isExpireTime(LocalDateTime expiryTime) {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    public boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public boolean sendVerificationEmail(String to, String token, String name) {
        if (!isValidEmail(to)) {
            System.out.println("Email không hợp lệ: " + to);
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
            msg.setSubject("Xác thực email đăng ký", "UTF-8");

            String link = "http://localhost:8080/testProject/verify?token=" + token;
            String content = "<h1>Xin chào " + name + "</h1>" +
                    "<p>Vui lòng nhấp vào liên kết dưới đây để xác thực email của bạn (hết hạn sau 20 phút):</p>" +
                    "<a href='" + link + "'>Xác thực email</a>";
            msg.setContent(content, "text/html; charset=UTF-8");

            Transport.send(msg);
            System.out.println("Gửi email xác thực thành công!");
            return true;
        } catch (MessagingException e) {
            System.out.println("Lỗi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}