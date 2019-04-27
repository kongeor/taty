package taty;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

public class Reader {

    private static final String ALPHANUMS = "1234567890abcdefghijklmnopqrstuvwxyz_!-+*/<>=?";

    public TatyExpr read(String source) {
        PushbackReader reader = new PushbackReader(new StringReader(source));

        TatyExpr exprs = NilExpr.NIL;

        try {
            char c = (char) reader.read();

            while (c != '\uFFFF') {
                reader.unread(c);
                exprs = new Cons(readExpr(reader), exprs);
                c = (char) reader.read();
            }

            if (exprs == NilExpr.NIL) {
                return NilExpr.NIL;
            }
            return ((Cons) exprs).reverse();
        } catch (IOException e) {
            throw new TatyException("Cannot read source", e);
        }
    }

    private TatyExpr readExpr(PushbackReader reader) throws IOException {
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
            return new Cons(SymbExpr.of("quote"), new Cons(readExpr(reader)));
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

    public TatyExpr readNumber(PushbackReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        char c = (char) reader.read();
        while (Character.isDigit(c)) {
            sb.append(c);
            c = (char) reader.read();
        }
        reader.unread(c);
        return new NumberExpr(Integer.parseInt(sb.toString()));
    }

    public TatyExpr readSymbol(PushbackReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        char c = (char) reader.read();
        while (ALPHANUMS.contains("" + c)) {
            sb.append(c);
            c = (char) reader.read();
        }
        reader.unread(c);

        String s = sb.toString();
        if ("true".equals(s)) {
            return BoolExpr.T;
        } else if ("false".equals(s)) {
            return BoolExpr.F;
        } else if ("nil".equals(s)) {
            return NilExpr.NIL;
        } else {
            return SymbExpr.of(s);
        }
    }

    private TatyExpr readString(PushbackReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        char c = (char) reader.read();
        while (c != '"') {
            sb.append(c);
            c = (char) reader.read();
        }
        return StringExpr.of(sb.toString());
    }


    public TatyExpr readList(PushbackReader reader, char terminator) throws IOException {
        char c = (char) reader.read();

        TatyExpr exprs = NilExpr.NIL;

        while (c != terminator && c != '\uFFFF') {
            reader.unread(c);
            TatyExpr tatyExpr = readExpr(reader);
            exprs = new Cons(tatyExpr, exprs);
            readWhitespace(reader);
            c = (char) reader.read();
        }

        if (exprs == NilExpr.NIL) {
            return exprs;
        } else {
            return ((Cons) exprs).reverse();
        }
    }

    private void readWhitespace(PushbackReader reader) throws IOException {
        char c = (char) reader.read();
        while(Character.isWhitespace(c)) {
            c = (char) reader.read();
        }
        reader.unread(c);
    }

}
