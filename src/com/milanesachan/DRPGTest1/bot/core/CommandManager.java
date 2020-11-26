package com.milanesachan.DRPGTest1.bot.core;

import com.milanesachan.DRPGTest1.bot.handlers.*;
import com.milanesachan.DRPGTest1.bot.handlers.character.CharacterCreatorHandler;
import com.milanesachan.DRPGTest1.bot.handlers.character.CharacterDeletionHandler;
import com.milanesachan.DRPGTest1.bot.handlers.character.GuildJoinHandler;
import com.milanesachan.DRPGTest1.bot.handlers.character.InfoCharacterHandler;
import com.milanesachan.DRPGTest1.bot.handlers.guild.GuildCreatorHandler;
import com.milanesachan.DRPGTest1.bot.handlers.guild.GuildDeletionHandler;
import com.milanesachan.DRPGTest1.bot.handlers.guild.InfoGuildHandler;
import com.milanesachan.DRPGTest1.bot.handlers.inventory.*;
import com.milanesachan.DRPGTest1.bot.handlers.item.InfoItemHandler;
import com.milanesachan.DRPGTest1.bot.handlers.statcards.FusionHandler;
import com.milanesachan.DRPGTest1.commons.console.ConsoleManager;
import com.milanesachan.DRPGTest1.networking.DatabaseConnector;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.SQLException;
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
            //con.PrintMessageEventInfo(event);

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
            inventoryCommands(event);
            testingCommands(event);
        }
    }

    private void inventoryCommands(MessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        assert event.getMember() != null;
        long userID = event.getMember().getIdLong();

        if(matchCommand(args[0], "inv")){
            long inventoryUID;
            if(!event.getMessage().getMentionedMembers().isEmpty()){
                User mentioned = event.getMessage().getMentionedMembers().get(0).getUser();
                inventoryUID = mentioned.getIdLong();
            }else{
                inventoryUID = userID;
            }
            ShowInventoryHandler h = new ShowInventoryHandler(event.getChannel(), inventoryUID);
            onCharacterRequiredCommand(h, event, inventoryUID);
        }else if(matchCommand(args[0], "createitem")){
            if(args.length!=3
            || event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessage("<@" + userID + "> **Error:** correct format is '>createitem <@User> <itemID>'").queue();
            }else{
                User user = event.getMessage().getMentionedMembers().get(0).getUser();
                String itemID = args[2];
                CreateItemHandler h = new CreateItemHandler(event.getChannel(), user, itemID);
                onMasterCommand(h, event);
            }
        }else if(matchCommand(args[0], "equip")){
            if(args.length < 2) {
                ShowEquipmentHandler h = new ShowEquipmentHandler(event.getChannel(), userID);
                onCharacterRequiredCommand(h, event, userID);
            }else{
                EquipHandler h = new EquipHandler(event.getChannel(), userID, args[1]);
                onCharacterRequiredCommand(h, event, userID);
            }
        }else if(matchCommand(args[0], "unequip")){
            UnequipHandler h = new UnequipHandler(event.getChannel(), userID);
            onCharacterRequiredCommand(h, event, userID);
        }else if(matchCommand(args[0], "fuse")){
            FusionHandler h = new FusionHandler(event.getChannel(), userID);

            HandlerFilter filter = new HandlerFilter();
            filter.setCharacterRequired(true);
            filter.filterHandler(event, h, userID);
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
            String userInfo = event.getMember().getId();//self
            if (!event.getMessage().getMentionedMembers().isEmpty()) {//different player
                User mentioned = event.getMessage().getMentionedMembers().get(0).getUser();
                userInfo = mentioned.getId();
            } 
            new InfoCharacterHandler(event.getChannel(), userInfo, event.getGuild().getIdLong()).handle();
        } else if (matchCommand(args[0], "infoguild")) {
            new InfoGuildHandler(event.getChannel(), event.getGuild().getIdLong()).handle();
        } else if (matchCommand(args[0], "infoitem")) {
            if (args.length < 2) {
                event.getChannel().sendMessage("**Error:** You have to specify an itemID. Example: **>infoitem weapon:btb**.").queue();
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

    private void testingCommands(MessageReceivedEvent event){
        String args[] = event.getMessage().getContentRaw().split(" ");

        if(matchCommand(args[0], "test")){
            TesterHandler h = new TesterHandler(event);
            onMasterCommand(h, event);
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

    private void onCharacterRequiredCommand(Handler handler, MessageReceivedEvent event, long userID){
        try {
            if(DatabaseConnector.getInstance().isCharacterInDatabase(userID)){
                handler.handle();
            }else{
                event.getChannel().sendMessage("**Error:** User has no character.").queue();
            }
        } catch (SQLException e) {
            event.getChannel().sendMessage("There was an error connecting to the database. Try again later.").queue();
        }
    }

    private void onMasterCommand(Handler handler, MessageReceivedEvent event){
        User user = event.getMember().getUser();
        if(DRPGBot.getInstance().isMasterUser(user)){
            handler.handle();
        }
    }

    /**
     * Gets all the arguments in a command as a single string separated by spaces
     * (Example: ">command argument1 argument2" will return "argument1 argument2")
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
