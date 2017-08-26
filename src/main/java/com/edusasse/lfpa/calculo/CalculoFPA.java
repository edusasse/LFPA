package com.edusasse.lfpa.calculo;

public class CalculoFPA implements Constants {
	private static CalculoFPA tabc = null;

	private CalculoFPA() {

	}

	public static CalculoFPA getInstance() {
		if (tabc == null)
			tabc = new CalculoFPA();

		return tabc;
	}

	// Retorna a complexidade de um AIE/ALI
	private int getComplexidadeALI_AIE(int rl, int dr)
			throws IllegalArgumentException {
		if (rl == 1)
			if (dr <= 19)
				return FUN_SIMPLES;
			else if (dr <= 50)
				return FUN_SIMPLES;
			else
				return FUN_MEDIA;

		else if (rl >= 2 && rl <= 5)
			if (dr <= 19)
				return FUN_SIMPLES;
			else if (dr <= 50)
				return FUN_MEDIA;
			else
				return FUN_COMPLEXA;

		else if (rl >= 6)
			if (dr <= 19)
				return FUN_MEDIA;
			else if (dr <= 50)
				return FUN_COMPLEXA;
			else
				return FUN_COMPLEXA;
		else
			throw new IllegalArgumentException("Registro logico é invalido: "
					+ rl);
	}

	// Retorna a complexidade de um EE
	private int getComplexidadeEE(int rl, int dr)
			throws IllegalArgumentException {
		if (rl == 1)
			if (dr <= 4)
				return FUN_SIMPLES;
			else if (dr <= 15)
				return FUN_SIMPLES;
			else
				return FUN_MEDIA;

		else if (rl == 2)
			if (dr <= 4)
				return FUN_SIMPLES;
			else if (dr <= 15)
				return FUN_MEDIA;
			else
				return FUN_COMPLEXA;

		else if (rl >= 3)
			if (dr <= 4)
				return FUN_MEDIA;
			else if (dr <= 15)
				return FUN_COMPLEXA;
			else
				return FUN_COMPLEXA;

		else
			throw new IllegalArgumentException("Registro logico é invalido: "
					+ rl);
	}

	// Retorna a complexidade de um SE/CE
	private int getComplexidadeSE_CE(int rl, int dr)
			throws IllegalArgumentException {
		if (rl == 1)
			if (dr <= 5)
				return FUN_SIMPLES;
			else if (dr <= 19)
				return FUN_SIMPLES;
			else
				return FUN_MEDIA;

		else if (rl == 2 || rl == 3)
			if (dr <= 5)
				return FUN_SIMPLES;
			else if (dr <= 19)
				return FUN_MEDIA;
			else
				return FUN_COMPLEXA;

		else if (rl >= 3)
			if (dr <= 5)
				return FUN_MEDIA;
			else if (dr <= 19)
				return FUN_COMPLEXA;
			else
				return FUN_COMPLEXA;

		else
			throw new IllegalArgumentException("Registro logico é invalido: "
					+ rl);
	}

	public int getComplexidade(byte TIPO, int td, int tr) {
		if (TIPO == Constants.ALI || TIPO == Constants.AIE)
			return this.getComplexidadeALI_AIE(td, tr);
		if (TIPO == Constants.EE)
			return this.getComplexidadeEE(td, tr);
		if (TIPO == Constants.CE || TIPO == Constants.SE)
			return this.getComplexidadeSE_CE(td, tr);

		return Integer.MIN_VALUE;
	}

	// ** Retorna a com
	public int getValComplexidade(byte TIPO, int complexidade) {
		return FUN_TAB_CONTRIBUICAO[TIPO][complexidade];
	}

	// ** Calcula fatort de ajuste */
	public float calcFatorAjuste(int totNiveisInfluencia) {
		return (totNiveisInfluencia * 0.01f) + 0.65f;
	}

	// ** Calula o total de Pontos de Funcao Ajustados */
	public float calcPFAjustados(int totPF, float fatAjuste) {
		return (float) fatAjuste * totPF;
	}

	// ** Calcula o custo do sistema */
	public float calcCustoSistema(float PFAjustados, float PFhora,
			float cstHrPessoa) throws NumberFormatException {
		try {
			if (PFhora == 0)
				throw new IllegalArgumentException(
						"Erro: nao é permitido 0(zero em Pontos de Funcao por Hora");
			return (PFAjustados / PFhora) * cstHrPessoa;
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException("Erro: " + nfe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	// ** Calcula projeto de melhoria */
	public float calcProjetoMelhoria(int ADD, int CHGA, int DEL, int CFP,
			float VAFA, float VAFB) {
		return (float) (((ADD + CHGA + CFP) * VAFA) + (DEL * VAFB));
	}
}
