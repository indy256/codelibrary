package parsing;

import javax.script.*;

public class ExpressionParserRecursiveDescent {

    String s;
    int pos;
    char token;
    double tokval;

    static boolean isDigitOrDot(char x) {
        return Character.isDigit(x) || x == '.';
    }

    int next() {
        while (true) {
            if (pos == s.length())
                return token = 255;
            char c = s.charAt(pos++);
            if ("+-*/^()\n".indexOf(c) != -1)
                return token = c;
            if (Character.isSpaceChar(c))
                continue;
            if (Character.isDigit(c) || c == '.') {
                String operand = "" + c;
                while (pos < s.length() && isDigitOrDot(s.charAt(pos)))
                    operand += c = s.charAt(pos++);
                tokval = Double.parseDouble(operand);
                return token = 'n';
            }
            throw new RuntimeException("Bad character: " + c);
        }
    }

    void skip(int ch) {
        if (token != ch)
            throw new RuntimeException("Bad character: " + token + ", expected: " + ch);
        next();
    }

    // number ::= number | '(' expression ')'
    double number() {
        if (token == 'n') {
            double v = tokval;
            skip('n');
            return v;
        }
        skip('(');
        double v = expression();
        skip(')');
        return v;
    }

    // factor ::= number | number '^' factor
    double factor() {
        double v = number();
        if (token == '^') {
            skip('^');
            v = Math.pow(v, factor());
        }
        return v;
    }

    // term ::= factor | term '*' factor | term '/' factor
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
    double expression() {
        double v = term();
        for (; ; ) {
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
        parser.next();
        while (parser.token != 255) {
            if (parser.token == '\n') {
                parser.skip('\n');
                continue;
            }
            System.out.printf("%.5f", parser.expression());
        }
    }
}
