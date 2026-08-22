package com.example.demo.pot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
}
