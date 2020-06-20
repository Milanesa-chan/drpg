package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.handlers.*;
import com.milanesachan.DRPGTest1.commons.console.ConsoleManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class CommandManager extends ListenerAdapter {
    private static CommandManager instance;
    private ConsoleManager con;
    private HashMap<Long, Confirmable> confirmationList;

    private CommandManager() {
        con = ConsoleManager.getInstance();
        confirmationList = new HashMap<>();
    }

    public static CommandManager getInstance() {
        if (instance == null)
            instance = new CommandManager();
        return instance;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (!event.getMessage().getAuthor().isBot()) {
            con.PrintMessageEventInfo(event);

            assert event.getMember() != null;
            long userID = event.getMember().getIdLong();
            if (confirmationList.containsKey(userID)) {
                if (event.getMessage().getContentRaw().equalsIgnoreCase("y")) {
                    Confirmable c = confirmationList.get(userID);
                    confirmationList.remove(userID);
                    c.confirm();
                } else if (event.getMessage().getContentRaw().equalsIgnoreCase("n")) {
                    Confirmable c = confirmationList.get(userID);
                    confirmationList.remove(userID);
                    c.cancel();
                }
            }

            generalCommands(event);
        }
    }

    private void generalCommands(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        assert event.getMember() != null;
        long userID = event.getMember().getIdLong();

        if (matchCommand(args[0], "help")) {
            new HelpHandler(event.getChannel()).handle();
        } else if (matchCommand(args[0], "createchar")) {
            String charName = argsAsString(args);
            new CharacterCreatorHandler(event.getChannel(), event.getMember().getId(), charName).handle();
        } else if (matchCommand(args[0], "deletechar")) {
            new CharacterDeletionHandler(event.getChannel(), event.getMember().getId()).handle();
        } else if (matchCommand(args[0], "infochar")) {
            new InfoCharacterHandler(event.getChannel(), event.getMember().getId(), event.getGuild().getIdLong()).handle();
        } else if (matchCommand(args[0], "infoguild")) {
            new InfoGuildHandler(event.getChannel(), event.getGuild().getIdLong()).handle();
        } else if (matchCommand(args[0], "infoitem")) {
            if (args.length < 2) {
                event.getChannel().sendMessage("**Error:** You have to specify an itemID. Example: **>infoitem weapon:blade**.").queue();
            } else {
                new InfoItemHandler(event.getChannel(), args[1]).handle();
            }
        } else if (matchCommand(args[0], "createguild")) {
            String guildName = argsAsString(args);
            GuildCreatorHandler h = new GuildCreatorHandler(event.getChannel(), event.getGuild().getIdLong(), guildName);
            onOwnerCommand(h, event);
        } else if (matchCommand(args[0], "deleteguild")) {
            GuildDeletionHandler h = new GuildDeletionHandler(event.getChannel(), event.getGuild().getIdLong(), event.getGuild().getOwner().getUser());
            onOwnerCommand(h, event);
        } else if (matchCommand(args[0], "joinguild")) {
            long serverID = event.getGuild().getIdLong();
            new GuildJoinHandler(event.getChannel(), serverID, userID).handle();
        }
    }

    private boolean matchCommand(String comm, String test) {
        return comm.equalsIgnoreCase(DRPGBot.getInstance().getPrefix().concat(test));
    }

    private void onOwnerCommand(Handler handler, MessageReceivedEvent event) {
        if (event.getMember().isOwner()) {
            handler.handle();
        } else {
            event.getChannel().sendMessage("**Error:** This command is **OWNER ONLY**.").queue();
        }
    }

    /**
     * Gets all the arguments in a command as a single string separated by spaces
     *
     * @param fullCommand string-array containing the complete command sent by the user
     * @return string containing every argument of the command separated by a space (excludes the actual command)
     */
    private String argsAsString(String[] fullCommand) {
        String args = "";
        for (int i = 1; i < fullCommand.length; i++) {
            args = args.concat(fullCommand[i]);
            if (i != fullCommand.length - 1)
                args = args.concat(" ");
        }
        return args;
    }

    public void addToConfirmationList(Long userID, Confirmable h) {
        if (confirmationList.containsKey(userID)) {
            confirmationList.get(userID).cancel();
        }
        confirmationList.put(userID, h);
    }
}
