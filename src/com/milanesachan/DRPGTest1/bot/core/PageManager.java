package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class PageManager extends ListenerAdapter implements Runnable {
    private static PageManager instance;
    private ArrayList<PagedEmbed> validPagedEmbeds = new ArrayList<>();
    public static final String reactNext = "\uD83D\uDC49";
    public static final String reactPrev = "\uD83D\uDC48";
    private static final int expirationTime = 5;
    private static final int cleanupLoopSleep = 5000;
    private boolean doCleanupLoop = true;

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        System.out.println("Current valid embeds: "+validPagedEmbeds.size());

        assert event.getMember() != null;
        if(!event.getMember().getUser().isBot()) {
            if (isMessageValid(event.getMessageIdLong())) {
                filterReaction(event);
            }
        }
    }

    private void filterReaction(MessageReactionAddEvent event){
        String messageReact = event.getReactionEmote().getEmoji();
        PagedEmbed pagedEmbed = getPagedEmbedFromMessageID(event.getMessageIdLong());
        assert pagedEmbed != null;
        if(messageReact.equals(reactNext))
            pagedEmbed.nextPage();
        else if(messageReact.equals(reactPrev))
            pagedEmbed.prevPage();
    }

    private boolean isMessageValid(long messageID){
        for(PagedEmbed pe : validPagedEmbeds){
            if(pe.getMessageID() == messageID){
                return true;
            }
        }
        return false;
    }

    private PagedEmbed getPagedEmbedFromMessageID(long messageID){
        for(PagedEmbed pe : validPagedEmbeds){
            if(pe.getMessageID() == messageID){
                return pe;
            }
        }
        return null;
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
        int embedsCleaned;
        while(doCleanupLoop){
            embedsCleaned = 0;
            OffsetDateTime currentTime = OffsetDateTime.now();
            OffsetDateTime threshold = currentTime.minusSeconds(expirationTime);

            for(PagedEmbed pe : validPagedEmbeds){
                if(pe.getMessageTime().isBefore(threshold)){
                    validPagedEmbeds.remove(pe);
                    embedsCleaned++;
                }
            }
            System.out.println("Cleanup loop removed: "+embedsCleaned+" embeds from the list.");
            try {
                Thread.sleep(cleanupLoopSleep);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    public void stopCleanupLoop(){
        doCleanupLoop = false;
    }

    public void addToValidList(PagedEmbed pagedEmbed){
        validPagedEmbeds.add(pagedEmbed);
    }
}
