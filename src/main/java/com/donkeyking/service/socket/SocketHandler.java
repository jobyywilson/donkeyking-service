package com.donkeyking.service.socket;

import com.donkeyking.service.domain.Room;
import com.donkeyking.service.domain.RoomService;
import com.donkeyking.service.domain.UserInfo;
import com.donkeyking.service.domain.WebSocketMessage;
import com.donkeyking.service.dto.DealResponse;
import com.donkeyking.service.dto.StartGameResponse;
import com.donkeyking.service.intelligent.Bot;
import com.donkeyking.service.util.AesBase64Wrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SocketHandler extends TextWebSocketHandler {
    @Autowired
    private RoomService roomService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    // session id to room mapping
    private static final Map<String, WeakReference<Room>> userIdToRoomMap =
            Collections.synchronizedMap(new WeakHashMap<>());

    private static final Map<String, WeakReference<String>> sessionIdToUserIdMap =
            Collections.synchronizedMap(new WeakHashMap<>());


    // message types, used in signalling:
    // text message
    private static final String MSG_START_GAME = "start-game";

    private static final String MSG_SUBMIT_CARD = "submit-card";

    private static final String MSG_TYPE_TEXT = "text";
    // join room data message
    private static final String MSG_TYPE_JOIN = "join";
    // leave room data message
    private static final String MSG_TYPE_LEAVE = "leave";

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        logger.debug("[ws] Session has been closed with status {}", status);
        leaveGame(session);
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        // webSocket has been opened, send a message to the client
        // when data field contains 'true' value, the client starts negotiating
        // to establish peer-to-peer connection, otherwise they wait for a counterpart
        sendMessage(session, new WebSocketMessage("Server", MSG_TYPE_JOIN, Boolean.toString(!userIdToRoomMap.isEmpty())),null);
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
        // a message has been received
        try {
            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
            logger.debug("[ws] Message of {} type from {} received", message.getType(), message.getFrom());
            String userId = message.getFrom(); // origin of the message

            Object data = message.getData(); // payload
            logger.debug("[ws] userId :  {}", userId);
            Room room;
            switch (message.getType()) {
                // text message from client has been received
                case MSG_TYPE_TEXT:
                    logger.debug("[ws] Text message: {}", message.getData());
                    break;

                case MSG_TYPE_JOIN:
                    // message.data contains connected room id
                    logger.debug("[ws] {} has joined Roo m: #{}", userId, message.getData());
                    Map<String,String> joinInfo = (Map<String, String>) data;
                    String roomId = joinInfo.get("roomId");
                    String from = message.getFrom();
                    room = roomService.findRoomByStringId(roomId);
                    if(!room.isGameInProgress()){
                        // add client to the Room clients list
                        roomService.addClient(room, userId,joinInfo,from, session);
                        userIdToRoomMap.put(userId, new WeakReference<>(room));
                        sessionIdToUserIdMap.put(session.getId(),new WeakReference<>(userId));
                        this.sendMessageToAll("userInfo",room,room.getClientsForPublic());
                    }else{
                        this.sendMessage(session,new WebSocketMessage(
                                "Server",
                                "game-already-started",
                                null),room);
                    }
                    break;
                case MSG_SUBMIT_CARD:
                    this.submitCard(message);
                    break;
                case MSG_START_GAME:
                    String[] cards= new String[]{"AC","AD","AH","AS","2C","2D","2H","2S","3C","3D","3H","3S","4C","4D","4H","4S","5C","5D","5H","5S","6C","6D","6H","6S","7C","7D","7H","7S","8C","8D","8H","8S","9C","9D","9H","9S","10C","10D","10H","10S","JC","JD","JH","JS","QC","QD","QH","QS","KC","KD","KH","KS"};
                    //String[] cards= new String[]{"AC","AD","AH","AS","2C","2D","2H","2S","3C"};
                    List<String> shuffledCards = Arrays.asList(cards);
                    Collections.shuffle(shuffledCards);
                    room = userIdToRoomMap.get(message.getFrom()).get();
                    room.init();

                    int chunkSize = cards.length/room.getClients().size();
                    List<List<String>> suffledCardList = divideIntoSubArrays(chunkSize,cards);
                    int index = 0;
                    HashMap<String,Object> deck = new HashMap<>();

                    LinkedHashMap<String,Integer> orderInfo = new LinkedHashMap<>();
                    ArrayList<HashMap<String,Object>> playersInfo = new ArrayList<>();
                    HashMap<String,String> cardInfo = new HashMap<>();
                    deck.put("cardInfo",cardInfo);
                    deck.put("orderInfo",orderInfo);
                    deck.put("playersInfo",playersInfo);
                    for (Map.Entry<String, UserInfo> entry: room.getClients().entrySet() ) {
                        String cardStr = String.join(",", suffledCardList.get(index));
                        String secretId = entry.getValue().getSecretId();
                        room.updateUserCardInfo(entry.getKey(),suffledCardList.get(index));
                        String cardEncrypted = AesBase64Wrapper.encryptAndEncode(cardStr,secretId);

                        cardInfo.put(entry.getKey(),cardEncrypted);
                        orderInfo.put(entry.getKey(), index);
                        //TODO need to remove in future

                        HashMap<String,Object> player = new HashMap<>();
                        player.put("nickName",entry.getValue().getNickName());
                        player.put("index",index);
                        player.put("noOfCards",suffledCardList.get(index).size());
                        player.put("userId",entry.getKey());
                        playersInfo.add(player);
                        index++;
                    }
                    room.setOrderInfo(orderInfo);
                    room.setGameInProgress(true);
                    String nextChanceUserId = orderInfo.entrySet().iterator().next().getKey();
                    room.setNextChanceUser(nextChanceUserId);
                    this.sendInitialGameStartDetailsToAllUsers(room,playersInfo);
                    LinkedHashMap<String,List<String>> nextUserCards = room.getUserCardInfo().get(nextChanceUserId);
                    UserInfo userInfo = room.getClients().get(nextChanceUserId);
                    TimerTask action = new TimerTask() {
                        public void run() {
                            String nextCard = Bot.pickCard(nextUserCards, room.getNextCardType());
                            userInfo.decrementChances();
                            LinkedHashMap<String,Object> data = new LinkedHashMap<>();
                            data.put("from",nextChanceUserId);
                            data.put("message",nextCard);
                            data.put("type","message");
                            WebSocketMessage webSocketMessage = new WebSocketMessage(nextChanceUserId, MSG_SUBMIT_CARD, data);
                            submitCard(webSocketMessage);
                        }

                    };


                    room.setTimerTask(action,15000);
                    break;
                case MSG_TYPE_LEAVE:
                    // message data contains connected room id
                    leaveGame(session);
                    break;
                default:
                    logger.debug("[ws] Type of the received message {} is undefined!", message.getType());
                    // handle this if needed
            }

        } catch (IOException e) {
            logger.debug("An error occured: {}", e.getMessage());
        }
    }
    private void sendInitialGameStartDetailsToAllUsers(Room room,ArrayList<HashMap<String, Object>> playersInfo) {

        Map<String, UserInfo> userInfo = roomService.getClients(room);
        for(Map.Entry<String, UserInfo> client : userInfo.entrySet())  {
            StartGameResponse startGameResponse = new StartGameResponse(room, client.getKey(), playersInfo);
            sendMessage(client.getValue().getWebSocketSession(),
                    new WebSocketMessage(
                            "Server",
                            MSG_START_GAME,
                            startGameResponse),room);
        }

    }

    private void sendDealCardDetailToAllUsers(Room room) {
        Map<String, UserInfo> userInfo = roomService.getClients(room);
        for(Map.Entry<String, UserInfo> client : userInfo.entrySet())  {
            DealResponse dealResponse = new DealResponse(room,client.getKey(),
                    room.getTransaction().get(room.getTransaction().size()-1));
            sendMessage(client.getValue().getWebSocketSession(),
                    new WebSocketMessage(
                            "Server",
                            MSG_SUBMIT_CARD,
                            dealResponse),room);
        }

    }

    private void sendMessageToAll(String type, Room room,Object data) {

        Map<String, UserInfo> userInfo = roomService.getClients(room);
        for(Map.Entry<String, UserInfo> client : userInfo.entrySet())  {
            // send messages to all clients except current user
            sendMessage(client.getValue().getWebSocketSession(),
                    new WebSocketMessage(
                            "Server",
                            type,
                            data),room);
        }

    }


    private void sendMessage(WebSocketSession session, WebSocketMessage message,Room room) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            logger.debug("An error occured: {}", e.getMessage());
            // remove the client which leaves from the Room clients list
//            Optional<String> client = roomService.getClients(room).entrySet().stream()
//                    .filter(entry -> Objects.equals(entry.getValue().getWebSocketSession().getId(), session.getId()))
//                    .map(Map.Entry::getKey)
//                    .findAny();
//            client.ifPresent(c -> roomService.removeClientByName(room, c));
        }
    }


    public void leaveGame(final WebSocketSession session){
        String sessionId = session.getId();
        logger.debug("[ws] {} is going to leave Room", sessionId);
        // room id taken by session id

        WeakReference<String> userIdWeakReference = sessionIdToUserIdMap.get(sessionId);
        if(userIdWeakReference!=null){
            String userId = userIdWeakReference.get();
            Room room = userIdToRoomMap.get(userId).get();
            room.updateLeftUser(userId);
            if(!room.isGameInProgress()){
                // remove the client which leaves from the Room clients list
                Optional<String> clientOpty = roomService.getClients(room).entrySet().stream()
                        .filter(entry -> Objects.equals(entry.getValue().getUserId(), userId))
                        .map(Map.Entry::getKey)
                        .findAny();

                clientOpty.ifPresent(c -> roomService.removeClientByName(room, c));
                userIdToRoomMap.remove(userId);
                sessionIdToUserIdMap.remove(sessionId);
                if(!room.getClients().isEmpty()){
                    this.sendMessageToAll("leave",room,userId);
                }
            }

        }



    }

    public void submitCard(WebSocketMessage message){

        Room room = userIdToRoomMap.get(message.getFrom()).get();
        room.setLastDealUser(message.getFrom());

        LinkedHashMap<String,Object> fromUserData = (LinkedHashMap<String, Object>) message.getData();
        String card = (String)fromUserData.get("message");
        LinkedHashMap<String, String> currentTransaction = room.updateTransaction(message.getFrom(),(String)fromUserData.get("message"));
        room.findAndUpdateNextPlayer(currentTransaction);
        room.removeCardFromUser(message.getFrom(),card);
        room.updateWinners();
        this.sendDealCardDetailToAllUsers(room);
        String nextUser = room.getNextChanceUser();
        LinkedHashMap<String,List<String>> nextUserCards = room.getUserCardInfo().get(nextUser);
        if(room.isGameInProgress()) {
            UserInfo userInfo = room.getClients().get(nextUser);
            TimerTask action = new TimerTask() {
                public void run() {
                    userInfo.decrementChances();
                    String nextCard = Bot.pickCard(nextUserCards, room.getNextCardType());
                    LinkedHashMap<String,Object> data = new LinkedHashMap<>();
                    data.put("from",nextUser);
                    data.put("message",nextCard);
                    data.put("type","message");
                    WebSocketMessage webSocketMessage = new WebSocketMessage(nextUser, MSG_SUBMIT_CARD, data);
                    submitCard(webSocketMessage);
                }

            };
            if(userInfo.isChanceOver() || room.isLeftUser(nextUser)){
                room.setTimerTask(action,2000);
            }else{
                room.setTimerTask(action,15000);
            }

        }else{
            room.clear();
        }
    }


    public static List<List<String>> divideIntoSubArrays(int bucketSize, String[] array) {
        float noOfBuckets = array.length / (float) bucketSize;

        List<List<String>> result = new ArrayList<>();
        String[] extraCards = new String[bucketSize];
        for (int currentBucket = 0; currentBucket < noOfBuckets; currentBucket++) {
            int fromRange = (int) Math.ceil(currentBucket * bucketSize);
            int toRange = (int) Math.ceil(currentBucket * bucketSize + bucketSize);
            if(toRange>array.length){
                extraCards = Arrays.copyOfRange(array,fromRange,toRange);
            }else{
                result.add(Arrays.stream(Arrays.copyOfRange(array,fromRange,toRange))
                        .collect(Collectors.toCollection(ArrayList::new)));
            }

        }
        int cardIndex = 0;
        for (String card : extraCards) {
            if(card!=null){
                result.get(cardIndex).add(card);
                cardIndex++;
            }

        }

        return result;
    }

}
