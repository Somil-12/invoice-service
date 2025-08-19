package com.example.invoice;

import com.example.invoice.model.Dealer;
import com.example.invoice.model.Vehicle;
import com.example.invoice.repo.DealerRepository;
import com.example.invoice.repo.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InvoiceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvoiceServiceApplication.class, args);
    }

    // Seed some sample data (in-memory)
    @Bean
    CommandLineRunner seed(DealerRepository dealers, VehicleRepository vehicles) {
        return args -> {
            dealers.save(new Dealer("D001", "AutoWorld Delhi", "A-12, Ring Road, Delhi", "+91-99999-11111"));
            dealers.save(new Dealer("D002", "Prime Motors Noida", "Sector 62, Noida", "+91-99999-22222"));

            vehicles.save(new Vehicle("V1001", "Toyota", "Corolla", 2021, 1200000.00));
            vehicles.save(new Vehicle("V1002", "Hyundai", "Creta", 2022, 1500000.00));
            vehicles.save(new Vehicle("V1003", "Tata", "Punch", 2024, 900000.00));
        };
    }
}
