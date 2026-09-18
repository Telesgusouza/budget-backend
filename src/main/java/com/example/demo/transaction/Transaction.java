package com.example.demo.transaction;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.example.demo.budget.CategoryEnum;
import com.example.demo.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "tb_transanction")
@Table(name = "tb_transanction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Float value;

	@Column(nullable = false)
	private Boolean transactionStatus;

	@Column(nullable = false)
	private Instant date;
	
	private CategoryEnum category;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Transaction() {}
	
	public Transaction(UUID id, String name, Float value, Boolean transactionStatus, Instant dateFormat,
			CategoryEnum category) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.transactionStatus = transactionStatus;
		this.date = dateFormat;
		this.category = category;
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

	public Boolean getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(Boolean transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
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
		return "Transaction [id=" + id + ", name=" + name + ", value=" + value + ", transactionStatus="
				+ transactionStatus + ", date=" + date + ", category=" + category + "]";
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
		Transaction other = (Transaction) obj;
		return Objects.equals(id, other.id);
	}

}
