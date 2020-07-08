package com.milanesachan.DRPGTest1.game.model;

import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;

import java.util.ArrayList;

public class Inventory extends ArrayList<UserItem> {
    private long userID;

    public Inventory(long uid){
        super();
        userID = uid;
    }

    public void saveToDatabase(){

    }

    public void loadFromDatabase() throws CharacterNotFoundException {
        
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
