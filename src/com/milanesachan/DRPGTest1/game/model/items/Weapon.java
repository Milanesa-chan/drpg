package com.milanesachan.DRPGTest1.game.model.items;

import com.milanesachan.DRPGTest1.game.model.Equipable;
import com.milanesachan.DRPGTest1.game.model.Item;
import net.dv8tion.jda.api.EmbedBuilder;

public abstract class Weapon extends Item implements Equipable {
    private double damage;

    public Weapon(String itemID, String itemName) {
        super(itemID, itemName);
    }

    @Override
    public EmbedBuilder getEmbed() {
        EmbedBuilder emb = super.getEmbed();
        String stats = "Damage: "+damage;
        emb.addField("Stats", stats, false);
        return emb;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
}
