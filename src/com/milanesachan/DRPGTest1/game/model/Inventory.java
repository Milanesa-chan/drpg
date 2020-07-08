package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Inventory extends ArrayList<UserItem> {
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
                    stmt.setString(4, ui.getDateObtained().format(DateTimeFormatter.ISO_DATE_TIME));
                }
            }
        }
    }

    public void loadFromDatabase() throws CharacterNotFoundException, SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM `inventory` WHERE `UID`="+userID+";");
            this.clear();
            while(result.next()){
                UserItem ui = new UserItem();
                ui.setUserID(userID);
                ui.setItem(ItemFactory.getInstance().getItemFromID(result.getString("ItemID")));
                ui.setQuantity(result.getInt("Quantity"));
                OffsetDateTime dateTime = result.getTimestamp("DateObtained").toInstant().atOffset(ZoneOffset.ofHours(-3));
                ui.setDateObtained(dateTime);
                this.add(ui);
            }
        }
    }

    public void sortByDate(){
        
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
