package com.milanesachan.DRPGTest1.game.model.items;

import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Item;
import net.dv8tion.jda.api.EmbedBuilder;

public class StatCard extends Item {
    private char type;
    public static final char TYPE_RED = 'r', TYPE_GREEN = 'g', TYPE_BLUE = 'b';
    private int strength;
    private int accuracy;
    private int vitality;
    private int magic;

    public StatCard(String itemID) throws ItemNotFoundException {
        super(itemID, "Stat Card");
        decodeItemID(itemID);
    }

    private void decodeItemID(String itemID) throws ItemNotFoundException {
        String[] idParts = itemID.split(":");
        if (idParts.length != 6) throw new ItemNotFoundException(itemID);

        try {
            char type = idParts[1].charAt(0);
            if (type == 'r' || type == 'g' || type == 'b') {
                this.type = type;
                switch (type) {
                    case 'r':
                        setItemName("Red Stat Card");
                        setIconUrl("https://cdn.discordapp.com/attachments/723244808147304519/746242578982109269/statcard_red.png");
                        break;
                    case 'g':
                        setItemName("Green Stat Card");
                        setIconUrl("https://cdn.discordapp.com/attachments/723244808147304519/746242586142048296/statcard_green.png");
                        break;
                    case 'b':
                        setItemName("Blue Stat Card");
                        setIconUrl("https://cdn.discordapp.com/attachments/723244808147304519/746242582547529849/statcard_blue.png");
                        break;
                }
            } else throw new ItemNotFoundException(itemID);

            for (int stat = 0; stat < 4; stat++) {
                int statValue = Integer.parseInt(idParts[2 + stat]);
                switch (stat) {
                    case 0:
                        this.strength = statValue;
                        break;
                    case 1:
                        this.accuracy = statValue;
                        break;
                    case 2:
                        this.vitality = statValue;
                        break;
                    case 3:
                        this.magic = statValue;
                        break;
                }
            }
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            throw new ItemNotFoundException(itemID);
        }
    }

    @Override
    public EmbedBuilder getEmbed() {
        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle(toString());
        emb.setFooter("itemID: " + getItemID());
        emb.setThumbnail(getIconUrl());
        emb.setColor(0x450000);

        emb.addField("Strength: ", String.valueOf(strength), false);
        emb.addField("Accuracy: ", String.valueOf(accuracy), false);
        emb.addField("Vitality: ", String.valueOf(vitality), false);
        emb.addField("Magic: ", String.valueOf(magic), false);
        return emb;
    }

    @Override
    public String toString() {
        return getItemName() + " (" + strength + "/" + accuracy + "/" + vitality + "/" + magic + ")";
    }

    public char getType() {
        return type;
    }

    public int getStrength() {
        return strength;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getVitality() {
        return vitality;
    }

    public int getMagic() {
        return magic;
    }
}
