package com.company;

import java.util.concurrent.ConcurrentSkipListSet;

public class Node {

    private int nodeID;
    private String hostName;
    private int port;
    private double x, y;
    private ConcurrentSkipListSet<Integer> links;

    public Node(int nodeID, String hostName, int port, double x, double y) {
        this.nodeID = nodeID;
        this.hostName = hostName;
        this.port = port;
        this.x = x;
        this.y = y;
        this.links = new ConcurrentSkipListSet<Integer>();
    }

    

}
