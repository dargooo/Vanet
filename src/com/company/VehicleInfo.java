package com.company;

import java.io.Serializable;

public class VehicleInfo implements Serializable {

    private static final long serialVersionUID = -2009521561501538620L;
    private double x, y;
    private double velocity, acceleration;
    @SuppressWarnings("unused") private int brake;
    @SuppressWarnings("unused") private double gas;

    public VehicleInfo(GPS gps, double velocity, double acceleration) {
        x = gps.getX();
        y = gps.getY();
        this.velocity = velocity;
        this.acceleration = acceleration;
        brake = 1;
        gas = 100;
    }

    public GPS getGPS() {
        return new GPS(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public String toString() {
        return String.format("GPS = (%.1f, %.1f), speed = %.2f, acceleration = %.3f", x, y, velocity, acceleration);
    }


}
