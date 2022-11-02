package com.donkeyking.service;

import com.donkeyking.service.util.CardUtil;
import org.junit.Assert;
import org.junit.Test;


public class CardUtilTest {

    @Test
    public void getPriorityTest(){
        Assert.assertEquals(CardUtil.getCardPriority("9C"),9);
        Assert.assertEquals(CardUtil.getCardPriority("TC"),10);
        Assert.assertEquals(CardUtil.getCardPriority("JC"),11);
        Assert.assertEquals(CardUtil.getCardPriority("QC"),12);
        Assert.assertEquals(CardUtil.getCardPriority("KC"),13);
        Assert.assertEquals(CardUtil.getCardPriority("AC"),15);
    }

}
