package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.bot.core.PagedEmbedBuilder;
import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class Inventory extends ArrayList<UserItem> {
    private long userID;

    public Inventory(long uid) {
        super();
        userID = uid;
    }

    public void saveToDatabase() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if (con != null) {
            Statement stmt = con.createStatement();
            stmt.execute("DELETE FROM `inventory` WHERE `UID`=" + userID);
            for (UserItem ui : this) {
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO `inventory` (UID, ItemID, Quantity, DateObtained) VALUES (?, ?, ?, ?);");
                pstmt.setLong(1, userID);
                pstmt.setString(2, ui.getItem().getItemID());
                pstmt.setInt(3, ui.getQuantity());
                Timestamp timestamp = Timestamp.from(Instant.from(ui.getDateObtained()));
                pstmt.setTimestamp(4, timestamp);
                pstmt.execute();
            }
        }
    }

    public Inventory loadFromDatabase() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if (con != null) {
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM `inventory` WHERE `UID`=" + userID + ";");
            this.clear();
            while (result.next()) {
                UserItem ui = new UserItem();
                ui.setUserID(userID);
                try {
                    ui.setItem(ItemFactory.getInstance().getItemFromID(result.getString("ItemID")));
                } catch (ItemNotFoundException ex) {
                    System.err.println("There was an error getting itemID: " + result.getString("ItemID") + " from database.");
                }
                ui.setQuantity(result.getInt("Quantity"));
                OffsetDateTime dateTime = result.getTimestamp("DateObtained").toInstant().atOffset(ZoneOffset.ofHours(-3));
                ui.setDateObtained(dateTime);
                this.add(ui);
            }
        }
        return this;
    }


    public boolean deleteInDatabase() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if (con != null) {
            Statement stmt = con.createStatement();
            return stmt.execute("DELETE FROM `inventory` WHERE `UID`=" + userID);
        } else return false;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public PagedEmbed getPagedEmbed(MessageChannel channel) throws SQLException, CharacterNotFoundException {
        PagedEmbedBuilder builder = new PagedEmbedBuilder(channel);
        User user = DRPGBot.getInstance().getJda().getUserById(userID);
        Character cha = new Character(userID);
        cha.loadFromDatabase();

        builder.setTitle(cha.getName() + "'s Inventory");
        builder.setDescription("User: " + user.getName());
        builder.setLinesPerPage(5);
        builder.setThumbnailUrl(user.getAvatarUrl());

        ArrayList<String> data = new ArrayList<>();
        for (UserItem ui : this) {
            String itemName = ui.getItem().toString();
            int quantity = ui.getQuantity();
            String itemID = ui.getItem().getItemID();
            //data.add(itemName + " (x" + quantity + ") _ID:" + itemID + "_");
            data.add(itemName + " (x" + quantity + ")");
        }
        builder.setData(data);
        return builder.fromStringArray();
    }


    @Override
    public boolean add(UserItem userItem) {
        if (this.contains(userItem)) {
            int indexOfItem = this.indexOf(userItem);
            this.get(indexOfItem).addOne();
            return true;
        } else {
            return super.add(userItem);
        }
    }

    public boolean removeOne(Item item) {
        UserItem userItem = new UserItem(item, userID, OffsetDateTime.now());
        if (!contains(userItem)) {
            return false;
        } else {
            userItem = get(indexOf(userItem));
            int finalCount = userItem.removeOne();
            if (finalCount > 0) {
                return true;
            } else {
                return remove(userItem);
            }
        }
    }

    public boolean add(Item item) {
        UserItem userItem = new UserItem(item, userID, OffsetDateTime.now());
        return add(userItem);
    }

    public boolean removeUserItem(UserItem userItem) {
        if (this.contains(userItem)) {
            int index = this.indexOf(userItem);
            int finalQ = this.get(index).removeOne();
            if (finalQ <= 0) {
                return this.remove(userItem);
            } else {
                return true;
            }
        } else return false;
    }


}
