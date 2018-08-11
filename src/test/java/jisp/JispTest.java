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
        assertEquals(NumberExpr_(5), Eval.eval(env, readFirst("5")));
    }

    @Test
    public void def_sym() {
        assertEquals(NumberExpr_(1), Eval.eval(env, readFirst("(def a 1)")));
        assertEquals(NumberExpr_(1), Eval.eval(env, readFirst("a")));
    }

    @Test
    public void let() {
        assertEquals(NumberExpr_(1), Eval.eval(env, readFirst("(let [a 1] a)")));
        try {
            assertEquals(NumberExpr_(1), Eval.eval(env, readFirst("a")));
            throw new RuntimeException("should fail");
        } catch(IllegalArgumentException e) {
            // ok!
        }
    }

    @Test
    public void let_mult_exp() {
        assertEquals(NumberExpr_(2), Eval.eval(env, readFirst("(let [] 1 2)")));
    }

    @Test
    public void fn() {
        assertEquals(NumberExpr_(3), Eval.eval(env, readFirst("((fn [a b] (+ a b)) 1 2)")));
    }

    @Test
    public void fn_mult_expr() {
        assertEquals(NumberExpr_(2), Eval.eval(env, readFirst("((fn [] 1 2))")));
    }

    @Test
    public void quote_cons() {
        assertEquals(Cons_(NumberExpr_(1), Cons_(NumberExpr_(2), Cons_(NumberExpr_(3)))),
                Eval.eval(env, readFirst("(quote (1 2 3))")));
    }

    @Test
    public void quoted_list_with_false() {
        assertEquals(Cons_(BoolExpr.F, Cons_(NumberExpr_(1))),
                Eval.eval(env, readFirst("'(false 1)")));
    }

    @Test
    public void quote_cons_short() {
        assertEquals(Cons_(NumberExpr_(1), Cons_(NumberExpr_(2), Cons_(NumberExpr_(3)))),
                Eval.eval(env, readFirst("'(1 2 3)")));
    }

    @Test
    public void quote() {
        assertEquals(SymbExp_("a"), Eval.eval(env, readFirst("'a")));
    }

    @Test
    public void do_exp() {
        assertEquals(NumberExpr_(3), Eval.eval(env, readFirst("(do (def a 3) a")));
    }

    @Test
    public void cond_expr() {
        assertEquals(NumberExpr_(3), Eval.eval(env, readFirst("(cond (= 1 1) 3")));
    }

    @Test
    public void cond_exp_second_clause() {
        assertEquals(NumberExpr_(2), Eval.eval(env, readFirst("(cond (= 1 2) 3 true 2")));
    }

    @Test
    public void cond_exp_with_false() {
        assertEquals(NumberExpr_(2), Eval.eval(env, readFirst("(cond (if true false 1) 3 true 2")));
    }

    @Test(expected = JispException.class)
    public void code_exp_no_clause() {
        assertEquals(null, Eval.eval(env, readFirst("(cond (= 1 2) 3 false 2")));
    }

    @Test
    public void apply() {
        assertEquals(NumberExpr_(6), Eval.eval(env, readFirst("(apply + '(1 2 3)")));
    }

    @Test
    public void vararg() {
        assertEquals(NumberExpr_(6), Eval.eval(env, readFirst("((vararg (fn [args] (apply + args))) 1 2 3)")));
    }

    @Test
    public void read_file() {
        assertEquals(Cons.class, Eval.eval(env, readFirst("(read-file \"src/test/resources/math.jisp\")")).getClass());
    }

    @Test
    public void load_file() {
        assertEquals(NumberExpr_(120), Eval.eval(env, readFirst("(do (load-file \"src/test/resources/math.jisp\") (fact 5))")));
    }

    @Test
    public void nil_is_false() {
        assertEquals(NumberExpr_(2), Eval.eval(env, readFirst("(if nil 1 2)")));
    }

    @Test
    public void false_is_false() {
        assertEquals(NumberExpr_(2), Eval.eval(env, readFirst("(if false 1 2)")));
    }

    @Test
    public void nums_are_true() {
        assertEquals(NumberExpr_(1), Eval.eval(env, readFirst("(if 5 1 2)")));
    }

    @Test
    public void funcs_are_true() {
        assertEquals(NumberExpr_(1), Eval.eval(env, readFirst("(if (fn []) 1 2)")));
    }

    @Test
    public void eval_nested_form() {
        assertEquals(NumberExpr_(2), Eval.eval(env, readFirst("(if (car ((fn [] '(false 3)))) 1 2)")));
    }

    @Test
    public void eval_nested_lambdas() {
        assertEquals(NumberExpr_(3), Eval.eval(env, readFirst("(((fn [x] (fn [y] (+ x y))) 1) 2)")));
    }
}
