package com.api.sw.web.transporte.objetos;

import java.util.ArrayList;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespostaPesquisaPlaneta {

	private Long total;
	private String proxima;
	private String anterior;
	
	private Collection<RespostaPlaneta> planetas;
	
	public void adicionaPlaneta(final RespostaPlaneta planeta) {
		if (planetas == null) {
			planetas = new ArrayList<RespostaPlaneta>();
		}
		planetas.add(planeta);
	}

}
