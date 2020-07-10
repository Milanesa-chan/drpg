package com.milanesachan.DRPGTest1.bot.image_generator;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TestImageGenerator {
    private static TestImageGenerator instance;

    public static TestImageGenerator getInstance(){
        if(instance == null)
            instance = new TestImageGenerator();
        return instance;
    }

    public void postImage(MessageChannel channel){
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 600);
        g.setColor(Color.RED);
        g.fillRect(350, 250, 100, 100);
        g.dispose();

        ByteOutputStream out = new ByteOutputStream();
        try {
            ImageIO.write(image, "png", out);
            channel.sendFile(out.getBytes(), "image.png").queue();
        } catch (IOException e) {
            e.printStackTrace();
            channel.sendMessage("Error creating image.").queue();
        }
    }

}
