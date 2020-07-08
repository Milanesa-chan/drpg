package com.milanesachan.DRPGTest1.game.model;

import net.dv8tion.jda.api.EmbedBuilder;

public abstract class Item implements Embeddable{
    private String itemID;
    private String itemName;
    private String iconUrl = "https://cdn.discordapp.com/attachments/723244808147304519/730217799552729208/default_item.png";
    private String itemDesc;

    public Item(String itemID, String itemName) {
        this.itemID = itemID;
        this.itemName = itemName;
    }

    public EmbedBuilder getEmbed(){
        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle(itemName);
        emb.setFooter("itemID: "+itemID);
        emb.setDescription(itemDesc);
        emb.setThumbnail(iconUrl);
        emb.setColor(0x450000);
        return emb;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
}
