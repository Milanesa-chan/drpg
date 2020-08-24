package com.milanesachan.DRPGTest1.bot.handlers;

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

    }

    @Override
    public void cancel() {

    }
}
