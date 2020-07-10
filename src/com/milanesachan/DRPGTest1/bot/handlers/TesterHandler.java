package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.image_generator.TestImageGenerator;
import net.dv8tion.jda.api.entities.MessageChannel;

public class TesterHandler implements Handler{
    private MessageChannel channel;

    public TesterHandler(MessageChannel mc){
        channel = mc;
    }

    @Override
    public void handle() {
        TestImageGenerator.getInstance().postImage(channel);
    }
}
