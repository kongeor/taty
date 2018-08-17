package taty;

import org.junit.Test;

import static taty.NumberExpr.NumberExpr_;
import static org.junit.Assert.assertEquals;

public class ReaderTest {

    Reader reader = new Reader();

    @Test
    public void test_simple_read() {
        assertEquals(NumberExpr_(6), reader.read("6").car());
    }
}
