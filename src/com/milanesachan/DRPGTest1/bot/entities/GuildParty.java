package com.milanesachan.DRPGTest1.bot.entities;

import com.milanesachan.DRPGTest1.bot.handlers.guild.GuildDeletionHandler;
import com.milanesachan.DRPGTest1.commons.exceptions.AlreadyInPartyException;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;

import java.util.ArrayList;

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
}
