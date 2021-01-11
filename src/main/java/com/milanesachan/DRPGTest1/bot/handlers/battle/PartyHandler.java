package com.milanesachan.DRPGTest1.bot.handlers.battle;

import com.milanesachan.DRPGTest1.bot.core.BattleCommandManager;
import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PartyHandler implements Handler {
    private MessageChannel channel;
    private long guildID;

    public PartyHandler(MessageChannel channel, long guildID) {
        this.channel = channel;
        this.guildID = guildID;
    }

    @Override
    public void handle() {
        BattleCommandManager bcm = BattleCommandManager.getInstance();

        if(!bcm.doesPartyExist(guildID)){
            channel.sendMessage("This server has no party! Create one with '>battle'.").queue();
        }else{
            GuildParty p = bcm.getParty(guildID);
            MessageEmbed eb = p.getEmbed().build();
            channel.sendMessage(eb).queue();
        }
    }
}
