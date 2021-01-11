package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Guild implements Embeddable {
    private long guildID;
    private long battleChannelID;
    private String name;

    public Guild(long guildID) {this.guildID = guildID;}

    public long getGuildID() {
        return guildID;
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public long getBattleChannelID() {
        return battleChannelID;
    }

    public void setBattleChannelID(long battleChannelID) {
        this.battleChannelID = battleChannelID;
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

    public Guild loadFromDatabase() throws SQLException, ServerNotFoundException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM `guilds` WHERE `UID`="+guildID);
            if(res.next()){
                this.name = res.getString("Name");
                this.battleChannelID = res.getLong("BattleChannelID");
                return this;
            }else throw new ServerNotFoundException();
        }else throw new SQLException("Unknown");
    }

    public void saveToDatabase() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            stmt.executeQuery("SELECT * FROM `guilds` WHERE `UID`="+guildID);
            if(!stmt.getResultSet().next()){
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO `guilds` (UID, Name, BattleChannelID) VALUES (?, ?, ?);");
                pstmt.setLong(1, guildID);
                pstmt.setString(2, name);
                pstmt.setLong(3, battleChannelID);
                pstmt.execute();
            }else{
                PreparedStatement pstmt = con.prepareStatement("UPDATE `guilds` SET `Name`=?, `BattleChannelID`=? WHERE `UID`=?;");
                pstmt.setString(1, name);
                pstmt.setLong(2, battleChannelID);
                pstmt.setLong(3, guildID);
                pstmt.execute();
            }
            con.close();
        }else throw new SQLException("Unknown");
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
