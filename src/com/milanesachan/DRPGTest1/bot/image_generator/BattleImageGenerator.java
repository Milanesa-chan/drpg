package com.milanesachan.DRPGTest1.bot.image_generator;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class BattleImageGenerator {
    private static BattleImageGenerator instance;

    public static BattleImageGenerator getInstance() {
        if (instance == null)
            instance = new BattleImageGenerator();
        return instance;
    }

    public void sendArenaImage(MessageChannel channel, GuildParty allies, GuildParty enemies) {
        try {
            BufferedImage imageToSend = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);

            Graphics g = imageToSend.getGraphics();

            //Paint background
            BufferedImage arena = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("images/battle/arena.png")));
            g.drawImage(arena, 0, 0, null);

            //Paint characters
            paintCharacter(g, allies.getRandomChar(), new Point(400, 420), true);
            paintCharacter(g, enemies.getRandomChar(), new Point(400, 180), false);
            g.dispose();

            ByteOutputStream out = new ByteOutputStream();
            ImageIO.write(imageToSend, "png", out);
            channel.sendFile(out.getBytes(), "battleimage.png").queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void paintCharacter(Graphics g, BattleCharacter character, Point center, boolean isAlly){
        //Paint name
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.setColor(Color.BLACK);
        String charName = character.getCharacter().getName();
        int xPos = (int) (center.getX() - g.getFontMetrics().stringWidth(charName)/2);
        g.drawString(charName, xPos, (int) (center.getY()-70));

        //Paint circle
        g.setColor(isAlly ? Color.BLUE : Color.RED);
        g.fillOval((int) center.getX()-50, (int) center.getY()-50, 100, 100);

        //Paint HP bar
        int barXPos = (int) (center.getX()-50);
        int barYPos = (int) (center.getY()+70);
        int HP = character.getHP();
        int maxHP = character.getMaxHP();
        Color bColor = new Color(0x044500);
        Color fColor = new Color(0x00FF19);
        paintBar(g, barXPos, barYPos, 100, 5, HP, maxHP, bColor, fColor);

        //Paint HP Value
        String hpValue = HP+"/"+maxHP;
        g.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    private void paintBar(Graphics g, int x, int y, int width, int height, int val, int maxVal, Color backColor, Color frontColor){
        Color colorBackup = g.getColor();

        g.setColor(backColor);
        int filledPixels = (int) ((((double) val)/((double) maxVal))*width);
        g.fillRect(x, y, width, height);
        g.setColor(frontColor);
        g.fillRect(x, y, filledPixels, height);

        g.setColor(colorBackup);
    }
}
