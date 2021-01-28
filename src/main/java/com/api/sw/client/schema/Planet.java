package com.api.sw.client.schema;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planet implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String climate;
	private String terrain;
	private Collection<String> films;

	public Long obtemNumeroAparicoes() {
		if (films != null) {
			return films.stream().count();
		}
		return 0L;
	}

}
