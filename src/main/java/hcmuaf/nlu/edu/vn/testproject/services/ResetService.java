package hcmuaf.nlu.edu.vn.testproject.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

public class ResetService {
    private final int LIMIT_MINUS = 3;
    private final String from = "anhtuanwork0925@gmail.com";
    private final String password = "hbnw mtjx zikp icnw";

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(3);
    }

    public boolean isExpireTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

    public boolean sendEmail(String to, String link, String name) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.debug", "true"); // Bật debug

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };

        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);

        try {
            msg.addHeader("Content-type", "text/html; charset=UTF-8");
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject("Reset Password", "UTF-8");
            String content = "<h1>Xin chào " + name + "</h1>" +
                    "<p>Đây là email tự động từ hệ thống đặt lại mật khẩu của chúng tôi.</p>" +
                    "<p>Click vào liên kết sau để đặt lại mật khẩu: <a href=\"" + link + "\">Click here</a></p>";
            msg.setContent(content, "text/html; charset=UTF-8");
            Transport.send(msg);
            System.out.println("Gửi email thành công tới: " + to);
            return true;
        } catch (MessagingException e) {
            System.out.println("Lỗi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
