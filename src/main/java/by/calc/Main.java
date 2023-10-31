package by.calc;

import by.calc.calculator.LexemeAnalyzer;
import by.calc.calculator.OperationParser;
import by.calc.exceptions.LexemeException;
import by.calc.exceptions.ParserException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

public class Main {
    public static final String FILE_NAME = "rate.txt";

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            BigDecimal rate = new BigDecimal(br.readLine());

            Reader reader = new InputStreamReader(System.in);
            LexemeAnalyzer lexer = new LexemeAnalyzer(reader);
            OperationParser parser = new OperationParser(lexer, rate);
            System.out.println(parser.calculate());
        } catch (FileNotFoundException e) {
            System.out.println(FILE_NAME + " file is missing");
        } catch (NullPointerException e) {
            System.out.println(FILE_NAME + " file is empty");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input in " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Reading was failed");
        } catch (LexemeException e) {
            System.out.println(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
        } catch (ParserException e) {
            System.out.println(Constants.INVALID_EXPRESSION_EXCEPTION_MESSAGE);
        }
    }
}
