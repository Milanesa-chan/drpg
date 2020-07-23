package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.handlers.HandlerFilter;
import com.milanesachan.DRPGTest1.bot.handlers.battle.CreatePartyHandler;
import com.milanesachan.DRPGTest1.bot.handlers.battle.PartyHandler;
import com.milanesachan.DRPGTest1.bot.handlers.battle.PartyInviteHandler;
import com.milanesachan.DRPGTest1.bot.handlers.battle.SetBattleChannelHandler;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
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
            filter.filterHandler(event, h, userID);
        }else if(matchCommand(args[0], "invite")){
            if(args.length < 2 ||
            event.getMessage().getMentionedMembers().isEmpty()){
                event.getChannel().sendMessage("Problem in command. Correct format is: '>invite <@User>'.").queue();
            }else{
                long invitedUserID = event.getMessage().getMentionedMembers().get(0).getIdLong();

                PartyInviteHandler h = new PartyInviteHandler(event.getChannel(), event.getGuild().getIdLong(), invitedUserID);

                HandlerFilter filter = new HandlerFilter();
                filter.setBattleChannelRequired(true);
                filter.setGuildMemberRequired(true);
                filter.setCharacterRequired(true);
                filter.filterHandler(event, h, invitedUserID);
            }
        }else if(matchCommand(args[0], "party")) {
            long guildID = event.getGuild().getIdLong();

            PartyHandler h = new PartyHandler(event.getChannel(), guildID);

            HandlerFilter filter = new HandlerFilter();
            filter.setBattleChannelRequired(true);
            filter.filterHandler(event, h, 0);
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
