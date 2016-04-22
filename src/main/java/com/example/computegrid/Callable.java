package com.example.computegrid;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by user on 22/4/16.
 */
public class Callable implements IgniteCallable<Integer> {

    private static final Logger LOGGER = Logger.getLogger(Callable.class);
    private final String input;
    private static final int TEST_DATA_SIZE = 10;

    public Callable(String input) {
        this.input = input;
    }

    @Override
    public Integer call() throws Exception {
        LOGGER.info("processing input: " + input);
        Thread.sleep((int)(Math.random() * 500));   // to simulate time taken in ptocessing
        return input.length();
    }

    public static void main(String[] args){
        Ignite ignite = Ignition.start();
        List<String> testData = generateData(TEST_DATA_SIZE);
        List<IgniteCallable<Integer>> calls = testData.stream().map(s -> new Callable(s)).collect(Collectors.toList());
        int totalCount = ignite.compute().call(calls).stream().mapToInt(i->i).sum();
        LOGGER.info("total character count: " + totalCount);
        ignite.close();
    }

    private static List<String> generateData(int size) {
        List<String> out = new ArrayList<>(size);
        for(int i=0; i<size; i++){
            out.add(RandomStringUtils.randomAlphanumeric(3));
        }
        return out;
    }
}
