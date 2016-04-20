package com.example;

import com.example.model.*;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by user on 19/4/16.
 */
public class TestUtil {

    private static final Random random = new Random();

    public static Trade newRandomTrade(){
        Trade trade = new Trade();
        trade.setPrice(Math.random());
        trade.setAmount(Math.random() * 1000);
        trade.setQuantity(random.nextInt());
        trade.setSedol(RandomStringUtils.randomAlphanumeric(5));
        trade.setExecutionDate(Calendar.getInstance());
        trade.setSettlementDate(Calendar.getInstance());
        trade.setSide(Side.values()[random.nextInt(Side.values().length)]);
        trade.setStrategy(Strategy.values()[random.nextInt(Strategy.values().length)]);
        return trade;
    }

    public static Map<Integer, Trade> generateTestData(int size){
        Map<Integer, Trade> tradeMap = new HashMap<>(size);
        for(int i=0; i<size; i++){
            tradeMap.put(i, newRandomTrade());
        }
        return tradeMap;
    }

    public static SecurityDetail newSecurityDetail(String sedol){
        SecurityDetail securityDetail = new SecurityDetail();
        securityDetail.setSedol(sedol);
        securityDetail.setSector(Sector.values()[random.nextInt(Sector.values().length)]);
        return securityDetail;
    }

    public static Map<Integer, SecurityDetail> generateTestData(Map<Integer, Trade> trades){
        Map<Integer, SecurityDetail> securityDetailHashMap = new HashMap<>(trades.size());
        int i=0;
        for(Trade trade: trades.values()){
            securityDetailHashMap.put(i++, newSecurityDetail(trade.getSedol()));
        }
        return securityDetailHashMap;
    }

}
