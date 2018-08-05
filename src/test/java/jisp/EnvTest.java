package jisp;

import org.junit.Before;
import org.junit.Test;

import static jisp.SymbExpr.SymbExp_;
import static org.junit.Assert.*;

public class EnvTest {

    @Before
    public void init() {
        Env.resetGlobalEnv();
    }

    @Test
    public void global_lookup() {
        NumberExpr num = new NumberExpr(5);
        SymbExpr sym = SymbExp_("a");
        Env.bindGlobal(sym, num);
        assertEquals(num, Env.lookupGlobal(sym));
    }

    @Test
    public void lookup() {
        NumberExpr num = new NumberExpr(5);
        SymbExpr sym = SymbExp_("a");
        Env env = Env.Env_();
        env = env.bind(sym, num);
        assertEquals(num, env.lookup(sym));
    }

    @Test
    public void lookup_shadows_global() {
        NumberExpr num = new NumberExpr(5);
        NumberExpr fallback = new NumberExpr(6);
        SymbExpr sym = SymbExp_("a");
        Env.bindGlobal(sym, fallback);
        Env env = Env.Env_();
        env = env.bind(sym, num);
        assertEquals(num, env.lookup(sym));
    }

    @Test
    public void lookup_fallback() {
        NumberExpr n5 = new NumberExpr(5);
        NumberExpr n6 = new NumberExpr(6);
        SymbExpr a = SymbExp_("a");
        SymbExpr b = SymbExp_("b");
        Env.bindGlobal(a, n5);
        Env env = Env.Env_();
        env = env.bind(b, n6);
        assertEquals(n5, env.lookup(a));
        assertEquals(n6, env.lookup(b));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unbound_lookup() {
        SymbExpr a = SymbExp_("a");
        SymbExpr b = SymbExp_("b");
        NumberExpr n6 = new NumberExpr(6);
        Env.bindGlobal(b, n6);
        Env env = Env.Env_();
        env.lookup(a);
    }
}
