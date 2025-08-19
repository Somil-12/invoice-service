package com.example.invoice.repo;

import com.example.invoice.model.Dealer;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DealerRepository {
    private final Map<String, Dealer> store = new ConcurrentHashMap<>();

    public Dealer findById(String dealerId) {
        return store.get(dealerId);
    }

    public void save(Dealer dealer) {
        store.put(dealer.getDealerId(), dealer);
    }
}
