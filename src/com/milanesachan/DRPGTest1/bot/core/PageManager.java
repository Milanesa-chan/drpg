package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class PageManager extends ListenerAdapter implements Runnable {
    private static PageManager instance;
    private ArrayList<PagedEmbed> validPagedEmbeds = new ArrayList<>();
    public static final String reactNext = "➡️️", reactPrev = "⬅️";
    private static final int expirationTime = 120;

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        System.out.println(event.getReactionEmote().getEmoji()); //TODO get rid of this of-fucking-course
        

    }

    private boolean isMessageValid(long messageID){
        for(PagedEmbed pe : validPagedEmbeds){
            if(pe.getMessageID() == messageID){
                return true;
            }
        }
        return false;
    }

    public static PageManager getInstance(){
        if(instance == null)
            instance = new PageManager();
        return instance;
    }

    private PageManager(){
        initializeThread();
    }

    private void initializeThread(){
        Thread cleanupThread = new Thread(this);
        cleanupThread.start();
    }

    @Override
    public void run() {
        //TODO cleanup routine
    }
}
