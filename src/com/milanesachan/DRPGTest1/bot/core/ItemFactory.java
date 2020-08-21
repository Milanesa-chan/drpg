package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Item;
import com.milanesachan.DRPGTest1.game.model.items.StatCard;
import com.milanesachan.DRPGTest1.game.model.items.test.Test_ItemA;
import com.milanesachan.DRPGTest1.game.model.items.test.Test_ItemB;
import com.milanesachan.DRPGTest1.game.model.items.test.Test_ItemC;
import com.milanesachan.DRPGTest1.game.model.items.weapons.Weapon_BetaTestersBlade;
import com.milanesachan.DRPGTest1.game.model.items.weapons.Weapon_TestWeapon;

import java.util.HashMap;

public class ItemFactory {
    private static ItemFactory instance;
    HashMap<String, Item> items;

    private ItemFactory(){
        items = new HashMap<>();
        registerItems();
    }

    public static ItemFactory getInstance(){
        if(instance==null){
            instance = new ItemFactory();
        }
        return instance;
    }

    private void registerItems(){
        registerTestItems();
        registerWeaponItems();
    }

    private void registerWeaponItems(){
        items.put("weapon:btb", new Weapon_BetaTestersBlade());
        items.put("weapon:test", new Weapon_TestWeapon());
    }

    private void registerTestItems(){
        items.put("test:itema", new Test_ItemA());
        items.put("test:itemb", new Test_ItemB());
        items.put("test:itemc", new Test_ItemC());
    }

    public Item getItemFromID(String itemID) throws ItemNotFoundException {
        if(itemID.startsWith("statcard:")){
            return new StatCard(itemID);
        }else {
            if (!items.containsKey(itemID)) {
                throw new ItemNotFoundException(itemID);
            }
            return items.get(itemID);
        }
    }
}
