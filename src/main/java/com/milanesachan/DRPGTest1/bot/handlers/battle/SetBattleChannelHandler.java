package com.milanesachan.DRPGTest1.bot.handlers.battle;

import com.milanesachan.DRPGTest1.bot.core.CommandManager;
import com.milanesachan.DRPGTest1.bot.handlers.Confirmable;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SetBattleChannelHandler implements Handler, Confirmable {
    private MessageChannel channel;
    private long userID;
    private long guildID;
    private long battleChannelID;

    public SetBattleChannelHandler(MessageChannel mc, long userID, long guildID){
        channel = mc;
        this.userID = userID;
        this.guildID = guildID;
        this.battleChannelID = channel.getIdLong();
    }

    @Override
    public void handle() {
        try {
            if (isAlreadyBattleChannel())
                channel.sendMessage("You are already in the battle channel!").queue();
            else {
                channel.sendMessage("Are you sure you want to set this channel (<#" + channel.getIdLong() + ">) as the battle channel? (y/n)").queue();
                CommandManager.getInstance().addToConfirmationList(userID, this);
            }
        }catch(SQLException e){
            e.printStackTrace();
            channel.sendMessage("Error connecting to the database. Try again later.").queue();
        }
    }

    @Override
    public void confirm() {
        try {
            Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
            if(con != null){
                Statement stmt = con.createStatement();
                stmt.execute("UPDATE `guilds` SET `BattleChannelID`="+battleChannelID+" WHERE `UID`="+guildID);
                if(stmt.getUpdateCount()==0)
                    channel.sendMessage("There was an error updating the battle channel. Try again later.").queue();
                else
                    channel.sendMessage("Success! This channel is now the battle channel of this server.\n" +
                            "The bot will use this channel to spam the battle status when you are in the arena!\n" +
                            "This channel can be used to organize the party and queue for battle.\n" +
                            "Good luck!").queue();
            }else throw new SQLException();
        } catch (SQLException e) {
            e.printStackTrace();
            channel.sendMessage("Error connecting to the database. Try again later.").queue();
        }
    }

    @Override
    public void cancel() {
        channel.sendMessage("Action cancelled.").queue();
    }

    private boolean isAlreadyBattleChannel() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        boolean res = false;
        if(con != null){
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `guilds` WHERE `UID`="+guildID);
            while(rs.next()){
                long bcID = rs.getLong("BattleChannelID");
                res = battleChannelID == bcID;
            }
        }else throw new SQLException();
        return res;
    }
}
