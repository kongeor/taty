package taty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuiltinTest {

    Env env;

    Reader reader = new Reader();

    @BeforeEach
    public void init() {
        env = Env.initBaseEnv();
    }

    private TatyExpr readFirst(String source) {
        return ((Cons) reader.read(source)).car();
    }

    @ParameterizedTest
    @CsvSource({
            "(+ 1 2), 3",
            "(* 4 2), 8",
            "(- 5 2), 3",
            "(/ 10 3), 3",
            "(< 10 3), false",
            "(> 10 3), true",
            "(<= 3 3), true",
            "(>= 3 3), true",
            "(= 3 3), true",
            "(= 3 4), false",
            "(inc 0), 1",
            "(dec 2), 1",
            "(car '(1 2 3)), 1",
            "(cdr '(1 2 3)), (2 3)",
            "(cons 1 '(2 3)), (1 2 3)",
            "(cons 1 2), (1 . 2)",
            "(cons? 1), false",
            "(cons? '(1)), true",
            "(cons? '()), false",
            "(cons? nil), false",
    })
    void testBuiltins(String source, String result) {
        assertEquals(result, "" + Eval.eval(env, readFirst(source)));
    }
}
