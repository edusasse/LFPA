package com.edusasse.lfpa.gals;

public class Lexico implements Constants {
    private int end;
    private String input;
    public int linhaAtual;
    private int position;

    public Lexico() {
        this("");
    }

    public Lexico(String input) {
        this.linhaAtual = 1;
        this.end = -1;
        setInput(input);
    }

    public void setInput(String input) {
        this.input = input;
        setPosition(0);
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public Token nextToken() throws LexicalError {
        if (!hasInput()) {
            return null;
        }
        int start = this.position;
        int state = 0;
        int lastState = 0;
        int endState = -1;
        int end = -1;
        while (hasInput()) {
            lastState = state;
            state = nextState(nextChar(), state);
            if (state < 0) {
                break;
            } else if (tokenForState(state) >= 0) {
                endState = state;
                end = this.position;
            }
        }
        if (endState < 0 || (endState != state && tokenForState(lastState) == -2)) {
            throw new LexicalError(SCANNER_ERROR[lastState], start);
        }
        this.position = end;
        int token = tokenForState(endState);
        if (token == 0) {
            return nextToken();
        }
        return new Token(token, this.input.substring(start, end), start);
    }

    private int nextState(char c, int state) {
        int start = SCANNER_TABLE_INDEXES[state];
        int end = SCANNER_TABLE_INDEXES[state + 1] - 1;
        while (start <= end) {
            int half = (start + end) / 2;
            if (SCANNER_TABLE[half][0] == c) {
                return SCANNER_TABLE[half][1];
            }
            if (SCANNER_TABLE[half][0] < c) {
                start = half + 1;
            } else {
                end = half - 1;
            }
        }
        return -1;
    }

    private int tokenForState(int state) {
        if (state < 0 || state >= TOKEN_STATE.length) {
            return -1;
        }
        return TOKEN_STATE[state];
    }

    private boolean hasInput() {
        return this.position < this.input.length();
    }

    private char nextChar() {
        if (hasInput()) {
            if (this.input.charAt(this.position) == '\n' && this.position != this.end) {
                this.linhaAtual++;
            }
            String str = this.input;
            int i = this.position;
            this.position = i + 1;
            return str.charAt(i);
        }
        this.linhaAtual = 0;
        return '\uffff';
    }
}
