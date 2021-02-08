package main.java.model;


public class BatteryData {
    private final Double time;
    private final Double percentage;
    private final Double temperature;
  

    private BatteryData(Double time, Double percentage, Double temperature){
        this.time = time;
        this.percentage = percentage;
        this.temperature = temperature;
    }

    public Double getTime() {
        return this.time;
    }

    public Double getPercentage() {
        return this.percentage;
    }

    public Double getTemperature() {
        return this.temperature;
    }

}
