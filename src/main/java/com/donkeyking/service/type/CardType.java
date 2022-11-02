package com.donkeyking.service.type;

public enum CardType {
    DIAMONDS("spades"),
    SPADES("diamonds"),
    CLUBS("clubs"),
    HEARTS("hearts"),
    ANY("any");

    private String value;

    CardType(String cardType) {
        this.value = cardType;
    }

    public String getValue() {
        return value;
    }

    public static CardType getCardType(String cardTypeStr) {
        CardType cardType = ANY;
        if(CardType.CLUBS.value.equals(cardTypeStr)){
            cardType = CardType.CLUBS;
        }
        else if(CardType.HEARTS.value.equals(cardTypeStr)){
            cardType = CardType.HEARTS;
        }
        else if(CardType.SPADES.value.equals(cardTypeStr)){
            cardType = CardType.SPADES;
        }
        else if(CardType.DIAMONDS.value.equals(cardTypeStr)){
            cardType = CardType.DIAMONDS;
        }
        return cardType;
    }
}
