package hcmuaf.nlu.edu.vn.testproject.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

public class VerifyService {
    private final String from = "anhtuanwork0925@gmail.com";
    private final String password = "rmvm vpro bbxb xixd"; // App password từ Gmail

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public boolean sendVerificationEmail(String to, String token, String username) {
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
            String content = "<h1>Xin chào " + username + "</h1>" +
                    "<p>Vui lòng nhấp vào liên kết dưới đây để xác thực email của bạn:</p>" +
                    "<a href='" + link + "'>Xác thực email</a>";
            msg.setContent(content, "text/html; charset=UTF-8");

            Transport.send(msg);
            System.out.println("Gửi email xác thực thành công!");
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}