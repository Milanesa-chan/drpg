package com.milanesachan.DRPGTest1.networking;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MatchMaker {
    private static MatchMaker instance;
    public static final int[] teamSizes = {1, 6};
    private HashMap<Integer, ArrayList<GuildParty>> queuesList;

    private MatchMaker(){
        queuesList = new HashMap<>();
    }

    public static MatchMaker getInstance(){
        if(instance == null)
            instance = new MatchMaker();
        return instance;
    }

    public int addToQueuesList(GuildParty party){
        int teamSize = party.getCharList().size();

        if (!queuesList.containsKey(teamSize)) {
            queuesList.put(teamSize, new ArrayList<>());
        }
        queuesList.get(teamSize).add(party);
        return queuesList.get(teamSize).size();
    }
}
