package com.edusasse.lfpa.gals;

import java.util.Stack;

public class Sintatico implements Constants {
    private Token currentToken;
    private Token previousToken;
    private Lexico scanner;
    private Semantico semanticAnalyser;
    private Stack stack;

    public Sintatico() {
        this.stack = new Stack();
    }

    private static final boolean isTerminal(int x) {
        return x < 35;
    }

    private static final boolean isNonTerminal(int x) {
        return x >= 35 && x < 54;
    }

    private static final boolean isSemanticAction(int x) {
        return x >= 54;
    }

    private boolean step() throws LexicalError, SyntaticError, SemanticError {
        if (this.currentToken == null) {
            int pos = 0;
            if (this.previousToken != null) {
                pos = this.previousToken.getPosition() + this.previousToken.getLexeme().length();
            }
            this.currentToken = new Token(1, "$", pos);
        }
        int x = ((Integer) this.stack.pop()).intValue();
        int a = this.currentToken.getId();
        if (x == 0) {
            return false;
        }
        if (isTerminal(x)) {
            if (x != a) {
                throw new SyntaticError(PARSER_ERROR[x], this.currentToken.getPosition());
            } else if (this.stack.empty()) {
                return true;
            } else {
                this.previousToken = this.currentToken;
                this.currentToken = this.scanner.nextToken();
                return false;
            }
        } else if (!isNonTerminal(x)) {
            this.semanticAnalyser.executeAction(x - 54, this.previousToken);
            return false;
        } else if (pushProduction(x, a)) {
            return false;
        } else {
            throw new SyntaticError(PARSER_ERROR[x], this.currentToken.getPosition());
        }
    }

    private boolean pushProduction(int topStack, int tokenInput) {
        int p = PARSER_TABLE[topStack - 35][tokenInput - 1];
        if (p < 0) {
            return false;
        }
        int[] production = PRODUCTIONS[p];
        for (int i = production.length - 1; i >= 0; i--) {
            this.stack.push(new Integer(production[i]));
        }
        return true;
    }

    public void parse(Lexico scanner, Semantico semanticAnalyser) throws LexicalError, SyntaticError, SemanticError {
        this.scanner = scanner;
        this.semanticAnalyser = semanticAnalyser;
        this.stack.clear();
        this.stack.push(new Integer(1));
        this.stack.push(new Integer(35));
        this.currentToken = scanner.nextToken();
        do {
        } while (!step());
    }
}
