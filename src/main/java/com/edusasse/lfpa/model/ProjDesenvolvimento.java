package com.edusasse.lfpa.model;

import com.edusasse.lfpa.calculo.Constants;

public class ProjDesenvolvimento extends Projeto {

	public ProjDesenvolvimento(String nome) {
		super(nome);
	}

	public byte getTipo(){
		return Constants.DESENVOLVIMENTO;
	}
}
