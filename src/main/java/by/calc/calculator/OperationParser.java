package by.calc.calculator;

import by.calc.Constants;
import by.calc.exceptions.LexemeException;
import by.calc.exceptions.ParserException;

import java.math.BigDecimal;
import java.math.MathContext;

public class OperationParser {
    private final LexemeAnalyzer lexemeAnalyzer;
    private final BigDecimal rate;
    private Lexeme current;

    public OperationParser(LexemeAnalyzer lexemeAnalyzer, BigDecimal rate) throws LexemeException {
        this.lexemeAnalyzer = lexemeAnalyzer;
        this.current = lexemeAnalyzer.getLexeme();
        this.rate = rate;
    }

    private BigDecimal operationDollars() throws LexemeException, ParserException {
        BigDecimal temp = new BigDecimal(0);
        switch (current.token()) {
            case PLUS -> {
                current = lexemeAnalyzer.getLexeme();
                temp = temp.add(parseDollars());
            }
            case MINUS -> {
                current = lexemeAnalyzer.getLexeme();
                temp = temp.subtract(parseDollars());
            }
        }
        return temp;
    }

    private BigDecimal parseDollars() throws LexemeException, ParserException {
        BigDecimal temp;
        switch (current.token()) {
            case DOLL -> {
                temp = new BigDecimal((current.value()));
                current = lexemeAnalyzer.getLexeme();
                temp = temp.add(operationDollars());
            }
            case TO_DOLLARS -> {
                current = lexemeAnalyzer.getLexeme();
                if (current.token() == Token.OPEN) {
                    current = lexemeAnalyzer.getLexeme();
                    temp = parseRubles().divide(rate, MathContext.DECIMAL128);
                } else {
                    throw new ParserException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
                }
                if (current.token() != Token.CLOSE) {
                    throw new ParserException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
                }
                current = lexemeAnalyzer.getLexeme();
                temp = temp.add(operationDollars());
            }
            default -> throw new ParserException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
        }
        return temp;
    }

    private BigDecimal operationRubles() throws LexemeException, ParserException {
        BigDecimal temp = new BigDecimal(0);
        switch (current.token()) {
            case PLUS -> {
                current = lexemeAnalyzer.getLexeme();
                temp = temp.add(parseRubles());
            }
            case MINUS -> {
                current = lexemeAnalyzer.getLexeme();
                temp = temp.subtract(parseRubles());
            }
        }
        return temp;
    }

    private BigDecimal parseRubles() throws LexemeException, ParserException {
        BigDecimal temp;
        switch (current.token()) {
            case RUB -> {
                temp = new BigDecimal(current.value());
                current = lexemeAnalyzer.getLexeme();
                temp = temp.add(operationRubles());
            }
            case TO_RUBLES -> {
                current = lexemeAnalyzer.getLexeme();
                if (current.token() == Token.OPEN) {
                    current = lexemeAnalyzer.getLexeme();
                    temp = parseDollars().multiply(rate);
                } else {
                    throw new ParserException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
                }
                if (current.token() != Token.CLOSE) {
                    throw new ParserException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
                }
                current = lexemeAnalyzer.getLexeme();
                temp = temp.add(operationRubles());
            }
            default -> throw new ParserException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
        }
        return temp;
    }

    private String parseExpression() throws LexemeException, ParserException {
        BigDecimal temp;
        switch (current.token()) {
            case RUB, TO_RUBLES -> {
                temp = parseRubles();
                return String.format("%.2fp", temp);
            }
            case DOLL, TO_DOLLARS -> {
                temp = parseDollars();
                return String.format("$%.2f", temp);
            }
            default -> throw new ParserException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
        }
    }

    public String calculate() throws ParserException, LexemeException {
        String answer = parseExpression();
        if (current.token() != Token.EOF) {
            throw new ParserException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
        }
        return answer;
    }
}

