package com.milanesachan.DRPGTest1.bot.handlers;

import com.milanesachan.DRPGTest1.bot.core.AnswerManager;
import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.core.ItemFactory;
import com.milanesachan.DRPGTest1.bot.handlers.statcards.FusionHandler;
import com.milanesachan.DRPGTest1.bot.image_generator.TestImageGenerator;
import com.milanesachan.DRPGTest1.commons.exceptions.ItemNotFoundException;
import com.milanesachan.DRPGTest1.game.model.Inventory;
import com.milanesachan.DRPGTest1.game.model.items.StatCard;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;

public class TesterHandler implements Handler, Answerable{
    private MessageChannel channel;
    private MessageReceivedEvent event;

    public TesterHandler(MessageReceivedEvent ev){
        channel = ev.getChannel();
        event = ev;
    }

    @Override
    public void handle() {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args.length < 4){
            channel.sendMessage("Format is: '>test amount stat_average stat_radius'").queue();
        }else{
            try {
                generateRandomCards(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), event.getMember().getIdLong());
                channel.sendMessage("Generated cards.").queue();
            } catch (SQLException | ItemNotFoundException throwables) {
                channel.sendMessage("Unexpected error.").queue();
                throwables.printStackTrace();
            }
        }
    }

    private boolean isInt(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private void generateRandomCards(int amount, int statAvg, int statRadius, long userID) throws SQLException, ItemNotFoundException {
        Inventory userInv = new Inventory(userID).loadFromDatabase();
        char[] colors = {'r', 'g', 'b'};
        for(int c=0; c<amount; c++){
            int[] stats = new int[4];
            for(int s=0; s<4; s++){
                stats[s] = (int) (statAvg - statRadius + Math.random()*statRadius*2);
            }

            char color = colors[(int)(Math.random()*3)];

            StatCard newCard = ItemFactory.getInstance().getStatCard(color, stats[0], stats[1], stats[2], stats[3]);
            userInv.add(newCard);
        }
        userInv.saveToDatabase();
    }

    @Override
    public void answer(String answer) {
        channel.sendMessage("Received response: "+answer).queue();
    }

    @Override
    public boolean tryAnswer(String answer) {
        if(answer.startsWith(DRPGBot.getInstance().getPrefix())) return false;
        else answer(answer);
        return true;
    }

    @Override
    public void cancel() {
        channel.sendMessage("Cancelled response command.").queue();
    }
}
