package com.milanesachan.DRPGTest1.bot.handlers;

public interface Answerable {

    void answer(String answer);

    boolean tryAnswer(String answer);

    void cancel();
}
