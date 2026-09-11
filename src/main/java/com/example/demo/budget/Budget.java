package com.example.demo.budget;

import java.util.Objects;
import java.util.UUID;

import com.example.demo.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "tb_budget")
@Table(name = "tb_budget")
public class Budget {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private Float valueSpent;

	@Column(nullable = false)
	private Float budget;

	@Column(nullable = false)
	private String color;

	@Column(nullable = false)
	private CategoryEnum category;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Budget() {
	}

	public Budget(UUID id, Float valueSpent, Float budget, String color, CategoryEnum category) {
		super();
		this.id = id;
		this.valueSpent = valueSpent;
		this.budget = budget;
		this.color = color;
		this.category = category;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Float getValueSpent() {
		return valueSpent;
	}

	public void setValueSpent(Float valueSpent) {
		this.valueSpent = valueSpent;
	}

	public Float getBudget() {
		return budget;
	}

	public void setBudget(Float budget) {
		this.budget = budget;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public CategoryEnum getCategory() {
		return category;
	}

	public void setCategory(CategoryEnum category) {
		this.category = category;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Budget [id=" + id + ", valueSpent=" + valueSpent + ", budget=" + budget + ", color="
				+ color + ", category=" + category + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Budget other = (Budget) obj;
		return Objects.equals(id, other.id);
	}

}
