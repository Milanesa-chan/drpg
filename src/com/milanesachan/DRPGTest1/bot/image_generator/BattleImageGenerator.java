package com.milanesachan.DRPGTest1.bot.image_generator;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void paintCharacter(Graphics g, BattleCharacter character, Point center, boolean isAlly){
        //Paint name
        String charName = character.getCharacter().getName();
        int xPos = (int) (center.getX() - g.getFontMetrics().stringWidth(charName)/2);
        g.drawString(charName, xPos, (int) (center.getY()-30));

        //Paint circle
        g.drawOval((int) center.getX()-20, (int) center.getY()-20, 40, 40);

        //Paint HP bar

    }

    private void paintBar(Graphics g, int x, int y, int val, int maxVal, Color color){
        
    }
}
