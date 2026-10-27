package com.example.demo.budget;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.AuthenticationRequiredOrFailed;
import com.example.demo.exceptions.InvalidFieldException;
import com.example.demo.exceptions.ResourceNotFoundException;
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
import jakarta.transaction.Transactional;

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
	       
	       if (budgets == null) {
	    	   throw new AuthenticationRequiredOrFailed("User not found");
	       }

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
	
	@Transactional
	public Budget newBudget(BudgetDTO data, User user) {

	    if (data == null) {
	        throw new InvalidFieldException("Budget data cannot be null.");
	    }

	    if (user == null) {
	        throw new AuthenticationRequiredOrFailed("User not found.");
	    }

	    if (data.maximumSpend() == null || data.maximumSpend() <= 0) {
	        throw new InvalidFieldException("The maximum value must be greater than zero.");
	    }

	    if (data.category() == null) {
	        throw new InvalidFieldException("Mandatory category.");
	    }

	    if (data.color() == null || data.color().isBlank()) {
	        throw new InvalidFieldException("Mandatory color.");
	    }

	    if (budgetRepository.existsByUserAndCategory(user, data.category())) {
	        throw new InvalidFieldException(
	            "There is already a budget for this category."
	        );
	    }

	    try {

	        Budget budget = new Budget(
	                null,
	                0.0f,
	                data.maximumSpend(),
	                data.color(),
	                data.category()
	        );

	        budget.setUser(user);

	        Budget savedBudget = budgetRepository.save(budget);

	        if (user.getBudgets() == null) {
	            user.setBudgets(new ArrayList<>());
	        }

	        user.getBudgets().add(savedBudget);

	        userRepository.save(user);

	        return savedBudget;

	    } catch (DataAccessException e) {
	        throw new ResourceNotFoundException(
	            "Internal error while creating a quote." +
	            e
	        );
	    }
	}
		
	@Transactional
	public void editBudget(BudgetDTO data, UUID id) {

	    if (data == null) {
	        throw new InvalidFieldException("Budget data cannot be null.");
	    }

	    if (id == null) {
	        throw new InvalidFieldException("Budget id cannot be null.");
	    }

	    if (data.category() == null) {
	        throw new InvalidFieldException("Mandatory category.");
	    }

	    if (data.maximumSpend() == null || data.maximumSpend() <= 0) {
	        throw new InvalidFieldException(
	                "The maximum value must be greater than zero.");
	    }

	    if (data.color() == null || data.color().isBlank()) {
	        throw new InvalidFieldException("Mandatory color.");
	    }

	    Budget request = budgetRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Budget not found"));

	    if (!Objects.equals(data.category(), request.getCategory())
	            && budgetRepository.existsByUserAndCategory(
	                    request.getUser(),
	                    data.category())) {

	        throw new InvalidFieldException(
	                "There is already a budget for this category.");
	    }

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
	        throw new InvalidFieldException(
	                "There are no changes to be made.");
	    }

	    try {
	        budgetRepository.save(request);
	    } catch (DataAccessException e) {
	        throw new ResourceNotFoundException(
	                "Internal error while updating budget.");
	    }
	}

	public void deleteBudget(UUID id) {
		Budget request = this.budgetRepository.findById(id)
				.orElseThrow(() ->
				new ResourceNotFoundException("Budget not found"));
		this.budgetRepository.delete(request);
	}

}
