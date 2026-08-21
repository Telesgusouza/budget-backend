package com.example.demo.budget;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "tb_budget")
@Table(name = "tb_budget")
public class Budget {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Float valueSpent;

	@Column(nullable = false)
	private Float valueRemaining;

	@Column(nullable = false)
	private String color;

	public Budget() {
	}

	public Budget(UUID id, String name, Float valueSpent, Float valueRemaining, String color) {
		super();
		this.id = id;
		this.name = name;
		this.valueSpent = valueSpent;
		this.valueRemaining = valueRemaining;
		this.color = color;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getValueSpent() {
		return valueSpent;
	}

	public void setValueSpent(Float valueSpent) {
		this.valueSpent = valueSpent;
	}

	public Float getValueRemaining() {
		return valueRemaining;
	}

	public void setValueRemaining(Float valueRemaining) {
		this.valueRemaining = valueRemaining;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Budget [id=" + id + ", name=" + name + ", valueSpent=" + valueSpent + ", valueRemaining="
				+ valueRemaining + ", color=" + color + "]";
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
