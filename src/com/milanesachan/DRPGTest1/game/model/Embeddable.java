package com.milanesachan.DRPGTest1.game.model;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface Embeddable {
    EmbedBuilder getEmbed();
}
