package com.donkeyking.service.domain;

import com.donkeyking.service.type.CardType;
import com.donkeyking.service.util.CardUtil;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Room {
    @NotNull private final String id;

    @NotNull private String sessionId;

    private Map<String, UserInfo> clients = new ConcurrentHashMap<>();

    private boolean isGameInProgress = false;

    private String lastDealUser;

    private String nextChanceUser;

    private boolean isResetDeck;

    private CardType nextCardType;

    private Timer timer;

    private final Set<String> usersLeft = new HashSet<>();

    private LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();

    private final List<LinkedHashMap<String,String>> transactions = new ArrayList<>();

    private Map<String,LinkedHashMap<String,List<String>>> userCardInfo = new HashMap<>();

    private LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();


    public boolean isResetDeck() {
        return isResetDeck;
    }

    public void setResetDeck(boolean resetDeck) {
        isResetDeck = resetDeck;
    }

    public Room(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Map<String, UserInfo> getClients() {
        return clients;
    }

    public void setClients(Map<String, UserInfo>  clients) {
        this.clients = clients;
    }

    public void clear(){
        this.init();
        this.clients.clear();
        this.timer.cancel();

    }

    public void init(){
        this.lastDealUser = null;
        this.nextChanceUser = null;
        this.orderInfo.clear();
        this.userCardInfo.clear();
        this.transactions.clear();
        this.winners.clear();
        this.usersLeft.clear();
        this.sessionId =  UUID.randomUUID().toString();

    }

    public List<HashMap<String,String>> getClientsForPublic(){
        List<HashMap<String,String>> clientList = new ArrayList<>();
       for(Map.Entry<String,UserInfo> entry: clients.entrySet()){
           String userId = entry.getKey();
           UserInfo userInfo = entry.getValue();
           HashMap<String,String> map = new HashMap<>();
           map.put("userId",userId);
           map.put("nickName",userInfo.getNickName());
           clientList.add(map);
        }
        return clientList;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Room room = (Room) o;
        return Objects.equals(getId(), room.getId()) &&
                Objects.equals(getClients(), room.getClients());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getClients());
    }

    public boolean isGameInProgress() {
        return isGameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        isGameInProgress = gameInProgress;
    }

    public void setLastDealUser(String lastDealUser) {
        this.lastDealUser = lastDealUser;
    }

    public String getLastDealUser() {
        return lastDealUser;
    }

    public synchronized void setTimerTask(TimerTask action,int time) {
        if(this.timer!=null){
            this.timer.cancel();
        }
        this.timer = new Timer();



        this.timer.schedule(action, time); //this starts the task
    }

    public boolean updateLeftUser(String userId){
        return this.usersLeft.add(userId);
    }

    public boolean isLeftUser(String userId){
        return this.usersLeft.contains(userId);
    }

    public void updateUserCardInfo(String userId , List<String> cards){

        this.userCardInfo.put(userId, CardUtil.sortAndGroupCards(cards));
    }

    public void updateUserCardInfo(String userId , LinkedHashMap<String,List<String>> cards){
        this.userCardInfo.put(userId,cards);
    }
    public void removeCardFromUser(String userId, String card){
        String cardType = CardUtil.getCardType(card);
        LinkedHashMap<String,List<String>> userCardInfo = this.userCardInfo.get(userId);
        List<String> listOfCardByType = userCardInfo.getOrDefault(cardType,new ArrayList<>());
        listOfCardByType.remove(card);
        userCardInfo.put(cardType,listOfCardByType);
        this.userCardInfo.put(userId,userCardInfo);
    }

    public void addHitCardsToUser(AbstractMap.SimpleEntry<String, List<String>> hit){
        if(hit==null){
            return;
        }
        String userId = hit.getKey();

        List<String> cardListToInsert = hit.getValue();
        if(cardListToInsert!=null){
            for(String cardToInsert : cardListToInsert){
                String cardType = CardUtil.getCardType(cardToInsert);
                List<String> cardList = userCardInfo.get(userId).getOrDefault(cardType,new ArrayList<>());
                CardUtil.insertCardInOrder(cardList,cardToInsert);
                userCardInfo.get(userId).put(cardType,cardList);
            }
        }



    }

    public Map<String, LinkedHashMap<String,List<String>>> getUserCardInfo() {
        return userCardInfo;
    }


    public void setOrderInfo(LinkedHashMap<String, Integer> orderInfo) {
        this.orderInfo = orderInfo;
    }

    public LinkedHashMap<String, Integer> getOrderInfo() {
        return orderInfo;
    }

    public LinkedHashMap<String, String> updateTransaction(String userId,String cardShortName) {

        LinkedHashMap<String, String> lastTransaction = null;
        if(transactions.size()>0){
            lastTransaction = this.transactions.get(transactions.size()-1);
        }

        if(lastTransaction==null||
                (lastTransaction!=null && (lastTransaction.size()+winners.size())==getClients().size())||
                (lastTransaction!=null && lastTransaction.get(userId)!=null)){
            lastTransaction = new LinkedHashMap<>();
            this.transactions.add(lastTransaction);
        }

        lastTransaction.put(userId,cardShortName);
        return lastTransaction;

    }

    public List<LinkedHashMap<String, String>> getTransaction() {
        return transactions;
    }

    public LinkedHashMap<String,Integer> getUsersWon() {
        return winners;
    }
    public void setUsersWon(String userWon,Integer rank) {
        this.winners.put(userWon,rank);
    }

    public void setUsersWon(LinkedHashMap<String,Integer> winners) {
        this.winners = winners;
    }

    public void findAndUpdateNextPlayer(LinkedHashMap<String, String> transaction) {
        long noOfCardTypesInTrans = transaction.values().stream().map(cardName->CardUtil.getCardType(cardName)).distinct().count();
        boolean isHit = false;
        if(noOfCardTypesInTrans > 1){
            isHit = true;
        }
        Map.Entry<String, String> lastUser = transaction.entrySet().stream().skip(transaction.size() - 1).findFirst().get();

        if(isHit){
            LinkedHashMap<String,String> transWithoutPrvsUser = (LinkedHashMap<String,String>)transaction.clone();
            transWithoutPrvsUser.remove(lastUser.getKey());
            ArrayList<String> hitCards = new ArrayList<>(transaction.values());
            Map.Entry<String,String> user = Collections.max(transWithoutPrvsUser.entrySet(), (entry1, entry2) -> CardUtil.getCardPriority(entry1.getValue()) - CardUtil.getCardPriority(entry2.getValue()));
            this.addHitCardsToUser(new AbstractMap.SimpleEntry<>(user.getKey(), hitCards));
            this.setNextChanceUser(user.getKey());
            this.setResetDeck(true);
            this.setNextCardType("any");
        }else {
            if(transaction.size()+this.winners.size() == this.clients.size()){
                Map.Entry<String,String> user = Collections.max(transaction.entrySet(), (entry1, entry2) -> CardUtil.getCardPriority(entry1.getValue()) - CardUtil.getCardPriority(entry2.getValue()));
                this.setNextChanceUser(user.getKey());
                this.setResetDeck(true);
                this.setNextCardType("any");
            }else{
                int lastUserIndex = this.orderInfo.get(lastUser.getKey());
                int nextUserIndexTmp = lastUserIndex+1;
                int noPlayers = this.orderInfo.size();
                LinkedHashMap<Integer, String> orderInfoByPosition = this.orderInfo.entrySet().stream().collect( LinkedHashMap::new,                           // Supplier LinkedHashMap to keep the order
                        (map, item) -> map.put(item.getValue(), item.getKey()),
                        Map::putAll);
                String nextUserId = null;
                while(lastUserIndex!=nextUserIndexTmp ){
                    if(nextUserIndexTmp>=this.clients.size()){
                        nextUserIndexTmp = 0;
                    }
                    if(this.winners.get(orderInfoByPosition.get(nextUserIndexTmp))!=null){
                        nextUserIndexTmp++;
                        continue;
                    }else{
                        nextUserId =  orderInfoByPosition.get(nextUserIndexTmp);
                        break;
                    }

                }

                String nextCardType = CardUtil.getCardType(transaction.get(this.lastDealUser));
                this.setNextCardType(nextCardType);
                this.setResetDeck(false);
                this.setNextChanceUser(nextUserId);


            }

        }

    }

    public void updateWinners(){
        for(Map.Entry<String,LinkedHashMap<String,List<String>>> entry : userCardInfo.entrySet()){
            String userId = entry.getKey();
            UserInfo userInfo =getClients().get(userId);
            if(userInfo.isChanceOver()){
                usersLeft.add(userId);
            }
            LinkedHashMap<String,List<String>> userCardGroupedByCardType = entry.getValue();
            long remCardTypeByCount = userCardGroupedByCardType.entrySet().stream().filter(e -> e.getValue().size()>0).count();
            //boolean isAllPlayersSubmitted = ((transaction.size() + this.winners.size()) == this.clients.size());
            if(remCardTypeByCount==0){
                if(winners.get(userId)==null){
                    winners.put(userId,winners.size()+1);
                }
            }
        }
        if(winners.size() == clients.size()-1){
            setGameInProgress(false);
        }
        if(usersLeft.size() == clients.size()){
            setGameInProgress(false);
        }


    }

    public String getNextChanceUser() {
        return nextChanceUser;
    }

    public void setNextChanceUser(String nextChanceUser) {
        this.nextChanceUser = nextChanceUser;
    }

    public void setUserCardInfo(Map<String, LinkedHashMap<String, List<String>>> userCardInfo) {
        this.userCardInfo = userCardInfo;
    }

    public Set<String> getUsersLeft() {
        return usersLeft;
    }

    public String getSessionId() {
        return sessionId;
    }

    public CardType getNextCardType() {
        return nextCardType;
    }

    public void setNextCardType(String nextCardType) {
        this.nextCardType = CardType.getCardType(nextCardType);
    }
}
