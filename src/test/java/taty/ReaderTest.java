package taty;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReaderTest {

    Reader reader = new Reader();

    @Test
    public void test_simple_read() {
        assertEquals(NumberExpr.of(6), ((Cons) reader.read("6")).car());
    }

    @Test
    public void whitespace_read() {
        assertEquals(NumberExpr.of(6), ((Cons) reader.read("    6")).car());
    }

    @Test
    public void whitespace_end_read() {
        assertEquals(NumberExpr.of(6), ((Cons) reader.read("    6   ")).car());
    }

    @Test
    public void list_with_comment() {
        assertEquals(new Cons(NumberExpr.of(6),
                new Cons(NumberExpr.of(5),
                        new Cons(NumberExpr.of(2)))), ((Cons) reader.read("(6 ;yolo\n 5 2)")).car());
    }

    @Test
    public void mixed() {
        assertEquals(new Cons(NumberExpr.of(6),
                new Cons(NumberExpr.of(5),
                        new Cons(NumberExpr.of(2)))), ((Cons) reader.read("    6   ;;yo\n\n;;yo\n\n5 2")));
    }

    @Test
    public void comment_read() {
        assertEquals(NumberExpr.of(6), ((Cons) reader.read("; comment!\n6")).car());
    }

    @Test
    public void end_comment() {
        assertEquals(NumberExpr.of(6), ((Cons) reader.read("6 ; comment!\n")).car());
    }

    @Test
    public void comment_whitespace_read() {
        assertEquals(NumberExpr.of(6), ((Cons) reader.read("; comment 1!\n\n; comment 2\n 6")).car());
    }
}