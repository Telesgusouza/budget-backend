package com.example.demo.transaction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private UserRepository userRepository;

	// ATENÇÃO ler arquivo
	
	public Page<Transaction> getTransactionsPageable(Pageable pageable) {
		
		return this.transactionRepository.findAll(pageable);
	}
	
	public List<TransactionsResponseHomeDTO> getForHomeTransactions(User user) {
		
		List<TransactionsResponseHomeDTO> list = new ArrayList<>();
		
		int max = Math.min(user.getTransactions().size(), 6);
		
		for (int i = 0; i < max; i++) {
		    if (user.getTransactions().get(i) != null) {
		        Transaction currentTransaction = user.getTransactions().get(i);

		        list.add(
		            new TransactionsResponseHomeDTO(
		                currentTransaction.getId(),
		                currentTransaction.getName(),
		                currentTransaction.getDate(),
		                currentTransaction.getValue(),
		                currentTransaction.getTransactionStatus()
		            )
		        );
		    }
		}
		
		return list;
	}

	public Transaction addNewTransaction(TransactionDTO data, User user) {
		
		Instant currentTime = Instant.now();

		Transaction newTransaction = new Transaction(null, data.name(), data.value(), data.statusValue(), currentTime,
				data.category());
		newTransaction.setUser(user);
		
		Transaction save = this.transactionRepository.save(newTransaction);
		
		user.getTransactions().add(save);
		
		this.userRepository.save(user);

		return save;
	}
	
	public void editTransaction(TransactionDTO data, UUID id) {
		
		Transaction requestTransaction = this.transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));

		boolean changed = false;
		
		if (!Objects.equals(data.name(), requestTransaction.getName())) {
			requestTransaction.setName(data.name());
			changed = true;
		}
		
		if (!Objects.equals(data.category(), requestTransaction.getCategory())) {
			requestTransaction.setCategory(data.category());
			changed = true;
		}
		
		if (!Objects.equals(data.value(), requestTransaction.getValue())) {
			requestTransaction.setValue(data.value());
			changed = true;
		}
		
		if (!Objects.equals(data.statusValue(), requestTransaction.getTransactionStatus())) {
			requestTransaction.setTransactionStatus(data.statusValue());
			changed = true;
		}
		
		if (!changed) {
			throw new RuntimeException("There are no changes to be made.");
		}
		
		this.transactionRepository.save(requestTransaction);
		
	}

	public void deleteTransaction(UUID id) {

		Transaction request = this.transactionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Transaction not found"));
		this.transactionRepository.delete(request);

	}

}
