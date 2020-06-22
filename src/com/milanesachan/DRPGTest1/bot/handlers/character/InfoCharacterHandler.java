package com.milanesachan.DRPGTest1.bot.handlers.character;

import com.milanesachan.DRPGTest1.bot.core.CharacterFactory;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Character;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.sql.SQLException;

public class InfoCharacterHandler implements Handler {
    private long userID;
    private long currentServerID;
    private MessageChannel channel;

    public InfoCharacterHandler(MessageChannel mc, String userID, long currentServerID) {
        this.userID = Long.parseLong(userID);
        this.channel = mc;
        this.currentServerID = currentServerID;
    }

    public void handle() {
        try {
            Character character = new CharacterFactory().characterFromUserID(userID);
            if (character.getGuildID() == 0 || character.getGuildID() != currentServerID) {
                String name = character.getName();
                name = name.concat(" (Forager)");
                character.setName(name);
            }
            MessageEmbed mes = character.getEmbed().build();
            channel.sendMessage(mes).queue();
        } catch (SQLException e) {
            channel.sendMessage("**Error:** Failed to connect to database. Try again later.").queue();
        } catch (CharacterNotFoundException e) {
            channel.sendMessage("**Error:** No character found for you account").queue();
        }
    }
}
