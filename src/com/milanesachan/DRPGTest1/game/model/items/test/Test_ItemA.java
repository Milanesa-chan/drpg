package com.milanesachan.DRPGTest1.game.model.items.test;

import com.milanesachan.DRPGTest1.game.model.items.Weapon;

public class Test_ItemA extends Weapon {

    public Test_ItemA() {
        super("test:itema", "Test Item A");
        setItemDesc("For the early adopters.");
        setIconUrl("https://cdn.discordapp.com/attachments/723244808147304519/723697312190300282/weapon_betatestersblade.png");
        setDamage(10000);
    }
}
