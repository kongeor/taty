package jisp;

import org.junit.Test;

import static jisp.NumberExp.NumberExp_;
import static org.junit.Assert.assertEquals;

public class ReaderTest {

    Reader reader = new Reader();

    @Test
    public void test_simple_read() {
        assertEquals(NumberExp_(6), reader.read("6").car());
    }

}
