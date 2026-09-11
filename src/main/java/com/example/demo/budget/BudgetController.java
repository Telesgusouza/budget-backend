package com.example.demo.budget;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.User;

@RestController
@RequestMapping("/api/v1/budget")
public class BudgetController {

	@Autowired
	private BudgetService budgetService;
	
	@GetMapping("/home")
	public ResponseEntity<ResponseHomeBudgetDTO> getHome(@AuthenticationPrincipal User user) {
		
		ResponseHomeBudgetDTO response = this.budgetService.getHome(user);
		
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping
	public ResponseEntity<List<BudgetWithTransactionsDTO>> getBudgets(@AuthenticationPrincipal User user) {
		
		List<BudgetWithTransactionsDTO> response = this.budgetService.getBudgets(user.getId());
		
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping
	public ResponseEntity<Budget> addNewBudget(@RequestBody BudgetDTO data, @AuthenticationPrincipal User user) {
		
		Budget response = this.budgetService.newBudget(data, user);
		
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> editBudget(@RequestBody BudgetDTO data, @PathVariable UUID id) {
		
		this.budgetService.editBudget(data, id);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBudget(@PathVariable UUID id) {
		
		this.budgetService.deleteBudget(id);
		
		return ResponseEntity.noContent().build();
	}
	
}
