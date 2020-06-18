package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

public class HelpHandler implements Handler{
    private MessageChannel channel;

    public HelpHandler(MessageChannel mc){
        channel = mc;
    };

    public void handle(){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("DRPG Help");
        eb.setDescription("Help about how to use this bot.");
        eb.addField("Commands:", "**>help** : displays this message xd.\n" +
                "**>createchar name** : creates a new character for your account.\n" +
                "**>deletechar** : deletes your existing character.\n" +
                "**>infochar** : shows info for your current character.\n" +
                "**>createguild name** : (owner) creates a new guild for this server.\n" +
                "**>deleteguild** : (owner) deletes the guild for this server.\n" +
                "**>joinguild** : join this server's guild.", false);
        eb.setThumbnail(DRPGBot.getInstance().getJda().getSelfUser().getAvatarUrl());
        eb.setColor(0x450000);

        channel.sendMessage(eb.build()).queue();
        eb.clear();
    }


}
