package com.milanesachan.DRPGTest1.commons.console;


import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.format.DateTimeFormatter;

public class ConsoleManager {
    private static ConsoleManager cmInstance;

    private ConsoleManager(){}

    public static ConsoleManager getInstance(){
        if(cmInstance == null)
            cmInstance = new ConsoleManager();

        return cmInstance;
    }

    public void Print(String s){
        System.out.println(s);
    }

    public void PrintMessageEventInfo(MessageReceivedEvent event){
        String timeSent = event.getMessage().getTimeCreated().format(DateTimeFormatter.ISO_TIME);
        String message = event.getMessage().getContentRaw();
        String user = event.getAuthor().getAsTag();
        String server = event.getGuild().getName();

        Print(timeSent+" Message: \""+message+"\" from user: \""+user+"\" in server: \""+server+"\".");
    }
}
