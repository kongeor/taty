package jisp;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class EnvTest {

    @Test
    public void lookup() throws IOException {
        NumberExp num = new NumberExp(5);
        SymbExp sym = new SymbExp("a");
        Env.bind(sym, num);
        assertEquals(num, Env.lookup(sym));
    }
}
