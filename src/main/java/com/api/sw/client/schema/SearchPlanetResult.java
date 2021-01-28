package com.api.sw.client.schema;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPlanetResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long count;
	private String next;
	private String previous;

	private Collection<Planet> results;

	public Optional<Integer> extraiNumeroProximaPagina() {
		if (next != null && !next.trim().isEmpty()) {
			return estraiPagina(next);
		}
		return Optional.empty();
	}

	public Optional<Integer> extraiNumeroPaginaAnterior() {
		if (previous != null && !previous.trim().isEmpty()) {
			return estraiPagina(previous);
		}
		return Optional.empty();
	}

	private Optional<Integer> estraiPagina(String url) {
		String[] partes = url.split("\\?");
		String[] queries = partes[1].split("=");
		Integer pagina = queries != null && queries.length > 1 ? Integer.valueOf(queries[1]) : null;
		return Optional.ofNullable(pagina);
	}

}
