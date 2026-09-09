package com.example.demo.pot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

/*
 
  
  preciso fazer a paginação, a entidade usuario pode ter varios pots, enquanto o pot pode ter apenas um usuario, preciso adicionar uma requisição
  get que faça a paginação dos pots do usuario
  
  	@GetMapping
	public ResponseEntity<Page<Pot>> getPotForPagination(Pageable pageable) {
		
		Page<Pot> response = this.potService.getPotForPagination(pageable);
		
		return ResponseEntity.ok().body(response);
	}
  
  	public Page<Pot> getPotForPagination(Pageable pageable) {

		Page<Pot> request = this.repo.findAll(pageable);

		return this.repo.findAll(pageable);
	}
  
  
 */

@Service
public class PotService {

	@Autowired
	private PotRepository repo;

	@Autowired
	private UserRepository userRepository;

	public Page<Pot> getPotForPagination(UUID idUser, Pageable pageable) {

		Page<Pot> request = this.repo.findByUserId(idUser, pageable);

		return this.repo.findAll(pageable);
	}

//	public Page<Pot> getPotForPagination(Pageable pageable) {
//		
//		Page<Pot> request = this.repo.findAll(pageable);
//		
//		return this.repo.findAll(pageable);
//	}

	public ResponsePotHomeDTO getForHome(User user) {

		List<Pot> pots = user.getPots();

		Float total = 0.0f;
		List<PotHomeDTO> list = new ArrayList<>();

		Integer count = 0;

		for (Pot pot : pots) {
			total += pot.getCurrentValue();

			if (count <= 4) {
				list.add(new PotHomeDTO(pot.getName(), pot.getCurrentValue(), pot.getColor()));
				count++;
			}
		}

		return new ResponsePotHomeDTO(total, list);

	}

	public Pot addNewPot(PotDTO data, User user) {
		Pot pot = new Pot(null, data.name(), (float) 0.0, data.target(), data.color());
		pot.setUser(user);
		Pot save = repo.save(pot);

		user.getPots().add(save);
		this.userRepository.save(user);

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
