package com.example.demo.pot;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "tb_pot")
@Table(name = "tb_pot")
public class Pot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Float currentValue;

	@Column(nullable = false)
	private Float targetValue;

	@Column(nullable = false)
	private String color;

	public Pot() {
	}

	public Pot(UUID id, String name, Float currentValue, Float targetValue, String color) {
		super();
		this.id = id;
		this.name = name;
		this.currentValue = currentValue;
		this.targetValue = targetValue;
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

	public Float getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Float currentValue) {
		this.currentValue = currentValue;
	}

	public Float getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Float targetValue) {
		this.targetValue = targetValue;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Pot [id=" + id + ", name=" + name + ", currentValue=" + currentValue + ", targetValue=" + targetValue
				+ ", color=" + color + "]";
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
		Pot other = (Pot) obj;
		return Objects.equals(id, other.id);
	}

}
