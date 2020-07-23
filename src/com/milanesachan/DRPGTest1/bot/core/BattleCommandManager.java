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
        //First check if the received message is a battle command
        //If it is, and it's not coming from a battle channel, print it
        //If it is coming from a battle channel, continue
    }

    private void onBattleChannelRequiredCommand(MessageReceivedEvent event, Handler h){
        if(isBattleChannel(event)){
            h.handle();
        }else{
            try {
                Guild g = new GuildFactory().guildFromServerID(event.getGuild().getIdLong());
                event.getChannel().sendMessage("This command is only for battle channels.").queue();
                if(g.getBattleChannelID()==0) {
                    event.getChannel().sendMessage("This server has no battle channel! Ask the owner to type" +
                            " '>setbattlechannel' on a channel of this server.").queue();
                }else{
                    long channelID = g.getBattleChannelID();
                    event.getChannel().sendMessage("This server's battle channel is: <#"+channelID+">").queue();
                }
            } catch (SQLException throwables) {
                event.getChannel().sendMessage("Error connecting to the database.").queue();
            } catch (ServerNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isBattleChannel(MessageReceivedEvent event){
        try {
            long guildID = event.getGuild().getIdLong();
            long channelID = event.getChannel().getIdLong();
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
