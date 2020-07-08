package com.milanesachan.DRPGTest1.bot.handlers.inventory;

import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import sun.plugin2.message.Message;

public class CreateItemHandler implements Handler {
    private MessageChannel channel;
    private User user;
    private long itemID;

    public CreateItemHandler(MessageChannel channel, User user, long itemID) {
        this.channel = channel;
        this.user = user;
        this.itemID = itemID;
    }

    @Override
    public void handle() {
        
    }
}
