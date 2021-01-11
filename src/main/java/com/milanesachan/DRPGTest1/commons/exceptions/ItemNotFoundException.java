package com.milanesachan.DRPGTest1.commons.exceptions;

public class ItemNotFoundException extends Throwable {
    private String message;

    public ItemNotFoundException(String itemID){
        message = itemID+" is not a recognized itemID.";
    }

    public String getMessage() {
        return message;
    }
}
