package taty;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReaderTest {

    Reader reader = new Reader();

    @Test
    public void test_simple_read() {
        assertEquals(NumberExpr.of(6), ((Cons) reader.read("6")).car());
    }
}