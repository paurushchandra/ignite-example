package com.example.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.log4j.Logger;

import java.lang.management.ManagementFactory;

/**
 * Created by user on 22/4/16.
 */
public class Runnable implements IgniteRunnable {
    private static final Logger LOGGER = Logger.getLogger(Runnable.class);

    private String processId;

    public Runnable() {
        this.processId = ManagementFactory.getRuntimeMXBean().getName();
    }

    @Override
    public void run() {
        String currentProcessId = ManagementFactory.getRuntimeMXBean().getName();
        LOGGER.info(String.format("process sent from: %s and is running on: &s ", processId, currentProcessId));
    }

    public static void main(String[] agrs) {
//        Ignition.setClientMode(true);
        Ignite ignite = Ignition.start();
        ignite.compute().broadcast(new Runnable()); // this will run on all nodes
        ignite.compute().run(new Runnable());   //this will run on only one node of the cluster
        ignite.close();
    }
}
