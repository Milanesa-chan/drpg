package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.bot.core.CharacterFactory;
import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.bot.core.PagedEmbedBuilder;
import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;

public class Inventory extends ArrayList<UserItem>{
    private long userID;

    public Inventory(long uid){
        super();
        userID = uid;
    }

    public void saveToDatabase() throws SQLException {
        if(!super.isEmpty()){
            Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
            if(con != null) {
                for (UserItem ui : this) {
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO `inventory` (UID, ItemID, Quantity, DateObtained) VALUES (?, ?, ?, ?)" +
                            "ON DUPLICATE KEY UPDATE Quantity = Quantity + 1;");
                    stmt.setLong(1, userID);
                    stmt.setString(2, ui.getItem().getItemID());
                    stmt.setInt(3, 1);
                    Timestamp timestamp = Timestamp.from(Instant.from(ui.getDateObtained()));
                    stmt.setTimestamp(4, timestamp);

                    stmt.execute();
                }
            }
        }
    }

    public void loadFromDatabase() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM `inventory` WHERE `UID`="+userID+";");
            this.clear();
            while(result.next()){
                UserItem ui = new UserItem();
                ui.setUserID(userID);
                try {
                    ui.setItem(ItemFactory.getInstance().getItemFromID(result.getString("ItemID")));
                }catch(ItemNotFoundException ex){
                    System.err.println("There was an error getting itemID: "+result.getString("ItemID")+" from database.");
                }
                ui.setQuantity(result.getInt("Quantity"));
                OffsetDateTime dateTime = result.getTimestamp("DateObtained").toInstant().atOffset(ZoneOffset.ofHours(-3));
                ui.setDateObtained(dateTime);
                this.add(ui);
            }
        }
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
        Character cha = new CharacterFactory().characterFromUserID(userID);

        builder.setTitle(cha.getName()+"'s Inventory");
        builder.setDescription("User: "+user.getName());
        builder.setLinesPerPage(5);
        builder.setThumbnailUrl(user.getAvatarUrl());

        ArrayList<String> data = new ArrayList<>();
        for(UserItem ui : this){
            String itemName = ui.getItem().getItemName();
            int quantity = ui.getQuantity();
            data.add(itemName+" (x"+quantity+")");
        }
        builder.setData(data);
        return builder.fromStringArray();
    }
}
