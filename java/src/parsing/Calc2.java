package parsing;

public class Calc2 {

    static enum TOKEN {
        PLUS, MINUS, MUL, DIV, NUMBER, LP, RP, END, FUNCTION;
    }

    double expr() {
        double left = term();
        while (true) {
            switch (curr_tok) {
                case PLUS:
                    left += term();
                    break;
                case MINUS:
                    left -= term();
                    break;
                default:
                    return left;
            }
        }
    }

    double term() {
        double left = prim();
        while (true) {
            switch (curr_tok) {
                case MUL:
                    left *= prim();
                    break;
                case DIV:
                    left /= prim();
                    break;
                default:
                    return left;
            }
        }
    }

    double prim() {
        get_token();
        switch (curr_tok) {
            case NUMBER:
                double v = number_value;
                get_token();
                return v;
            case MINUS:
                return -prim();
            case LP:
                double e = expr();
                if (curr_tok != TOKEN.RP)
                    return error(")expected");
                return e;
            case FUNCTION:
                String f = function_name;
                get_token();
                if (curr_tok != TOKEN.LP)
                    return error("( expected");
                double z = prim();
                if (curr_tok != TOKEN.RP)
                    return error(") expected");
                if ("sin".equals(f)) {
                    return Math.sin(z);
                } else {
                    return error("unknown function");
                }
            default:
                return error("primary expression expected");
        }
    }

    double error(String msg) {
        error_msg = msg;
        return Double.NaN;
    }

    TOKEN curr_tok;
    double number_value;
    String function_name;
    String error_msg;
    String input;
    int pos;

    void get_token() {
        if (pos >= input.length()) {
            curr_tok = TOKEN.END;
            return;
        }
        char x = input.charAt(pos++);
        switch (x) {
            case '+':
                curr_tok = TOKEN.PLUS;
                break;
            case '-':
                curr_tok = TOKEN.MINUS;
                break;
            case '*':
                curr_tok = TOKEN.MUL;
                break;
            case '/':
                curr_tok = TOKEN.DIV;
                break;
            case '(':
                curr_tok = TOKEN.LP;
                break;
            case ')':
                curr_tok = TOKEN.RP;
                break;
            default:
                if (Character.isDigit(x) || x == '.') {
                    curr_tok = TOKEN.NUMBER;
                    String s = "" + x;
                    while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
                        s += input.charAt(pos);
                        ++pos;
                    }
                    number_value = Double.parseDouble(s);
                } else if (Character.isLetter(x)) {
                    curr_tok = TOKEN.FUNCTION;
                    String s = "" + x;
                    while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
                        s += input.charAt(pos);
                        ++pos;
                    }
                    function_name = s;
                }
        }
    }

    public static void main(String[] args) {
        Calc2 calc = new Calc2();
        calc.input = "sin(1)";
        calc.pos = 0;
        double res = calc.expr();
        if (Double.isNaN(res)) {
            System.out.println(calc.error_msg);
        } else {
            System.out.println(res);
        }
    }
}
