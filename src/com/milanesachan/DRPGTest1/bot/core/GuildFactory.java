package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Guild;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GuildFactory {

    public Guild guildFromServerID(long serverID) throws SQLException, ServerNotFoundException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM `guilds` WHERE `UID`="+serverID+";");
            if(result.next()){
                Guild guild = new Guild();
                guild.setName(result.getString("Name"));
                guild.setGuildID(serverID);
                guild.setBattleChannelID(result.getLong("BattleChannelID"));
                return guild;
            }else{
                throw new ServerNotFoundException();
            }
        }else{
            throw new SQLException();
        }
    }
}
