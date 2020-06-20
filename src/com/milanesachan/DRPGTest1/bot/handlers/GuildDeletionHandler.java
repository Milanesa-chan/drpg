package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.core.CommandManager;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.Statement;

public class GuildDeletionHandler implements Handler, Confirmable {
    private long guildID;
    private User serverOwner;
    private MessageChannel channel;
    private int remainingConfirmations = 1;

    public GuildDeletionHandler(MessageChannel mc, long guildID, User serverOwner){
        this.guildID = guildID;
        this.channel = mc;
        this.serverOwner = serverOwner;
    }

    public void handle(){
        channel.sendMessage("**YOU ARE ABOUT TO DELETE THE WHOLE GUILD. ARE YOU SURE? (y/n)**").queue();
        CommandManager.getInstance().addToConfirmationList(serverOwner.getIdLong(), this);
    }

    @Override
    public void confirm() {
        if(remainingConfirmations>0){
            channel.sendMessage("**ONE LAST TIME. ARE YOU SURE? (y/n)**").queue();
            remainingConfirmations--;
            CommandManager.getInstance().addToConfirmationList(serverOwner.getIdLong(), this);
        }else{
            try {
                Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
                if(con != null){
                    Statement stmt = con.createStatement();
                    int amountDeleted = stmt.executeUpdate("DELETE FROM `guilds` WHERE `UID`="+guildID);
                    if(amountDeleted == 0){
                        channel.sendMessage("**Error:** This server doesn't have a guild registered!").queue();
                    }else{
                        channel.sendMessage("**Success:** The guild has been deleted.").queue();
                    }
                    con.close();
                }else{
                    throw new Exception();
                }
            }catch (Exception ex) {
                channel.sendMessage("**Error:** Failed to connect to database. Try again later.").queue();
            }
        }
    }

    @Override
    public void cancel() {
        channel.sendMessage("Guild deletion cancelled.").queue();
    }
}
