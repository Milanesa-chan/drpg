package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.bot.handlers.HandlerFilter;
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
        }
        if(matchCommand(args[0], "battle")){
            long guildID = event.getGuild().getIdLong();
            long userID = event.getMember().getUser().getIdLong();
            CreatePartyHandler h = new CreatePartyHandler(event.getChannel(), guildID, userID);

            HandlerFilter filter = new HandlerFilter();
            filter.setBattleChannelRequired(true);
            filter.setCharacterRequired(true);
            filter.setGuildMemberRequired(true);
            filter.FilterHandle(event, h, userID);
        }
    }

    private boolean matchCommand(String comm, String test) {
        return comm.equalsIgnoreCase(DRPGBot.getInstance().getPrefix().concat(test));
    }

    public ArrayList<GuildParty> getParties(){
        return parties;
    }

    public GuildParty getParty(long guildID){
        for(GuildParty p : parties){
            if(p.getGuildID() == guildID) return p;
        }
        return null;
    }

    public boolean doesPartyExist(long guildID){
        for(GuildParty p : parties){
            if(p.getGuildID() == guildID) return true;
        }
        return false;
    }
}
