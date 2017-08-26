package com.edusasse.lfpa.gals;

import com.edusasse.lfpa.calculo.CalculoFPA;
import com.edusasse.lfpa.calculo.ControlaCalc;
import com.edusasse.lfpa.model.FPA;
import com.edusasse.lfpa.model.FPA_Melhoria;
import com.edusasse.lfpa.model.Programa;
import com.edusasse.lfpa.model.ProjDesenvolvimento;
import com.edusasse.lfpa.model.ProjMelhoria;
import com.edusasse.lfpa.model.Projeto;

public class Semantico implements Constants {
	private String projetoAtual = null;
	private String programaAtual = null;
	private String textoAtual = "";
	private boolean programaEstende = false;

	// indeces
	private byte nivelComplexidadeAtual = -1;
	private byte acaoMelhoriaAtual = -1;
	private byte origemAtual = -1;
	private byte varAtual = -1;

	// valores
	private int valorAtualTR = 0;
	private int valorAtualTD = 0;
	private int multiplicador = 1;
	private byte valorNivel = -1;

	// flags
	private boolean declarandoFPAs = false;
	private boolean declarandoNiveisDeComplexidade = false;
	private boolean estaNumGrupo = false;
	private boolean vaiMultiplicar = false;

	public void executeAction(int action, Token token) throws SemanticError {
		switch (action) {
		case 1:
			this.guardaNomeProjeto(token.getLexeme());
			break;
		case 2:
			this.criaProjetoMelhoria(projetoAtual);
			break;
		case 3:
			this.criaProjetoDesenvolvimento(projetoAtual);
			break;
		case 4:
			this.guardaNomePrograma(token.getLexeme());
			break;
		case 5:
			this.programaEstende = true;
			break;
		case 6:
			this.programaAtualEstendeDe(token.getLexeme());
			break;
		case 7:
			this.declarandoFPAs = true;
			break;
		case 8:
			this.declarandoNiveisDeComplexidade = true;
			break;
		case 9:
			this.acaoMelhoria(token.getLexeme());
			break;
		case 10:
			this.origem(token.getLexeme());
			break;
		case 11:
			this.estaNumGrupo = true;
			break;
		case 12:
			this.textoAtual = token.getLexeme();
			break;
		case 13:
			this.setValorAtualTD(token.getLexeme());
			break;
		case 14:
			this.setValorAtualTR(token.getLexeme());
			break;
		case 15:
			this.valorAtualTD = 1; // Valor default caso nao informado
			break;
		case 16:
			this.vaiMultiplicar = true;
			break;
		case 17:
			if (this.vaiMultiplicar) {
				this.multiplicador = Integer.parseInt(token.getLexeme());
				this.addFPAaoProg();
			} else
				; // Errp
			break;
		case 18:
			this.estaNumGrupo = false;
			break;
		case 19:
			this.armazenaIndNivelComplexidadeAtual(token.getLexeme());
			break;
		case 20:
			this.recebeIntNivel(token.getLexeme());
			break;
		case 21:
			this.guardaTipoVariavel(token.getLexeme());
			break;
		case 22:
			this.recebeValorVariavel(token.getId(), token.getLexeme());
			break;
		case 23:
			this.multiplicador = 1;
			this.addFPAaoProg();
			break;
		case 98:
			this.programaEncerrado();
			break;
		case 99:
			this.projetoEncerrado();
			break;
		}
	}

	// 1
	private void guardaNomeProjeto(String nome) {
		this.projetoAtual = nome;
		 ControlaCalc.getInstance().setProjetoAtual(nome);
	}

	// 2
	private void criaProjetoMelhoria(String nome) {
		ControlaCalc.getInstance().addProjeto(new ProjMelhoria(nome));
	}

	// 3
	private void criaProjetoDesenvolvimento(String nome) {
		ControlaCalc.getInstance().addProjeto(new ProjDesenvolvimento(nome));
	}

	// 4
	private void guardaNomePrograma(String nome) {
		this.programaAtual = nome;

		Projeto patual = ControlaCalc.getInstance().getProjeto(
				this.projetoAtual);

		if (patual.getPrograma(nome) != null)
			return; // Erro semantico, programa ja declarado

		// Adiciona o programa ao projeto
		patual.addProg(new Programa(this.programaAtual));
	}

	// 6
	private void programaAtualEstendeDe(String nome) {
		if (programaEstende) {
			// Adiciona o programa ao projeto
			ControlaCalc.getInstance().getProjeto(this.projetoAtual)
					.getPrograma(this.programaAtual).setProgEstende(nome);
		} else
			; // Erro
	}

	// 9
	public void acaoMelhoria(String acao) {
		if (acao.equals("ADD"))
			this.acaoMelhoriaAtual = com.edusasse.lfpa.calculo.Constants.ADD;
		else if (acao.equals("CHGA"))
			this.acaoMelhoriaAtual = com.edusasse.lfpa.calculo.Constants.CHGA;
		else
			this.acaoMelhoriaAtual = com.edusasse.lfpa.calculo.Constants.DEL;

	}

	// 10
	private void origem(String org) {

		if (org.toUpperCase().equals("ALI"))
			this.origemAtual = com.edusasse.lfpa.calculo.Constants.ALI;
		else if (org.toUpperCase().equals("AIE"))
			this.origemAtual = com.edusasse.lfpa.calculo.Constants.AIE;
		else if (org.toUpperCase().equals("EE"))
			this.origemAtual = com.edusasse.lfpa.calculo.Constants.EE;
		else if (org.toUpperCase().equals("SE"))
			this.origemAtual = com.edusasse.lfpa.calculo.Constants.SE;
		else
			this.origemAtual = com.edusasse.lfpa.calculo.Constants.CE;
	}

	// 13
	private void setValorAtualTR(String v) {
		try {
			this.valorAtualTR = Integer.parseInt(v);
		} catch (NumberFormatException nfe) {
			; // Erro
		}
	}

	// 14
	private void setValorAtualTD(String v) {
		try {
			this.valorAtualTD = Integer.parseInt(v);
		} catch (NumberFormatException nfe) {
			; // Erro
		}
	}

	// 17
	private void addFPAaoProg() {
		Programa paux = ControlaCalc.getInstance()
				.getProjeto(this.projetoAtual).getPrograma(this.programaAtual);
		for (int i = 0; i < this.multiplicador; i++) {
			// Adiciona o programa ao projeto
			if (ControlaCalc.getInstance().getProjeto(this.projetoAtual)
					.getTipo() == com.edusasse.lfpa.calculo.Constants.MELHORIA) {
				paux.addFPA(new FPA_Melhoria((byte) this.acaoMelhoriaAtual,
						(byte) this.origemAtual, this.textoAtual,
						this.valorAtualTD, this.valorAtualTR));
			} else if (ControlaCalc.getInstance().getProjeto(this.projetoAtual)
					.getTipo() == com.edusasse.lfpa.calculo.Constants.DESENVOLVIMENTO) {
				paux.addFPA(new FPA((byte) this.origemAtual, this.textoAtual,
						this.valorAtualTD, this.valorAtualTR));
			}
		}
		this.textoAtual = "";
	}

	// 19
	private void armazenaIndNivelComplexidadeAtual(String comp) {
		byte i = 0;
		if (comp.length() <= 3) {
			this.nivelComplexidadeAtual = Byte.parseByte(comp.replace(":", ""));
			this.nivelComplexidadeAtual--; // pois indece
		} else {
			for (i = 0; (listaNiv[i].equals(comp)) && i < 14; i++)
				;
			this.nivelComplexidadeAtual = i;
		}

	}

	// 20
	private void recebeIntNivel(String v) {
		byte val = Byte.parseByte(v);
		if (val < 0 || val > 5)
			; // Erro

		Programa paux = ControlaCalc.getInstance()
				.getProjeto(this.projetoAtual).getPrograma(this.programaAtual);
		paux.setValorNivelComplexidade(this.nivelComplexidadeAtual, val);
	}

	// 21
	private void guardaTipoVariavel(String v) {
		byte i = 0;
		for (i = 0; !com.edusasse.lfpa.calculo.Constants.VARIAVEIS[i].equals(v
				.toUpperCase()); i++)

			this.varAtual = (byte) (i + 1);

	}

	// 22
	private void recebeValorVariavel(int classId, String v) {
		Projeto paux = ControlaCalc.getInstance().getProjeto(this.projetoAtual);
		if (classId == 5) //real
			paux.setValorVariavel(this.varAtual, Float.parseFloat(v));
		else
			paux.setValorVariavel(this.varAtual, Integer.parseInt(v));

	}

	// 98
	private void programaEncerrado() {
		this.programaAtual = null;
		this.textoAtual = "";
		this.programaEstende = false;

		// indeces
		this.nivelComplexidadeAtual = -1;
		this.acaoMelhoriaAtual = -1;
		this.origemAtual = -1;

		// valores
		this.valorAtualTR = 0;
		this.valorAtualTD = 0;
		this.multiplicador = 1;
		this.valorNivel = -1;
		this.varAtual = -1;

		// flags
		this.declarandoFPAs = false;
		this.declarandoNiveisDeComplexidade = false;
		this.estaNumGrupo = false;
		this.vaiMultiplicar = false;
	}

	// 99
	private void projetoEncerrado() {
		ControlaCalc.getInstance().estenderProgramas();
		ControlaCalc.getInstance().calculaFPA(projetoAtual);
	}

	// Niveis
	private final String[] listaNiv = { ":comunicacao_de_dados",
			":processamento_distribuido", ":performance",
			":configuracao_altamente_utilizada", ":taxa_de_transacoes",
			":entrada_de_dados_on_line", ":eficiencia_do_usuario_final",
			":atualizacao_on_line", ":complexidade_de_processamento",
			":reutilizacao", ":facilidade_de_instalacao",
			":facilidade_de_operacao", ":multiplas_localidades",
			":facilidade_de_mudancas" };

}
