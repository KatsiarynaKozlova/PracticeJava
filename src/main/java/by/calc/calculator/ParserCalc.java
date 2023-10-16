package by.calc.calculator;

import by.calc.exceptions.LexemeException;
import by.calc.exceptions.ParserException;

public class ParserCalc {
    private LexemeAnalyzer lexemeAnalyzer;
    private Lexeme current;
    private final float rate;
    private Boolean answerType;
    public ParserCalc (LexemeAnalyzer lexemeAnalyzer, float rate) throws LexemeException {
                this.lexemeAnalyzer = lexemeAnalyzer;
                this.current = lexemeAnalyzer.getLexeme();
                this.rate = rate;
    }
    private double parseD_() throws LexemeException, ParserException {
        double temp = 0.0;
        switch (current.getToken()){
            case PLUS:
                current = lexemeAnalyzer.getLexeme();
                temp += parseD();
                break;
            case MINUS:
                current = lexemeAnalyzer.getLexeme();
                temp -= parseD();
                break;
        }
        return temp;
    }
    private double parseD() throws LexemeException, ParserException {
        double temp = 0.0;
        switch (current.getToken()){
            case DOLL:
                temp = Double.parseDouble(current.getValue());
                current = lexemeAnalyzer.getLexeme();
                temp += parseD_();
                break;
            case TODOLLARS:
                current = lexemeAnalyzer.getLexeme();
                if (current.getToken() == Token.OPEN) {
                    current = lexemeAnalyzer.getLexeme();
                    temp = parseR()/rate;
                }
                if (current.getToken() != Token.CLOSE) {
                         throw new ParserException("invalid expression");
                }
                current = lexemeAnalyzer.getLexeme();
                temp += parseD_();
                break;
        }
        return temp;
    }
    private double parseR_() throws LexemeException, ParserException {
        double temp = 0.0;
        switch (current.getToken()){
            case PLUS:
                current = lexemeAnalyzer.getLexeme();
                temp += parseR();
                break;
            case MINUS:
                current = lexemeAnalyzer.getLexeme();
                temp -= parseR();
                break;
        }
        return temp;
    }
    private double parseR() throws LexemeException, ParserException {
        double temp = 0.0;
        switch(current.getToken()){
            case RUB: temp = Double.parseDouble(current.getValue());
                      current = lexemeAnalyzer.getLexeme();
                      temp += parseR_();
                      break;
            case TORUBLES:
                current = lexemeAnalyzer.getLexeme();
                if (current.getToken() == Token.OPEN) {
                    current = lexemeAnalyzer.getLexeme();
                    temp = parseD()*rate;
                }
                if (current.getToken() != Token.CLOSE) {
                       throw new ParserException("Invalid expression");
                }
                current = lexemeAnalyzer.getLexeme();
                temp += parseR_();
                break;
            default:
                throw new ParserException("Invalid expression");
        }
        return temp;
    }
    private double parseExpression() throws LexemeException, ParserException {
        double temp;
        switch (current.getToken()) {
            case RUB, TORUBLES:
                temp = parseR();
                this.answerType = true;
                break;
            case DOLL, TODOLLARS:
                temp = parseD();
                this.answerType = false;
                break;
            default:
                throw new ParserException("Invalid expression");
        }
        return temp;
    }
    public String calculate() throws ParserException, LexemeException
        {
        double temp = parseExpression();
        if (current.getToken() != Token.EOF) {
            throw new ParserException("invalid expression: end != EOF");
        }
        if(answerType) return String.format("%.2fp",temp);
        else return String.format("$%.2f",temp);
    }
}

