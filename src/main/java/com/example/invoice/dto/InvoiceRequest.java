package com.example.invoice.dto;

import jakarta.validation.constraints.NotBlank;

public class InvoiceRequest {
    @NotBlank
    private String dealerId;
    @NotBlank
    private String vehicleId;
    @NotBlank
    private String customerName;

    public String getDealerId() { return dealerId; }
    public void setDealerId(String dealerId) { this.dealerId = dealerId; }

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
