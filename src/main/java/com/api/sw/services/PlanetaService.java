package com.api.sw.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.api.sw.client.PlanetsClient;
import com.api.sw.client.schema.SearchPlanetResult;
import com.api.sw.modelo.Planeta;
import com.api.sw.repositories.PlanetaRepository;

@Service
public class PlanetaService {

	@Autowired
	private PlanetsClient clientApiExterna;

	@Autowired
	private PlanetaRepository repository;

	@Transactional(readOnly = true)
	public Optional<Planeta> buscaPorId(final Long id) {
		Optional<Planeta> optResultante = repository.findById(id);
		return optResultante;
	}

	@Transactional(readOnly = true)
	public Optional<Planeta> buscaPorNome(final String nome) {
		Optional<Planeta> optResultante = repository.findByNomeContaining(nome);
		return optResultante;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleta(final Long id) {
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Collection<Planeta>> listaTodos() {

		return Optional.ofNullable(repository.findAll());

	}

	private Integer obtemQuantidadeAparicoesNaApiExterna(final String nome) {
		Integer quantidade = null;
		try {
			SearchPlanetResult retornoApiExterna = clientApiExterna.buscaPorNome(nome);
			if (retornoApiExterna != null) {
				switch (retornoApiExterna.getCount().intValue()) {
				case 0:
					quantidade = 0;
					break;
				case 1:
					quantidade = retornoApiExterna.getResults().stream().findFirst().get().obtemNumeroAparicoes()
							.intValue();
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			// Do nothing
		}
		return quantidade;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Planeta salva(final Planeta planeta) {

		Planeta planetaParaPersistencia = Planeta.builder()
				.nome(planeta.getNome())
				.clima(planeta.getClima())
				.terreno(planeta.getTerreno())
				.numeroAparicoes(obtemQuantidadeAparicoesNaApiExterna(planeta.getNome()))
				.build();

		repository.save(planetaParaPersistencia);

		return planetaParaPersistencia;
	}

	public SearchPlanetResult buscaTodosApiExterna() {

		SearchPlanetResult resultado = clientApiExterna.listaTodos();
		return resultado;

	}

	public SearchPlanetResult buscaTodosApiExternaPaginada(Long pagina) {
		SearchPlanetResult resultado = clientApiExterna.listaTodosPaginado(pagina);
		return resultado;
	}

}
