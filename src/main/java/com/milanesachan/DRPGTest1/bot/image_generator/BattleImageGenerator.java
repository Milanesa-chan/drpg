package com.milanesachan.DRPGTest1.bot.image_generator;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;
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

    /*TODO Make all of this code more "parameterizable" and flexible. Things like the width and offset of HP bars
    and the image size itself have to be adjustable in order to fiddle with it.*/
    public void sendArenaImage(MessageChannel channel, GuildParty allies, GuildParty enemies) {
        try {
            BufferedImage imageToSend = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);

            Graphics g = imageToSend.getGraphics();

            //Paint background
            BufferedImage arena = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("images/battle/arena.png")));
            g.drawImage(arena, 0, 0, null);

            BattleCharacter bc;
            int listSize;
            int paintStart;
            int paintWidth;
            int offset;

            //Paint ally characters
            listSize = allies.getCharList().size();
            paintWidth = listSize*100 + ((listSize-1)*20);
            paintStart = 400 - paintWidth/2;
            for(int i=0; i<listSize; i++) {
                bc = allies.getCharList().get(i);
                offset = i*100;
                offset += i*20;
                paintCharacter(g, bc, new Point(paintStart+offset+50, 420), true);
            }

            //Paint enemy characters
            listSize = enemies.getCharList().size();
            paintWidth = listSize*100 + ((listSize-1)*20);
            paintStart = 400 - paintWidth/2;
            for(int i=0; i<listSize; i++) {
                bc = enemies.getCharList().get(i);
                offset = i*100;
                offset += i*20;
                paintCharacter(g, bc, new Point(paintStart+offset+50, 180), false);
            }
            g.dispose();

            ByteOutputStream out = new ByteOutputStream();
            ImageIO.write(imageToSend, "png", out);
            channel.sendFile(out.getBytes(), "battleimage.png").queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //All of this code should mantain an "offset" variable that goes from top to down mantaining the bottom of the last
    //thing drawn. This way you don't have to guess what is the offset in every fucking part of the character
    private void paintCharacter(Graphics g, BattleCharacter character, Point center, boolean isAlly){
        //Paint name
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.setColor(Color.BLACK);
        String charName = character.getCharacter().getName();
        int xPos = (int) (center.getX() - g.getFontMetrics().stringWidth(charName)/2);
        g.drawString(charName, xPos, (int) (center.getY()-70));

        //Paint circle
        g.setColor(isAlly ? Color.BLUE : Color.RED);
        g.fillOval((int) center.getX()-40, (int) center.getY()-40, 80, 80);

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
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        int hpValueHeight = g.getFontMetrics().getHeight();
        int hpValueWidth = g.getFontMetrics().stringWidth(hpValue);
        g.drawString(hpValue, (int) center.getX()-hpValueWidth/2, (int) center.getY()+hpValueHeight+80);

        //Paint Energy bar
        int yOffset = hpValueHeight+85;
        barYPos = (int) center.getY()+yOffset;
        int energy = character.getEnergy();
        int maxEnergy = character.getMaxEnergy();
        bColor = new Color(0x50006F);
        fColor = new Color(0xDD66FF);
        paintBar(g, barXPos, barYPos, 100, 5, energy, maxEnergy, bColor, fColor);

        //Paint energy value
        String enValue = energy+"/"+maxEnergy;
        g.setColor(Color.BLACK);
        int enValueWidth = g.getFontMetrics().stringWidth(enValue);
        g.drawString(enValue, (int) center.getX()-enValueWidth/2, (int) center.getY()+yOffset+hpValueHeight+5);
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
