package com.milanesachan.DRPGTest1.bot.handlers.inventory;

import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.game.model.*;
import com.milanesachan.DRPGTest1.game.model.items.StatCard;
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

    @Override
    public void handle() {
        try {
            if (!itemID.startsWith("weapon:") && !itemID.startsWith("statcard:")) {
                channel.sendMessage("That item is not equippable!").queue();
            } else {
                Item equippedItem = ItemFactory.getInstance().getItemFromID(itemID);
                Inventory userInv = new Inventory(userID).loadFromDatabase();

                if(!userInv.contains(new UserItem(equippedItem, userID, OffsetDateTime.now()))){
                    channel.sendMessage("You don't have the item in your inventory!").queue();
                }else{
                    Equipment userEquip = new Equipment(userID);
                    try {
                        userEquip = new Equipment(userID).loadFromDatabase();
                        Item unequippedItem = itemID.startsWith("weapon:") ? userEquip.getWeapon() : userEquip.getStatCard();
                        if(unequippedItem != null)
                            userInv.add(unequippedItem);
                    } catch (EquipmentNotFoundException ignored) {}

                    if(itemID.startsWith("weapon:"))
                        userEquip.setWeapon((Weapon) equippedItem);
                    else
                        userEquip.setStatCard((StatCard) equippedItem);

                    userInv.removeOne(equippedItem);

                    userInv.saveToDatabase();
                    userEquip.saveToDatabase();

                    channel.sendMessage("Item equipped!").queue();
                }
            }
        }catch(SQLException e){
            channel.sendMessage("Error connecting to the database. Try again later.").queue();
        } catch (ItemNotFoundException e) {
            channel.sendMessage("Unrecognized itemID: '"+e.getMessage()+"'.").queue();
        }
    }
}
