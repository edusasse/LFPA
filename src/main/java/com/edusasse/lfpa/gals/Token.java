package com.edusasse.lfpa.gals;

public class Token {
    private int id;
    private String lexeme;
    private int position;

    public Token(int id, String lexeme, int position) {
        this.id = id;
        this.lexeme = lexeme;
        this.position = position;
    }

    public final int getId() {
        return this.id;
    }

    public final String getLexeme() {
        return this.lexeme;
    }

    public final int getPosition() {
        return this.position;
    }

    public String toString() {
        return this.id + " ( " + this.lexeme + " ) @ " + this.position;
    }
}
