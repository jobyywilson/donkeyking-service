package com.donkeyking.service.util;

import com.donkeyking.service.domain.Room;

import java.util.*;
import java.util.stream.Collectors;

public class CardUtil {


    public static final Map<String, String> SUITS_SHORT_TO_FULL;
    public static final Map<String, String> SUITS_FULL_TO_SHORT;

    public static Map<String, String> CARD_SHORT_TO_FULL;
    public static Map<String, Integer> CARD_PRIORITY;

    public static Map<String, String> CARD_FULL_TO_SHORT;
    public static String DEFAULT = "DEFAULT";

    static {
        SUITS_SHORT_TO_FULL = new HashMap<>();
        SUITS_SHORT_TO_FULL.put("S", "spades");
        SUITS_SHORT_TO_FULL.put("D", "diamonds");
        SUITS_SHORT_TO_FULL.put("H", "hearts");
        SUITS_SHORT_TO_FULL.put("C", "clubs");

        SUITS_FULL_TO_SHORT = new HashMap<>();
        SUITS_FULL_TO_SHORT.put("spades", "S");
        SUITS_FULL_TO_SHORT.put("diamonds", "D");
        SUITS_FULL_TO_SHORT.put("hearts", "H");
        SUITS_FULL_TO_SHORT.put("clubs", "C");

        CARD_SHORT_TO_FULL = new HashMap<>();
        CARD_SHORT_TO_FULL.put("A", "ace");
        CARD_SHORT_TO_FULL.put("J", "jack");
        CARD_SHORT_TO_FULL.put("K", "king");
        CARD_SHORT_TO_FULL.put("Q", "queen");

        CARD_PRIORITY = new HashMap<>();
        CARD_PRIORITY.put("A", 15);
        CARD_PRIORITY.put("T", 10);
        CARD_PRIORITY.put("J", 11);
        CARD_PRIORITY.put("K", 13);
        CARD_PRIORITY.put("Q", 12);

        CARD_FULL_TO_SHORT = new HashMap<>();
        CARD_FULL_TO_SHORT.put("ace", "A");
        CARD_FULL_TO_SHORT.put("jack", "J");
        CARD_FULL_TO_SHORT.put("king", "K");
        CARD_FULL_TO_SHORT.put("queen", "Q");

    }

    static public String getCardType(String cardName) {
        if (cardName != null && cardName.length() >= 2 && cardName != "back-gray.png") {
            String cardShortName = String.valueOf(cardName.charAt(cardName.length() - 1));

            if (SUITS_SHORT_TO_FULL.get(cardShortName) != null) {
                return SUITS_SHORT_TO_FULL.get(cardShortName);
            }
        }
        return DEFAULT;
    }


    //
    public static int getCardPriority(String cardName) {
        if (cardName != null && cardName.length() >= 2 ) {
            String charNumStr = String.valueOf(cardName.charAt(0));
            int cardNum = 0;
            if (CARD_PRIORITY.get(charNumStr) != null) {
                cardNum = CARD_PRIORITY.get(charNumStr);
            }else{
                cardNum = Character.getNumericValue(cardName.charAt(0));
            }
            if (cardName.length() == 3) {
                cardNum = Integer.parseInt(cardName.substring(0, 2));
            }

            return cardNum;

        }
        return 0;
    }

    public static LinkedHashMap<String, List<String>> sortAndGroupCards(List<String> cardNameList) {
        Map<String, List<String>> groupedCards =  cardNameList.stream()
                .collect(Collectors.groupingBy(CardUtil::getCardType));
        LinkedHashMap<String, List<String>> sortedCards = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : groupedCards.entrySet()) {
            List<String> sortedList = entry.getValue().stream().sorted(Comparator.comparingInt(CardUtil::getCardPriority)).collect(Collectors.toList());
            sortedCards.put(entry.getKey(), sortedList);
        }
        return sortedCards;
    }
    public static List<String> insertCardInOrder(List<String> cardNameList, List<String> cardNameToInsertList) {
        cardNameList.addAll(cardNameToInsertList);
        return cardNameList.stream().distinct().sorted(Comparator.comparingInt(CardUtil::getCardPriority)).collect(Collectors.toList());
    }

     public static List<String> insertCardInOrder(List<String> cardNameList, String cardNameToInsert) {
         cardNameList.add(cardNameToInsert);
         return cardNameList.stream().distinct().sorted(Comparator.comparingInt(CardUtil::getCardPriority)).collect(Collectors.toList());

     }

    Integer getFirstRightPlayerWithCard(int currentIndex,ArrayList<HashMap<String,Object>> playersInfo){
        return getFirstRightPlayerWithCard(currentIndex,playersInfo,false);
    }
    Integer getFirstRightPlayerWithCard(int currentIndex,ArrayList<HashMap<String,Object>> playersInfo,boolean foundCurrentIndex) {
        for (HashMap<String, Object> player : playersInfo) {

            Integer noOfCards = (Integer) player.get("noOfCards");
            Integer index = (Integer) player.get("index");
            if (foundCurrentIndex && noOfCards > 0) {
                return index;
            }
            if (index == currentIndex) {
                foundCurrentIndex = true;
            }

        }
        if (foundCurrentIndex) {
            return this.getFirstRightPlayerWithCard(0, playersInfo, foundCurrentIndex);
        } else {
            return this.getFirstRightPlayerWithCard(0, playersInfo);
        }
    }

    Integer getPreviousCardIndex(Integer currentIndex,List<HashMap<String,String>> bendCards,ArrayList<HashMap<String,Object>> playersInfo){
        int index = currentIndex -1;
        while (index!=currentIndex) {
            if(index== -1){
                index = bendCards.size() -1;
            }
            HashMap<String,Object> player = null;
            for(HashMap<String,Object> playerObj : playersInfo){
                if(((Integer)playerObj.get("index"))==index){

                    player = playerObj;
                    break;
                }
            }

            if(player!=null &&((Integer)player.get("noOfCards"))!=-1){
                return index;
            }
            index--;

        }
        return index;
    }

    public static String getNextUser(String currentUserId , Room room , LinkedHashMap<String, String> currentTransaction){
        LinkedHashMap<String, Integer> orderInfo = room.getOrderInfo();
        List<String> users = new ArrayList<>(orderInfo.keySet());
        int previousUserIndex = orderInfo.get(currentUserId);
        int nextUserIndex = orderInfo.get(currentUserId) +1;
        int smallestPriority = getCardPriority(currentTransaction.get(currentUserId));
        String smallestPriorityUserId = currentUserId;
        while(previousUserIndex!=nextUserIndex){
            if(users.size() > nextUserIndex && room.getUsersWon().get(users.get(nextUserIndex))==null) {
                int priority = getCardPriority(currentTransaction.get(users.get(nextUserIndex)));
                if (priority > smallestPriority) {
                    smallestPriority = priority;
                    smallestPriorityUserId = users.get(nextUserIndex);
                }
                if (currentTransaction.get(users.get(nextUserIndex)) == null) {
                    return users.get(nextUserIndex);
                }

            }
            if(users.size() <= nextUserIndex){
                nextUserIndex = 0;
            }else{
                nextUserIndex =nextUserIndex+1;
            }
        }
        return smallestPriorityUserId;
    }

    HashMap<String,Boolean> validateCard(String myBendCard,Integer nextPlayerIndex,List<HashMap<String,String>> bendCards,ArrayList<HashMap<String,Object>> playersInfo,LinkedHashMap<String,List<String>> myCards){
        //Check if is of different type
        HashMap<String,Boolean> status = new HashMap<>();
        String currentCardType = getCardType(myBendCard);
        Integer previousPlayerIndex = getPreviousCardIndex(nextPlayerIndex,bendCards,playersInfo);
        String previousBendCard = bendCards.get(previousPlayerIndex).get("bendCard");
        String previousBendCardType =  getCardType(previousBendCard);
        if(currentCardType != "DEFAULT" && previousBendCardType !="DEFAULT" && currentCardType != previousBendCardType){
            if(myCards.get(previousBendCardType)!=null && myCards.get(previousBendCardType).size()!=0){
                System.out.println("Please choose card of same suit!!");
            }

            // for(let card of this.mycards){
            //   if(myBendCard != card && previousBendCardType == this.cardService.getCardType(card)){
            //     this.showError ("Please choose card of same suit!!");
            //     throw new Error("Please choose card of same suit!!")
            //   }
            // }
            status.put("attack",true);
            return status;
        }
        status.put("attack",false);
        return status;
    }
        void autoDeal(String bendCard,LinkedHashMap<String,Object> fromUserData,HashMap<String,Integer> orderInfo,String currentUserId,List<HashMap<String,String>> bendCards,ArrayList<HashMap<String,Object>> playersInfo,LinkedHashMap<String,List<String>> myCards) {
            Integer nextPlayerIndex = (Integer) fromUserData.get("nextPlayerIndex");

            if (nextPlayerIndex != orderInfo.get(currentUserId)) {
                //this.showError ("Not your chance , next player is "+this.nextPlayerIndex);
                return;
            }
            String bendCardType = getCardType(bendCard);

            HashMap<String, Boolean> validateResult = validateCard(bendCard, nextPlayerIndex, bendCards, playersInfo, myCards);
            for (HashMap<String, String> card : bendCards) {
                if (card.get("userId").equals(currentUserId)) {
                    card.put("bendCard", bendCard);
                }
            }
            for (Map.Entry<String, List<String>> card : myCards.entrySet()) {
                if (card.getKey().equals(bendCardType)) {
                    List<String> myCardType = card.getValue().stream().filter((c) -> c.equals(bendCard)).collect(Collectors.toList());
                    myCards.put(bendCardType, myCardType);
                }
            }

      /*
        TODO Validate the user cards
      */
//
//            let resetBendCards = false;
//            let nextPlayerPosition = 0;
//            let isAllPlayerSubmitted =  this.checkAllPlayersSubmitted()
//            let attackedCardsInfo :any= {}
//            if(isAllPlayerSubmitted || validateResult.attack){
//                if(validateResult.attack){
//                    nextPlayerPosition =  this.findLowestCardIndex(this.nextPlayerIndex)
//                    attackedCardsInfo["index"]= nextPlayerPosition
//                    attackedCardsInfo["attackedCards"] = this.getAttackedCards()
//                }else{
//                    nextPlayerPosition =  this.findLowestCardIndex()
//                }
//                resetBendCards = true;
//            }else{
//                nextPlayerPosition = this.getFirstRightPlayerWithCard(this.nextPlayerIndex)
//            }
//            this.updatePlayerCardCount(attackedCardsInfo);
//            let data = {
//                    type:"message",
//                    message:bendCard,
//                    from:this.currentUserId,
//                    nextPlayerIndex:nextPlayerPosition,
//                    isReset:resetBendCards,
//                    attackedCardsInfo:attackedCardsInfo,
//                    playersInfo: this.playersInfo,
//                    cards:this.mycards
//      }
//            this.sendToPeer.emit(data);
//
//            if(resetBendCards){
//                await this.delay(1000);
//                this.trashCards(attackedCardsInfo)
//                this.fillBlankCards();
//                this.filterOutWinners()
//            }
//            this.nextPlayerIndex = nextPlayerPosition;
//            this.loadMyDeck();


//    }
//    boolean checkAllPlayersSubmitted(List<HashMap<String,String>> bendCards){
//        boolean isSubmitted = true;
//        for(let card of this.bendCards){
//            let noOfCards = this.getNoOfCardOfPlayer(card)
//            if(card["bendCard"] == "back-gray.png" && noOfCards !=-1){
//                isSubmitted = false;
//            }
//        }
//        return isSubmitted;
//    }
//    getNoOfCardOfPlayer(ArrayList<HashMap<String,Object>> playersInfo){
//        for(let player of this.playersInfo){
//            if(player["index"] == this.orderInfo[card["userId"]]){
//                return player["noOfCards"]
//            }
//        }
//    }
        }
}


