package com.milanesachan.DRPGTest1.bot.handlers.inventory;

import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Inventory;
import com.milanesachan.DRPGTest1.game.model.Item;
import com.milanesachan.DRPGTest1.game.model.UserItem;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class CreateItemHandler implements Handler {
    private MessageChannel channel;
    private User user;
    private String itemID;

    public CreateItemHandler(MessageChannel channel, User user, String itemID) {
        this.channel = channel;
        this.user = user;
        this.itemID = itemID;
    }

    @Override
    public void handle() {
        Inventory inv = new Inventory(user.getIdLong());
        try {
            inv.loadFromDatabase();
            Item i = ItemFactory.getInstance().getItemFromID(itemID);
            UserItem ui = new UserItem(i, user.getIdLong(), OffsetDateTime.now());
            inv.add(ui);
            inv.saveToDatabase();
        }catch(ItemNotFoundException ex){
            channel.sendMessage("**Error:** itemID '"+itemID+"' not found.").queue();
        } catch (SQLException e) {
            channel.sendMessage("**Error:** couldn't connect to database. Try again later.").queue();
            e.printStackTrace();
        }
    }
}
