package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.core.PagedEmbedBuilder;
import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

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
        /*
        PagedEmbedBuilder b = new PagedEmbedBuilder(channel);
        b.setTitle("DRPG Help");
        b.setDescription("Help about how to use this bot.");
        b.setData(helpData);
        b.setLinesPerPage(4);
        b.setThumbnailUrl(DRPGBot.getInstance().getJda().getSelfUser().getAvatarUrl());
        b.setColor(0x450000);
        PagedEmbed eb = b.fromStringArray();
        eb.send();
        */

        PagedEmbed pe = new PagedEmbed(channel);
        pe.addPage(getPageMiscelaneous());
        pe.addPage(getPageCharacter());
        pe.addPage(getPageInventory());
        pe.addPage(getPageGuild());
        pe.send();
    }

    private MessageEmbed getPageMiscelaneous(){
        EmbedBuilder eb = getBlankPage();
        eb.addField("Miscelaneous", "**>help** : displays this message", false);
        return eb.build();
    }

    private MessageEmbed getPageCharacter(){
        EmbedBuilder eb = getBlankPage();
        eb.addField("Character", "**>infochar** : see your character's info \n" +
                "**>createchar <name>** : create your character and set its name\n" +
                "**>deletechar** : delete your character (irreversible)", false);
        return eb.build();
    }

    private MessageEmbed getPageInventory(){
        EmbedBuilder eb = getBlankPage();
        eb.addField("Inventory", "**>infoitem <itemID>** : show info about an item \n" +
                "**>inv** : show your inventory \n" +
                "**>inv <@User>** : show another user's inventory \n" +
                "**>equip** : show your equipment \n" +
                "**>equip <itemID>** : equip an item from your inventory \n" +
                "**>unequip** : unequip your current weapon", false);
        return eb.build();
    }

    private MessageEmbed getPageGuild(){
        EmbedBuilder eb = getBlankPage();
        eb.addField("Guild", "**>infoguild** : show this server's guild info \n" +
                "**>joinguild** : join this server's guild \n" +
                "**>createguild <name>** (owner only) : create a guild for this server \n" +
                "**>deleteguild** (owner only) : delete this server's guild", false);
        return eb.build();
    }

    private EmbedBuilder getBlankPage(){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("DRPG Bot Help");
        eb.setDescription("Use the reactions to scroll through the pages!");
        eb.setThumbnail(DRPGBot.getInstance().getJda().getSelfUser().getAvatarUrl());
        eb.setColor(0x450000);
        return eb;
    }

}
