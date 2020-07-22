package com.milanesachan.DRPGTest1.game.battle;

import com.milanesachan.DRPGTest1.game.model.Character;
import com.milanesachan.DRPGTest1.game.model.items.Weapon;

public class BattleCharacter {
    private Character character;
    private Weapon weapon;

    public BattleCharacter(Character character) {
        this.character = character;
        //weapon = character.getEquipment().getWeapon();
    }
}
