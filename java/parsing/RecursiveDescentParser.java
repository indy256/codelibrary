package parsing;

import java.util.*;

public class RecursiveDescentParser {

    enum Lexeme {
        NUM, ID, IF, ELSE, WHILE, DO, LBRA, RBRA, LPAR, RPAR, PLUS, MINUS, LESS, EQUAL, SEMICOLON, EOF
    }

    static class Lexer {
        static final Map<Character, Lexeme> SYMBOLS = new HashMap<>();

        static {
            SYMBOLS.put('{', Lexeme.LBRA);
            SYMBOLS.put('}', Lexeme.RBRA);
            SYMBOLS.put('=', Lexeme.EQUAL);
            SYMBOLS.put(';', Lexeme.SEMICOLON);
            SYMBOLS.put('(', Lexeme.LPAR);
            SYMBOLS.put(')', Lexeme.RPAR);
            SYMBOLS.put('+', Lexeme.PLUS);
            SYMBOLS.put('-', Lexeme.MINUS);
            SYMBOLS.put('<', Lexeme.LESS);
        }

        static final Map<String, Lexeme> WORDS = new HashMap<>();

        static {
            WORDS.put("if", Lexeme.IF);
            WORDS.put("else", Lexeme.ELSE);
            WORDS.put("do", Lexeme.DO);
            WORDS.put("while", Lexeme.WHILE);
        }

        final String s;
        int pos = 0;
        Character ch = ' ';
        Integer value;
        Lexeme sym;

        Lexer(String s) {
            this.s = s;
        }

        void error(String message) {
            throw new RuntimeException("Lexer error: " + message);
        }

        void readCh() {
            ch = pos < s.length() ? s.charAt(pos++) : null;
        }

        void readNextToken() {
            value = null;
            sym = null;
            while (sym == null) {
                if (ch == null) {
                    sym = Lexeme.EOF;
                } else if (Character.isSpaceChar(ch)) {
                    readCh();
                } else if (SYMBOLS.containsKey(ch)) {
                    sym = SYMBOLS.get(ch);
                    readCh();
                } else if (Character.isDigit(ch)) {
                    int val = 0;
                    while (ch != null && Character.isDigit(ch)) {
                        val = val * 10 + ch - '0';
                        readCh();
                    }
                    value = val;
                    sym = Lexeme.NUM;
                } else if (Character.isLetter(ch)) {
                    String ident = "";
                    while (ch != null && Character.isLetter(ch)) {
                        ident += ch;
                        readCh();
                    }
                    if (WORDS.containsKey(ident)) {
                        sym = Lexer.WORDS.get(ident);
                    } else if (ident.length() == 1) {
                        sym = Lexeme.ID;
                        value = ident.charAt(0) - 'a';
                    } else {
                        error("Unknown identifier: " + ident);
                    }
                } else {
                    error("Unexpected symbol: " + ch);
                }
            }
        }
    }

    enum Nonterminal {
        VAR, CONST, ADD, SUB, LT, SET, IF1, IF2, WHILE, DO, EMPTY, SEQ, EXPR, PROG
    }

    static class Node {
        Nonterminal kind;
        int value;
        Node op1;
        Node op2;
        Node op3;

        Node(Nonterminal kind, int value, Node op1, Node op2) {
            this.kind = kind;
            this.value = value;
            this.op1 = op1;
            this.op2 = op2;
        }
    }

    static class Parser {
        final Lexer lexer;

        Parser(Lexer lexer) {
            this.lexer = lexer;
        }

        void error(String message) {
            throw new RuntimeException("Parser error: " + message);
        }

        Node term() {
            if (lexer.sym == Lexeme.ID) {
                Node node = new Node(Nonterminal.VAR, lexer.value, null, null);
                lexer.readNextToken();
                return node;
            } else if (lexer.sym == Lexeme.NUM) {
                Node node = new Node(Nonterminal.CONST, lexer.value, null, null);
                lexer.readNextToken();
                return node;
            } else {
                return parenExpr();
            }
        }

        Node summa() {
            Node node = term();
            while (lexer.sym == Lexeme.PLUS || lexer.sym == Lexeme.MINUS) {
                Nonterminal kind = lexer.sym == Lexeme.PLUS ? Nonterminal.ADD : Nonterminal.SUB;
                lexer.readNextToken();
                node = new Node(kind, 0, node, term());
            }
            return node;
        }

        Node test() {
            Node node = summa();
            if (lexer.sym == Lexeme.LESS) {
                lexer.readNextToken();
                node = new Node(Nonterminal.LT, 0, node, summa());
            }
            return node;
        }

        Node expr() {
            if (lexer.sym != Lexeme.ID) {
                return test();
            }
            Node node = test();
            if (node.kind == Nonterminal.VAR && lexer.sym == Lexeme.EQUAL) {
                lexer.readNextToken();
                node = new Node(Nonterminal.SET, 0, node, expr());
            }
            return node;
        }

        Node parenExpr() {
            if (lexer.sym != Lexeme.LPAR) {
                error("'(' expected");
            }
            lexer.readNextToken();
            Node node = expr();
            if (lexer.sym != Lexeme.RPAR) {
                error("')' expected");
            }
            lexer.readNextToken();
            return node;
        }

        Node statement() {
            Node node;
            if (lexer.sym == Lexeme.IF) {
                node = new Node(Nonterminal.IF1, 0, null, null);
                lexer.readNextToken();
                node.op1 = parenExpr();
                node.op2 = statement();
                if (lexer.sym == Lexeme.ELSE) {
                    node.kind = Nonterminal.IF2;
                    lexer.readNextToken();
                    node.op3 = statement();
                }
            } else if (lexer.sym == Lexeme.WHILE) {
                node = new Node(Nonterminal.WHILE, 0, null, null);
                lexer.readNextToken();
                node.op1 = parenExpr();
                node.op2 = statement();
            } else if (lexer.sym == Lexeme.DO) {
                node = new Node(Nonterminal.DO, 0, null, null);
                lexer.readNextToken();
                node.op1 = statement();
                if (lexer.sym != Lexeme.WHILE) {
                    error("'while' expected");
                }
                lexer.readNextToken();
                node.op2 = parenExpr();
                if (lexer.sym != Lexeme.SEMICOLON) {
                    error("';' expected");
                }
            } else if (lexer.sym == Lexeme.SEMICOLON) {
                node = new Node(Nonterminal.EMPTY, 0, null, null);
                lexer.readNextToken();
            } else if (lexer.sym == Lexeme.LBRA) {
                node = new Node(Nonterminal.EMPTY, 0, null, null);
                lexer.readNextToken();
                while (lexer.sym != Lexeme.RBRA) {
                    node = new Node(Nonterminal.SEQ, 0, node, statement());
                }
                lexer.readNextToken();
            } else {
                node = new Node(Nonterminal.EXPR, 0, expr(), null);
                if (lexer.sym != Lexeme.SEMICOLON) {
                    error("';' expected");
                }
                lexer.readNextToken();
            }
            return node;
        }

        Node parse() {
            lexer.readNextToken();
            Node node = new Node(Nonterminal.PROG, 0, statement(), null);
            if (lexer.sym != Lexeme.EOF) {
                error("Invalid statement syntax");
            }
            return node;
        }
    }

    enum Command {
        IFETCH, ISTORE, IPUSH, IPOP, IADD, ISUB, ILT, JZ, JNZ, JMP, HALT
    }

    static class Compiler {
        List<Object> compile(Node node) {
            List<Object> program = new ArrayList<>();

            switch (node.kind) {
                case VAR:
                    program.add(Command.IFETCH);
                    program.add(node.value);
                    break;
                case CONST:
                    program.add(Command.IPUSH);
                    program.add(node.value);
                    break;
                case ADD:
                    compile(node.op1);
                    compile(node.op2);
                    program.add(Command.IADD);
                    break;
                case SUB:
                    compile(node.op1);
                    compile(node.op2);
                    program.add(Command.ISUB);
                    break;
                case LT:
                    compile(node.op1);
                    compile(node.op2);
                    program.add(Command.ILT);
                    break;
                case SET:
                    compile(node.op2);
                    program.add(Command.ISTORE);
                    program.add(node.op1.value);
                    break;
                case IF1: {
                    compile(node.op1);
                    program.add(Command.JZ);
                    int addr = program.size();
                    program.add(0);
                    compile(node.op2);
                    program.set(addr, program.size());
                    break;
                }
                case IF2: {
                    compile(node.op1);
                    program.add(Command.JZ);
                    int addr1 = program.size();
                    program.add(0);
                    compile(node.op2);
                    program.add(Command.JMP);
                    int addr2 = program.size();
                    program.add(0);
                    program.set(addr1, program.size());
                    compile(node.op3);
                    program.set(addr2, program.size());
                    break;
                }
                case WHILE: {
                    int addr1 = program.size();
                    compile(node.op1);
                    program.add(Command.JZ);
                    int addr2 = program.size();
                    program.add(0);
                    compile(node.op2);
                    program.add(Command.JMP);
                    program.add(addr1);
                    program.set(addr2, program.size());
                    break;
                }
                case DO: {
                    int addr = program.size();
                    compile(node.op1);
                    compile(node.op2);
                    program.add(Command.JNZ);
                    program.add(addr);
                    break;
                }
                case SEQ:
                    compile(node.op1);
                    compile(node.op2);
                    break;
                case EXPR:
                    compile(node.op1);
                    program.add(Command.IPOP);
                    break;
                case PROG:
                    compile(node.op1);
                    program.add(Command.HALT);
                    break;
            }
            return program;
        }
    }

    static class VirtualMachine {
        void run(List<Object> program) {
            int[] var = new int[26];
            Stack<Integer> stack = new Stack<>();
            int pc = 0;
            m1:
            while (true) {
                Command op = (Command) program.get(pc);
                int arg = pc < program.size() - 1 && program.get(pc + 1) instanceof Integer ? (int) program.get(pc + 1) : 0;
                switch (op) {
                    case IFETCH:
                        stack.push(var[arg]);
                        pc += 2;
                        break;
                    case ISTORE:
                        var[arg] = stack.pop();
                        pc += 2;
                        break;
                    case IPUSH:
                        stack.push(arg);
                        pc += 2;
                        break;
                    case IPOP:
                        stack.push(arg);
                        stack.pop();
                        pc += 1;
                        break;
                    case IADD:
                        stack.push(stack.pop() + stack.pop());
                        pc += 1;
                        break;
                    case ISUB:
                        stack.push(-stack.pop() + stack.pop());
                        pc += 1;
                        break;
                    case ILT:
                        stack.push(stack.pop() > stack.pop() ? 1 : 0);
                        pc += 1;
                        break;
                    case JZ:
                        if (stack.pop() == 0) {
                            pc = arg;
                        } else {
                            pc += 2;
                        }
                        break;
                    case JNZ:
                        if (stack.pop() != 0) {
                            pc = arg;
                        } else {
                            pc += 2;
                        }
                        break;
                    case JMP:
                        pc = arg;
                        break;
                    case HALT:
                        break m1;
                }
            }
            System.out.println("Execution finished.");
            for (int i = 0; i < var.length; i++) {
                if (var[i] != 0) {
                    System.out.println((char) ('a' + i) + " = " + var[i]);
                }
            }
        }
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer("{ a = 10; do { a = a - 2;}  while (3 < a); }");
        Parser parser = new Parser(lexer);
        Node ast = parser.parse();

        Compiler compiler = new Compiler();
        List<Object> program = compiler.compile(ast);

        VirtualMachine vm = new VirtualMachine();
        vm.run(program);
    }
}
