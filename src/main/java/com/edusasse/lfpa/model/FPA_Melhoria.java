package com.edusasse.lfpa.model;

import com.edusasse.lfpa.calculo.Constants;

public class FPA_Melhoria extends FPA {
	private final byte acao;
	
	public FPA_Melhoria(byte acao, byte tipo_fpa, String texto, int valorTD, int valorTR) {
		super(tipo_fpa, texto, valorTD, valorTR);
		this.acao = acao;
	}

	public byte getAcao() {
		return acao;
	}
	
	public int getTipo(){
		return Constants.MELHORIA;
	}
	
	@Override
	public String toString(){
		return Constants.ACOES[this.acao] + "\t"+ Constants.FPAS[this.getTipo_fpa()] + " " + (super.getTexto() == null || super.getTexto().equals("") ? "" : super.getTexto()) + " (" + super.getValorTD() + "," + super.getValorTR() + ")";
	}
}
