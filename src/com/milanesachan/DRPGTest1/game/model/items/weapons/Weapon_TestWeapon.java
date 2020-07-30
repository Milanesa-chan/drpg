package com.milanesachan.DRPGTest1.game.model.items.weapons;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.game.battle.BattleController;
import com.milanesachan.DRPGTest1.game.model.items.Weapon;

public class Weapon_TestWeapon extends Weapon {

    public Weapon_TestWeapon() {
        super("weapon:test", "Test Weapon");
    }

    @Override
    public void useActive(GuildParty allies, GuildParty enemies, BattleController controller) {

    }
}
