package com.example.demo.transaction;

import com.example.demo.budget.CategoryEnum;

public record AddTransactionDTO(String name, CategoryEnum category, Float value, Boolean statusValue) {

}
