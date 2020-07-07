package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.game.model.Item;
import com.milanesachan.DRPGTest1.game.model.items.weapons.Weapon_BetaTestersBlade;

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
        items.put("weapon:btb", new Weapon_BetaTestersBlade());
    }

    public Item getItemFromID(String itemID){
        return items.get(itemID);
    }
}