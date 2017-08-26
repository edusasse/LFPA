package com.edusasse.lfpa.model;

import java.util.ArrayList;

import com.edusasse.lfpa.calculo.Constants;

public class Programa {
	private final String nome;
	private ArrayList<FPA> listaFPAs = null;
	private String estende;
	private byte[] nivelComplexidade;

	public Programa(String nome) {
		this.nome = nome;
		this.listaFPAs = new ArrayList<FPA>();
		this.estende = null;
		this.nivelComplexidade = new byte[14];
	}

	public ArrayList<FPA> getListaFPAs() {
		return listaFPAs;
	}

	// ** Adiciona um FPA */
	public void addFPA(FPA o) {
		this.listaFPAs.add(o);
	}

	public String getNome() {
		return nome;
	}

	public void setProgEstende(String nomeProg) {
		this.estende = nomeProg;
	}
	
	public void setValorNivelComplexidade(byte pos, byte valor){
		if ( (pos < 0 || pos > 13) || (valor < 0 || valor > 5) )
			throw new IllegalArgumentException("Valor para nivel de complexidade é invalido");
		this.nivelComplexidade[pos] = valor;
		
	}
	
	public String getProgEstende() {
		return estende;
	}
	
	public byte[] getNivelComplexidade() {
		return nivelComplexidade;
	}
	
	@Override
	public String toString() {
		String aux = "";
		aux += "Programa" + this.nome.substring(this.nome.indexOf(" "), this.nome.length());
		if (this.estende != null)
			aux += " estende de " + this.estende + "";
		aux +="\n";
		for (int i = 0 ; i < 14; i++){
			aux += " \t\t" + Constants.DESC_NIVEIS_COMPLEXIDADE[i] + ": " + this.nivelComplexidade[i] + ";\n";
		}
		
		return aux;
	}
}
