package com.example.demo.budget;

import java.util.List;

public record ResponseHomeBudgetDTO(Float totalSpent, Float totalBudget, List<BudgetDTO> budgets) {

}
