package hcmuaf.nlu.edu.vn.testproject.controllers.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@WebServlet(name = "CaptchaServlet", value = "/captcha")
public class CaptchaServlet extends HttpServlet {
    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String captcha = generateCaptchaText(6);
        HttpSession session = request.getSession();
        session.setAttribute("captcha", captcha);

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        Random random = new Random();
        int x = 10;
        int y = 35;
        for (char c : captcha.toCharArray()) {
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g2d.drawString(String.valueOf(c), x, y + random.nextInt(10) - 5);
            x += 20;
        }

        for (int i = 0; i < 50; i++) {
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g2d.fillOval(random.nextInt(WIDTH), random.nextInt(HEIGHT), 3, 3);
        }

        g2d.dispose();

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
        os.flush();
        os.close();
    }

    private String generateCaptchaText(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < length; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }
        return captcha.toString();
    }
}