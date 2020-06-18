package com.milanesachan.DRPGTest1.bot.handlers;
import com.milanesachan.DRPGTest1.bot.core.CharacterFactory;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Character;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.sql.SQLException;

public class InfoCharacterHandler implements Handler{
    private long userID;
    private MessageChannel channel;

    public InfoCharacterHandler(MessageChannel mc, String userID){
        this.userID = Long.parseLong(userID);
        this.channel = mc;
    }

    public void handle(){
        try {
            Character character = new CharacterFactory().characterFromUserID(userID);
            MessageEmbed mes = character.getEmbed();
            channel.sendMessage(mes).queue();
        } catch (SQLException e) {
            channel.sendMessage("**Error:** Failed to connect to database. Try again later.").queue();
        } catch (CharacterNotFoundException e) {
            channel.sendMessage("**Error:** No character found for you account").queue();
        }
    }
}
