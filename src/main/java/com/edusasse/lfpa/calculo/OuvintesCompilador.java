package com.edusasse.lfpa.calculo;


public interface OuvintesCompilador {
	public final byte LEXICO =0;
	public final byte SINTATICO =1;
	public final byte SEMANTICO =2;
	
	public void emiteMensagemCompilacao(String msg);
	public void exibeDadosDeSaida(String msg, boolean analisouComSucesso);
	public void exibeDadosDeSaida(String msg);
	public void setSelecaoErroNoEditor(int ini, int end, int TIPO);
}
