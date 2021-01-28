package com.api.sw.web.transporte.objetos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequisicaoNovoPlaneta implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String clima;
	private String terreno;

}
