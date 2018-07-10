package jisp;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ReaderTest {

    Reader reader = new Reader();

    @Test
    public void test_simple_read() throws IOException {
        assertEquals(new NumberExp(6), reader.read("6").car());
    }

}