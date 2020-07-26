package com.milanesachan.DRPGTest1.bot.entities;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.core.GuildFactory;
import com.milanesachan.DRPGTest1.bot.handlers.guild.GuildDeletionHandler;
import com.milanesachan.DRPGTest1.commons.exceptions.AlreadyInPartyException;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
import com.milanesachan.DRPGTest1.game.model.Embeddable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class GuildParty implements Embeddable {
    private long guildID;
    private MessageChannel battleChannel;
    private ArrayList<BattleCharacter> charList;

    public GuildParty(long guildID) throws ServerNotFoundException, SQLException {
        this.guildID = guildID;
        charList = new ArrayList<>();
        long bChannelID = new GuildFactory().guildFromServerID(guildID).getBattleChannelID();
        battleChannel = DRPGBot.getInstance().getJda().getTextChannelById(bChannelID);
    }

    public long getGuildID() {
        return guildID;
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public void add(BattleCharacter character) throws AlreadyInPartyException {
        if (charList.contains(character))
            throw new AlreadyInPartyException(character, this);
        else charList.add(character);
    }

    public boolean isCharInParty(long userID) {
        for (BattleCharacter bc : charList) {
            if (bc.getUserID() == userID) return true;
        }
        return false;
    }

    public boolean getReady(int[] teamSizes) throws EquipmentNotFoundException, SQLException {
        if(!containsInt(teamSizes, charList.size())){
            return false;
        }else{
            for(BattleCharacter bc : charList){
                bc.getReady();
            }
            return true;
        }
    }

    private boolean containsInt(int[] array, int num){
        for(int i : array){
            if(i==num) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GuildParty)) return false;
        if (obj == this) return true;
        return ((GuildParty) obj).guildID == this.guildID;
    }

    public void addChar(BattleCharacter character) {
        charList.add(character);
    }

    public void removeChar(BattleCharacter character) {
        charList.remove(character);
    }

    public BattleCharacter getRandomChar() {
        int i = new Random().nextInt(charList.size());
        return charList.get(i);
    }

    public ArrayList<BattleCharacter> getCharList() {
        return charList;
    }

    public void setCharList(ArrayList<BattleCharacter> charList) {
        this.charList = charList;
    }

    @Override
    public EmbedBuilder getEmbed() {
        JDA jda = DRPGBot.getInstance().getJda();
        String serverName = jda.getGuildById(guildID).getName();
        String iconUrl = jda.getGuildById(guildID).getIconUrl();

        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle("Battle Party");
        emb.setDescription("Server: " + serverName);
        emb.setThumbnail(iconUrl);
        emb.setColor(0x450000);

        String membersList = "";
        User user;
        for(BattleCharacter bc : charList){
            user = jda.getUserById(bc.getUserID());
            membersList = membersList.concat(bc.getCharacter().getName()+" ("+user.getAsTag()+")");
            if(!(charList.indexOf(bc)==charList.size()-1))
                membersList = membersList.concat("\n");
        }

        emb.addField("Party Members", membersList, false);
        return emb;
    }

    public boolean isUserInParty(long userID){
        for(BattleCharacter bc : charList){
            if(bc.getUserID()==userID) return true;
        }
        return false;
    }

    public MessageChannel getBattleChannel() {
        return battleChannel;
    }

    public void setBattleChannel(MessageChannel battleChannel) {
        this.battleChannel = battleChannel;
    }
}
