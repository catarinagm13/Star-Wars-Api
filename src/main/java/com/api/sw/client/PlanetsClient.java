package com.api.sw.client;

import com.api.sw.client.schema.SearchPlanetResult;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface PlanetsClient {

	@RequestLine("GET /?search={nome}")
	@Headers("Accept: application/json")
	SearchPlanetResult buscaPorNome(@Param("nome") final String nome);

	@RequestLine("GET /?page={page}")
	@Headers("Accept: application/json")
	SearchPlanetResult listaTodosPaginado(@Param("page") final Long pagina);

	@RequestLine("GET /")
	@Headers("Accept: application/json")
	SearchPlanetResult listaTodos();

}
