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