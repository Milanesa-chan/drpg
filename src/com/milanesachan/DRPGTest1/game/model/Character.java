package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.io.*;

public class Character implements Embeddable {
    private long userID;
    private long guildID;
    private String name;
    private int HP, maxHP;

    public Character(String userID, String name, int HP, int maxHP){
        this.userID = Long.parseLong(userID);
        this.name = name;
        this.HP = HP;
        this.maxHP = maxHP;
    }

    public MessageEmbed getEmbed(){
        User user = DRPGBot.getInstance().getJda().getUserById(userID);
        assert user != null;
        String avatarUrl = user.getAvatarUrl();
        String userName = user.getName();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(this.name);
        eb.setDescription(userName+"'s current character");
        eb.addField("Health", HP+"/"+maxHP, false);
        eb.setThumbnail(avatarUrl);
        eb.setColor(0x450000);

        return eb.build();
    }
}
