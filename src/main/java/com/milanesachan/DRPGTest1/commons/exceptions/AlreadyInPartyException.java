package com.milanesachan.DRPGTest1.commons.exceptions;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;

public class AlreadyInPartyException extends Exception{
    private BattleCharacter character;
    private GuildParty party;

    public AlreadyInPartyException(BattleCharacter character, GuildParty party) {
        this.character = character;
        this.party = party;
    }

    @Override
    public String getMessage() {
        return "User <@"+character.getUserID()+"> is already in the party!";
    }

    public BattleCharacter getCharacter() {
        return character;
    }

    public void setCharacter(BattleCharacter character) {
        this.character = character;
    }

    public GuildParty getParty() {
        return party;
    }

    public void setParty(GuildParty party) {
        this.party = party;
    }
}
