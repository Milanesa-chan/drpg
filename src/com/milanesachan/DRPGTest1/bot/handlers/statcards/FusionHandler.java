package com.milanesachan.DRPGTest1.bot.handlers.statcards;

import com.milanesachan.DRPGTest1.bot.core.AnswerManager;
import com.milanesachan.DRPGTest1.bot.core.DRPGBot;
import com.milanesachan.DRPGTest1.bot.core.PagedEmbedBuilder;
import com.milanesachan.DRPGTest1.bot.entities.PagedEmbed;
import com.milanesachan.DRPGTest1.bot.handlers.Answerable;
import com.milanesachan.DRPGTest1.bot.handlers.Confirmable;
import com.milanesachan.DRPGTest1.bot.handlers.Handler;
import com.milanesachan.DRPGTest1.bot.handlers.HandlerFilter;
import com.milanesachan.DRPGTest1.commons.exceptions.FusionFailedException;
import com.milanesachan.DRPGTest1.commons.parameters.StatCardsParameters;
import com.milanesachan.DRPGTest1.game.model.Embeddable;
import com.milanesachan.DRPGTest1.game.model.Inventory;
import com.milanesachan.DRPGTest1.game.model.UserItem;
import com.milanesachan.DRPGTest1.game.model.items.StatCard;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.sql.SQLException;
import java.util.*;

public class FusionHandler implements Handler, Confirmable, Answerable {
    private MessageChannel channel;
    private long userID;
    private ArrayList<StatCard> fullCardList;
    private ArrayList<StatCard> fuseCardList;

    class FusionData implements Embeddable {
        public float redChance;
        public float greenChance;
        public float blueChance;

        public int minStr, maxStr;
        public int minAcc, maxAcc;
        public int minVit, maxVit;
        public int minMag, maxMag;

        @Override
        public EmbedBuilder getEmbed() {
            EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("Fusion Result");
            builder.setColor(0x450000);

            builder.addField("Color Chances:", "Red: " + redChance + "%\n" +
                    "Green: " + greenChance + "%\nBlue: " + blueChance + "%", false);

            return builder;
        }
    }

    public FusionHandler(MessageChannel channel, long userID) {
        this.channel = channel;
        this.userID = userID;
        fullCardList = new ArrayList<>();
        fuseCardList = new ArrayList<>();
    }

    @Override
    public void handle() {
        try {
            fillFullCardList();

            PagedEmbedBuilder builder = new PagedEmbedBuilder(channel);

            ArrayList<String> cardsStrList = new ArrayList<>();
            for (StatCard sc : fullCardList) {
                cardsStrList.add(fullCardList.indexOf(sc) + " : " + sc.toString());
            }

            builder.setData(cardsStrList);
            builder.setLinesPerPage(10);
            PagedEmbed cardsEmbed = builder.fromStringArray();

            cardsEmbed.send();
            channel.sendMessage("Type the numbers of the cards you want to fuse, separated by spaces," +
                    " for example: '1 2 3 14 15'.").queue();
            AnswerManager.getInstance().addToWaitingList(userID, this);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            channel.sendMessage("Error connecting to the database. Try again later.").queue();
        } catch (FusionFailedException e) {
            System.out.println(e.getMessage());
            channel.sendMessage("<@" + userID + "> Fusion failed: " + e.getMessage()).queue();
        }
    }

    private void fillFullCardList() throws SQLException, FusionFailedException {
        Inventory usersInv = new Inventory(userID).loadFromDatabase();
        ArrayList<StatCard> pulledCards = new ArrayList<>();

        if (usersInv.isEmpty()) throw new FusionFailedException("Inventory is empty!");

        for (UserItem it : usersInv) {
            if (it.getItem().getClass() == StatCard.class) {
                pulledCards.add((StatCard) it.getItem());
            }
        }

        if (pulledCards.size() < 2) throw new FusionFailedException("You need at least two stat cards to fuse!");
        else {
            fullCardList.clear();
            fullCardList.addAll(pulledCards);
        }
    }


    @Override
    public boolean tryAnswer(String answer) {
        if (answer.startsWith(DRPGBot.getInstance().getPrefix())) return false;

        String[] ansParts = answer.split(" ");
        if (ansParts.length < 2) {
            channel.sendMessage("You must choose at least two cards.").queue();
            return false;
        } else if (!isIntStringArray(ansParts)) {
            channel.sendMessage("Unrecognized format. You must **only** send whole numbers " +
                    "separated by spaces.").queue();
            return false;
        } else {
            try {
                if (areCardNumbersPossible(ansParts)) {
                    new Thread(() -> answer(answer)).start();
                    return true;
                } else {
                    channel.sendMessage("Unexpected error. Try again later.").queue();
                    return false;
                }
            } catch (FusionFailedException e) {
                channel.sendMessage(e.getMessage()).queue();
                return false;
            }
        }
    }

    private boolean areCardNumbersPossible(String[] numbers) throws FusionFailedException {
        for (String s : numbers) {
            int i = Integer.parseInt(s);
            if (i >= fullCardList.size() || i < 0) throw new FusionFailedException("'" + i + "' is not a " +
                    "possible card number!");
        }
        return true;
    }

    private boolean isIntStringArray(String[] array) {
        for (String s : array) {
            try {
                Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void answer(String answer) {
        fuseCardList.clear();
        for (String cardStr : answer.split(" ")) {
            StatCard nextCard = fullCardList.get(Integer.parseInt(cardStr));
            fuseCardList.add(nextCard);
        }

        channel.sendMessage(preComputeFusion().getEmbed().build()).queue();

    }

    private FusionData preComputeFusion() {
        FusionData fusionData = new FusionData();

        int totalCards = fuseCardList.size();
        int countRedCards = 0;
        int countGreenCards = 0;
        int countBlueCards = 0;

        for (StatCard card : fuseCardList) {
            switch (card.getType()) {
                case 'r':
                    countRedCards++;
                    break;
                case 'g':
                    countGreenCards++;
                    break;
                case 'b':
                    countBlueCards++;
                    break;
            }
        }

        fusionData.redChance = ((float) countRedCards / (float) totalCards) * 100;
        fusionData.greenChance = ((float) countGreenCards / (float) totalCards) * 100;
        fusionData.blueChance = ((float) countBlueCards / (float) totalCards) * 100;

        /*
        ArrayList<Integer> strValues = new ArrayList<>();
        for (StatCard card : fuseCardList) {
            strValues.add(card.getStrength());
        }
        channel.sendMessage("Min STR: "+computeStat(strValues, true, StatCardsParameters.statcardFuseStatMin)).queue();
        channel.sendMessage("Max STR: "+computeStat(strValues, true, StatCardsParameters.statcardFuseStatMax)).queue();
        channel.sendMessage(String.valueOf(computeStat(strValues, false, 0.0f))).queue();
        channel.sendMessage(String.valueOf(computeStat(strValues, false, 0.0f))).queue();
        channel.sendMessage(String.valueOf(computeStat(strValues, false, 0.0f))).queue();
        channel.sendMessage(String.valueOf(computeStat(strValues, false, 0.0f))).queue();
        channel.sendMessage(String.valueOf(computeStat(strValues, false, 0.0f))).queue();
        */

        return fusionData;
    }

    @Override
    public void confirm() {
    }

    @Override
    public void cancel() {
        channel.sendMessage("<@" + userID + "> Fusion cancelled.").queue();
    }

    private int computeStat(ArrayList<Integer> values, boolean deterministic, float deterministicMul) {
        ArrayList<Integer> valuesCopy = new ArrayList<>(values);
        valuesCopy.sort(Collections.reverseOrder());
        float resultingStat = valuesCopy.remove(0);
        float mul = deterministic ? deterministicMul : 0.0f;
        float mulMin = StatCardsParameters.statcardFuseStatMin;
        float mulMax = StatCardsParameters.statcardFuseStatMax;

        for (int val : valuesCopy) {
            if (!deterministic) mul = mulMin + (float) Math.random() * (mulMax - mulMin);
            resultingStat += val * mul < 1 ? 1 : val * mul;
        }

        return (int) resultingStat;
    }
}
