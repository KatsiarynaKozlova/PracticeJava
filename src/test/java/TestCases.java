import by.calc.calculator.LexemeAnalyzer;
import by.calc.calculator.ParserCalc;
import by.calc.exceptions.LexemeException;
import by.calc.exceptions.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.StringReader;

public class TestCases {
    @Test
    public void ValidTests() throws LexemeException, ParserException {
        Assertions.assertEquals(
                "$97,68",
                new ParserCalc(new LexemeAnalyzer(new StringReader("toDollars(737р + toRubles($85.4))")),60).calculate()
        );
        Assertions.assertEquals(
                "60,00p",
                new ParserCalc(new LexemeAnalyzer(new StringReader("toRubles($1)")),60).calculate()
        );
        Assertions.assertEquals(
                "$1,00",
                new ParserCalc(new LexemeAnalyzer(new StringReader("toDollars(60p)")),60).calculate()
          //      "0.1"
        );
        Assertions.assertEquals(
                "2,00p",
                new ParserCalc(new LexemeAnalyzer(new StringReader("toRubles($1)-58p")),60).calculate()
        );
        Assertions.assertEquals(
                "$-57,00",
                new ParserCalc(new LexemeAnalyzer(new StringReader("toDollars(60p)-$58")),60).calculate()
        );
        Assertions.assertEquals(
                "72,00p",
                new ParserCalc(new LexemeAnalyzer(new StringReader("toRubles($1)+12p")),60).calculate()
        );
        Assertions.assertEquals(
                "$14,00",
                new ParserCalc(new LexemeAnalyzer(new StringReader("toDollars(120p)+$12")),60).calculate()
        );
        Assertions.assertEquals(
                "72,00p",
                new ParserCalc(new LexemeAnalyzer(new StringReader("12p + toRubles($1)")),60).calculate()
        );
        Assertions.assertEquals(
                "$13,00",
                new ParserCalc(new LexemeAnalyzer(new StringReader("$12 + toDollars(60p)")),60).calculate()
        );
    }
}
