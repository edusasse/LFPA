package com.edusasse.lfpa.calculo;

import java.util.HashMap;

import com.edusasse.lfpa.model.FPA;
import com.edusasse.lfpa.model.Programa;
import com.edusasse.lfpa.model.Projeto;

public class ControlaCalc {
	private static ControlaCalc cc = null;
	private HashMap<String, Projeto> mapProj;
	private HashMap<String, Resultado> mapProjResult;
	  private String projetoAtual;


	private ControlaCalc() {
		this.mapProj = new HashMap<String, Projeto>();
		this.mapProjResult = new HashMap<String, Resultado>();
	}


    public void setProjetoAtual(String projetoAtual) {
        this.projetoAtual = projetoAtual;
    }
    
	// ** Pega a instancia da Controladora de Calculo */
	public static ControlaCalc getInstance() {
		if (cc == null)
			cc = new ControlaCalc();
		return cc;
	}
	
	   public String getProjetoAtual() {
	        return this.projetoAtual;
	    }

	// ** Retorna o projeto baseado no nome/chave do mesmo */
	public Projeto getProjeto(String nomeProjeto) {
		return this.mapProj.get(nomeProjeto);
	}

	// ** Adiciona um projeto a lista */
	public void addProjeto(Projeto o) {
		this.mapProj.put(o.getNome(), o);
	}

	// ** Efetua o calculo do FPA */
	public void calculaFPA(String nomeProjeto) {
		if (this.mapProj.get(nomeProjeto).getTipo() == Constants.MELHORIA)
			this.calcProjMelhoria(nomeProjeto);
		else if (this.mapProj.get(nomeProjeto).getTipo() == Constants.DESENVOLVIMENTO)
			this.calcProjDesenvolvimento(nomeProjeto);

	}

	private void calcProjMelhoria(String proj) {
		Resultado r = new Resultado();
		
		java.util.Iterator<Programa> iProg = this.mapProj.get(proj).getMapaProgramas().values().iterator();
		// Para cada programa
		while (iProg.hasNext()) {
			Programa pp = iProg.next();
			java.util.Iterator<FPA> iFPA = pp.getListaFPAs().iterator();
			while (iFPA.hasNext()) {
				FPA f = iFPA.next();

				int valComp = CalculoFPA.getInstance().getComplexidade(
						f.getTipo_fpa(), f.getValorTD(), f.getValorTR());
				int novoValor = CalculoFPA.getInstance().getValComplexidade(
						f.getTipo_fpa(), valComp);

				if (f.getAcao() == Constants.ADD)
					r.addADD(novoValor);
				else if (f.getAcao() == Constants.DEL)
					r.addDEL(novoValor);
				else if (f.getAcao() == Constants.CHGA)
					r.addCHGA(novoValor);
			}
		}
		r.addCFP((int) this.mapProj.get(proj).getValorVariavel(Constants.CFP));
		r.addVAFA(this.mapProj.get(proj).getValorVariavel(Constants.VAFA)); 
		r.addVAFB(this.mapProj.get(proj).getValorVariavel(Constants.VAFB));
		
		this.mapProjResult.put(proj, r);
	}

	private void calcProjDesenvolvimento(String proj) {
		Resultado r = new Resultado();

		// FPA
		java.util.Iterator<Programa> iProg = this.mapProj.get(proj)
				.getMapaProgramas().values().iterator();
		// Para cada programa
		while (iProg.hasNext()) {
			Programa pp = iProg.next();
			java.util.Iterator<FPA> iFPA = pp.getListaFPAs().iterator();
			while (iFPA.hasNext()) {
				FPA f = iFPA.next();

				int valComp = CalculoFPA.getInstance().getComplexidade(
						f.getTipo_fpa(), f.getValorTD(), f.getValorTR());
				r.addValFPA(CalculoFPA.getInstance().getValComplexidade(
						f.getTipo_fpa(), valComp));

			}
		}

		// NC
		java.util.Iterator<Programa> listaProgs = this.mapProj.get(proj)
				.getMapaProgramas().values().iterator();
		while (listaProgs.hasNext()) {
			Programa p = listaProgs.next();
			for (int i = 0; i < 14; i++)
				r.addValNC(p.getNivelComplexidade()[i]);
		}
		this.mapProjResult.put(proj, r);	
	}

	// ** Faz com que os programas estendam de outros*/
	public void estenderProgramas() {
		java.util.Iterator<Projeto> iProj = this.mapProj.values().iterator();
		// Para cada projeto
		while (iProj.hasNext()) {
			Projeto p = iProj.next();
			java.util.Iterator<Programa> iProg = p.getMapaProgramas().values()
					.iterator();
			// Para cada programa
			while (iProg.hasNext()) {
				Programa pp = iProg.next();
				if (pp.getProgEstende() != null) {
					Programa pext = p.getPrograma(pp.getProgEstende());
					if (pext == null)
						return; // Erro
					// Passa os FPA para o programa pp
					java.util.Iterator<FPA> iPest = pext.getListaFPAs()
							.iterator();
					while (iPest.hasNext()) {
						pp.addFPA(iPest.next());
					}
					// Passa os niveis de complexidade para o programa
					for (byte i = 0; i < 14; i++) {
						if (pext.getNivelComplexidade()[i] != 0
								&& pp.getNivelComplexidade()[i] == 0)
							pp.setValorNivelComplexidade(i, pext
									.getNivelComplexidade()[i]);
					}

				}
			}

		}
	}

	@Override
	public String toString() {
		String ret = "";

		java.util.Iterator<Projeto> iProj = this.mapProj.values().iterator();
		while (iProj.hasNext()) {
			Projeto p = iProj.next();
			ret += p.toString() + "\n";
			java.util.Iterator<Programa> iProg = p.getMapaProgramas().values()
					.iterator();

			while (iProg.hasNext()) {
				Programa pp = iProg.next();
				ret += "\t" + pp.toString() + "\n";
				java.util.Iterator<FPA> iFPA = pp.getListaFPAs().iterator();
				while (iFPA.hasNext()) {
					FPA f = iFPA.next();
					ret += "\t\t" + f.toString() + "\n";
				}
			}

		}
		return ret;
	}
	
	public Resultado getResultado(String proj){
		return this.mapProjResult.get(proj);		
	}
}
