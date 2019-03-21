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

    public void setLinks(ConcurrentSkipListSet<Integer> neighborSet) {
        this.links = neighborSet;
    }

    public int getNodeID() {
        return nodeID;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPortNum() {
        return port;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public GPS getGPS() {
        return new GPS(x, y);
    }

    public void setGPS(GPS gps) {
        x = gps.getX();
        y = gps.getY();
    }

    //wa~
    synchronized public ConcurrentSkipListSet<Integer> getLinks() {
        return links;
    }

    public void addLink(int linkId) {
        links.add(linkId);
    }

    public boolean removeLink(int linkId) {
        return links.remove((Integer)linkId);
    }

    @Override
    synchronized public String toString() {
        StringBuffer sb = new StringBuffer("Node: ");
        sb.append(getNodeID() + " " + getHostName() + ", " + getPortNum() + " ");
        sb.append(String.format("%.1f % .1f links", getX(), getY()));
        for (int link:getLinks()) {
            sb.append(" ");
            sb.append(link);
        }
        return sb.toString();
    }

    public boolean equals(Node node) {
        return nodeID == node.nodeID;
    }






}
