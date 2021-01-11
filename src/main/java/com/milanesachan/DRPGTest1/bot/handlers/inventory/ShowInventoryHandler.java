package com.milanesachan.DRPGTest1.bot.handlers.inventory;

import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Inventory;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.SQLException;

public class ShowInventoryHandler implements Handler {
    private MessageChannel channel;
    private long userID;

    public ShowInventoryHandler(MessageChannel channel, long userID){
        this.channel = channel;
        this.userID = userID;
    }

    @Override
    public void handle() {
        Inventory inv = new Inventory(userID);
        try {
            inv.loadFromDatabase();
            if(inv.isEmpty()){
                channel.sendMessage("<@"+userID+"> your inventory is empty!").queue();
            }else{
                PagedEmbed inventoryEmbed = inv.getPagedEmbed(channel);
                inventoryEmbed.send();
            }
        } catch (SQLException | CharacterNotFoundException e) {
            e.printStackTrace();
        }
    }
}
