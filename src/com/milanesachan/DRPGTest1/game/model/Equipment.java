package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.game.model.items.StatCard;
import com.milanesachan.DRPGTest1.game.model.items.Weapon;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Equipment implements Embeddable{
    private long userID;
    private Weapon weapon;
    private StatCard statCard;

    public Equipment(long userID){
        this.userID = userID;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public StatCard getStatCard() {
        return statCard;
    }

    public void setStatCard(StatCard statCard) {
        this.statCard = statCard;
    }

    public void loadFromDatabase() throws SQLException, EquipmentNotFoundException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `equipment` WHERE `UID`="+userID);
            if(rs.next()){
                String weaponID = rs.getString("WeaponID");
                String statCardID = rs.getString("StatCardID");
                try {
                    this.weapon = (Weapon) ItemFactory.getInstance().getItemFromID(weaponID);
                } catch (ItemNotFoundException e) {
                    //e.printStackTrace();
                }
                try {
                    this.statCard = (StatCard) ItemFactory.getInstance().getItemFromID(statCardID);
                } catch (ItemNotFoundException e) {
                    //e.printStackTrace();
                }
            }else{
                throw new EquipmentNotFoundException(userID);
            }
            con.close();
        }
    }

    public void saveToDatabase() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null){
            Statement stmt = con.createStatement();
            String weaponID = weapon != null ? weapon.getItemID() : "";
            String statCardID = statCard != null ? statCard.getItemID() : "";
            stmt.executeQuery("SELECT * FROM `equipment` WHERE `UID`="+userID);
            if(!stmt.getResultSet().next()){
                stmt.execute("INSERT INTO `equipment` (UID, WeaponID, StatCardID) VALUES ("+userID+", '"+weaponID+"', '"+statCardID+"');");
            }else{
                stmt.execute("UPDATE `equipment` SET `WeaponID`='"+weaponID+"', `StatCardID`='"+statCardID+"' WHERE `UID`="+userID);
            }
            con.close();
        }
    }

    public boolean deleteInDatabase() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con !=null){
            Statement stmt = con.createStatement();
            stmt.execute("DELETE FROM `equipment` WHERE `UID`="+userID);
            System.out.println(stmt.getUpdateCount()+" rows deleted from equipment '"+userID+"'.");
            return stmt.getUpdateCount()>0;
        }else return false;
    }

    @Override
    public EmbedBuilder getEmbed() {
        JDA jda = DRPGBot.getInstance().getJda();
        String userName = jda.getUserById(userID).getName();
        String userAvatar = jda.getUserById(userID).getAvatarUrl();
        Character cha = new Character(userID);
        try {
            cha.loadFromDatabase();
        } catch (SQLException | CharacterNotFoundException throwables) { throwables.printStackTrace(); }
        String charName = cha.getName();

        EmbedBuilder emb = new EmbedBuilder();
        emb.setColor(0x450000);
        emb.setTitle(charName+"'s Equipment");
        emb.setDescription("User: "+userName);
        emb.addField("Weapon", weapon != null ? weapon.getItemName() : "*No weapon equipped*", false);
        emb.addField("Stat Card", statCard != null ? statCard.toString() : "*No stat card equipped*", false);
        emb.setThumbnail(userAvatar);
        return emb;
    }
}
