package com.edusasse.lfpa.calculo;

public interface Constants {
	// Funcao com cada indece
	// [0] - Simples
	// [1] - Media
	// [2] - Complexa
	public final byte FUN_SIMPLES = 0;
	public final byte FUN_MEDIA = 1;
	public final byte FUN_COMPLEXA = 2;

	public final byte ALI = 0;
	public final byte AIE = 1;
	public final byte EE = 2;
	public final byte SE = 3;
	public final byte CE = 4;

	public final byte ADD = 0;
	public final byte CHGA = 1;
	public final byte DEL = 2;
	
	public final byte VAF = 0;
	public final byte VAFA = 1;
	public final byte VAFB = 2;
	public final byte UFPC = 3;
	public final byte CHGB = 4;
	public final byte UFP = 5;
	public final byte CFP = 6;	

	public final byte DESENVOLVIMENTO = 0;
	public final byte MELHORIA = 1;

	public final byte[][] FUN_TAB_CONTRIBUICAO = { 
			{ 7, 10, 15 }, 
			{ 5, 7, 10 },
			{ 3, 4, 6 }, 
			{ 4, 5, 7 }, 
			{ 3, 4, 6 } };

	public final String[] FPAS = { "ALI", "AIE", "EE", "SE", "CE" };
	public final String[] ACOES = { "ADD", "CHGA", "DEL" };
	public final String[] TIPO_PROJETO = { "DESENVOLVIMENTO", "MELHORIA" };
	public final String[] VARIAVEIS = { "VAF","VAFA","VAFB","UFPC","CHGB","UFP","CFP" };
	// Niveis
	public final String[] DESC_NIVEIS_COMPLEXIDADE = {
			"Comunicacao De Dados...............",
			"Processamento Distribuido..........",
			"Performance........................",
			"Configuracao Altamente Utilizada...", 
			"Taxa De Transacoes.................",
			"Entrada De Dados On Line...........",
			"Eficiencia Do Usuario Final........",
			"Atualizacao On Line................",
			"Complexidade De Processamento......",
			"Reutilizacao.......................", 
			"Facilidade De Instalacao...........",
			"Facilidade De Operacao.............",
			"Multiplas Localidades..............",
			"Facilidade De Mudancas............." };

}
