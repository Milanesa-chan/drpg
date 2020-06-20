package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.game.model.Item;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class InfoItemHandler implements Handler{
    private MessageChannel channel;
    private String itemID;

    public InfoItemHandler(MessageChannel mc, String itemID){
        channel = mc;
        this.itemID = itemID;
    }

    @Override
    public void handle() {
        Item it = ItemFactory.getInstance().getItemFromID(itemID);
        if(it!=null){
            EmbedBuilder emb = it.getEmbed();
            channel.sendMessage(emb.build()).queue();
        }else{
            channel.sendMessage("**Error:** Couldn't find that itemID.").queue();
        }
    }
}
