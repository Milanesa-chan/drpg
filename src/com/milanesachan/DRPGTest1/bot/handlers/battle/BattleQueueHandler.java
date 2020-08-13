package com.milanesachan.DRPGTest1.bot.handlers.battle;

import com.milanesachan.DRPGTest1.bot.core.BattleCommandManager;
import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.Prompts;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.networking.MatchMaker;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.SQLException;

public class BattleQueueHandler implements Handler {
    private MessageChannel channel;
    private long guildID;

    public BattleQueueHandler(MessageChannel channel, long guildID) {
        this.channel = channel;
        this.guildID = guildID;
    }

    @Override
    public void handle() {
        GuildParty party = BattleCommandManager.getInstance().getParty(guildID);
        int[] teamSizes = MatchMaker.teamSizes;

        try {
            if(party.isInQueue()){
                MatchMaker.getInstance().removeFromQueue(party);
                party.setInQueue(false);
                channel.sendMessage("**Stopped searching!**").queue();
            }else if(!party.getReady(teamSizes)){
                channel.sendMessage(Prompts.teamSizeError(teamSizes)).queue();
            }else{
                int inQueue = MatchMaker.getInstance().addToQueuesList(party);
                party.setInQueue(true);
                channel.sendMessage("**Searching for a match...** (Currently in queue: "+inQueue+"" +
                        " guild"+(inQueue>1 ? "s" : "")+") (type '>queue' again to cancel)").queue();
            }
        } catch (EquipmentNotFoundException e) {
            long charUserID = e.getCharacter().getUserID();
            channel.sendMessage("User <@"+charUserID+"> has no equipment!").queue();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
