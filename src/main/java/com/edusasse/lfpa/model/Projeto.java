package com.edusasse.lfpa.model;

import java.util.HashMap;

import com.edusasse.lfpa.calculo.Constants;

public abstract class Projeto {
	private final String nome;
	private HashMap<String,Programa> mapProg;
	private float[] listaValorVars;
	
	public Projeto(String nome){
		this.nome = nome;
		this.mapProg = new HashMap<String,Programa>();
		this.listaValorVars = new float[7];
	}
	
	public HashMap<String,Programa> getMapaProgramas() {
		return this.mapProg;
	}
	
	public Programa getPrograma(String nomePrograma) {
		return this.mapProg.get(nomePrograma);
	}
	
	/** Adiciona programa a lista */
	public void addProg(Programa p) {
		this.mapProg.put(p.getNome(), p);
	}
	
	public String getNome() {
		return nome;
	}
	
	
	@Override
	public String toString(){
		String ret = "Projeto" + this.nome.substring(this.nome.indexOf(" "),this.nome.length()) + " tipo: " + Constants.TIPO_PROJETO[getTipo()] + "\n";
		ret += "* Variaveis:\n";
		for (int i = 0; i < 7; i++){
			ret += "\t" + Constants.VARIAVEIS[i] + "\t: "+ this.listaValorVars[i]+"\n";
		}
		return ret;
	}
	
	public float getValorVariavel(byte VARIAVEL){
		return this.listaValorVars[VARIAVEL];
	}
	
	public void setValorVariavel(byte ind, float valor) {
		this.listaValorVars[ind] = valor;
	}
	
	abstract public byte getTipo();
}
