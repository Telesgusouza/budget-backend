package com.example.demo.transaction;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	// ATENÇÃO ler arquivo

	public Transaction addNewTransaction(AddTransactionDTO data) {

		Instant currentTime = Instant.now();

		Transaction newTransaction = new Transaction(null, data.name(), data.value(), data.statusValue(), currentTime,
				data.category());
		Transaction save = this.transactionRepository.save(newTransaction);

		return save;
	}

	public void deleteTransaction(UUID id) {

		Transaction request = this.transactionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Transaction not found"));
		this.transactionRepository.delete(request);

	}

}
