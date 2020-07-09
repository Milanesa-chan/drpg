package com.milanesachan.DRPGTest1.bot.handlers.character;

import com.milanesachan.DRPGTest1.bot.core.CommandManager;
import com.milanesachan.DRPGTest1.bot.handlers.Confirmable;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.game.model.Inventory;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.Connection;
import java.sql.Statement;

public class CharacterDeletionHandler implements Handler, Confirmable {
    private Long userID;
    private MessageChannel channel;

    public CharacterDeletionHandler(MessageChannel channel, String userID) {
        this.userID = Long.parseLong(userID);
        this.channel = channel;
    }

    public void handle(){
        channel.sendMessage("<@"+userID+"> Are you sure you want to delete your character? (y/n).").queue();
        CommandManager.getInstance().addToConfirmationList(userID, this);
    }

    public void confirm(){
        try {
            Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
            if(con != null){
                Statement stmt = con.createStatement();
                int amountDeleted = stmt.executeUpdate("DELETE FROM `characters` WHERE `UID`="+userID);
                if(amountDeleted == 0){
                    channel.sendMessage("**Error:** No character found for your account!").queue();
                }else{
                    new Inventory(userID).deleteInDatabase();
                    channel.sendMessage("**Success:** Your character was deleted.").queue();
                }
            }else{
                throw new Exception();
            }
        }catch (Exception ex) {
            channel.sendMessage("**Error:** Failed to connect to database. Try again later.").queue();
        }
    }

    public void cancel(){
        channel.sendMessage("<@"+userID+"> Character deletion cancelled.").queue();
    }
}
