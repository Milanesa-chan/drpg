package com.milanesachan.DRPGTest1.bot.entities;

import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.commons.exceptions.*;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
import com.milanesachan.DRPGTest1.game.model.Embeddable;
import com.milanesachan.DRPGTest1.game.model.Guild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class GuildParty implements Embeddable {
    private long guildID;
    private MessageChannel battleChannel;
    private ArrayList<BattleCharacter> charList;
    private boolean inQueue, inBattle;

    public GuildParty(long guildID) throws ServerNotFoundException, SQLException {
        this.guildID = guildID;
        charList = new ArrayList<>();
        //long bChannelID = new GuildFactory().guildFromServerID(guildID).getBattleChannelID();
        long bChannelID = new Guild(guildID).loadFromDatabase().getBattleChannelID();
        battleChannel = DRPGBot.getInstance().getJda().getTextChannelById(bChannelID);
    }

    public long getGuildID() {
        return guildID;
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public void add(BattleCharacter character) throws AlreadyInPartyException {
        if (charList.contains(character))
            throw new AlreadyInPartyException(character, this);
        else charList.add(character);
    }

    public boolean isCharInParty(long userID) {
        for (BattleCharacter bc : charList) {
            if (bc.getUserID() == userID) return true;
        }
        return false;
    }

    public boolean getReady(int[] teamSizes) throws EquipmentNotFoundException, SQLException, UnassignedLaneException {
        if(!containsInt(teamSizes, charList.size())){
            return false;
        }else{
            for(BattleCharacter bc : charList){
                bc.getReady();
            }
            return true;
        }
    }

    private boolean containsInt(int[] array, int num){
        for(int i : array){
            if(i==num) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GuildParty)) return false;
        if (obj == this) return true;
        return ((GuildParty) obj).guildID == this.guildID;
    }

    public void addChar(BattleCharacter character) {
        charList.add(character);
    }

    public void removeChar(BattleCharacter character) {
        charList.remove(character);
    }

    public BattleCharacter getRandomChar() {
        int i = new Random().nextInt(charList.size());
        return charList.get(i);
    }

    public BattleCharacter getRandomAbleChar(){
        Random rand = new Random();
        if(!isWholeGuildDead() && canGuildFight()) {
            int rnd;
            BattleCharacter bc = null;
            do {
                rnd = rand.nextInt(charList.size());
                bc = charList.get(rnd);
            }while(!bc.canFight());

            return bc;
        }
        return null;
    }

    public BattleCharacter getRandomAliveChar(){
        Random rand = new Random();
        if(!isWholeGuildDead()){
            int rnd;
            BattleCharacter bc = null;
            do {
                rnd = rand.nextInt(charList.size());
                bc = charList.get(rnd);
            }while(bc.getHP()<=0);
            return bc;
        }
        return null;
    }

    public boolean isWholeGuildDead(){
        boolean result = true;
        for(BattleCharacter bc : charList){
            if(bc.getHP()>0) result = false;
        }
        return result;
    }

    private boolean canGuildFight(){
        for(BattleCharacter bc : charList){
            if(bc.canFight()) return true;
        }
        return false;
    }

    public ArrayList<BattleCharacter> getCharList() {
        return charList;
    }

    public void setCharList(ArrayList<BattleCharacter> charList) {
        this.charList = charList;
    }

    @Override
    public EmbedBuilder getEmbed() {
        JDA jda = DRPGBot.getInstance().getJda();
        String serverName = jda.getGuildById(guildID).getName();
        String iconUrl = jda.getGuildById(guildID).getIconUrl();

        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle("Battle Party");
        emb.setDescription("Server: " + serverName);
        emb.setThumbnail(iconUrl);
        emb.setColor(0x450000);

        ArrayList<BattleCharacter> nextList;

        for(int lane=0; lane<3; lane++){
            nextList = getCharactersInLane(lane);
            if(!nextList.isEmpty()){
                String members = getLaneMemberList(jda, nextList);
                switch(lane){
                    case 0:
                        emb.addField("Unassigned", members, false);
                        break;
                    case 1:
                        emb.addField("Back lane", members, false);
                        break;
                    case 2:
                        emb.addField("Front lane", members, false);
                }
            }
        }

        return emb;
    }

    private String getLaneMemberList(JDA jda, ArrayList<BattleCharacter> chars){
        String list = "";

        User user;
        for(BattleCharacter bc : chars){
            user = jda.getUserById(bc.getUserID());
            list = list.concat(bc.getCharacter().getName()+" ("+user.getAsTag()+")");
            if(!(chars.indexOf(bc)==chars.size()-1))
                list = list.concat("\n");
        }

        return list;
    }

    private ArrayList<BattleCharacter> getCharactersInLane(int lane){
        ArrayList<BattleCharacter> ret = new ArrayList<>();
        for(BattleCharacter bc : charList){
            if(bc.getBattleLane()==lane) ret.add(bc);
        }
        return ret;
    }

    public boolean isUserInParty(long userID){
        for(BattleCharacter bc : charList){
            if(bc.getUserID()==userID) return true;
        }
        return false;
    }

    public BattleCharacter getBattleCharacter(long userID){
        for(BattleCharacter bc : charList){
            if(bc.getUserID()==userID) return bc;
        }
        return null;
    }

    public MessageChannel getBattleChannel() {
        return battleChannel;
    }

    public void setBattleChannel(MessageChannel battleChannel) {
        this.battleChannel = battleChannel;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    public void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }

    public boolean isInBattle() {
        return inBattle;
    }

    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }
}
