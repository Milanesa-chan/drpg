package com.milanesachan.DRPGTest1.bot.handlers.guild;

import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GuildCreatorHandler implements Handler {
    private Long serverID;
    private String guildName;
    private MessageChannel channel;

    public GuildCreatorHandler(MessageChannel mc, long serverID, String name){
        channel = mc;
        this.serverID = serverID;
        this.guildName = name;
    }

    @Override
    public void handle() {
        try {
            if (DatabaseConnector.getInstance().isGuildInDatabase(serverID)){
                String name = DatabaseConnector.getInstance().getGuildName(serverID);
                channel.sendMessage("**Error:** This server already has a guild: \""+name+"\"").queue();
            }else if(guildName.isEmpty()) {
                channel.sendMessage("**Error:** You must specify a name. Format: **\">createguild name\"**.").queue();
            }else{
                createNewGuild();
                channel.sendMessage("**Success:** The guild \""+guildName+"\" was created for this server!").queue();
            }
        }catch(SQLException | ServerNotFoundException ex){
            channel.sendMessage("**Error:** Failed to create guild. Try again later.").queue();
        }
    }

    private void createNewGuild() throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con != null) {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO `guilds`(`UID`, `Name`) VALUES (?, ?);");
            stmt.setLong(1, serverID);
            stmt.setString(2, guildName);
            stmt.execute();
        }else{
            throw new SQLException();
        }
    }
}
