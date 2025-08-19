package com.example.invoice.model;

public class Dealer {
    private String dealerId;
    private String name;
    private String address;
    private String phone;

    public Dealer() {}
    public Dealer(String dealerId, String name, String address, String phone) {
        this.dealerId = dealerId;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
    public String getDealerId() { return dealerId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
}
