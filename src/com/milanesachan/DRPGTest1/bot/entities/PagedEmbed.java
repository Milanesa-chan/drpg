package com.milanesachan.DRPGTest1.bot.entities;

import com.milanesachan.DRPGTest1.bot.core.PageManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class PagedEmbed {
    private Message message;
    private MessageChannel channel;
    private ArrayList<MessageEmbed> pages = new ArrayList<>();
    private int currentPage;

    public PagedEmbed(MessageChannel mc){
        channel = mc;
    }

    public void send(){
        currentPage = 0;
        message = channel.sendMessage(pages.get(currentPage)).complete();
        if(pages.size()>1) {
            message.addReaction(PageManager.reactPrev).queue();
            message.addReaction(PageManager.reactNext).queue();
            PageManager.getInstance().addToValidList(this);
        }
    }

    private void editToCurrentPage(){
        message.editMessage(pages.get(currentPage)).queue(); //Does this create a new message? TODO //no! by milanesachan
    }

    public void nextPage(){
        if(currentPage == pages.size()-1)
            currentPage = 0;
        else
            currentPage++;
        editToCurrentPage();
    }

    public void prevPage(){
        if(currentPage == 0)
            currentPage = pages.size()-1;
        else
            currentPage--;
        editToCurrentPage();
    }

    public OffsetDateTime getMessageTime(){
        return message.getTimeCreated();
    }

    public void setPages(ArrayList<MessageEmbed> pages){
        this.pages = new ArrayList<>(pages);
    }

    public void addPage(MessageEmbed mes){
        pages.add(mes);
    }

    public long getMessageID(){
        return message.getIdLong();
    }
}
