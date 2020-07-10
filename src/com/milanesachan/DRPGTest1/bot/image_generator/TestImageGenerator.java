package com.milanesachan.DRPGTest1.bot.image_generator;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class TestImageGenerator {
    private static TestImageGenerator instance;

    public static TestImageGenerator getInstance() {
        if (instance == null)
            instance = new TestImageGenerator();
        return instance;
    }

    public void postImage(MessageChannel channel, long userID, Point pos) {
        try {
            BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);

            JDA jda = DRPGBot.getInstance().getJda();
            String avatarUrl = jda.getUserById(userID).getAvatarUrl();

            URLConnection con = new URL(avatarUrl).openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            con.connect();

            BufferedImage userAvatar = ImageIO.read(con.getInputStream());

            Graphics g = image.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 800, 600);
            g.setColor(Color.RED);
            g.fillRect(350, 250, 100, 100);

            g.drawImage(userAvatar, (int)pos.getX(), (int)pos.getY(), new Color(0, 0, 0, 0), null);

            g.dispose();

            ByteOutputStream out = new ByteOutputStream();
            ImageIO.write(image, "png", out);
            channel.sendFile(out.getBytes(), "image.png").queue();
        } catch (IOException e) {
            e.printStackTrace();
            channel.sendMessage("Error creating image.").queue();
        }
    }

}
