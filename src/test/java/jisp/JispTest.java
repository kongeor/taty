package jisp;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static jisp.Cons.Cons_;
import static jisp.NumberExpr.NumberExpr_;
import static jisp.SymbExpr.SymbExp_;
import static org.junit.Assert.assertEquals;

public class JispTest {

    Env env;
    Reader reader = new Reader();

    @Before
    public void init() {
        env = Env.initBaseEnv();
    }

    private JispExpr readFirst(String source) {
        return (JispExpr) reader.read(source).car();
    }

    @Test
    public void num() {
        assertEquals(5, readFirst("5").eval(env));
    }

    @Test
    public void def_sym() {
        assertEquals(1, readFirst("(def a 1)").eval(env));
        assertEquals(1, readFirst("a").eval(env));
    }

    @Test
    public void let() {
        assertEquals(1, readFirst("(let [a 1] a)").eval(env));
        try {
            assertEquals(1, readFirst("a").eval(env));
            throw new RuntimeException("should fail");
        } catch(IllegalArgumentException e) {
            // ok!
        }
    }

    @Test
    public void let_mult_exp() {
        assertEquals(2, readFirst("(let [] 1 2)").eval(env));
    }

    @Test
    public void fn() {
        assertEquals(3, readFirst("((fn [a b] (+ a b)) 1 2)").eval(env));
    }

    @Test
    public void fn_mult_expr() {
        assertEquals(2, readFirst("((fn [] 1 2))").eval(env));
    }

    @Test
    public void quote_cons() {
        assertEquals(Cons_(NumberExpr_(1), Cons_(NumberExpr_(2), Cons_(NumberExpr_(3)))),
                readFirst("(quote (1 2 3))").eval(env));
    }

    @Test
    public void quote_cons_short() {
        assertEquals(Cons_(NumberExpr_(1), Cons_(NumberExpr_(2), Cons_(NumberExpr_(3)))),
                readFirst("'(1 2 3)").eval(env));
    }

    @Test
    public void quote() {
        assertEquals(SymbExp_("a"), readFirst("'a").eval(env));
    }

    @Test
    public void do_exp() {
        assertEquals(3, readFirst("(do (def a 3) a").eval(env));
    }

    @Test
    public void code_exp() {
        assertEquals(3, readFirst("(cond (= 1 1) 3").eval(env));
    }

    @Test
    public void code_exp_second_clause() {
        assertEquals(2, readFirst("(cond (= 1 2) 3 true 2").eval(env));
    }

    @Test(expected = JispException.class)
    public void code_exp_no_clause() {
        assertEquals(null, readFirst("(cond (= 1 2) 3 false 2").eval(env));
    }

    @Test
    public void apply() {
        assertEquals(6, readFirst("(apply + '(1 2 3)").eval(env));
    }

    @Test
    @Ignore
    public void vararg() {
        assertEquals(6, readFirst("((vararg (fn [args] (apply + args))) 1 2 3)").eval(env));
    }

    @Test
    public void read_file() {
        assertEquals(String.class, readFirst("(read-file \"src/test/resources/fact.jisp\")").eval(env).getClass());
    }

    @Test
    public void load_file() {
        assertEquals(120, readFirst("(do (load-file \"src/test/resources/fact.jisp\") (fact 5))").eval(env));
    }

    @Test
    public void nil_is_false() {
        assertEquals(2, readFirst("(if nil 1 2)").eval(env));
    }

    @Test
    public void false_is_false() {
        assertEquals(2, readFirst("(if false 1 2)").eval(env));
    }

    @Test
    public void nums_are_true() {
        assertEquals(1, readFirst("(if 5 1 2)").eval(env));
    }

    @Test
    public void funcs_are_true() {
        assertEquals(1, readFirst("(if (fn []) 1 2)").eval(env));
    }
}
