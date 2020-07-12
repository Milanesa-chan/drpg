package com.milanesachan.DRPGTest1.bot.core;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class BattleCommandManager extends ListenerAdapter {
    private static BattleCommandManager instance;
    private String[] battleCommands = {"battle"};


    public static BattleCommandManager getInstance(){
        if(instance == null)
            instance = new BattleCommandManager();
        return instance;
    }


    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        String[] args = event.getMessage().getContentRaw().split(" ");
        //Also check if command is set battle channel
        if(matchCommand(args[0], "setbattlechannel")){

        }
        //First check if the received message is a battle command
        //If it is, and it's not coming from a battle channel, print it
        //If it is coming from a battle channel, continue
    }

    private boolean matchCommand(String comm, String test) {
        return comm.equalsIgnoreCase(DRPGBot.getInstance().getPrefix().concat(test));
    }
}
