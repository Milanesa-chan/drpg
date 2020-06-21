package com.milanesachan.DRPGTest1.bot.entities;

import net.dv8tion.jda.api.entities.Message;

public class PagedEmbed {
    private Message message;

    public long getMessageID(){
        return message.getIdLong();
    }
}
