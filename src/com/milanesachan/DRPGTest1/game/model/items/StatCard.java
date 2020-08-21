package com.milanesachan.DRPGTest1.game.model.items;

import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Item;

public class StatCard extends Item {
    private char type;
    public static final char TYPE_RED = 'r', TYPE_GREEN = 'g', TYPE_BLUE = 'b';
    private int strength;
    private int accuracy;
    private int vitality;
    private int magic;

    public StatCard(String itemID, String itemName) throws ItemNotFoundException {
        super(itemID, "Stat Card");
        decodeItemID(itemID);
    }

    private void decodeItemID(String itemID) throws ItemNotFoundException {
        String[] idParts = itemID.split(":");
        if (idParts.length != 6) throw new ItemNotFoundException(itemID);

        try {
            char type = idParts[1].charAt(0);
            switch (type) {
                case 'r':
                case 'g':
                case 'b':
                    this.type = type;
                    break;
                default:
                    throw new ItemNotFoundException(itemID);
            }

            for(int stat=0; stat<4; stat++){
                int statValue = Integer.parseInt(idParts[2+stat]);
                switch(stat){
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
        }catch (IndexOutOfBoundsException | NumberFormatException ex){
            throw new ItemNotFoundException(itemID);
        }
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
