package com.milanesachan.DRPGTest1.game.model.items;

import com.milanesachan.DRPGTest1.commons.parameters.BattleParameters;
import com.milanesachan.DRPGTest1.game.model.Equipable;
import com.milanesachan.DRPGTest1.game.model.Item;
import net.dv8tion.jda.api.EmbedBuilder;

public abstract class Weapon extends Item implements Equipable {
    private int damage = 0;
    private int maxEnergy = 0;
    private int energyInc = 0;
    private String powerDescription = "No power description";

    public Weapon(String itemID, String itemName) {
        super(itemID, itemName);
    }

    public Weapon(String itemID, String itemName, int damage, int maxEnergy, int energyInc) {
        super(itemID, itemName);
        this.damage = damage;
        this.maxEnergy = maxEnergy;
        this.energyInc = energyInc;
    }

    @Override
    public EmbedBuilder getEmbed() {
        int minDamage = (int) (damage * (1 - BattleParameters.damageRadiusPercent));
        int maxDamage = (int) (damage * (1 + BattleParameters.damageRadiusPercent));
        int minEngInc = (int) (energyInc * (1 - BattleParameters.energyRadiusPercent));
        int maxEngInc = (int) (energyInc * (1 + BattleParameters.energyRadiusPercent));
        EmbedBuilder emb = super.getEmbed();

        String stats = "";
        stats = stats.concat("Damage: " + minDamage + " to " + maxDamage + "\n");
        stats = stats.concat("Max Energy: " + maxEnergy + "\n");
        stats = stats.concat("Energy Increment: " + minEngInc + " to " + maxEngInc);
        emb.addField("Stats", stats, false);

        emb.addField("Power", powerDescription, false);

        return emb;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public int getEnergyInc() {
        return energyInc;
    }

    public void setEnergyInc(int energyInc) {
        this.energyInc = energyInc;
    }

    public String getPowerDescription() {
        return powerDescription;
    }

    public void setPowerDescription(String powerDescription) {
        this.powerDescription = powerDescription;
    }
}
