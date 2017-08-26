package com.edusasse.lfpa.calculo;

public class Resultado {
	private int totalNC = 0;
	private int totalFPA = 0;
	
	private int ADD = 0;
	private int DEL = 0;
	private int CHGA = 0;
	private int CHGB = 0;
	
	private int CFP = 0;
	
	private float VAFA = 0;
	private float VAFB = 0;
	private float UFPB = 0;
	
	public Resultado(){
		
	}
	public Resultado(int totalNC, int totalFPA){
		this.totalNC = totalNC;
		this.totalFPA = totalFPA;
	}
	
	
	void addValFPA(int totalFPA) {
		this.totalFPA += totalFPA;
	} 
	void addValNC(int totalNC) {
		this.totalNC += totalNC;
	}
	void addADD(int add) {
		ADD += add;
	}
	void addCFP(int cfp) {
		CFP += cfp;
	}
	void addCHGA(int chga) {
		CHGA += chga;
	}
	void addCHGB(int chgb) {
		CHGB += chgb;
	}
	void addDEL(int del) {
		DEL += del;
	}
	void addUFPB(float ufpb) {
		UFPB += ufpb;
	}
	void addVAFA(float vafa) {
		VAFA += vafa;
	}
	void addVAFB(float vafb) {
		VAFB += vafb;
	}
	
	public int getTotalFPA() {
		return totalFPA;
	}
	public int getTotalNC() {
		return totalNC;
	}	
	public int getADD() {
		return ADD;
	}
	public int getCFP() {
		return CFP;
	}
	public int getCHGA() {
		return CHGA;
	}
	public int getCHGB() {
		return CHGB;
	}
	public int getDEL() {
		return DEL;
	}
	public float getUFPB() {
		return UFPB;
	}
	public float getVAFA() {
		return VAFA;
	}
	public float getVAFB() {
		return VAFB;
	}
}
