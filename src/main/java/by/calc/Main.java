package by.calc;

import by.calc.calculator.LexemeAnalyzer;
import by.calc.calculator.ParserCalc;
import by.calc.exceptions.LexemeException;
import by.calc.exceptions.ParserException;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("rate.txt"));
            float rate = Float.parseFloat(br.readLine());

            Reader reader = new InputStreamReader(System.in);
            LexemeAnalyzer lexer = new LexemeAnalyzer(reader);
            ParserCalc parser = new ParserCalc(lexer,rate);
            System.out.println(parser.calculate());
        } catch (FileNotFoundException e) {
            System.out.println("The \"rate.txt\" file is missing");
        } catch (NullPointerException e){
            System.out.println("The \"rate.txt\" file is empty");
        } catch (NumberFormatException e){
            System.out.println("Invalid input for \"rate.txt\" file");
        } catch (IOException e){
            System.out.println("Reading \"rate.txt\" was failed");
        } catch (LexemeException e) {
            System.out.println("Invalid Input");
        } catch (ParserException e) {
            System.out.println(e.getMessage());
          //  throw new RuntimeException(e);
        }
    }
}
