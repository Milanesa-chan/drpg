package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;

public class Character implements Embeddable {
    private long userID;
    private long guildID;
    private String name;
    private int HP = 100, maxHP = 100;
    //private Equipment equipment;

    public Character(long userID){
        this.userID = userID;
    }

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

    /*
    public Equipment getEquipment() {
        return equipment;
    }
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
    */

    public void loadFromDatabase() throws SQLException, CharacterNotFoundException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `characters` WHERE `UID`="+userID+";");
            if(rs.next()){
                this.name = rs.getString("Name");
                this.HP = rs.getInt("HP");
                this.maxHP = rs.getInt("MaxHP");
                this.guildID = rs.getLong("GuildID");
                /*
                this.equipment = new Equipment(userID);
                try {
                    equipment.loadFromDatabase();
                }catch(CharacterNotFoundException ignored){}
                */
            }else throw new CharacterNotFoundException();
            con.close();
        }
    }

    public void saveToDatabase() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            stmt.execute("SELECT * FROM `characters` WHERE `UID`="+userID);
            if(stmt.getFetchSize()==0){
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO `characters` (UID, GuildID, Name, HP, MaxHP) VALUES (?, ?, ?, ?, ?);");
                pstmt.setLong(1, this.userID);
                pstmt.setLong(2, this.guildID);
                pstmt.setString(3, this.name);
                pstmt.setInt(4, this.HP);
                pstmt.setInt(5, this.maxHP);
                pstmt.execute();
                /*
                if(this.equipment != null){
                    equipment.saveToDatabase();
                }
                 */
            }else{
                PreparedStatement pstmt = con.prepareStatement("UPDATE `characters` SET `Name`=?, `GuildID`=?, `HP`=?, `MaxHP`=? WHERE `UID`=?;");
                pstmt.setString(1, this.name);
                pstmt.setLong(2, this.guildID);
                pstmt.setInt(3, this.HP);
                pstmt.setInt(4, this.maxHP);
                pstmt.setLong(5, this.userID);
                pstmt.execute();
                /*
                if(this.equipment != null){
                    equipment.saveToDatabase();
                }
                */
            }
        }
    }
}
