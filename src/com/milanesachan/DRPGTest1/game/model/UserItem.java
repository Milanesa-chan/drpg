package com.milanesachan.DRPGTest1.game.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public class UserItem {
    private Item item;
    private long userID;
    private OffsetDateTime dateObtained;
    private int quantity;

    public UserItem() {}

    public UserItem(Item item, long userID, OffsetDateTime dateObtained) {
        super();
        this.item = item;
        this.userID = userID;
        this.dateObtained = dateObtained;
        quantity = 1;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addOne(){
        this.quantity++;
    }

    public int removeOne(){
        this.quantity--;
        return quantity;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public OffsetDateTime getDateObtained() {
        return dateObtained;
    }

    public void setDateObtained(OffsetDateTime dateObtained) {
        this.dateObtained = dateObtained;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserItem userItem = (UserItem) o;
        return this.item.getItemID().equals(userItem.getItem().getItemID())
                && this.userID==userItem.userID;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
