package com.api.sw.web.controller;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.sw.client.schema.Planet;
import com.api.sw.client.schema.SearchPlanetResult;
import com.api.sw.modelo.Planeta;
import com.api.sw.services.PlanetaService;
import com.api.sw.web.transporte.objetos.RequisicaoNovoPlaneta;
import com.api.sw.web.transporte.objetos.RespostaPesquisaPlaneta;
import com.api.sw.web.transporte.objetos.RespostaPlaneta;

@RestController
@RequestMapping("/planeta")
public class PlanetaController {

	@Autowired
	private PlanetaService planetaService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<RespostaPesquisaPlaneta> buscaPorId(@PathVariable("id") Long id) {

		Optional<Planeta> resultadoPesquisa = planetaService.buscaPorId(id);

		if (!resultadoPesquisa.isPresent()) {
			return new ResponseEntity<RespostaPesquisaPlaneta>(HttpStatus.NOT_FOUND);
		} else {
			RespostaPesquisaPlaneta respostaPesquisaPlaneta = resultadoPesquisa.map(p -> {
				RespostaPesquisaPlaneta resposta = RespostaPesquisaPlaneta.builder().total(1L).proxima(null)
						.anterior(null).build();
				resposta.adicionaPlaneta(RespostaPlaneta.builder().id(p.getId()).nome(p.getNome()).clima(p.getClima())
						.terreno(p.getTerreno()).numeroAparicoes(p.getNumeroAparicoes()).build());
				return resposta;
			}).get();
			return new ResponseEntity<RespostaPesquisaPlaneta>(respostaPesquisaPlaneta, HttpStatus.OK);
		}

	}

	@GetMapping(value = "/nome/{nome}")
	public ResponseEntity<RespostaPesquisaPlaneta> buscaPorNome(@PathVariable("nome") String nome) {

		Optional<Planeta> resultadoPesquisa = planetaService.buscaPorNome(nome);

		if (!resultadoPesquisa.isPresent()) {
			return new ResponseEntity<RespostaPesquisaPlaneta>(HttpStatus.NOT_FOUND);
		} else {
			RespostaPesquisaPlaneta respostaPesquisaPlaneta = resultadoPesquisa.map(p -> {
				RespostaPesquisaPlaneta resposta = RespostaPesquisaPlaneta.builder().total(1L).proxima(null)
						.anterior(null).build();
				resposta.adicionaPlaneta(RespostaPlaneta.builder().id(p.getId()).nome(p.getNome()).clima(p.getClima())
						.terreno(p.getTerreno()).numeroAparicoes(p.getNumeroAparicoes()).build());
				return resposta;
			}).get();
			return new ResponseEntity<RespostaPesquisaPlaneta>(respostaPesquisaPlaneta, HttpStatus.OK);
		}

	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleta(@PathVariable("id") Long id) {

		Optional<Planeta> resultadoPesquisa = planetaService.buscaPorId(id);

		if (!resultadoPesquisa.isPresent()) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			planetaService.deleta(id);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

	}

	@GetMapping(value = { "", "/" })
	public ResponseEntity<RespostaPesquisaPlaneta> listaTodos() {

		Optional<Collection<Planeta>> todos = planetaService.listaTodos();

		RespostaPesquisaPlaneta resposta = RespostaPesquisaPlaneta.builder().total(0L).proxima(null).anterior(null)
				.build();

		Consumer<Collection<Planeta>> consumer = cp -> {
			resposta.setTotal(Long.valueOf(cp.size()));
			cp.stream().forEach(i -> {
				resposta.adicionaPlaneta(RespostaPlaneta.builder().id(i.getId()).nome(i.getNome()).clima(i.getClima())
						.terreno(i.getTerreno()).numeroAparicoes(i.getNumeroAparicoes()).build());
			});
		};

		todos.ifPresent(consumer);

		if (resposta.getTotal() > 0) {
			return new ResponseEntity<RespostaPesquisaPlaneta>(resposta, HttpStatus.OK);
		} else {
			return new ResponseEntity<RespostaPesquisaPlaneta>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(value = "/lista-api-externa")
	public ResponseEntity<RespostaPesquisaPlaneta> listaTodosApiExterna(UriComponentsBuilder uriComponentsBuilder) {

		SearchPlanetResult buscaTodosApiExterna = planetaService.buscaTodosApiExterna();

		RespostaPesquisaPlaneta resposta = RespostaPesquisaPlaneta.builder().total(0L).proxima(null).anterior(null)
				.build();

		if (buscaTodosApiExterna != null) {
			resposta.setTotal(buscaTodosApiExterna.getCount());

			buscaTodosApiExterna.extraiNumeroProximaPagina().ifPresent(
					p -> resposta.setProxima(uriComponentsBuilder.path("/planeta/lista-api-externa-paginado?pagina=" + p).build().toString()));

			buscaTodosApiExterna.extraiNumeroPaginaAnterior().ifPresent(p -> resposta
					.setAnterior(uriComponentsBuilder.replacePath("/planeta/lista-api-externa-paginado?pagina=" + p).build().toString()));

			Collection<Planet> planetas = buscaTodosApiExterna.getResults();
			if (planetas != null) {
				planetas.stream()
						.forEach(p -> resposta.adicionaPlaneta(RespostaPlaneta.builder().nome(p.getName())
								.clima(p.getClimate()).terreno(p.getClimate())
								.numeroAparicoes(p.obtemNumeroAparicoes().intValue()).build()));
			}

			return new ResponseEntity<RespostaPesquisaPlaneta>(resposta, HttpStatus.OK);

		} else {

			return new ResponseEntity<RespostaPesquisaPlaneta>(HttpStatus.NOT_FOUND);

		}

	}

	@GetMapping(value = "/lista-api-externa-paginado", params = { "pagina" })
	public ResponseEntity<RespostaPesquisaPlaneta> listaTodosApiExternaPaginado(
			@RequestParam(name = "pagina", required = true) Long pagina, UriComponentsBuilder uriComponentsBuilder) {

		SearchPlanetResult buscaTodosApiExterna = planetaService.buscaTodosApiExternaPaginada(pagina);

		RespostaPesquisaPlaneta resposta = RespostaPesquisaPlaneta.builder().total(0L).proxima(null).anterior(null)
				.build();

		if (buscaTodosApiExterna != null) {
			resposta.setTotal(buscaTodosApiExterna.getCount());

			buscaTodosApiExterna.extraiNumeroProximaPagina().ifPresent(
					p -> resposta.setProxima(uriComponentsBuilder.path("/planeta/lista-api-externa-paginado?pagina=" + p).build().toString()));

			buscaTodosApiExterna.extraiNumeroPaginaAnterior().ifPresent(p -> resposta
					.setAnterior(uriComponentsBuilder.replacePath("/planeta/lista-api-externa-paginado?pagina=" + p).build().toString()));

			Collection<Planet> planetas = buscaTodosApiExterna.getResults();
			if (planetas != null) {
				planetas.stream()
						.forEach(p -> resposta.adicionaPlaneta(RespostaPlaneta.builder().nome(p.getName())
								.clima(p.getClimate()).terreno(p.getClimate())
								.numeroAparicoes(p.obtemNumeroAparicoes().intValue()).build()));
			}

			return new ResponseEntity<RespostaPesquisaPlaneta>(resposta, HttpStatus.OK);

		} else {

			return new ResponseEntity<RespostaPesquisaPlaneta>(HttpStatus.NOT_FOUND);

		}

	}

	@PostMapping(value = { "", "/" })
	public ResponseEntity<Void> salva(@RequestBody @Validated RequisicaoNovoPlaneta requisicao,
			UriComponentsBuilder uriComponentsBuilder) {

		Planeta planeta = Planeta.builder().nome(requisicao.getNome()).clima(requisicao.getClima())
				.terreno(requisicao.getTerreno()).build();

		planeta = planetaService.salva(planeta);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponentsBuilder.path("/planeta/{id}").buildAndExpand(planeta.getId()).toUri());

		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

}
