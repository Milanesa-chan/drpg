package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import com.milanesachan.DRPGTest1.game.model.Character;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CharacterFactory {

    public Character characterFromUserID(Long userID) throws SQLException, CharacterNotFoundException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM `characters` WHERE `UID`="+userID);
            if(result.next()){
                Character ch = new Character();
                ch.setUserID(userID);
                ch.setName(result.getString("Name"));
                ch.setHP(result.getInt("HP"));
                ch.setMaxHP(result.getInt("MaxHP"));
                ch.setGuildID(result.getLong("GuildID"));
                return ch;
            }else{
                con.close();
                throw new CharacterNotFoundException();
            }
        }else{
            throw new SQLException("Failed to connect.");
        }
    }

}
