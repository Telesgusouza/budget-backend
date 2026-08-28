package com.example.demo.budget;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.pot.Pot;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

}
