package com.company;

import java.io.Serializable;

public class Packet implements Serializable {

    private static final long serialVersionID = -5088013409407823196L;  //??
    private Header header;
    private VehicleInfo vehicleInfo;
    private HelloMessage hello;
    private TCMessage tc;

    public Packet(Header header) {
        this.header = header;
    }

    public Packet(Header header, VehicleInfo vehicleInfo) {
        this.header = header;
        this.vehicleInfo = vehicleInfo;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setVehicleInfo(VehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public void setHello(HelloMessage hello) {
        this.hello = hello;
    }

    public void setTc(TCMessage tc) {
        this.tc = tc;
    }




    public Header getHeader() {
        return header;
    }

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public HelloMessage getHello() {
        return hello;
    }

    public TCMessage getTc() {
        return tc;
    }

    public int getPacketSize() {
        return 4096;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
    }

}
