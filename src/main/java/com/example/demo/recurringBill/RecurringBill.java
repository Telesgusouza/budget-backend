package com.example.demo.recurringBill;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "tb_recurring_bill")
@Table(name = "tb_recurring_bill")
public class RecurringBill {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Float value;

	@Column(nullable = false)
	private Instant currentDate;

	@Column(nullable = false)
	private Instant dueDate;

	@Column(nullable = false)
	private String color;

	public RecurringBill() {
	}

	public RecurringBill(UUID id, String name, Float value, Instant currentDate, Instant dueDate, String color) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.currentDate = currentDate;
		this.dueDate = dueDate;
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

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public Instant getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Instant currentDate) {
		this.currentDate = currentDate;
	}

	public Instant getDueDate() {
		return dueDate;
	}

	public void setDueDate(Instant dueDate) {
		this.dueDate = dueDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "RecurringBill [id=" + id + ", name=" + name + ", value=" + value + ", currentDate=" + currentDate
				+ ", dueDate=" + dueDate + ", color=" + color + "]";
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
		RecurringBill other = (RecurringBill) obj;
		return Objects.equals(id, other.id);
	}

}
