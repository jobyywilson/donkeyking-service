package com.donkeyking.service.dto;

import com.donkeyking.service.domain.Room;

import java.util.*;

public class StartGameResponse {

    private final String nextChanceUserId;

    private final String roomSessionId ;

    private final LinkedHashMap<String, List<String>> cardInfo;

    private final LinkedHashMap<String,Integer> orderInfo;

    private final ArrayList<HashMap<String,Object>> playersInfo;


    public StartGameResponse(Room room,String userId ,ArrayList<HashMap<String, Object>> playersInfo) {
        this.nextChanceUserId = room.getNextChanceUser();
        this.cardInfo = room.getUserCardInfo().get(userId);
        this.orderInfo = room.getOrderInfo();
        this.playersInfo = playersInfo;
        this.roomSessionId = room.getSessionId();
    }

    public Map<String, List<String>> getCardInfo() {
        return cardInfo;
    }

    public String getNextChanceUserId() {
        return nextChanceUserId;
    }

    public LinkedHashMap<String, Integer> getOrderInfo() {
        return orderInfo;
    }

    public ArrayList<HashMap<String, Object>> getPlayersInfo() {
        return playersInfo;
    }

    public String getRoomSessionId() {
        return roomSessionId;
    }
}
