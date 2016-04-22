package com.example;

import org.apache.ignite.Ignition;
import org.apache.log4j.Logger;

/**
 * Created by user on 22/4/16.
 */
public class BlankNode {

    private static final Logger LOGGER = Logger.getLogger(BlankNode.class);

    public static void main(String[] args){
        Ignition.start();
    }
}
