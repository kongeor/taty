package jisp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static jisp.Cons.Cons_;
import static jisp.NumberExp.NumberExp_;
import static org.junit.Assert.assertEquals;

public class JispTest {

    Env env;
    Reader reader = new Reader();

    @Before
    public void init() {
        env = Env.initBaseEnv();
    }

    private JispExp readFirst(String source) throws IOException {
        return (JispExp) reader.read(source).car();
    }

    @Test
    public void num() throws IOException {
        assertEquals(5, readFirst("5").eval(env));
    }

    @Test
    public void def_sym() throws IOException {
        assertEquals(1, readFirst("(def a 1)").eval(env));
        assertEquals(1, readFirst("a").eval(env));
    }

    @Test
    public void let() throws IOException {
        assertEquals(1, readFirst("(let [a 1] a)").eval(env));
        try {
            assertEquals(1, readFirst("a").eval(env));
            throw new RuntimeException("should fail");
        } catch(IllegalArgumentException e) {
            // ok!
        }
    }

    @Test
    public void fn() throws IOException {
        assertEquals(3, readFirst("((fn [a b] (+ a b)) 1 2)").eval(env));
    }

    @Test
    public void quote_cons() throws IOException {
        assertEquals(Cons_(NumberExp_(1),
                Cons_(NumberExp_(2),
                        Cons_(NumberExp_(3)))),
                readFirst("(quote (1 2 3))").eval(env));
    }

    @Test
    public void do_exp() throws IOException {
        assertEquals(3, readFirst("(do (def a 3) a").eval(env));
    }
}
