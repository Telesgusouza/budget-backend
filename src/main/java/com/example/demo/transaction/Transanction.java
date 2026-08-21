package com.example.demo.transaction;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "tb_transanction")
@Table(name = "tb_transanction")
public class Transanction {

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

	public Transanction(UUID id, String name, Float value, Boolean transactionStatus, Instant date) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.transactionStatus = transactionStatus;
		this.date = date;
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

	@Override
	public String toString() {
		return "Transanction [id=" + id + ", name=" + name + ", value=" + value + ", transactionStatus="
				+ transactionStatus + ", date=" + date + "]";
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
		Transanction other = (Transanction) obj;
		return Objects.equals(id, other.id);
	}

}
