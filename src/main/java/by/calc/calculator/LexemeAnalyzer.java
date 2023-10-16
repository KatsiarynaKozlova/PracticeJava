package by.calc.calculator;

import by.calc.exceptions.LexemeException;

import java.io.IOException;
import java.io.Reader;

public class LexemeAnalyzer {
    private static final String toD = "toDollar";
    private static final String toR = "toRuble";
    private final Reader reader;
    private int current;
    public LexemeAnalyzer(Reader r) {
        reader = r;
        try {
            current = reader.read();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public Lexeme getLexeme() throws LexemeException {
        Lexeme lexeme;
        while((char)current == (' ')){
            try {
                if (reader.ready())  current = reader.read();
            } catch (IOException ex) {
                //ex.printStackTrace();
                throw new LexemeException("Read error");
            }
        }
        if ((char) current == '+') {
            lexeme = new Lexeme(Token.PLUS, "+");       // +
        } else if ((char) current == '-') {
            lexeme = new Lexeme(Token.MINUS, "-");      // -
        } else if ((char) current == '(') {
            lexeme = new Lexeme(Token.OPEN, "(");       // (
        } else if ((char) current == ')') {
            lexeme = new Lexeme(Token.CLOSE, ")");      // )
        } else if ((char) current == 't') {
            StringBuilder sb = new StringBuilder();
            sb.append((char) current);
            do{
                try{
                    if (reader.ready()) current = reader.read();
                }catch (IOException ex) {
                   // ex.printStackTrace();
                     throw new LexemeException("Read error");
                }
                sb.append((char)current);
            }while ((toD.contains(sb.toString()) || toR.contains(sb.toString()))&& current!=-1);
            if (sb.toString().equals(toD+"s")){
                lexeme = new Lexeme(Token.TODOLLARS, sb.toString());       //toDollars
            }
            else if (sb.toString().equals(toR+"s")){
                lexeme = new Lexeme(Token.TORUBLES, sb.toString());        //toRubles
            }else { throw new LexemeException("Invalid name for conversion function");}
        } else if (Character.isDigit((char) current)) {
            StringBuilder sb = new StringBuilder();
            int dotCounter = 0;
            do {
                sb.append((char) current);
                if((char)current == '.') dotCounter += 1;
                try {
                    if (reader.ready()) current = reader.read();
                } catch (IOException ex) {
                    //ex.printStackTrace();
                   throw new LexemeException("Read error");
                }
            } while ((Character.isDigit((char) current) || (char)current == '.') && current != -1);
            if( dotCounter > 1 )  throw new LexemeException("Invalid number input");
            if( (char)current == 'p' || (char)current == 'р') {             //rubles
                lexeme = new Lexeme(Token.RUB, sb.toString());
            }else{
                throw new LexemeException("read error");
            }

        }else if ((char)current == '$'){
            int dotCounter = 0;
            StringBuilder sb = new StringBuilder();
            do {
                try {
                    if (reader.ready()) current = reader.read();             //dollaryyyy
                } catch (IOException ex) {
                   // ex.printStackTrace();
                     throw new LexemeException("read error");
                }
                sb.append((char) current);
                if((char)current == '.') dotCounter += 1;
            } while ((Character.isDigit((char) current) || (char)current == '.') && current != -1);
            if( dotCounter > 1 )  throw new LexemeException("Invalid number input");
            return new Lexeme(Token.DOLL, sb.toString().substring(0,sb.length()-1)); //
        }else if (current == -1 || (char) current == '\n') { // -1 or 44
            lexeme = new Lexeme(Token.EOF, "-1");
        } else {
           throw new LexemeException("Unknown lexeme: " + (char) current);
        }
        try {
            if (reader.ready()) current = reader.read();
        } catch (IOException ex) {
           // ex.printStackTrace();
            throw new LexemeException("read error");
        }
        return lexeme;
    }
}
