package com.company;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class Vehicle {

    public static final String FILE_NAME = "config.txt";
    public static final int SERVER_PORT = 20120;

    protected boolean SET_BROADCAST;

    protected double length, width;

    protected GPS gps;
    protected double velocity, acceleration;
    protected long timeStamp;

    protected String hostName;
    protected int serverPort;
    protected int packetSequenceNumber;
    protected int topologySequenceNumber;

    protected int nodeID;
    protected ConcurrentSkipListMap<Integer, Node> nodesTopology;
    protected ConcurrentLinkedQueue<Packet> forwardQueue;



    public Vehicle (int nodeID) {
        this.nodeID = nodeID;
        gps = new GPS();
        timeStamp = System.currentTimeMillis();

        packetSequenceNumber = 0;
        topologySequenceNumber = 0;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            if (hostName.indexOf(".") > -1)     hostName = hostName.substring((0, hostName.indexOf('.')));
            if (hostName.substring(0, 3).equals("tux"))     this.SET_BROADCAST = true;
            else    this.SET_BROADCAST = false;
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (SET_BROADCAST)      serverPort = SERVER_PORT;
        else    serverPort = SERVER_PORT + nodeID;


    }





}
