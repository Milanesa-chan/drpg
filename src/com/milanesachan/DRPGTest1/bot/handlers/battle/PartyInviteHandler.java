package com.milanesachan.DRPGTest1.bot.handlers.battle;

import com.milanesachan.DRPGTest1.bot.core.BattleCommandManager;
import com.milanesachan.DRPGTest1.bot.core.CommandManager;
import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.handlers.Confirmable;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.AlreadyInPartyException;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.SQLException;

public class PartyInviteHandler implements Handler, Confirmable {
    private MessageChannel channel;
    private long guildID;
    private long userID;
    private long inviterID;

    public PartyInviteHandler(MessageChannel channel, long guildID, long inviterID ,long userID) {
        this.channel = channel;
        this.guildID = guildID;
        this.userID = userID;
        this.inviterID = inviterID;
    }

    @Override
    public void handle() {
        BattleCommandManager bcm = BattleCommandManager.getInstance();
        if(!bcm.doesPartyExist(guildID)){
            channel.sendMessage("There is no party created for this guild. Create one with '>battle'.").queue();
        }else{
            GuildParty p = bcm.getParty(guildID);
            if(p.isCharInParty(userID)){
                channel.sendMessage("User is already in party!").queue();
            else if (!p.isCharInParty(inviterID))
                channel.sendMessage("You can't use this command! You're not in party").queue();
            }else{
                channel.sendMessage("<@"+userID+"> you were invited to the battle party. Do you accept? (y/n)").queue();
                CommandManager.getInstance().addToConfirmationList(userID, this);
            }
        }
    }

    @Override
    public void confirm() {
        GuildParty p = BattleCommandManager.getInstance().getParty(guildID);
        try {
            BattleCharacter bc = new BattleCharacter(userID);
            p.add(bc);
            channel.sendMessage("<@"+userID+"> joined the party!\n" +
                    "Now choose your desired lane with '>setlane front' or '>setlane back'.").queue();
        } catch (SQLException throwables) {
            channel.sendMessage("Error connecting to the database. Try again later.").queue();
        } catch (CharacterNotFoundException | AlreadyInPartyException e) {
            e.printStackTrace();
        } catch (EquipmentNotFoundException e) {
            channel.sendMessage("This character has no equipment. Equip something first with '>equip'.").queue();
        }
    }

    @Override
    public void cancel() {
        channel.sendMessage("Invite rejected.").queue();
    }
}
