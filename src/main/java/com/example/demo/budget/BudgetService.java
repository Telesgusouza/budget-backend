package com.example.demo.budget;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

	@Autowired
	private BudgetRepository budgetRepository;

	public Budget newBudget(BudgetDTO data) {

		Budget newBudget = new Budget(null, data.maximumSpend(), 0.0f, data.color(), data.category());

		Budget response = this.budgetRepository.save(newBudget);

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
	};

	public void deleteBudget(UUID id) {
		Budget request = this.budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget not found"));
		this.budgetRepository.delete(request);
	}

}
