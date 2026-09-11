package com.example.demo.transaction;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.budget.CategoryEnum;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

	List<Transaction> findByUserIdAndCategory(UUID userId, CategoryEnum category);
}
