package com.example.demo.budget;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pot.Pot;
import com.example.demo.user.User;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
	
	List<Budget> findByUserId(UUID userId);
	
	boolean existsByUserAndCategory(User user, CategoryEnum category);
}
