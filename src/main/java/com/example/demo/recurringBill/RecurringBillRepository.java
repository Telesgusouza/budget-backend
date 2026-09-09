package com.example.demo.recurringBill;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringBillRepository extends JpaRepository<RecurringBill, UUID> {

	List<RecurringBill> findByUserIdAndNameContainingIgnoreCase(UUID userId, String name);
}
