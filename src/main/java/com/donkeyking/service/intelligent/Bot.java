package com.donkeyking.service.intelligent;

import com.donkeyking.service.type.CardType;
import com.donkeyking.service.util.CardUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class Bot {

    static Random random = new Random();

    public static String pickCard(Map<String, List<String>> myCards, CardType cardType){
        List<String> filterCardBytType = null;
        if(!ObjectUtils.isEmpty(cardType)){
            filterCardBytType = myCards.get(cardType.getValue());
        }

        List<String> nextCardList = null;

        if(CardType.ANY == cardType || CollectionUtils.isEmpty(filterCardBytType)){

            List<String> cardTypes = myCards.entrySet().stream().filter(entry->!entry.getValue().isEmpty()).map(entry->entry.getKey()).collect(Collectors.toList());
            nextCardList = myCards.get(cardTypes.get(random.nextInt(cardTypes.size())));

        }else{
            nextCardList = filterCardBytType;
        }
        return Collections.max(nextCardList, Comparator.comparingInt(CardUtil::getCardPriority));

    }

}
