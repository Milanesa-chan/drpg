package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.ArrayList;

public class PagedEmbedBuilder {
    private ArrayList<String> data;
    private int linesPerPage = 5;
    private String title = "Default title", description = "Default description";
    private MessageChannel channel;
    private String thumbnailUrl = "https://cdn.discordapp.com/attachments/723244808147304519/730217799552729208/default_item.png";
    private int color = 0x450000;

    public PagedEmbedBuilder(MessageChannel mc){
        channel = mc;
    }

    public PagedEmbed fromStringArray(){
        PagedEmbed pe = new PagedEmbed(channel);
        ArrayList<String> tempData = new ArrayList<>(data);

        String currentString = "";
        int linesThisPage = 0;
        while(!tempData.isEmpty()){
            currentString = currentString.concat(tempData.remove(0));
            linesThisPage++;
            if(linesThisPage<linesPerPage){
                currentString = currentString.concat("\n");
            }else{
                pe.addPage(getBlankEmbedBuilder().addField("", currentString, false).build());
                linesThisPage = 0;
                currentString = "";
            }
            if(tempData.isEmpty()){
                pe.addPage(getBlankEmbedBuilder().addField("", currentString, false).build());
            }
        }
        return pe;
    }



    private EmbedBuilder getBlankEmbedBuilder(){
        EmbedBuilder blank = new EmbedBuilder();
        blank.setTitle(title);
        blank.setDescription(description);
        blank.setColor(color);
        blank.setThumbnail(thumbnailUrl);
        return blank;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public int getLinesPerPage() {
        return linesPerPage;
    }

    public void setLinesPerPage(int linesPerPage) {
        this.linesPerPage = linesPerPage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
