package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // Add custom queries if needed
}
