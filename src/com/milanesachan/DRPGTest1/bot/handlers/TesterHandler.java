package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.image_generator.TestImageGenerator;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class TesterHandler implements Handler{
    private MessageChannel channel;
    private MessageReceivedEvent event;

    public TesterHandler(MessageReceivedEvent ev){
        channel = ev.getChannel();
        event = ev;
    }

    @Override
    public void handle() {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args.length>=3 && isInt(args[1]) && isInt(args[2])) {
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            Point p = new Point(x, y);
            TestImageGenerator.getInstance().postImage(channel, event.getMember().getUser().getIdLong(), p);
        }else{
            channel.sendMessage("Incorrect format. Expected: '>test <x> <y>'").queue();
        }
    }

    private boolean isInt(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
