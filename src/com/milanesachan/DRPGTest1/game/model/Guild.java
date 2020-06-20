package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Guild implements Embeddable {
    private long guildID;
    private String name;

    public Guild() {
    }

    public long getGuildID() {
        return guildID;
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public EmbedBuilder getEmbed() {
        JDA jda = DRPGBot.getInstance().getJda();
        String serverName = jda.getGuildById(guildID).getName();
        String serverIconUrl = jda.getGuildById(guildID).getIconUrl();
        EmbedBuilder emb = new EmbedBuilder();

        try {
            emb.setTitle("Guild: " + name);
            emb.setDescription("Server: " + serverName);
            emb.addField("Members", memberListAsString(DatabaseConnector.getInstance().getGuildMembers(guildID)), true);
            emb.setThumbnail(serverIconUrl);
            emb.setColor(0x450000);
            return emb;
        } catch (SQLException | CharacterNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String memberListAsString(HashMap<User, Character> memberMap) {
        Iterator<Map.Entry<User, Character>> it = memberMap.entrySet().iterator();
        String memList = "";
        while(it.hasNext()){
            Map.Entry<User, Character> ent = it.next();
            String charName = ent.getValue().getName();
            String userName = ent.getKey().getAsTag();
            memList = memList.concat(charName + " ("+userName+")\n");
        }
        return memList;
    }
}
