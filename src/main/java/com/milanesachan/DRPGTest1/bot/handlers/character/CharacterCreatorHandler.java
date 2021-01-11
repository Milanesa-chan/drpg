package com.milanesachan.DRPGTest1.bot.handlers.character;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.parameters.CharacterParameters;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.*;

public class CharacterCreatorHandler implements Handler {
    private Long userID;
    private String charName;
    private MessageChannel channel;

    public CharacterCreatorHandler(MessageChannel mc, String userID, String charName){
        channel = mc;
        this.userID = Long.parseLong(userID);
        this.charName = charName;
    }

    public void handle(){
        String currentUserChar = "";
        try {
            currentUserChar = alreadyExists();
        } catch (SQLException e) {
            e.printStackTrace();
            channel.sendMessage("**Error:** Failed to create the new character. Try again later.").queue();
            return;
        }

        if(!currentUserChar.isEmpty()){
            channel.sendMessage("**Error:** You already have a character: '"+currentUserChar+"'!").queue();
        }else if(charName.isEmpty()){
            channel.sendMessage("**Error:** You must specify a name. Format: **\">createchar name\"**.").queue();
        }else{
            try{
                createNewCharacter();
                String userName = DRPGBot.getInstance().getJda().getUserById(userID).getAsTag();
                channel.sendMessage("**Success:** <@"+userID+"> created the new character: \""+charName+"\".").queue();
            }catch(Exception ex){
                ex.printStackTrace();
                channel.sendMessage("**Error:** Failed to create the new character. Try again later.").queue();
            }
        }
    }

    private void createNewCharacter() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null) {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO `characters` (`UID`, `Name`, `HP`, `MaxHP`) VALUES (?, ?, ?, ?) ");
            stmt.setLong(1, userID);
            stmt.setString(2, charName);
            stmt.setInt(3, CharacterParameters.initialMaxHealth);
            stmt.setInt(4, CharacterParameters.initialMaxHealth);
            stmt.execute();
            con.close();
        }else{
            throw new SQLException();
        }
    }

    /**
     * Checks if the userID is already associated with a character
     * @return name of the character if it already exists, empty string otherwise
     */
    private String alreadyExists() throws SQLException{
        String ret = "";
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM `characters` WHERE `UID`="+userID+";");
            if(result.next())
                ret = result.getString("Name");
        }else{
            throw new SQLException();
        }
        con.close();
        return ret;
    }
}
