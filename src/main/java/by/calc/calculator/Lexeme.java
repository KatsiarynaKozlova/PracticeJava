package by.calc.calculator;
public class Lexeme {        //record?
    private final Token token;
    private final String value;

    public Lexeme(Token token, String value) {
        this.token = token;
        this.value = value;
    }
    public Token getToken() {return token;}
    public String getValue() {return value;}

    @Override
    public String toString() {
        return "Lexeme{" +
                "token=" + token +
                ", value='" + value + '\'' +
                '}';
    }
}

