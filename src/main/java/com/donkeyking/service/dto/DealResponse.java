package com.donkeyking.service.dto;

import com.donkeyking.service.domain.Room;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class DealResponse {

    private final String roomSessionId;

    private final LinkedHashMap<String,Integer> winners ;

    private final LinkedHashMap<String, List<String>> cardInfo;

    private final LinkedHashMap<String, String> transaction;

    private final Set<String> usersLeft ;

    private final String previousUserId;

    private final String nextChanceUserId;

    private final boolean isGameOver;

    private final boolean isResetDeck;

    private final String nextCardType;

    private final int currentChance;

    public DealResponse(Room room, String userId, LinkedHashMap<String, String> transaction) {
        this.winners = room.getUsersWon();
        this.transaction = transaction;
        this.nextChanceUserId = room.getNextChanceUser();
        this.isGameOver = room.getUsersWon().size() == room.getClients().size()-1;
        this.previousUserId = room.getLastDealUser();
        this.cardInfo = room.getUserCardInfo().get(userId);
        this.isResetDeck = room.isResetDeck();
        this.nextCardType = room.getNextCardType().getValue();
        this.usersLeft = room.getUsersLeft();
        this.currentChance = room.getClients().get(userId).getChances();
        this.roomSessionId = room.getSessionId();
    }

    public LinkedHashMap<String,Integer> getWinners() {
        return winners;
    }

    public LinkedHashMap<String, List<String>> getCardInfo() {
        return cardInfo;
    }

    public LinkedHashMap<String, String> getTransaction() {
        return transaction;
    }

    public String getNextChanceUserId() {
        return nextChanceUserId;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public String getPreviousUserId() {
        return previousUserId;
    }

    public boolean isResetDeck() {
        return isResetDeck;
    }

    public String getNextCardType() {
        return nextCardType;
    }

    public Set<String> getUsersLeft() {
        return usersLeft;
    }

    public int getCurrentChance() {
        return currentChance;
    }

    public String getRoomSessionId() {
        return roomSessionId;
    }
}
