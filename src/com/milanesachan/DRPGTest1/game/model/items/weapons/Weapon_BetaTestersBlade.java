package com.milanesachan.DRPGTest1.game.model.items.weapons;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
import com.milanesachan.DRPGTest1.game.battle.BattleController;
import com.milanesachan.DRPGTest1.game.model.items.Weapon;

import java.util.Random;

public class Weapon_BetaTestersBlade extends Weapon {

    public Weapon_BetaTestersBlade() {
        super("weapon:btb", "Beta Tester's Blade", 30, 20, 5);
        setItemDesc("For the early adopters.");
        setIconUrl("https://cdn.discordapp.com/attachments/723244808147304519/723697312190300282/weapon_betatestersblade.png");
        setPowerDescription("Deal 20 to 30 damage to a random enemy.");
    }


    @Override
    public void useActive(GuildParty allies, GuildParty enemies, BattleController controller) {
        Random r = new Random();
        BattleCharacter receiver = enemies.getRandomAliveChar();

        int dmg = 20 + r.nextInt(11);
        controller.dealDamage(receiver, dmg);
    }
}
