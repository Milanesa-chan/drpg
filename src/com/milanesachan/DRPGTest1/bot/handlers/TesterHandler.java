package com.milanesachan.DRPGTest1.bot.handlers;

import net.dv8tion.jda.api.entities.MessageChannel;

public class TesterHandler implements Handler{
    private MessageChannel channel;

    public TesterHandler(MessageChannel mc){
        channel = mc;
    }

    @Override
    public void handle() {

    }
}
