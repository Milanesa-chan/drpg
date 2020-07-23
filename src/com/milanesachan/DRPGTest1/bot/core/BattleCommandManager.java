package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.handlers.battle.CreatePartyHandler;
import com.milanesachan.DRPGTest1.bot.handlers.battle.SetBattleChannelHandler;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.ArrayList;

public class BattleCommandManager extends ListenerAdapter {
    private static BattleCommandManager instance;
    private ArrayList<GuildParty> parties;
    private String[] battleCommands = {"battle"};

    public static BattleCommandManager getInstance(){
        if(instance == null)
            instance = new BattleCommandManager();
        return instance;
    }

    private BattleCommandManager(){
        parties = new ArrayList<>();
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        String[] args = event.getMessage().getContentRaw().split(" ");
        //Also check if command is set battle channel
        if(matchCommand(args[0], "setbattlechannel")){
            SetBattleChannelHandler h = new SetBattleChannelHandler(event.getChannel(),
                    event.getMember().getUser().getIdLong(),
                    event.getGuild().getIdLong());
            h.handle();
        }else if(matchCommand(args[0], "battle")){
            long guildID = event.getGuild().getIdLong();
            long userID = event.getMember().getUser().getIdLong();
            new CreatePartyHandler(event.getChannel(), guildID, userID).handle();
        }
        //First check if the received message is a battle command
        //If it is, and it's not coming from a battle channel, print it
        //If it is coming from a battle channel, continue
    }

    private boolean isBattleChannel(MessageReceivedEvent event, long guildID, long channelID){
        try {
            Guild g = new GuildFactory().guildFromServerID(guildID);
            return g.getBattleChannelID()==channelID;
        } catch (SQLException throwables) {
            event.getChannel().sendMessage("Error connecting to database. Try again later.").queue();
        } catch (ServerNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean matchCommand(String comm, String test) {
        return comm.equalsIgnoreCase(DRPGBot.getInstance().getPrefix().concat(test));
    }

    public ArrayList<GuildParty> getParties(){
        return parties;
    }
}
