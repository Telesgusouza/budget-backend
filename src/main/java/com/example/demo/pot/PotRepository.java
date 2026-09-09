package com.example.demo.pot;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PotRepository extends JpaRepository<Pot, UUID> {

	Page<Pot> findByUserId(UUID idUser, Pageable pageable);
	
}
