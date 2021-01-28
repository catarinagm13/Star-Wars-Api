package com.api.sw.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.sw.modelo.Planeta;

public interface PlanetaRepository extends JpaRepository<Planeta, Long> {

	Optional<Planeta> findByNomeContaining(final String nome);

}
