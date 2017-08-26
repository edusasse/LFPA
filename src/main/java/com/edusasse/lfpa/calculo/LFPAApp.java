package com.edusasse.lfpa.calculo;

import javax.swing.JOptionPane;

public class LFPAApp implements Constants, OuvintesCompilador {

	/**
	 * @param args
	 */
	public LFPAApp() {

		Facade f = new Facade();
		f.addOuvinte(this);
		f.analisa(JOptionPane.showInputDialog(null));
		System.out.println(ControlaCalc.getInstance().toString());
		Resultado r = ControlaCalc.getInstance().getResultado("projeto dudu");
		System.out.println(
		CalculoFPA.getInstance().calcProjetoMelhoria(r.getADD(), r.getCHGA(), r.getDEL(), r.getCFP(), r.getVAFA(), r.getVAFB())
				
	);
		}

	public static void main(String[] args) {
		new LFPAApp();

	}

	@Override
	public void emiteMensagemCompilacao(String msg) {
		System.out.println(msg);

	}

	@Override
	public void exibeDadosDeSaida(String msg, boolean analisouComSucesso) {
		System.out.println(msg);

	}

	@Override
	public void exibeDadosDeSaida(String msg) {
		System.out.println(msg);

	}

	@Override
	public void setSelecaoErroNoEditor(int ini, int end, int TIPO) {
		// TODO Auto-generated method stub

	}

}
