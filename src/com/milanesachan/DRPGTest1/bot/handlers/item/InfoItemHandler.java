package com.milanesachan.DRPGTest1.bot.handlers.item;

import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Item;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class InfoItemHandler implements Handler {
    private MessageChannel channel;
    private String itemID;

    public InfoItemHandler(MessageChannel mc, String itemID){
        this.channel = mc;
        this.itemID = itemID;
    }

    @Override
    public void handle() {
        Item it;
        try {
            it = ItemFactory.getInstance().getItemFromID(itemID);
            EmbedBuilder emb = it.getEmbed();
            channel.sendMessage(emb.build()).queue();
        } catch (ItemNotFoundException e) {
            channel.sendMessage("**Error:** '"+itemID+"' is not a valid itemID.").queue();
        }
    }
}
