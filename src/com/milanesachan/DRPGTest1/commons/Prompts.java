package com.milanesachan.DRPGTest1.commons;

public class Prompts {

    public static String teamSizeError(int[] teamSizes){
        String message = "Incorrect party size! Available sizes are: ";
        for(int i=0; i<teamSizes.length; i++){
            message = message.concat(String.valueOf(teamSizes[i]));
            if(i!=teamSizes.length-1){
                message = message.concat(", ");
            }
        }
        message = message.concat(".");
        return message;
    }
}
