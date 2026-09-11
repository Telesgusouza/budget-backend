package com.example.demo.transaction;

import com.example.demo.budget.CategoryEnum;

public record TransactionDTO(
		String name, 
		CategoryEnum category, 
		Float value, 
		Boolean statusValue) {

}
