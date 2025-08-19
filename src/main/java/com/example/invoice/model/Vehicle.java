package com.example.invoice.model;

public class Vehicle {
    private String vehicleId;
    private String make;
    private String model;
    private int year;
    private double price;

    public Vehicle() {}
    public Vehicle(String vehicleId, String make, String model, int year, double price) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
    }
    public String getVehicleId() { return vehicleId; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
}
