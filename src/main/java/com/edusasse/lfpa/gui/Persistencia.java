package com.edusasse.lfpa.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public final class Persistencia {
	private static Persistencia persistencia = null;
	private BufferedReader br;
	
	private Persistencia(){}
	
	public static Persistencia getInstancia(){
		if (persistencia == null)
			persistencia = new Persistencia();
		
		return persistencia;
	}
	
	// ** Faz a leitura do codigo fonte */
	public String lerArquivoFonte(String sourcePath) {
		String linhas = "";
		try {
			this.br = new BufferedReader(new FileReader(sourcePath));
			while (br.ready())
				linhas += br.readLine() + "\n";
			br.close();
		} catch (IOException e) {
			// Colocar mensagem
		}
		
		return linhas;
	}
	public void fechaArquivoCorrente(){
		// Implementar lock em arquivo aberto
		
	}
	
	// ** Faz a leitura do codigo fonte */
	public void gravarArquivoFonte(String sourceCode, String sourcePath) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(sourcePath));
			fos.write(sourceCode.getBytes());
			fos.close();
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
