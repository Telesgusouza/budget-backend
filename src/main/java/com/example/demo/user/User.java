package com.example.demo.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.budget.Budget;
import com.example.demo.pot.Pot;
import com.example.demo.recurringBill.RecurringBill;
import com.example.demo.transaction.Transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "tb_user")
@Table(name = "tb_user")
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(nullable = false, unique = true)
	private String login;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Pot> pots = new ArrayList();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Budget> budgets = new ArrayList();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<RecurringBill> recurringBills = new ArrayList();
	
	public User() {}

	public User(UUID id, String login, String password, UserRole role) {
		super();
		this.id = id;
		this.login = login;
		this.password = password;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public @Nullable String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.login;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Pot> getPots() {
		return pots;
	}

	public void setPots(List<Pot> pots) {
		this.pots = pots;
	}

	public List<Budget> getBudgets() {
		return budgets;
	}

	public void setBudgets(List<Budget> budgets) {
		this.budgets = budgets;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<RecurringBill> getRecurringBills() {
		return recurringBills;
	}

	public void setRecurringBills(List<RecurringBill> recurringBills) {
		this.recurringBills = recurringBills;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", role=" + role + "]";
	}

}
