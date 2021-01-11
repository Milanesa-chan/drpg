package com.milanesachan.DRPGTest1.commons.exceptions;

import com.milanesachan.DRPGTest1.game.model.Character;

import java.sql.SQLException;

public class EquipmentNotFoundException extends Exception{
    private Character character;

    public EquipmentNotFoundException(Character c){
        character = c;
    }

    public EquipmentNotFoundException(long userID){
        character = new Character(userID);
        try {
            character.loadFromDatabase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (CharacterNotFoundException e) {
            e.printStackTrace();
            character = null;
        }
    }

    public Character getCharacter() {
        return character;
    }
}
