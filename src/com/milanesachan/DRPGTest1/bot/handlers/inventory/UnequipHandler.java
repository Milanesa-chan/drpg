package com.milanesachan.DRPGTest1.bot.handlers.inventory;

import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Equipment;
import com.milanesachan.DRPGTest1.game.model.Inventory;
import com.milanesachan.DRPGTest1.game.model.UserItem;
import com.milanesachan.DRPGTest1.game.model.items.StatCard;
import com.milanesachan.DRPGTest1.game.model.items.Weapon;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class UnequipHandler implements Handler {
    private MessageChannel channel;
    private long userID;

    public UnequipHandler(MessageChannel channel, long userID) {
        this.channel = channel;
        this.userID = userID;
    }

    @Override
    public void handle() {
        Equipment equipment = new Equipment(userID);
        Inventory inv = new Inventory(userID);
        try {
            equipment.loadFromDatabase();
            inv.loadFromDatabase();

            Weapon removedWeapon = equipment.getWeapon();
            StatCard removedStatCard = equipment.getStatCard();

            if(removedWeapon == null && removedStatCard == null){
                channel.sendMessage("Your equipment is empty!").queue();
            }else{
                equipment.setWeapon(null);
                equipment.setStatCard(null);

                if(removedWeapon != null)
                    inv.add(new UserItem(removedWeapon, userID, OffsetDateTime.now()));
                if(removedStatCard != null)
                    inv.add(new UserItem(removedStatCard, userID, OffsetDateTime.now()));

                equipment.saveToDatabase();
                inv.saveToDatabase();
                channel.sendMessage("<@"+userID+"> your equipment has been returned to your inventory!").queue();
            }
            /*
            if(equipment.getWeapon() == null){
                channel.sendMessage("You have no weapon equipped.").queue();
            }else{
                Weapon unequipped = equipment.getWeapon();
                equipment.setWeapon(null);
                UserItem toInventory = new UserItem(unequipped, userID, OffsetDateTime.now());
                inv.add(toInventory);

                equipment.saveToDatabase();
                inv.saveToDatabase();
                channel.sendMessage("Item unequipped!").queue();
            }

             */
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (EquipmentNotFoundException e) {
            channel.sendMessage("No equipment found for your character!").queue();
        }
    }
}
