package com.milanesachan.DRPGTest1.bot.handlers.battle;

import com.milanesachan.DRPGTest1.bot.core.BattleCommandManager;
import com.milanesachan.DRPGTest1.bot.core.CommandManager;
import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.AlreadyInPartyException;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.EquipmentNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.game.battle.BattleCharacter;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.SQLException;

public class CreatePartyHandler implements Handler {
    private MessageChannel channel;
    private long guildID;
    private long userID;

    public CreatePartyHandler(MessageChannel channel, long guildID, long userID) {
        this.channel = channel;
        this.guildID = guildID;
        this.userID = userID;
    }

    @Override
    public void handle() {
        GuildParty party = null;
        try {
            party = new GuildParty(guildID);

            if (BattleCommandManager.getInstance().getParties().contains(party)) {
                channel.sendMessage("Party already created!").queue();
            } else {
                try {
                    BattleCharacter character = new BattleCharacter(userID);
                    party.add(character);
                    BattleCommandManager.getInstance().getParties().add(party);
                    channel.sendMessage("Party created!").queue();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    channel.sendMessage("Error connecting to database. Try again later.").queue();
                } catch (CharacterNotFoundException | AlreadyInPartyException e) {
                    e.printStackTrace();
                } catch (EquipmentNotFoundException e) {
                    channel.sendMessage("Your character has no equipment. Equip something first!").queue();
                }
            }
        } catch (ServerNotFoundException | SQLException e) {
            channel.sendMessage("Unexpected error. Try again later.").queue();
            e.printStackTrace();
        }
    }
}
