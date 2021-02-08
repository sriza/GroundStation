package main.java.model;


public class MainData {
    private final Double time;
    private final Double altitude;
    private final Double velocity;
    private final Double acceleration;

    private MainData(Double time, Double altitude, Double velocity, Double acceleration){
        this.time = time;
        this.altitude = altitude;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public Double getTime() {
        return this.time;
    }

    public Double getAltitude() {
        return this.altitude;
    }

    public Double getVelocity() {
        return this.velocity;
    }

    public Double getAcceleration() {
        return this.acceleration;
    }
    
}
