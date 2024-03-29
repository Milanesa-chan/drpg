package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.core.BattleCommandManager;
import com.milanesachan.DRPGTest1.bot.entities.GuildParty;
import com.milanesachan.DRPGTest1.commons.exceptions.CharacterNotFoundException;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Character;
import com.milanesachan.DRPGTest1.game.model.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class HandlerFilter {
    private boolean guildMemberRequired;
    private boolean battleChannelRequired;
    private boolean characterRequired;
    private boolean partyMemberRequired;
    private boolean partyRequired;

    public void filterHandler(MessageReceivedEvent event, Handler h, long userID) {
        MessageChannel channel = event.getChannel();
        long guildID = event.getGuild().getIdLong();
        long channelID = event.getChannel().getIdLong();
        boolean condition = true;

        try {
            if (battleChannelRequired && !isBattleChannel(event)) {
                try {
                    //Guild g = new GuildFactory().guildFromServerID(event.getGuild().getIdLong());
                    Guild g = new Guild(event.getGuild().getIdLong()).loadFromDatabase();
                    event.getChannel().sendMessage("This command is only for battle channels.").queue();
                    if (g.getBattleChannelID() == 0) {
                        event.getChannel().sendMessage("This server has no battle channel! Ask the owner to type" +
                                " '>setbattlechannel' on a channel of this server.").queue();
                    } else {
                        long battleChannelID = g.getBattleChannelID();
                        event.getChannel().sendMessage("This server's battle channel is: <#" + battleChannelID + ">").queue();
                    }
                } catch (SQLException throwables) {
                    event.getChannel().sendMessage("Error connecting to the database.").queue();
                } catch (ServerNotFoundException e) {
                    channel.sendMessage("This server has no guild!").queue();
                }
            }else if(partyRequired && !BattleCommandManager.getInstance().doesPartyExist(guildID)) {
                channel.sendMessage("This guild has no party! Create one with '>battle' in the battle channel.").queue();
            }else if (guildMemberRequired && !isGuildMember(guildID, userID)) {
                channel.sendMessage("This user's character is not part of this server's guild!").queue();
            } else if (characterRequired && !hasCharacter(userID)) {
                channel.sendMessage("No character registered to user's account!").queue();
            } else if (partyMemberRequired && !isUserInParty(channel, userID, guildID)) {
                channel.sendMessage("User is not in party!").queue();
            } else {
                h.handle();
            }
        } catch (SQLException ex) {
            channel.sendMessage("Error connecting to database. Try again later.").queue();
        } catch (CharacterNotFoundException ex) {
            channel.sendMessage("No character registered to user's account!").queue();
        }

    }

    private boolean hasCharacter(long userID) throws SQLException {
        Character c = new Character(userID);
        try {
            c.loadFromDatabase();
            return true;
        } catch (CharacterNotFoundException e) {
            return false;
        }
    }

    private boolean isGuildMember(long guildID, long userID) throws SQLException, CharacterNotFoundException {
        Character c = new Character(userID);
        c.loadFromDatabase();
        return c.getGuildID() == guildID;
    }

    private boolean isBattleChannel(MessageReceivedEvent event) {
        try {
            long guildID = event.getGuild().getIdLong();
            long channelID = event.getChannel().getIdLong();
            //Guild g = new GuildFactory().guildFromServerID(guildID);
            Guild g = new Guild(guildID).loadFromDatabase();
            return g.getBattleChannelID() == channelID;
        } catch (SQLException throwables) {
            event.getChannel().sendMessage("Error connecting to database. Try again later.").queue();
        } catch (ServerNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isUserInParty(MessageChannel channel, long userID, long guildID){
        GuildParty party = BattleCommandManager.getInstance().getParty(guildID);
        if(party == null) {
            channel.sendMessage("This guild has no party! Create one with '>battle' in the battle channel.").queue();
            return false;
        }else{
            return party.isCharInParty(userID);
        }
    }

    public void setGuildMemberRequired(boolean guildMemberRequired) {
        this.guildMemberRequired = guildMemberRequired;
    }

    public void setBattleChannelRequired(boolean battleChannelRequired) {
        this.battleChannelRequired = battleChannelRequired;
    }

    public void setCharacterRequired(boolean characterRequired) {
        this.characterRequired = characterRequired;
    }

    public void setPartyMemberRequired(boolean partyMemberRequired) {
        this.partyMemberRequired = partyMemberRequired;
    }

    public void setPartyRequired(boolean partyRequired) {
        this.partyRequired = partyRequired;
    }
}
