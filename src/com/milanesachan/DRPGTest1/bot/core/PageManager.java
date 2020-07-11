package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public class PageManager extends ListenerAdapter implements Runnable {
    private static PageManager instance;
    private ArrayList<PagedEmbed> validPagedEmbeds = new ArrayList<>();
    public static final String reactNext = "\uD83D\uDC49";
    public static final String reactPrev = "\uD83D\uDC48";

    private static final int expirationTime = 50;
    private static final int cleanupLoopSleep = 5000;
    private boolean doCleanupLoop = true;

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);

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

            synchronized (this) {
                Iterator<PagedEmbed> it = validPagedEmbeds.iterator();
                while (it.hasNext()) {
                    if (it.next().getMessageTime().isBefore(threshold)) {
                        it.remove();
                        embedsCleaned++;
                    }
                }
            }

            if(embedsCleaned>0) {
                System.out.println("PageManagerCleanup: " + embedsCleaned + " cleaned.");
            }

            try {
                Thread.sleep(cleanupLoopSleep);
                synchronized (this){
                    while(validPagedEmbeds.size()==0){
                        System.out.println("PageManagerCleanup goes to sleep.");
                        wait();
                    }
                }
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    public void stopCleanupLoop(){
        doCleanupLoop = false;
    }

    public synchronized void addToValidList(PagedEmbed pagedEmbed){
        notifyAll();
        validPagedEmbeds.add(pagedEmbed);
    }
}
