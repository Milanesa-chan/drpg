package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.core.PagedEmbedBuilder;
import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;

public class HelpHandler implements Handler{
    private MessageChannel channel;
    private ArrayList<String> helpData;
    public HelpHandler(MessageChannel mc){
        channel = mc;
        initializeHelpData();
    };

    //ADD COMMANDS TO >help HERE:
    private void initializeHelpData(){
        EmbedBuilder b = new EmbedBuilder();
        helpData = new ArrayList<>();
        helpData.add("**>help** : displays this message lol.");
        helpData.add("**>createchar name** : creates a new character for your account.");
        helpData.add("**>deletechar** : deletes your existing character.");
        helpData.add("**>infochar** : shows info for your current character.");
        helpData.add("**>infoguild** : shows info about the current server's guild.");
        helpData.add("**>createguild name** : (owner) creates a new guild for this server.");
        helpData.add("**>deleteguild** : (owner) deletes the guild for this server.");
        helpData.add("**>joinguild** : join this server's guild.");
    }

    public void handle(){
        PagedEmbedBuilder b = new PagedEmbedBuilder(channel);
        b.setTitle("DRPG Help");
        b.setDescription("Help about how to use this bot.");
        b.setData(helpData);
        b.setLinesPerPage(4);
        b.setThumbnailUrl(DRPGBot.getInstance().getJda().getSelfUser().getAvatarUrl());
        b.setColor(0x450000);
        PagedEmbed eb = b.fromStringArray();
        eb.send();
    }

}
