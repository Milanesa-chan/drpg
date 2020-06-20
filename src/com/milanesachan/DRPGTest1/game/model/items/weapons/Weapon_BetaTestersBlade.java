package com.milanesachan.DRPGTest1.game.model.items.weapons;

import com.milanesachan.DRPGTest1.game.model.items.Weapon;

public class Weapon_BetaTestersBlade extends Weapon {

    public Weapon_BetaTestersBlade() {
        super("weapon:btb", "Beta Tester's Blade");
        setItemDesc("For the early adopters.");
        setIconUrl("https://cdn.discordapp.com/attachments/723244808147304519/723697312190300282/weapon_betatestersblade.png");
        setDamage(10000);
    }
}
