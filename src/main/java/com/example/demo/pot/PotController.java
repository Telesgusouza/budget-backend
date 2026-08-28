package com.example.demo.pot;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/apit/v1/pot")
public class PotController {

	@Autowired
	private PotService potService;

	@PostMapping
	public ResponseEntity<Pot> addNewPot(@RequestBody @Valid PotDTO data) {
		Pot response = this.potService.addNewPot(data);

		return ResponseEntity.status(200).body(response);
	}

	@PatchMapping("/${id}")
	public ResponseEntity<Void> addOrWithdraw(@RequestBody @Valid addOrWithdrawDTO data, @RequestParam UUID id) {

		this.potService.addOrWithdraw(data, id);
		return ResponseEntity.status(204).build();
	}

	@PutMapping("/%{id}")
	public ResponseEntity<Pot> editPot(@RequestBody @Valid PotDTO data, @RequestParam UUID id) {

		Pot response = this.potService.editPot(data, id);

		return ResponseEntity.status(200).body(response);
	}

	@DeleteMapping("/${id}")
	public ResponseEntity<Void> deletePot(@RequestParam UUID id) {

		this.potService.deletePot(id);
		return ResponseEntity.status(204).build();
	}
}
