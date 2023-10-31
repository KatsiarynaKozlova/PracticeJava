package by.calc.calculator;

import by.calc.Constants;
import by.calc.exceptions.LexemeException;

import java.io.IOException;
import java.io.Reader;

public class LexemeAnalyzer {
    private static final String TO_DOLLARS = "toDollars";
    private static final String TO_RUBLES = "toRubles";
    private final Reader reader;
    private int currentChar;

    public LexemeAnalyzer(Reader r) {
        reader = r;
        try {
            currentChar = reader.read();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void nextChar() throws LexemeException {
        try {
            if (reader.ready()) currentChar = reader.read();
        } catch (IOException ex) {
            throw new LexemeException(Constants.READ_ERROR_EXCEPTION_MESSAGE);
        }
    }

    private String readAmount() throws LexemeException {
        StringBuilder amount = new StringBuilder();
        int dotCounter = 0;
        do {
            amount.append((char) currentChar);
            if ((char) currentChar == '.') dotCounter += 1;
            nextChar();
        } while ((Character.isDigit((char) currentChar) || (char) currentChar == '.') && currentChar != -1);
        if (dotCounter > 1) throw new LexemeException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
        return amount.toString();
    }


    private Lexeme readCurrencyFunction() throws LexemeException {
        StringBuilder functionName = new StringBuilder();
        functionName.append((char) currentChar);
        while ((!TO_DOLLARS.equals(functionName.toString()) && !TO_RUBLES.equals(functionName.toString())) || currentChar == -1) {
            nextChar();
            functionName.append((char) currentChar);
        }
        if (functionName.toString().equals(TO_DOLLARS)) {
            return new Lexeme(Token.TO_DOLLARS, functionName.toString());
        } else if (functionName.toString().equals(TO_RUBLES)) {
            return new Lexeme(Token.TO_RUBLES, functionName.toString());
        } else {
            throw new LexemeException(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
        }
    }

    public Lexeme getLexeme() throws LexemeException {
        Lexeme lexeme;

        switch ((char) currentChar) {
            case '+' -> lexeme = new Lexeme(Token.PLUS, "+");
            case '-' -> lexeme = new Lexeme(Token.MINUS, "-");
            case '(' -> lexeme = new Lexeme(Token.OPEN, "(");
            case ')' -> lexeme = new Lexeme(Token.CLOSE, ")");
            case 't' -> lexeme = readCurrencyFunction();
            case '$' -> {
                nextChar();
                lexeme = new Lexeme(Token.DOLL, readAmount());
                return lexeme;
            }
            default -> {
                if (currentChar == -1 || (char) currentChar == '\n') {
                    lexeme = new Lexeme(Token.EOF, "-1");
                } else if (Character.isDigit(currentChar)) {
                    String amount = readAmount();
                    if ((char) currentChar == 'p' || (char) currentChar == 'р') {
                        lexeme = new Lexeme(Token.RUB, amount);
                    } else {
                        throw new LexemeException(Constants.READ_ERROR_EXCEPTION_MESSAGE);
                    }
                } else {
                    throw new LexemeException(Constants.UNKNOWN_LEXEME_EXCEPTION_MESSAGE + (char) currentChar);
                }
            }
        }
        nextChar();
        return lexeme;
    }
}
