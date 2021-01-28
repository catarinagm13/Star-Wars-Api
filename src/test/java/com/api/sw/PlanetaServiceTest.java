package com.api.sw;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.api.sw.modelo.Planeta;
import com.api.sw.services.PlanetaService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("teste")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup(value = { "/datasets/planetas.xml" })
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { "/datasets/planetas.xml" })
public class PlanetaServiceTest {
	
	@Autowired
	private PlanetaService planetaService;
	
	@Test
	public void deveSalvarPlanetaComNumeroAparicoesPreenchido() {
		
		Planeta planeta = Planeta.builder().nome("Alderaan").clima("Arido").terreno("Rochoso").build();
		Planeta planetaBancoDeDados = planetaService.salva(planeta);
		
		Assert.assertNotNull(planetaBancoDeDados);
		Assert.assertNotNull(planetaBancoDeDados.getId());
		Assert.assertNotNull(planetaBancoDeDados.getNumeroAparicoes());
		MatcherAssert.assertThat(planetaBancoDeDados.getNumeroAparicoes(), Matchers.equalTo(2));
		
	}

}
