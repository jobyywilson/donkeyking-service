package com.donkeyking.service;

import com.donkeyking.service.intelligent.Bot;
import com.donkeyking.service.type.CardType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class BotTest {

    @Test
    public void nextCardTest1(){
        LinkedHashMap<String, List<String>> userIdCards = new LinkedHashMap<>();

        userIdCards.put("diamonds",new ArrayList<>(Arrays.asList("2D")));
        userIdCards.put("clubs",new ArrayList<>());
        userIdCards.put("hearts",new ArrayList<>());
        userIdCards.put("spades",new ArrayList<>());
        String cardType = Bot.pickCard(userIdCards, CardType.DIAMONDS);
        Assert.assertEquals("2D",cardType);

    }


    @Test
    public void nextCardTest2(){
        LinkedHashMap<String, List<String>> userIdCards = new LinkedHashMap<>();

        userIdCards.put("diamonds",new ArrayList<>(Arrays.asList("2D","AD")));
        userIdCards.put("clubs",new ArrayList<>());
        userIdCards.put("hearts",new ArrayList<>());
        userIdCards.put("spades",new ArrayList<>());
        String cardType = Bot.pickCard(userIdCards, CardType.DIAMONDS);
        Assert.assertEquals("AD",cardType);

    }



    @Test
    public void nextCardTest3(){
        LinkedHashMap<String, List<String>> userIdCards = new LinkedHashMap<>();

        userIdCards.put("diamonds",new ArrayList<>(Arrays.asList("2D","AD")));
        userIdCards.put("clubs",new ArrayList<>());
        userIdCards.put("hearts",new ArrayList<>());
        userIdCards.put("spades",new ArrayList<>());
        String cardType = Bot.pickCard(userIdCards, CardType.CLUBS);
        Assert.assertEquals("AD",cardType);

    }
}
