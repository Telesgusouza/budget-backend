package com.example.demo.category;

public enum CategoryEnum {
	BUDGET("budget"), 
	RECURRING_BILL("recurring_bill");

	private String role;

	CategoryEnum(String role) {
		this.role = role;
	}

	public String getRole() {
		return this.role;
	}
}
