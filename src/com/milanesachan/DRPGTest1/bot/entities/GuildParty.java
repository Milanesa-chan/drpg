package com.milanesachan.DRPGTest1.bot.entities;

import com.milanesachan.DRPGTest1.bot.handlers.guild.GuildDeletionHandler;
import com.milanesachan.DRPGTest1.commons.exceptions.AlreadyInPartyException;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
import com.milanesachan.DRPGTest1.game.model.Guild;

import java.util.ArrayList;
import java.util.Random;

public class GuildParty {
    private long guildID;
    private ArrayList<BattleCharacter> charList;

    public GuildParty(long guildID){
        this.guildID = guildID;
        charList = new ArrayList<>();
    }

    public long getGuildID() {
        return guildID;
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public void add(BattleCharacter character) throws AlreadyInPartyException {
        if(charList.contains(character))
            throw new AlreadyInPartyException(character, this);
        else charList.add(character);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GuildParty)) return false;
        if(obj == this) return true;
        return ((GuildParty) obj).guildID == this.guildID;
    }

    public void addChar(BattleCharacter character){
        charList.add(character);
    }

    public void removeChar(BattleCharacter character){
        charList.remove(character);
    }

    public BattleCharacter getRandomChar(){
        int i = new Random().nextInt(charList.size());
        return charList.get(i);
    }

    public ArrayList<BattleCharacter> getCharList() {
        return charList;
    }

    public void setCharList(ArrayList<BattleCharacter> charList) {
        this.charList = charList;
    }
}
