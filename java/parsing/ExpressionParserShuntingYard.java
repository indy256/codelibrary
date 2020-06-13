package parsing;

import java.util.LinkedList;
import javax.script.*;

public class ExpressionParserShuntingYard {
    static boolean isDelim(char c) {
        return c == ' ';
    }

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }

    static int priority(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            default:
                return -1;
        }
    }

    static void processOperator(LinkedList<Integer> st, LinkedList<Character> ops) {
        char op = ops.removeLast();
        int r = st.removeLast();
        int l = st.removeLast();
        switch (op) {
            case '+':
                st.add(l + r);
                break;
            case '-':
                st.add(l - r);
                break;
            case '*':
                st.add(l * r);
                break;
            case '/':
                st.add(l / r);
                break;
            case '%':
                st.add(l % r);
                break;
        }
    }

    public static int eval(String s) {
        LinkedList<Integer> st = new LinkedList<>();
        LinkedList<Character> ops = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (isDelim(c))
                continue;
            if (c == '(')
                ops.add('(');
            else if (c == ')') {
                while (ops.getLast() != '(') processOperator(st, ops);
                ops.removeLast();
            } else if (isOperator(c)) {
                while (!ops.isEmpty() && priority(c) <= priority(ops.getLast())) processOperator(st, ops);
                ops.add(c);
            } else {
                String operand = "";
                while (i < s.length() && Character.isDigit(s.charAt(i))) operand += s.charAt(i++);
                --i;
                st.add(Integer.parseInt(operand));
            }
        }
        while (!ops.isEmpty()) processOperator(st, ops);
        return st.get(0);
    }

    public static void main(String[] args) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

        String exp = "1+2*3*4+3*(2+2)-100";
        System.out.println(eval(exp));
        System.out.println((Integer) engine.eval(exp) == eval(exp));
    }
}