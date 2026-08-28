package com.example.demo.budget;

public record BudgetDTO(
		CategoryEnum category,
		Float maximumSpend,
		String color) {

}
