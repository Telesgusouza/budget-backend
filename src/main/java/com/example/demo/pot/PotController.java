package com.example.demo.pot;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pot")
public class PotController {

	@Autowired
	private PotService potService;
	
	@GetMapping("/home")
	public ResponseEntity<ResponsePotHomeDTO> getForhome(@AuthenticationPrincipal User user) {
		
		ResponsePotHomeDTO response = this.potService.getForHome(user);
		
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping
	public ResponseEntity<Page<Pot>> getPotForPagination(Pageable pageable, @AuthenticationPrincipal User user) {
		
		Page<Pot> response = this.potService.getPotForPagination(user.getId(), pageable);
		
		return ResponseEntity.ok().body(response);
	}

	@PostMapping
	public ResponseEntity<Pot> addNewPot(@RequestBody @Valid PotDTO data, @AuthenticationPrincipal User user) {
		Pot response = this.potService.addNewPot(data, user);

		return ResponseEntity.status(200).body(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> addOrWithdraw(@RequestBody @Valid addOrWithdrawDTO data, @PathVariable UUID id) {

		this.potService.addOrWithdraw(data, id);
		return ResponseEntity.status(204).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pot> editPot(@RequestBody @Valid PotDTO data, @PathVariable UUID id) {

		Pot response = this.potService.editPot(data, id);

		return ResponseEntity.status(200).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePot(@PathVariable UUID id) {

		this.potService.deletePot(id);
		return ResponseEntity.status(204).build();
	}
}
