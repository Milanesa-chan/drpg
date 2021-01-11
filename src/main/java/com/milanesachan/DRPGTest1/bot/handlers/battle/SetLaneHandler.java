package com.milanesachan.DRPGTest1.bot.handlers.battle;

import com.milanesachan.DRPGTest1.bot.core.BattleCommandManager;
import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.SQLException;

public class SetLaneHandler implements Handler {
    private MessageChannel channel;
    private long userID;
    private long guildID;
    private String lane;

    public SetLaneHandler(MessageChannel channel, long userID, long guildID, String lane) {
        this.channel = channel;
        this.userID = userID;
        this.guildID = guildID;
        this.lane = lane;
    }

    @Override
    public void handle() {
        int laneNum = 0;
        if(lane.equalsIgnoreCase("FRONT")){
            laneNum = BattleCharacter.FRONT_LANE;
        }else if(lane.equalsIgnoreCase("BACK")){
            laneNum = BattleCharacter.BACK_LANE;
        }
        if(laneNum == 0){
            channel.sendMessage("'"+lane+"' is not a valid lane name! Correct format is: '>setlane front" +
                    "' or '>setlane back'.").queue();
        }else{
            try {
                GuildParty gp = BattleCommandManager.getInstance().getParty(guildID);
                BattleCharacter cha = gp.getBattleCharacter(userID);
                if(cha == null){
                    throw new CharacterNotFoundException();
                }else{
                    cha.setBattleLane(laneNum);
                    channel.sendMessage("<@"+userID+"> you were assigned to the "+lane+" lane of the party.").queue();
                }
            } catch (CharacterNotFoundException e) {
                e.printStackTrace();
                channel.sendMessage("Unexpected error. Try again later.").queue();
            }
        }
    }
}
