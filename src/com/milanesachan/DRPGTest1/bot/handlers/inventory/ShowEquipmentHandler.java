package com.milanesachan.DRPGTest1.bot.handlers.inventory;

import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Equipment;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.sql.SQLException;

public class ShowEquipmentHandler implements Handler {
    private MessageChannel channel;
    private long userID;

    public ShowEquipmentHandler(MessageChannel channel, long userID) {
        this.channel = channel;
        this.userID = userID;
    }

    @Override
    public void handle() {
        Equipment equipment = new Equipment(userID);
        try {
            equipment.loadFromDatabase();
            MessageEmbed emb = equipment.getEmbed().build();
            channel.sendMessage(emb).queue();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (EquipmentNotFoundException e) {
            channel.sendMessage("No equipment found for your character.").queue();
        }
    }
}
