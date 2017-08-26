package com.edusasse.lfpa.gals;

public class AnalysisError extends Exception {
    private int position;

    public AnalysisError(String msg, int position) {
        super(msg);
        this.position = position;
    }

    public AnalysisError(String msg) {
        super(msg);
        this.position = -1;
    }

    public int getPosition() {
        return this.position;
    }

    public String toString() {
        return super.toString() + ", @ " + this.position;
    }
}
