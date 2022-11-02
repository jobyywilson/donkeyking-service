package com.donkeyking.service;

import com.donkeyking.service.domain.Room;
import com.donkeyking.service.domain.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoomTest {


    Room room ;

    @Before
    public void init(){
        room = new Room("TEST");
    }

    @Test
    public void findNextPlayer1(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        orderInfo.put("userId4",3);
        orderInfo.put("userId5",4);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId3", "userId3", null);
        UserInfo userId4 = new UserInfo("userId4", "userId4", "userId4", null);
        UserInfo userId5 = new UserInfo("userId5", "userId5", "userId5", null);
        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        clients.put("userId4",userId4);
        clients.put("userId5",userId5);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId4","2H");
        currentTransaction.put("userId5","AH");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);

        room.findAndUpdateNextPlayer(currentTransaction);
        Assert.assertEquals("userId1",room.getNextChanceUser());
    }
    @Test
    public void findNextPlayer2(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        orderInfo.put("userId4",3);
        orderInfo.put("userId5",4);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId3", "userId3", null);
        UserInfo userId4 = new UserInfo("userId4", "userId4", "userId4", null);
        UserInfo userId5 = new UserInfo("userId5", "userId5", "userId5", null);
        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        clients.put("userId4",userId4);
        clients.put("userId5",userId5);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId3","2H");
        currentTransaction.put("userId4","AH");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);

        room.findAndUpdateNextPlayer(currentTransaction);
        Assert.assertEquals("userId5",room.getNextChanceUser());
    }
    @Test
    public void findNextPlayer3(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        orderInfo.put("userId4",3);
        orderInfo.put("userId5",4);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId3", "userId3", null);
        UserInfo userId4 = new UserInfo("userId4", "userId4", "userId4", null);
        UserInfo userId5 = new UserInfo("userId5", "userId5", "userId5", null);
        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        clients.put("userId4",userId4);
        clients.put("userId5",userId5);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId1","4H");
        currentTransaction.put("userId2","5H");
        currentTransaction.put("userId3","9H");
        currentTransaction.put("userId4","AH");
        currentTransaction.put("userId5","3H");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);

        room.findAndUpdateNextPlayer(currentTransaction);
        Assert.assertEquals("userId4",room.getNextChanceUser());
    }
    @Test
    public void findNextPlayer5(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        orderInfo.put("userId4",3);
        orderInfo.put("userId5",4);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId3", "userId3", null);
        UserInfo userId4 = new UserInfo("userId4", "userId4", "userId4", null);
        UserInfo userId5 = new UserInfo("userId5", "userId5", "userId5", null);
        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        clients.put("userId4",userId4);
        clients.put("userId5",userId5);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId1","4H");
        currentTransaction.put("userId2","5H");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);

        room.findAndUpdateNextPlayer(currentTransaction);
        Assert.assertEquals("userId3",room.getNextChanceUser());
    }
    @Test
    public void findNextPlayer6(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        orderInfo.put("userId4",3);
        orderInfo.put("userId5",4);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId3", "userId3", null);
        UserInfo userId4 = new UserInfo("userId4", "userId4", "userId4", null);
        UserInfo userId5 = new UserInfo("userId5", "userId5", "userId5", null);
        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        clients.put("userId4",userId4);
        clients.put("userId5",userId5);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId4","4H");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);

        room.findAndUpdateNextPlayer(currentTransaction);
        Assert.assertEquals("userId5",room.getNextChanceUser());
    }
    @Test
    public void findNextPlayer7(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        orderInfo.put("userId4",3);
        orderInfo.put("userId5",4);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId3", "userId3", null);
        UserInfo userId4 = new UserInfo("userId4", "userId4", "userId4", null);
        UserInfo userId5 = new UserInfo("userId5", "userId5", "userId5", null);
        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        clients.put("userId4",userId4);
        clients.put("userId5",userId5);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId1","AH");
        currentTransaction.put("userId2","5H");
        currentTransaction.put("userId3","9H");
        currentTransaction.put("userId4","2H");
        currentTransaction.put("userId5","3H");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);

        room.findAndUpdateNextPlayer(currentTransaction);
        Assert.assertEquals("userId1",room.getNextChanceUser());
    }
    @Test
    public void findNextPlayer8(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId1","AC");
        currentTransaction.put("userId2","3C");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);

        room.findAndUpdateNextPlayer(currentTransaction);
        Assert.assertEquals("userId1",room.getNextChanceUser());
    }
    @Test
    public void findNextPlayer9(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        winners.put("userId2",1);
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId2", "userId2", null);

        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId1","2C");
        currentTransaction.put("userId3","AC");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);

        room.findAndUpdateNextPlayer(currentTransaction);
        Assert.assertEquals("userId3",room.getNextChanceUser());
    }

    @Test
    public void findNextPlayer10(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId2", "userId2", null);

        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        Map<String, LinkedHashMap<String, List<String>>> userCardInfo = new HashMap<>();

        LinkedHashMap<String, List<String>> userId1Cards = new LinkedHashMap<>();
        userId1Cards.put("diamonds",new ArrayList<>());
        userId1Cards.put("clubs",new ArrayList<>());
        userId1Cards.put("hearts",new ArrayList<>());
        userId1Cards.put("spades",new ArrayList<>());
        userCardInfo.put("userId1",userId1Cards);

        LinkedHashMap<String, List<String>> userId2Cards = new LinkedHashMap<>();

        userId2Cards.put("diamonds",new ArrayList<>(Arrays.asList("2D")));
        userId2Cards.put("clubs",new ArrayList<>());
        userId2Cards.put("hearts",new ArrayList<>());
        userId2Cards.put("spades",new ArrayList<>());
        userCardInfo.put("userId2",userId2Cards);

        LinkedHashMap<String, List<String>> userId3Cards = new LinkedHashMap<>();
        userId3Cards.put("diamonds",new ArrayList<>());
        userId3Cards.put("clubs",new ArrayList<>(Arrays.asList("4C")));
        userId3Cards.put("hearts",new ArrayList<>(Arrays.asList("4H")));
        userId3Cards.put("spades",new ArrayList<>(Arrays.asList("3S","5S","6S","7S")));
        userCardInfo.put("userId3",userId3Cards);


        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId1","AH");
        currentTransaction.put("userId2","2H");
        currentTransaction.put("userId3","AD");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);
        room.setUserCardInfo(userCardInfo);
        room.findAndUpdateNextPlayer(currentTransaction);

        Assert.assertEquals("userId1",room.getNextChanceUser());

    }

    @Test
    public void findNextPlayer11(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        winners.put("userId1",1);
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId2", "userId2", null);

        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId2","AH");
        currentTransaction.put("userId3","2H");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);
        room.findAndUpdateNextPlayer(currentTransaction);

        Assert.assertEquals("userId2",room.getNextChanceUser());

    }

    @Test
    public void findNextPlayer12(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId2", "userId2", null);

        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId1","KC");
        currentTransaction.put("userId2","AC");
        currentTransaction.put("userId3","10C");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);
        room.findAndUpdateNextPlayer(currentTransaction);

        Assert.assertEquals("userId2",room.getNextChanceUser());

    }


    @Test
    public void findNextPlayer13(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        orderInfo.put("userId4",3);
        orderInfo.put("userId5",4);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        winners.put("userId5",1);
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId3", "userId3", null);
        UserInfo userId4 = new UserInfo("userId4", "userId4", "userId4", null);
        UserInfo userId5 = new UserInfo("userId5", "userId5", "userId5", null);

        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        clients.put("userId4",userId4);
        clients.put("userId5",userId5);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId4","9C");
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);
        room.findAndUpdateNextPlayer(currentTransaction);

        Assert.assertEquals("userId1",room.getNextChanceUser());

    }

    @Test
    public void updateWinnersTest1(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId2", "userId2", null);
        Map<String, LinkedHashMap<String, List<String>>> userCardInfo = new HashMap<>();

        LinkedHashMap<String, List<String>> userId1Cards = new LinkedHashMap<>();
        userId1Cards.put("diamonds",Arrays.asList());
        userId1Cards.put("clubs",Arrays.asList());
        userId1Cards.put("hearts",Arrays.asList());
        userId1Cards.put("spades",Arrays.asList());
        userCardInfo.put("userId1",userId1Cards);

        LinkedHashMap<String, List<String>> userId2Cards = new LinkedHashMap<>();
        userId2Cards.put("diamonds",Arrays.asList("2D"));
        userId2Cards.put("clubs",Arrays.asList());
        userId2Cards.put("hearts",Arrays.asList());
        userId2Cards.put("spades",Arrays.asList());
        userCardInfo.put("userId2",userId2Cards);

        LinkedHashMap<String, List<String>> userId3Cards = new LinkedHashMap<>();
        userId3Cards.put("diamonds",Arrays.asList());
        userId3Cards.put("clubs",Arrays.asList("4C"));
        userId3Cards.put("hearts",Arrays.asList("4H"));
        userId3Cards.put("spades",Arrays.asList("3S","5S","6S","7S"));
        userCardInfo.put("userId3",userId3Cards);



        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();
        currentTransaction.put("userId1","4H");
        currentTransaction.put("userId3","4C");
        room.setUserCardInfo(userCardInfo);
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);
        room.updateWinners();

        Assert.assertEquals(true,winners.containsKey("userId1"));

    }

    @Test
    public void updateWinnersTest2(){
        LinkedHashMap<String, Integer> orderInfo = new LinkedHashMap<>();
        orderInfo.put("userId1",0);
        orderInfo.put("userId2",1);
        orderInfo.put("userId3",2);
        LinkedHashMap<String,Integer> winners = new LinkedHashMap<>();
        Map<String, UserInfo> clients = new ConcurrentHashMap<>();
        UserInfo userId1 = new UserInfo("userId1", "userId1", "userId1", null);
        UserInfo userId2 = new UserInfo("userId2", "userId2", "userId2", null);
        UserInfo userId3 = new UserInfo("userId3", "userId2", "userId2", null);
        Map<String, LinkedHashMap<String, List<String>>> userCardInfo = new HashMap<>();

        LinkedHashMap<String, List<String>> userId1Cards = new LinkedHashMap<>();
        userId1Cards.put("diamonds",Arrays.asList());
        userId1Cards.put("clubs",Arrays.asList());
        userId1Cards.put("hearts",Arrays.asList());
        userId1Cards.put("spades",Arrays.asList());
        userCardInfo.put("userId1",userId1Cards);

        LinkedHashMap<String, List<String>> userId2Cards = new LinkedHashMap<>();
        userId2Cards.put("diamonds",Arrays.asList());
        userId2Cards.put("clubs",Arrays.asList());
        userId2Cards.put("hearts",Arrays.asList());
        userId2Cards.put("spades",Arrays.asList());
        userCardInfo.put("userId2",userId2Cards);

        LinkedHashMap<String, List<String>> userId3Cards = new LinkedHashMap<>();
        userId3Cards.put("diamonds",Arrays.asList());
        userId3Cards.put("clubs",Arrays.asList());
        userId3Cards.put("hearts",Arrays.asList("4H"));
        userId3Cards.put("spades",Arrays.asList("3S","5S","6S","7S"));
        userCardInfo.put("userId3",userId3Cards);



        clients.put("userId1",userId1);
        clients.put("userId2",userId2);
        clients.put("userId3",userId3);
        LinkedHashMap<String, String> currentTransaction = new LinkedHashMap<>();

        currentTransaction.put("userId3","4C");
        currentTransaction.put("userId2","2C");
        currentTransaction.put("userId1","4H");
        room.setUserCardInfo(userCardInfo);
        room.setOrderInfo(orderInfo);
        room.setUsersWon(winners);
        room.setClients(clients);
        room.updateWinners();

        Assert.assertEquals(true,winners.containsKey("userId1"));
        Assert.assertEquals(true,winners.containsKey("userId2"));

    }
}