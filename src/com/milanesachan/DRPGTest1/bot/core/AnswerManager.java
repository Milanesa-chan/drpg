package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.handlers.Answerable;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class AnswerManager extends ListenerAdapter {
    private HashMap<Long, Answerable> waitingList;
    private static AnswerManager instance;

    public static AnswerManager getInstance(){
        if(instance == null)
            instance = new AnswerManager();
        return instance;
    }

    private AnswerManager(){
        waitingList = new HashMap<>();
    }

    @Override
    public synchronized void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        assert event.getMember() != null;
        long userID = event.getMember().getIdLong();
        String messageRaw = event.getMessage().getContentRaw();

        if(!event.getMember().getUser().isBot()){
            if(waitingList.containsKey(userID)){
                Answerable answerable = waitingList.remove(userID);
                if(messageRaw.equals(">cancel"))
                    answerable.cancel();
                else
                    answerable.answer(messageRaw);
            }
        }
    }

    public synchronized void addToWaitingList(long userID, Answerable answerable){
        Answerable oldAnswerable = waitingList.remove(userID);
        if(oldAnswerable != null)
            oldAnswerable.cancel();

        waitingList.put(userID, answerable);
    }
}
