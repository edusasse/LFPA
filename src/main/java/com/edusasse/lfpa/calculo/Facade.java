package com.edusasse.lfpa.calculo;

import java.util.ArrayList;
import java.util.Iterator;

import com.edusasse.lfpa.gals.LexicalError;
import com.edusasse.lfpa.gals.Lexico;
import com.edusasse.lfpa.gals.SemanticError;
import com.edusasse.lfpa.gals.Semantico;
import com.edusasse.lfpa.gals.Sintatico;
import com.edusasse.lfpa.gals.SyntaticError;
import com.edusasse.lfpa.gals.Token;

public class Facade {
    private Lexico lexico;
    private ArrayList<OuvintesCompilador> listaOuvintes;
    private StringBuilder sb;
    private Semantico semantico;
    private Sintatico sintatico;
    private Token token;

    public Facade() {
        this.sb = new StringBuilder();
        this.lexico = new Lexico();
        this.sintatico = new Sintatico();
        this.semantico = new Semantico();
    }

    public void limpaCodigo() {
        this.sb.setLength(0);
    }

    public boolean analisa(String codigo) {
        limpaCodigo();
        this.lexico.setInput(codigo);
        boolean analisouComSucesso = analisaLexico();
        this.lexico.setInput(codigo);
        if (analisouComSucesso) {
            analisouComSucesso = analisaSintatico();
            if (analisouComSucesso) {
                avisaCompilou("Arquivo compilado com sucesso!");
                avisaInformacoesTokens("A compila\u00e7\u00e3o foi realizada com sucesso!", analisouComSucesso);
                return true;
            }
            avisaInformacoesTokens("A compila\u00e7\u00e3o nao teve sucesso!", analisouComSucesso);
            avisaCompilou("Houve um erro SINTATICO de Compila\u00e7\u00e3o!");
            return false;
        }
        avisaInformacoesTokens("A compila\u00e7\u00e3o nao teve sucesso!", analisouComSucesso);
        avisaCompilou("Houve um erro LEXICO de Compila\u00e7\u00e3o!");
        return false;
    }

    private boolean analisaLexico() {
        try {
            this.token = this.lexico.nextToken();
            while (this.token != null) {
                this.token = this.lexico.nextToken();
            }
            avisaInformacoesTokens(this.sb.toString());
            return true;
        } catch (LexicalError le) {
            this.sb.append("Erro na linha: <" + this.lexico.linhaAtual + "> - " + le.getMessage() + "\n");
            avisaInformacoesTokens(this.sb.toString());
            return false;
        }
    }

    private boolean analisaSintatico() {
        try {
            this.sintatico.parse(this.lexico, this.semantico);
            return true;
        } catch (SyntaticError e) {
            this.sb.append("Erro na linha: [" + this.lexico.linhaAtual + "] - " + e.getCause() + " " + e.getMessage() + "\n");
            avisaInformacoesTokens(this.sb.toString());
            return false;
        } catch (LexicalError e2) {
            avisaInformacoesTokens(this.sb.toString());
            return false;
        } catch (SemanticError e3) {
            return false;
        }
    }

    public void addOuvinte(OuvintesCompilador o) {
        if (this.listaOuvintes == null) {
            this.listaOuvintes = new ArrayList();
        }
        this.listaOuvintes.add(o);
    }

    private void avisaCompilou(String msg) {
        Iterator i$ = this.listaOuvintes.iterator();
        while (i$.hasNext()) {
            ((OuvintesCompilador) i$.next()).emiteMensagemCompilacao(msg);
        }
    }

    private void avisaInformacoesTokens(String msg, boolean analisouComSucesso) {
        Iterator i$ = this.listaOuvintes.iterator();
        while (i$.hasNext()) {
            ((OuvintesCompilador) i$.next()).exibeDadosDeSaida(msg, analisouComSucesso);
        }
    }

    private void avisaInformacoesTokens(String msg) {
        Iterator i$ = this.listaOuvintes.iterator();
        while (i$.hasNext()) {
            ((OuvintesCompilador) i$.next()).exibeDadosDeSaida(msg);
        }
    }

    private void avisaSelecaoErro(int ini, int end, int TIPO) {
        Iterator i$ = this.listaOuvintes.iterator();
        while (i$.hasNext()) {
            ((OuvintesCompilador) i$.next()).setSelecaoErroNoEditor(ini, end, TIPO);
        }
    }
}
