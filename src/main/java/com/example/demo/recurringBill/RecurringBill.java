package com.example.demo.recurringBill;

import java.time.Instant;
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
	private Integer dueDate;
	
	private Instant lastBillPaid;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public RecurringBill() {
	}

	public RecurringBill(UUID id, String name, Float value, Integer dueDate, Instant lastBillPaid) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.dueDate = dueDate;
		this.lastBillPaid = lastBillPaid;
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

	public Integer getDueDate() {
		return dueDate;
	}

	public void setDueDate(Integer dueDate) {
		this.dueDate = dueDate;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

	public Instant getLastBillPaid() {
		return lastBillPaid;
	}

	public void setLastBillPaid(Instant lastBillPaid) {
		this.lastBillPaid = lastBillPaid;
	}

	@Override
	public String toString() {
		return "RecurringBill [id=" + id + ", name=" + name + ", value=" + value + ", dueDate=" + dueDate + "]";
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
