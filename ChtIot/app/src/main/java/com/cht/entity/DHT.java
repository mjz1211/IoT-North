package com.cht.entity;

public class DHT {
    private double temper;
    private double humi;
    //java no Property getXxx setXxx get or set移除 xxxXxxx...
    public double getTemper() {
        return temper;
    }

    public void setTemper(double temper) {
        this.temper = temper;
    }

    public double getHumi() {
        return humi;
    }

    public void setHumi(double humi) {
        this.humi = humi;
    }
}
