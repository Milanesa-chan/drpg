package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.core.AnswerManager;
import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.handlers.statcards.FusionHandler;
import com.milanesachan.DRPGTest1.bot.image_generator.TestImageGenerator;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class TesterHandler implements Handler, Answerable{
    private MessageChannel channel;
    private MessageReceivedEvent event;

    public TesterHandler(MessageReceivedEvent ev){
        channel = ev.getChannel();
        event = ev;
    }

    @Override
    public void handle() {

    }

    private boolean isInt(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    @Override
    public void answer(String answer) {
        channel.sendMessage("Received response: "+answer).queue();
    }

    @Override
    public boolean tryAnswer(String answer) {
        if(answer.startsWith(DRPGBot.getInstance().getPrefix())) return false;
        else answer(answer);
        return true;
    }

    @Override
    public void cancel() {
        channel.sendMessage("Cancelled response command.").queue();
    }
}
