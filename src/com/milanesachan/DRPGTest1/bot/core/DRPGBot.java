package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.commons.console.ConsoleManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class DRPGBot {
    private static DRPGBot botInstance;
    private JDA jda;
    private String prefix = ">";
    private final Long masterID = 232572720632692737L;

    public static void main(String[] args){
        getInstance();
    }

    private DRPGBot(){
        InitializeJDA();
    }

    public static DRPGBot getInstance(){
        if(botInstance == null)
            botInstance = new DRPGBot();
        return botInstance;
    }

    private void InitializeJDA(){
        try {
            jda = new JDABuilder("NzIyNzUwNTIwODU0MzE1MDU4.Xunolg.-0_ZIxG94Jp_ez7SJFV-mzFIQG4").build();
            jda.getPresence().setActivity(Activity.of(Activity.ActivityType.DEFAULT, " \">help\" "));
            jda.addEventListener(CommandHandler.getInstance());
        }catch(Exception ex){
            System.out.println("There was an error Logging In. Failed to run.");
        }
    }

    public JDA getJda(){
        return jda;
    }

    public String getPrefix(){
        return prefix;
    }

    public Long getMasterID(){
        return masterID;
    }
}
