package com.milanesachan.DRPGTest1.bot.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;

public class DRPGBot {
    private static DRPGBot botInstance;
    private JDA jda;
    private String prefix = ">";
    private final Long[] masterIDs = {232572720632692737L};
    private final String version = "0.2-PRE";

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
            jda.addEventListener(CommandManager.getInstance());
            jda.addEventListener(PageManager.getInstance());
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

    public String getVersion() {
        return version;
    }

    public boolean isMasterUser(User user){
        long userID = user.getIdLong();
        for(long l : masterIDs){
            if(userID == l) return true;
        }
        return false;
    }
}
