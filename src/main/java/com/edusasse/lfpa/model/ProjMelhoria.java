package com.edusasse.lfpa.model;

import com.edusasse.lfpa.calculo.Constants;

public class ProjMelhoria extends Projeto {

	public ProjMelhoria(String nome) {
		super(nome);
	}
	
	public byte getTipo(){
		return Constants.MELHORIA;
	}

}
