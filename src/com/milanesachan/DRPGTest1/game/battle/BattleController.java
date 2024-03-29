package com.milanesachan.DRPGTest1.game.battle;

import com.milanesachan.DRPGTest1.bot.core.BattleCommandManager;
import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.image_generator.BattleImageGenerator;
import com.milanesachan.DRPGTest1.commons.parameters.BattleParameters;
import com.milanesachan.DRPGTest1.networking.MatchMaker;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.Random;

public class BattleController extends Thread {
    private static final int battleInterval = 10;
    private final MessageChannel aChannel;
    private final MessageChannel bChannel;
    private final GuildParty aParty;
    private final GuildParty bParty;

    private boolean battleRunning;
    private final ArrayList<BattleCharacter> alreadyFought;
    
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
        while (battleRunning) {
            try {
                Thread.sleep(battleInterval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doBattleStep();
        }
        MatchMaker.getInstance().removeFromBattles(this);
        BattleCommandManager.getInstance().removeParty(aParty.getGuildID());
        BattleCommandManager.getInstance().removeParty(bParty.getGuildID());
    }

    private void doBattleStep() {
        BattleCharacter aChar = aParty.getRandomAbleChar();
        BattleCharacter bChar = bParty.getRandomAbleChar();
        messageBothParties("**Fight: " + aChar.getCharacter().getNameAndMention() + " vs. " +
                bChar.getCharacter().getNameAndMention()+"**");
        attack(aChar, bChar);
        attack(bChar, aChar);

        throwActives(aChar, bChar);

        BattleImageGenerator.getInstance().sendArenaImage(aChannel, aParty, bParty);
        BattleImageGenerator.getInstance().sendArenaImage(bChannel, bParty, aParty);

        checkIfWon();
    }

    private void messageBothParties(String message) {
        aChannel.sendMessage(message).queue();
        bChannel.sendMessage(message).queue();
    }

    private void checkIfWon() {
        if (bParty.isWholeGuildDead()) {
            aChannel.sendMessage("You won!").queue();
            bChannel.sendMessage("You lost!").queue();
            battleRunning = false;
        } else if (aParty.isWholeGuildDead()) {
            bChannel.sendMessage("You won!").queue();
            aChannel.sendMessage("You lost!").queue();
            battleRunning = false;
        }
    }

    private void throwActives(BattleCharacter aChar, BattleCharacter bChar){
        if(aChar.isWeaponCharged() && !bParty.isWholeGuildDead()){
            messageBothParties("**"+aChar.getCharacter().getNameAndMention()+" is using its weapon active!**");
            aChar.getWeapon().useActive(aParty, bParty, this);
            //TODO: A BETTER SYSTEM FOR THIS!!!!!!!!!!!!!!!!
            aChar.setEnergy(0);
        }
        if(bChar.isWeaponCharged() && !aParty.isWholeGuildDead()){
            messageBothParties("**"+bChar.getCharacter().getNameAndMention()+" is using its weapon active!**");
            bChar.getWeapon().useActive(bParty, aParty, this);
            //TODO: YES AND THIS
            bChar.setEnergy(0);
        }
    }

    private void attack(BattleCharacter dealer, BattleCharacter receiver) {
        Random rand = new Random();
        double dmgRadius = BattleParameters.damageRadiusPercent;
        int minDamage = (int) (dealer.getWeapon().getDamage() * (1 - dmgRadius));
        int maxDamage = (int) (dealer.getWeapon().getDamage() * (1 + dmgRadius));
        int damageBound = maxDamage - minDamage;
        int damage = minDamage + rand.nextInt(damageBound + 1);
        dealDamage(receiver, damage);
        dealer.chargeWeapon();
    }

    public void dealDamage(BattleCharacter bc, int dmg) {
        bc.receiveDamage(dmg);
        if (dmg > 0)
            messageBothParties(bc.getCharacter().getName() + " (<@" + bc.getUserID() + ">) received " + dmg + " damage. " +
                    "HP: " + bc.getHP() + "/" + bc.getMaxHP());
    }
}
