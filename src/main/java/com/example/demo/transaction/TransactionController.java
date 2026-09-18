package com.example.demo.transaction;

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
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.budget.CategoryEnum;
import com.example.demo.user.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

	@Autowired
	private TransactionService transactionsService;

	@GetMapping
	public ResponseEntity<Page<Transaction>> getAll(@PageableDefault(size = 7, sort = { "date" }) Pageable pageable) {

		Page<Transaction> transactions = this.transactionsService.getTransactionsPageable(pageable);

		return ResponseEntity.ok().body(transactions);
	}

	@GetMapping("/home")
	public ResponseEntity<List<TransactionsResponseHomeDTO>> getForHomeTransactions(
			@AuthenticationPrincipal User user) {

		List<TransactionsResponseHomeDTO> response = this.transactionsService.getForHomeTransactions(user);

		return ResponseEntity.ok().body(response);
	}

	@PostMapping
	public ResponseEntity<Transaction> addNewTransaction(@RequestBody @Valid TransactionDTO data,
			@AuthenticationPrincipal User user) {

		Transaction response = this.transactionsService.addNewTransaction(data, user);

		return ResponseEntity.status(201).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> editTransaction(@RequestBody TransactionDTO data, @PathVariable UUID id) {
		this.transactionsService.editTransaction(data, id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {

		this.transactionsService.deleteTransaction(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/proofOfPayment/{status}/{category}")
	public ResponseEntity<Transaction> extractFromProofOfPayment(@RequestParam("file") MultipartFile file,

			@PathVariable boolean status, @PathVariable CategoryEnum category,

			@AuthenticationPrincipal User user) throws Exception {

		otherPaymentReceiptDetailsDTO data = new otherPaymentReceiptDetailsDTO(status, category);

		Transaction response = this.transactionsService.extract(file, data, user);

		return ResponseEntity.ok().body(response);
	}

}
