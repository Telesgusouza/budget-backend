package com.example.demo.pot;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PotService {

	@Autowired
	private PotRepository repo;

	public Pot addNewPot(PotDTO data) {
		Pot pot = new Pot(null, data.name(), (float) 0.0, data.target(), data.color());
		Pot save = repo.save(pot);

		return save;
	}

	public void addOrWithdraw(addOrWithdrawDTO data, UUID id) {
		Pot request = this.repo.findById(id).orElseThrow(() -> new RuntimeException("Pot not found"));
		request.setCurrentValue(data.value());
		this.repo.save(request);
	}

	public Pot editPot(PotDTO data, UUID id) {
		Pot pot = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Pot not found"));

		boolean changed = false;

		if (!Objects.equals(data.name(), pot.getName())) {
			pot.setName(data.name());
			changed = true;
		}

		if (!Objects.equals(data.target(), pot.getTargetValue())) {
			pot.setTargetValue(data.target());
			changed = true;
		}

		if (!Objects.equals(data.color(), pot.getColor())) {
			pot.setColor(data.color());
			changed = true;
		}

		if (!changed) {
			throw new IllegalArgumentException("There are no changes to be made.");
		}

		return repo.save(pot);
	}

	public void deletePot(UUID id) {
		Pot request = this.repo.findById(id).orElseThrow(() -> new RuntimeException("Pot not found"));
		this.repo.delete(request);
	}

}
