package com.example.demo.recurringBill;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringBillRepository extends JpaRepository<RecurringBill, UUID> {

}
