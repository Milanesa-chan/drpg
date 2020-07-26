package com.milanesachan.DRPGTest1.game.battle;

import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;

public class BattleController extends Thread{
    private static int battleInterval = 5;
    private MessageChannel aChannel;
    private MessageChannel bChannel;
    private GuildParty aParty;
    private GuildParty bParty;

    private boolean battleRunning;
    private ArrayList<BattleCharacter> alreadyFought;

    public BattleController(GuildParty aParty, GuildParty bParty) {
        this.aParty = aParty;
        this.bParty = bParty;
        this.aChannel = aParty.getBattleChannel();
        this.bChannel = bParty.getBattleChannel();
        alreadyFought = new ArrayList<>();
    }

    @Override
    public void run() {
        battleRunning = true;
        while(battleRunning){
            try {
                Thread.sleep(battleInterval*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doBattleStep();
        }
    }

    private void doBattleStep(){
        BattleCharacter aChar = aParty.getRandomAbleChar();
        checkIfWon();
    }

    private void checkIfWon(){
        if(bParty.isWholeGuildDead()){
            aChannel.sendMessage("You won!").queue();
            bChannel.sendMessage("You lost!").queue();
            battleRunning = false;
        }else if(aParty.isWholeGuildDead()){
            bChannel.sendMessage("You won!").queue();
            aChannel.sendMessage("You lost!").queue();
            battleRunning = false;
        }
    }
}
