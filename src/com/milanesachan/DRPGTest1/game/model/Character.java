package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.sql.SQLException;

public class Character implements Embeddable {
    private long userID;
    private long guildID;
    private String name;
    private int HP, maxHP;

    public Character(){}

    public EmbedBuilder getEmbed(){
        User user = DRPGBot.getInstance().getJda().getUserById(userID);
        assert user != null;
        String avatarUrl = user.getAvatarUrl();
        String userName = user.getName();

        String guildName;
        try {
            guildName = DatabaseConnector.getInstance().getGuildName(guildID);
            guildName = guildName.concat(" ("+DRPGBot.getInstance().getJda().getGuildById(guildID).getName()+")");
        } catch (ServerNotFoundException | SQLException e) {
            guildName = "";
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(this.name);
        eb.setDescription(userName+"'s current character");
        if(!guildName.isEmpty())
            eb.addField("Guild", guildName, false);
        eb.addField("Health", HP+"/"+maxHP, false);
        eb.setThumbnail(avatarUrl);
        eb.setColor(0x450000);

        return eb;
    }

    public long getGuildID() {
        return guildID;
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
