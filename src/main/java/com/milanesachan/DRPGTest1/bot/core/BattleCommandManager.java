package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.handlers.HandlerFilter;
import com.milanesachan.DRPGTest1.bot.handlers.battle.*;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class BattleCommandManager extends ListenerAdapter {
    private static BattleCommandManager instance;
    private final ArrayList<GuildParty> parties;

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
                long inviterID = event.getMember().getIdLong();
                PartyInviteHandler h = new PartyInviteHandler(event.getChannel(), event.getGuild().getIdLong(), inviterID ,invitedUserID);

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
        }else if(matchCommand(args[0], "queue")){
            long guildID = event.getGuild().getIdLong();

            BattleQueueHandler h = new BattleQueueHandler(event.getChannel(), guildID);

            HandlerFilter f = new HandlerFilter();
            f.setBattleChannelRequired(true);
            f.setGuildMemberRequired(true);
            f.setPartyRequired(true);
            f.setPartyMemberRequired(true);
            f.filterHandler(event, h, event.getMember().getUser().getIdLong());
        }else if(matchCommand(args[0], "setlane")){
            if(args.length<2) event.getChannel().sendMessage("Problem in command. Correct format is: '>setlane " +
                    "front' or '>setlane back'.").queue();
            else{
                long guildID = event.getGuild().getIdLong();
                long userID = event.getMember().getUser().getIdLong();

                SetLaneHandler h = new SetLaneHandler(event.getChannel(), userID, guildID, args[1]);

                HandlerFilter f = new HandlerFilter();
                f.setBattleChannelRequired(true);
                f.setGuildMemberRequired(true);
                f.setPartyRequired(true);
                f.setPartyMemberRequired(true);
                f.filterHandler(event, h, userID);
            }
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

    public boolean removeParty(long guildID){
        GuildParty toRemove = null;
        for(GuildParty p : parties){
            if(p.getGuildID() == guildID) toRemove = p;
        }
        if(toRemove != null) return parties.remove(toRemove);
        else return false;
    }

    public boolean doesPartyExist(long guildID){
        for(GuildParty p : parties){
            if(p.getGuildID() == guildID) return true;
        }
        return false;
    }
}
