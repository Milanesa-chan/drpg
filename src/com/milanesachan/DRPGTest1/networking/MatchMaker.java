package com.milanesachan.DRPGTest1.networking;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import sun.plugin2.message.Message;

import java.lang.reflect.Array;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;

public class MatchMaker extends Thread{
    private static MatchMaker instance;
    public static final int[] teamSizes = {1, 6};
    private final HashMap<Integer, ArrayList<GuildParty>> queuesList;
    private boolean matchMakerRunning;
    private final int searchInterval = 5;

    private MatchMaker(){
        queuesList = new HashMap<>();
        matchMakerRunning = true;
        this.start();
    }

    public static MatchMaker getInstance(){
        if(instance == null)
            instance = new MatchMaker();
        return instance;
    }

    public int addToQueuesList(GuildParty party){
        int teamSize = party.getCharList().size();

        synchronized (queuesList) {
            if (!queuesList.containsKey(teamSize)) {
                queuesList.put(teamSize, new ArrayList<>());
            }
            queuesList.get(teamSize).add(party);
            return queuesList.get(teamSize).size();
        }
    }

    public void removeFromQueue(GuildParty party){
        int teamSize = party.getCharList().size();

        synchronized (queuesList){
            queuesList.get(teamSize).remove(party);
        }
    }

    @Override
    public void run() {
        while(matchMakerRunning){
            try{
                Thread.sleep(searchInterval*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (queuesList){
                for(ArrayList<GuildParty> list : queuesList.values()){
                    if(list.size()>1){
                        matchParties(list.remove(0), list.remove(0));
                    }
                }
            }
        }
    }

    private void matchParties(GuildParty partyA, GuildParty partyB){
        partyA.setInQueue(false);
        partyA.setInBattle(true);
        partyB.setInQueue(false);
        partyB.setInBattle(true);

        MessageChannel chanA = partyA.getBattleChannel();
        MessageChannel chanB = partyB.getBattleChannel();
        MessageEmbed embA = partyA.getEmbed().build();
        MessageEmbed embB = partyB.getEmbed().build();

        chanA.sendMessage("**Match found! Your opponent is:**").queue();
        chanA.sendMessage(embB).queue();

        chanB.sendMessage("**Match found! Your opponent is:**").queue();
        chanB.sendMessage(embA).queue();
    }
}
