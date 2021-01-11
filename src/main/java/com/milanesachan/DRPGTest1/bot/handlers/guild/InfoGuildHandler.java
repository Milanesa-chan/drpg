package com.milanesachan.DRPGTest1.bot.handlers.guild;

import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.sql.SQLException;

public class InfoGuildHandler implements Handler {
    private MessageChannel channel;
    private long guildID;

    public InfoGuildHandler(MessageChannel mc, long guildID) {
        channel = mc;
        this.guildID = guildID;
    }

    @Override
    public void handle() {
        try {
            //Guild guild = new GuildFactory().guildFromServerID(guildID);
            Guild guild = new Guild(guildID).loadFromDatabase();
            MessageEmbed emb = guild.getEmbed().build();
            channel.sendMessage(emb).queue();
        } catch (SQLException e) {
            channel.sendMessage("**Error:** Failed to connect to database. Try again later.").queue();
        } catch (ServerNotFoundException e) {
            channel.sendMessage("**Error:** This server has no guild.").queue();
        }
    }
}
