package com.api.sw;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.sw.client.PlanetsClient;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;

@Configuration
@EnableFeignClients
public class AppFeingConfig {

	@Bean
	public PlanetsClient planetsClient() {

		OkHttpClient okHttpClient = new OkHttpClient(
				new okhttp3.OkHttpClient.Builder().hostnameVerifier((s, sslSession) -> true).build());

		PlanetsClient planetsClient = Feign.builder().client(okHttpClient).encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder()).target(PlanetsClient.class, "https://swapi.dev/api/planets");

		return planetsClient;

	}

}
