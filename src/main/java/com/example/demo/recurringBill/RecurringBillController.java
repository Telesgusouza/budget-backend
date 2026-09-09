package com.example.demo.recurringBill;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.User;

@RestController
@RequestMapping("/api/v1/recurringbills")
public class RecurringBillController {

	@Autowired
	private RecurringBillService recurringBillService;

	@GetMapping
	public ResponseEntity<Page<RecurringBill>> getRecurringBills(@PageableDefault(size = 7) Pageable pageable) {

		Page<RecurringBill> RecurringBills = this.recurringBillService.getRecurringBillsPageable(pageable);

		return ResponseEntity.status(200).body(RecurringBills);
	}

	@GetMapping("/home")
	public ResponseEntity<RecurringbillHomeDTO> getForHome(@AuthenticationPrincipal User user) {

		RecurringbillHomeDTO response = this.recurringBillService.calcularTotais(user);

		return ResponseEntity.status(200).body(response);
	}

	@GetMapping("/search")
	public ResponseEntity<List<RecurringBill>> searchRecurringBills(@AuthenticationPrincipal User user,
			@RequestParam String name) {
		List<RecurringBill> bills = recurringBillService.searchRecurringBills(user.getId(), name);
		return ResponseEntity.ok(bills);
	}

	@PostMapping
	public ResponseEntity<RecurringBill> addNewRecurringBill(@RequestBody RecurringBillDTO data,
			@AuthenticationPrincipal User user) {

		RecurringBill response = this.recurringBillService.addNewRecurringBill(data, user);

		return ResponseEntity.status(201).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> editRecurringBill(@RequestBody RecurringBillDTO data, @PathVariable UUID id) {

		this.recurringBillService.editRecurringBill(data, id);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRecurringBill(@PathVariable UUID id) {

		this.recurringBillService.deleteRecurringBills(id);

		return ResponseEntity.noContent().build();
	}

}
