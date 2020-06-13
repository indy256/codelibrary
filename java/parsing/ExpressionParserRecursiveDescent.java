package parsing;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ExpressionParserRecursiveDescent {
    String s;
    int pos;
    char token;
    double tokenValue;

    void readToken() {
        while (true) {
            if (pos == s.length()) {
                token = 255;
                return;
            }
            char c = s.charAt(pos++);
            if ("+-*/^()\n".indexOf(c) != -1) {
                token = c;
                return;
            }
            if (Character.isSpaceChar(c))
                continue;
            if (Character.isDigit(c) || c == '.') {
                String operand = "" + c;
                while (pos < s.length() && (Character.isDigit(s.charAt(pos)) || s.charAt(pos) == '.'))
                    operand += s.charAt(pos++);
                tokenValue = Double.parseDouble(operand);
                token = 'n';
                return;
            }
            throw new RuntimeException("Bad character: " + c);
        }
    }

    void skip(int ch) {
        if (token != ch)
            throw new RuntimeException("Bad character: " + token + ", expected: " + ch);
        readToken();
    }

    // Original grammar:
    // Е -> Е + Т | Е-Т | Т
    // Т -> T*F | T/F | F
    // F -> number | (Е)
    //
    // After left recursion elimination:
    // Е -> ТЕ'
    // Е'-> +ТЕ' | -ТЕ' | е
    // Т -> FT'
    // T'-> *FT' | /FT' | е
    // F -> number | (Е)

    // factor ::= number | '(' expression ')'
    double factor() {
        if (token == 'n') {
            double v = tokenValue;
            skip('n');
            return v;
        }
        skip('(');
        double v = expression();
        skip(')');
        return v;
    }

    // term ::= factor | term '*' factor | term '/' factor
    // Т -> FT'
    // T'-> *FT' | /FT' | е
    double term() {
        double v = factor();
        while (true) {
            if (token == '*') {
                skip('*');
                v *= factor();
            } else if (token == '/') {
                skip('/');
                v /= factor();
            } else
                return v;
        }
    }

    // expression ::= term | expression '+' term | expression '-' term
    // Е -> ТЕ'
    // Е'-> +ТЕ' | -ТЕ' | е
    double expression() {
        double v = term();
        while (true) {
            if (token == '+') {
                skip('+');
                v += term();
            } else if (token == '-') {
                skip('-');
                v -= term();
            } else
                return v;
        }
    }

    public static void main(String[] args) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

        String exp = "1+2*3*4+3*(2+2)-100\n";
        System.out.println(engine.eval(exp));

        ExpressionParserRecursiveDescent parser = new ExpressionParserRecursiveDescent();
        parser.s = exp;
        parser.readToken();
        while (parser.token != 255) {
            if (parser.token == '\n') {
                parser.skip('\n');
                System.out.println();
                continue;
            }
            System.out.printf("%.5f", parser.expression());
        }
    }
}
