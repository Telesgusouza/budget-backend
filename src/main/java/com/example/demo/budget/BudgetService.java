package com.example.demo.budget;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.transaction.Transaction;
import com.example.demo.transaction.TransactionRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Service
public class BudgetService {

	@Autowired
	private BudgetRepository budgetRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	public List<BudgetWithTransactionsDTO> getBudgets(UUID userId) {
	       List<Budget> budgets = budgetRepository.findByUserId(userId);

	       List<BudgetWithTransactionsDTO> response = budgets.stream().map(budget -> {
	            List<Transaction> transactions = transactionRepository
	                    .findByUserIdAndCategory(userId, budget.getCategory());

	            BudgetWithTransactionsDTO dto = new BudgetWithTransactionsDTO(
	            		budget.getId(),
	            		budget.getBudget(),
	            		budget.getValueSpent(),
	            		budget.getColor(),
	            		budget.getCategory(),
	            		transactions
	            		);

	            return dto;
	        }).collect(Collectors.toList());
	       
	       return response;
	}
	
	
	
	public ResponseHomeBudgetDTO getHome(User user) {
		
		List<Budget> budgets = user.getBudgets();
		
		Float totalSpent = 0.0f;
		Float totalBudget = 0.0f;
		
		List<BudgetDTO> list = new ArrayList();
		
		Integer count = 0;
		
		for (Budget budget : budgets) {
			
			totalSpent += budget.getValueSpent();
			totalBudget += budget.getBudget();
			
			if (count <= 3) {
				list.add(new BudgetDTO(budget.getCategory(), budget.getValueSpent(), budget.getColor()));
				count++;
			}	
		}
		
		return new ResponseHomeBudgetDTO(totalSpent, totalBudget, list);
	}
	

	public Budget newBudget(BudgetDTO data, User user) {

		Budget newBudget = new Budget(null, 0.0f, data.maximumSpend(),  data.color(), data.category());
		newBudget.setUser(user);
		Budget response = this.budgetRepository.save(newBudget);
		
		user.getBudgets().add(response);
		userRepository.save(user);

		return response;
	}

	public void editBudget(BudgetDTO data, UUID id) {

		Budget request = this.budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget not found"));

		boolean changed = false;

		if (!Objects.equals(data.category(), request.getCategory())) {
			request.setCategory(data.category());
			changed = true;
		}

		if (!Objects.equals(data.maximumSpend(), request.getValueSpent())) {
			request.setValueSpent(data.maximumSpend());
			changed = true;
		}

		if (!Objects.equals(data.color(), request.getColor())) {
			request.setColor(data.color());
			changed = true;
		}

		if (!changed) {
			throw new RuntimeException("There are no changes to be made.");
		}
		
		this.budgetRepository.save(request);
	};

	public void deleteBudget(UUID id) {
		Budget request = this.budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget not found"));
		this.budgetRepository.delete(request);
	}

}
