package jisp;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import static jisp.StringExpr.StringExp_;
import static jisp.SymbExpr.SymbExp_;

public class Reader {

    private static final String ALPHANUMS = "1234567890abcdefghijklmnopqrstuvwxyz_!-+*/<>=?";

    public Cons read(String source) {
        PushbackReader reader = new PushbackReader(new StringReader(source));

        Cons exprs = null;

        try {
            char c = (char) reader.read();

            while (c != '\uFFFF') {
                reader.unread(c);
                exprs = new Cons(readExpr(reader), exprs);
                c = (char) reader.read();
            }

            if (exprs == null) {
                return null;
            }
            return exprs.reverse();
        } catch (IOException e) {
            throw new JispException("Cannot read source", e);
        }
    }

    private JispExpr readExpr(PushbackReader reader) throws IOException {
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

        if (c == '\'') {
            return new Special.QuoteExpr(readExpr(reader));
        }

        if (c == '"') {
            return readString(reader);
        }

        if (c == '(') {
            return readList(reader, ')');
        }

        if (c == '[') {
            return readList(reader, ']');
        }

        throw new IllegalStateException("boom: " + c);
    }

    public JispExpr readNumber(PushbackReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        char c = (char) reader.read();
        while (Character.isDigit(c)) {
            sb.append(c);
            c = (char) reader.read();
        }
        reader.unread(c);
        return new NumberExpr(Integer.parseInt(sb.toString()));
    }

    public JispExpr readSymbol(PushbackReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        char c = (char) reader.read();
        while (ALPHANUMS.contains("" + c)) {
            sb.append(c);
            c = (char) reader.read();
        }
        reader.unread(c);
        return SymbExp_(sb.toString());
    }

    private JispExpr readString(PushbackReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        char c = (char) reader.read();
        while (c != '"') {
            sb.append(c);
            c = (char) reader.read();
        }
        return StringExp_(sb.toString());
    }


    public JispExpr readList(PushbackReader reader, char terminator) throws IOException {
        char c = (char) reader.read();

        Cons exprs = null;

        while (c != terminator && c != '\uFFFF') {
            reader.unread(c);
            JispExpr jispExpr = readExpr(reader);
            exprs = new Cons(jispExpr, exprs);
            readWhitespace(reader);
            c = (char) reader.read();
        }
        if (exprs != null) {
            return Special.checkForm(exprs.reverse());
        } else {
            return null;
        }
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
//        List<JispExpr> exp = new Reader().read("(6(3))");
//        List<JispExpr> exp = new Reader().read("(6)");
//        List<JispExpr> exp = new Reader().read("[6]");
//        List<JispExpr> exp = new Reader().read("6 is? 5");
//        List<JispExpr> exp = new Reader().read("(let [a 1] (+ a 5))");
//        List<JispExpr> exp = new Reader().read("(let [a 1] 10)");
        System.out.println(exp);
    }
}
