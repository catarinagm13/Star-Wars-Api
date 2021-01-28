package com.api.sw;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.api.sw.web.transporte.objetos.RequisicaoNovoPlaneta;
import com.api.sw.web.transporte.objetos.RespostaPesquisaPlaneta;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class PlanetaRestTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before()
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void deveRetornarPlanetaPorId() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		MockHttpServletRequestBuilder requisicao = MockMvcRequestBuilders.get("/planeta/" + 1L);
		ResultActions results = mockMvc.perform(requisicao);
		results.andExpect(MockMvcResultMatchers.status().isOk());
		String jsonResponse = results.andReturn().getResponse().getContentAsString();
		System.out.println(jsonResponse);

		RespostaPesquisaPlaneta resposta = objectMapper.readValue(jsonResponse, RespostaPesquisaPlaneta.class);

		assertNotNull(resposta);
		assertNotNull(resposta.getPlanetas());
		assertThat(resposta.getTotal(), Matchers.equalTo(1L));
		assertThat(resposta.getPlanetas().stream().findFirst().get().getNome(), equalTo("Jakku"));
	}

	@Test
	public void deveRetornarPlanetaPorNome() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		ResultActions results = mockMvc.perform(MockMvcRequestBuilders.get("/planeta/nome/" + "Jakku"));
		results.andExpect(MockMvcResultMatchers.status().isOk());

		String jsonResponse = results.andReturn().getResponse().getContentAsString();
		System.out.println(jsonResponse);

		RespostaPesquisaPlaneta resposta = objectMapper.readValue(jsonResponse, RespostaPesquisaPlaneta.class);

		assertNotNull(resposta);
		assertNotNull(resposta.getPlanetas());
		assertThat(resposta.getTotal(), Matchers.equalTo(1L));
		assertThat(resposta.getPlanetas().stream().findFirst().get().getNome(), equalTo("Jakku"));
	}

	@Test
	public void deveDeletarUmPlaneta() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/planeta/" + 1L)).andExpect(MockMvcResultMatchers.status().isOk());
		mockMvc.perform(MockMvcRequestBuilders.delete("/planeta/" + 1L))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}

	@Test
	public void deveListarTodosPlanetasBancoDeDados() throws Exception {
		MvcResult resultado = mockMvc.perform(MockMvcRequestBuilders.get("/planeta"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		String textoResposta = resultado.getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();

		RespostaPesquisaPlaneta resposta = mapper.readValue(textoResposta, RespostaPesquisaPlaneta.class);

		assertNotNull(resposta);
		assertNotNull(resposta.getPlanetas());
		assertThat(resposta.getTotal(), Matchers.equalTo(4L));
		assertThat(resposta.getPlanetas().stream().map(p -> p.getNome()).collect(Collectors.toList()),
				Matchers.containsInAnyOrder("Jakku", "Shili", "Muunilinst", "Skako"));

	}

	@Test
	public void deveSalvarPlanetaComNumeroAparicoesPreechido() throws Exception {

		RequisicaoNovoPlaneta objetoRequisicao = RequisicaoNovoPlaneta.builder().nome("Alderaan").clima("Arido")
				.terreno("Rochoso").build();

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(objetoRequisicao);

		mockMvc.perform(MockMvcRequestBuilders.post("/planeta").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		MvcResult resultado = mockMvc.perform(MockMvcRequestBuilders.get("/planeta/nome/" + "Alderaan"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		String textoResposta = resultado.getResponse().getContentAsString();

		RespostaPesquisaPlaneta resposta = mapper.readValue(textoResposta, RespostaPesquisaPlaneta.class);

		assertNotNull(resposta);
		assertNotNull(resposta.getPlanetas());
		assertThat(resposta.getTotal(), Matchers.equalTo(1L));
		assertThat(resposta.getPlanetas().stream().findFirst().get().getNome(), equalTo("Alderaan"));
		assertThat(resposta.getPlanetas().stream().findFirst().get().getNumeroAparicoes(), equalTo(2));

	}

	@Test
	public void deveSalvarPlanetaInexistenteApiExternaComNumeroAparicoesPreechidoComZero() throws Exception {

		RequisicaoNovoPlaneta objetoRequisicao = RequisicaoNovoPlaneta.builder().nome("Terra-X").clima("Arido")
				.terreno("Rochoso").build();

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(objetoRequisicao);

		mockMvc.perform(MockMvcRequestBuilders.post("/planeta").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		MvcResult resultado = mockMvc.perform(MockMvcRequestBuilders.get("/planeta/nome/" + "Terra-X"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		String textoResposta = resultado.getResponse().getContentAsString();

		RespostaPesquisaPlaneta resposta = mapper.readValue(textoResposta, RespostaPesquisaPlaneta.class);

		assertNotNull(resposta);
		assertNotNull(resposta.getPlanetas());
		assertThat(resposta.getTotal(), Matchers.equalTo(1L));
		assertThat(resposta.getPlanetas().stream().findFirst().get().getNome(), equalTo("Terra-X"));
		assertThat(resposta.getPlanetas().stream().findFirst().get().getNumeroAparicoes(), equalTo(0));

	}

}
