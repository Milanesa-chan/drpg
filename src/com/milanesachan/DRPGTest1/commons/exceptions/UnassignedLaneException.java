package com.milanesachan.DRPGTest1.commons.exceptions;

import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;

public class UnassignedLaneException extends Exception{
    BattleCharacter character;

    public UnassignedLaneException(BattleCharacter character) {
        this.character = character;
    }

    public BattleCharacter getCharacter() {
        return character;
    }
}
