package com.milanesachan.DRPGTest1.bot.handlers.inventory;

import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.game.model.*;
import com.milanesachan.DRPGTest1.game.model.items.Weapon;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class EquipHandler implements Handler {
    private MessageChannel channel;
    private long userID;
    private String itemID;

    public EquipHandler(MessageChannel channel, long userID, String itemID) {
        this.channel = channel;
        this.userID = userID;
        this.itemID = itemID;
    }

    //Deberian los items transferirse con el equipamiento? TODO
    @Override
    public void handle() {
        Equipment equipment = new Equipment(userID);
        try {
            equipment.loadFromDatabase();
            Inventory inv = new Inventory(userID);
            inv.loadFromDatabase();

            Item unequipedItem = equipment.getWeapon();
            if(unequipedItem != null) {
                UserItem unequiped = new UserItem(unequipedItem, userID, OffsetDateTime.now());
                inv.add(unequiped);
                inv.saveToDatabase();
            }

            try {
                Weapon equippedItem = (Weapon) ItemFactory.getInstance().getItemFromID(itemID);
                UserItem equipped = new UserItem(equippedItem, userID, OffsetDateTime.now());
                if(inv.removeUserItem(equipped)) {
                    equipment.setWeapon(equippedItem);
                    equipment.saveToDatabase();
                    inv.saveToDatabase();
                    channel.sendMessage("Item equipped!").queue();
                }else{
                    channel.sendMessage("**Error:** You do not have the specified item in your inventory.").queue();
                }
            }catch(ClassCastException ex){
                channel.sendMessage("ItemID: '"+itemID+"' is not a weapon!").queue();
            }
        } catch (SQLException | ItemNotFoundException throwables) {
            throwables.printStackTrace();
        } catch (EquipmentNotFoundException e) {
            try{
                Weapon weapon = (Weapon) ItemFactory.getInstance().getItemFromID(itemID);

                UserItem weaponInInventory = new UserItem(weapon, userID, OffsetDateTime.now());
                Inventory inv = new Inventory(userID);
                inv.loadFromDatabase();

                if(inv.removeUserItem(weaponInInventory)) {
                    equipment.setWeapon(weapon);
                    equipment.saveToDatabase();
                    channel.sendMessage("Item equipped!").queue();
                    inv.saveToDatabase();
                }else{
                    channel.sendMessage("**Error:** You do not have the specified item in your inventory.").queue();
                }
            } catch (ItemNotFoundException itemNotFoundException) {
                channel.sendMessage("'"+itemID+"' is not a valid itemID!").queue();
            }catch(ClassCastException ex){
                channel.sendMessage("ItemID: '"+itemID+"' is not a weapon!").queue();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                channel.sendMessage("Failed to connect to database. Try again later.").queue();
            }
        }
    }
}
