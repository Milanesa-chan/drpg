package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.core.CharacterFactory;
import com.milanesachan.DRPGTest1.bot.core.CommandHandler;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class GuildJoinHandler implements Handler, Confirmable {
    private MessageChannel channel;
    private long serverID;
    private long userID;

    public GuildJoinHandler(MessageChannel mc, long serverID, long userID) {
        channel = mc;
        this.serverID = serverID;
        this.userID = userID;
    }

    public void handle() {
        try {
            if (!DatabaseConnector.getInstance().isGuildInDatabase(serverID)) {
                channel.sendMessage("**Error:** This server has no guild registered to it.").queue();
            } else if (!DatabaseConnector.getInstance().isCharacterInDatabase(userID)) {
                channel.sendMessage("**Error:** You don't have a character. Create one using '>createchar'.").queue();
            } else {
                CharacterFactory cf = new CharacterFactory();
                long userGuildID = cf.characterFromUserID(userID).getGuildID();
                if (userGuildID == serverID) {
                    channel.sendMessage("<@" + userID + "> You are already in this guild.").queue();
                } else {
                    channel.sendMessage("<@" + userID + "> Are you sure you want to join this server's guild? (y/n)").queue();
                    CommandHandler.getInstance().addToConfirmationList(userID, this);
                }
            }
        } catch (
                SQLException ex) {
            channel.sendMessage("**Error:** Failed to connect to database. Try again later.").queue();
        } catch (
                CharacterNotFoundException e) {
            channel.sendMessage("**Error:** I don't know what happened, but try again later.").queue();
        }
    }


    public void confirm() {
        try {
            Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
            if (con != null) {
                PreparedStatement stmt = con.prepareStatement("UPDATE `characters` SET `GuildID` = ? WHERE `characters`.`UID` = ?;");
                stmt.setLong(1, serverID);
                stmt.setLong(2, userID);
                int upd = stmt.executeUpdate();
                if (upd > 0) {
                    String guildName = DatabaseConnector.getInstance().getGuildName(serverID);
                    channel.sendMessage("<@" + userID + "> You joined the guild: '" + guildName + "'. Welcome!").queue();
                } else {
                    throw new ServerNotFoundException();
                }
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            channel.sendMessage("**Error:** Failed to connect to database. Try again later.").queue();
        } catch (ServerNotFoundException e) {
            channel.sendMessage("**Error:** I don't know what happened, but try again later.").queue();
        }
    }

    public void cancel() {
        channel.sendMessage("<@" + userID + "> Guild join has been cancelled.").queue();
    }
}
