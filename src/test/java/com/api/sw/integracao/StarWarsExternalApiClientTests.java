package com.api.sw.integracao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.sw.client.PlanetsClient;
import com.api.sw.client.schema.SearchPlanetResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integracao")
public class StarWarsExternalApiClientTests {

	@Autowired
	private PlanetsClient client;

	@Test
	public void deveRealizarPesquisaPorNomeComSucesso() {

		SearchPlanetResult resultado = client.buscaPorNome("Tatooine");

		assertNotNull(resultado);
		assertNotNull(resultado.getResults());
		assertThat(resultado.getCount(), equalTo(1L));
		assertThat(resultado.getResults().size(), equalTo(1));
		assertThat(resultado.getResults().stream().findFirst().get().getName(), equalToIgnoringCase("tatooine"));

	}

}
