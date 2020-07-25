package com.milanesachan.DRPGTest1.game.battle;

import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Character;
import com.milanesachan.DRPGTest1.game.model.Equipment;
import com.milanesachan.DRPGTest1.game.model.items.Weapon;

import java.sql.SQLException;

public class BattleCharacter {
    private long userID;
    private Character character;
    private Equipment equipment;
    private Weapon weapon;
    private int HP, maxHP;

    public BattleCharacter(long userID) throws SQLException, CharacterNotFoundException, EquipmentNotFoundException {
        this.userID = userID;

        character = new Character(userID);
        character.loadFromDatabase();
        maxHP = character.getHP();
        HP = maxHP;

        /*
        equipment = new Equipment(userID);
        equipment.loadFromDatabase();
        weapon = equipment.getWeapon();
         */
    }

    public void getReady() throws EquipmentNotFoundException, SQLException {
        equipment = new Equipment(userID);
        equipment.loadFromDatabase();
        weapon = equipment.getWeapon();
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
