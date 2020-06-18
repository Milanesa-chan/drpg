package com.milanesachan.DRPGTest1.commons.console;


import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
        String message = event.getMessage().getContentRaw();
        String user = event.getAuthor().getAsTag();
        String server = event.getGuild().getName();
        Print("Message: \""+message+"\" from user: \""+user+"\" in server: \""+server+"\".");
    }
}
