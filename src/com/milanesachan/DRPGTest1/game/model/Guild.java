package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Guild implements Embeddable {
    private long guildID;
    private String name;

    public Guild(){}

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
    public MessageEmbed getEmbed() {
        return null;
    }
}
