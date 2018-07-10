package jisp;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

public class Reader {

    private static final String ALPHANUMS = "1234567890abcdefghijklmnopqrstuvwxyz_!-+*/<>=?";

    public Cons read(String source) throws IOException {
        PushbackReader reader = new PushbackReader(new StringReader(source));

        Cons exprs = null;

        char c = (char) reader.read();

        while(c != '\uFFFF') {
            reader.unread(c);
            exprs = new Cons(readExpr(reader), exprs);
            c = (char) reader.read();
        }

        return exprs.reverse();
    }

    private JispExp readExpr(PushbackReader reader) throws IOException {
        readWhitespace(reader);
        char c = (char) reader.read();

        if (Character.isDigit(c)) {
            reader.unread(c);
            return readNumber(reader);
        }

        if (ALPHANUMS.contains("" + c)) {
            reader.unread(c);
            return readSymbol(reader);
        }

        if (c == '(') {
            return readList(reader, ')');
        }

        if (c == '[') {
            return readList(reader, ']');
        }

        throw new IllegalStateException("boom: " + c);
    }

    public JispExp readNumber(PushbackReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        char c = (char) reader.read();
        while (Character.isDigit(c)) {
            sb.append(c);
            c = (char) reader.read();
        }
        reader.unread(c);
        return new NumberExp(Integer.parseInt(sb.toString()));
    }

    public JispExp readSymbol(PushbackReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        char c = (char) reader.read();
        while (ALPHANUMS.contains("" + c)) {
            sb.append(c);
            c = (char) reader.read();
        }
        reader.unread(c);
        return new SymbExp(sb.toString());
    }

    public Cons readList(PushbackReader reader, char terminator) throws IOException {
        char c = (char) reader.read();

        Cons exprs = null;

        while (c != terminator && c != '\uFFFF') {
            reader.unread(c);
            JispExp jispExp = readExpr(reader);
            exprs = new Cons(jispExp, exprs);
            readWhitespace(reader);
            c = (char) reader.read();
        }
        return exprs.reverse();
    }

    private void readWhitespace(PushbackReader reader) throws IOException {
        char c = (char) reader.read();
        while(Character.isWhitespace(c)) {
            c = (char) reader.read();
        }
        reader.unread(c);
    }

    public static void main(String[] args) throws IOException {
        Cons exp = new Reader().read("5 10 (6 ( 3 33 ) 20 )");
//        List<JispExp> exp = new Reader().read("(6(3))");
//        List<JispExp> exp = new Reader().read("(6)");
//        List<JispExp> exp = new Reader().read("[6]");
//        List<JispExp> exp = new Reader().read("6 is? 5");
//        List<JispExp> exp = new Reader().read("(let [a 1] (+ a 5))");
//        List<JispExp> exp = new Reader().read("(let [a 1] 10)");
        System.out.println(exp);
    }
}
