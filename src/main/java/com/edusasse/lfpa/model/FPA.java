package com.edusasse.lfpa.model;

import com.edusasse.lfpa.calculo.Constants;

public class FPA {
	private final byte tipo_fpa; 
	private final String texto;
	private final int valorTD;
	private final int valorTR;
	
	public FPA(byte tipo_fpa,String texto, int valorTD, int valorTR){
		this.tipo_fpa = tipo_fpa;
		this.texto = texto;
		this.valorTD = valorTD;
		this.valorTR = valorTR;
	}
	
	public String getTexto() {
		return texto;
	}
	public byte getTipo_fpa() {
		return tipo_fpa;
	}
	
	public int getValorTD() {
		return valorTD;
	}
	
	public int getValorTR() {
		return valorTR;
	}
	
	public int getTipo(){
		return Constants.DESENVOLVIMENTO;
	}
	
	public String toString(){
		return Constants.FPAS[this.getTipo_fpa()] + " " + (this.texto == null || this.texto.equals("") ? "" : this.texto) + " (" + this.valorTD + "," + valorTR + ")";
	}
	
	public byte getAcao(){
		return -1;
	}
}
