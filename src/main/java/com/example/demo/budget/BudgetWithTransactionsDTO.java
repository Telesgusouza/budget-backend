package com.example.demo.budget;

import java.util.List;
import java.util.UUID;

import com.example.demo.transaction.Transaction;

public record BudgetWithTransactionsDTO(
	    UUID id,
	    Float budget,
	    Float valueSpent,
	    String color,
	    CategoryEnum category,
	    List<Transaction> transactions
		) {

}
