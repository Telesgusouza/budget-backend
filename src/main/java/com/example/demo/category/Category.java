package com.example.demo.category;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "tb_category")
@Table(name = "tb_category")
public class Category {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private CategoryEnum category;

	public Category() {
	}

	public Category(UUID id, String name, CategoryEnum category) {
		super();
		this.id = id;
		this.name = name;
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

	public CategoryEnum getCategory() {
		return category;
	}

	public void setCategory(CategoryEnum category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", category=" + category + "]";
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
		Category other = (Category) obj;
		return Objects.equals(id, other.id);
	}

}
