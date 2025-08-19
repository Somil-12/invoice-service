package com.example.invoice.service;

import com.example.invoice.dto.InvoiceRequest;
import com.example.invoice.model.Dealer;
import com.example.invoice.model.Vehicle;
import com.example.invoice.repo.DealerRepository;
import com.example.invoice.repo.VehicleRepository;
import com.example.invoice.util.PdfUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
public class InvoiceService {
    private static final double TAX_RATE = 0.10; // 10%

    private final DealerRepository dealerRepository;
    private final VehicleRepository vehicleRepository;

    public InvoiceService(DealerRepository dealerRepository, VehicleRepository vehicleRepository) {
        this.dealerRepository = dealerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public GeneratedInvoice generate(InvoiceRequest req) {
        Dealer dealer = dealerRepository.findById(req.getDealerId());
        if (dealer == null) {
            throw new IllegalArgumentException("Dealer not found: " + req.getDealerId());
        }

        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId());
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found: " + req.getVehicleId());
        }

        String invoiceNo = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String transactionId = "TX-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        double price = vehicle.getPrice();
        double tax = round2(price * TAX_RATE);
        double total = round2(price + tax);

        byte[] pdf = PdfUtils.buildInvoicePdf(
                dealer, vehicle, req.getCustomerName(),
                invoiceNo, transactionId, timestamp, price, tax, total
        );

        return new GeneratedInvoice(invoiceNo, transactionId, pdf);
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    public class GeneratedInvoice {
        private final String invoiceNo;
        private final String transactionId;
        private final byte[] pdfBytes;

        public GeneratedInvoice(String invoiceNo, String transactionId, byte[] pdfBytes) {
            this.invoiceNo = invoiceNo;
            this.transactionId = transactionId;
            this.pdfBytes = pdfBytes;
        }

        public String getInvoiceNo() { return invoiceNo; }
        public String getTransactionId() { return transactionId; }
        public byte[] getPdfBytes() { return pdfBytes; }
    }

}
