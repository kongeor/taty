package taty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnvTest {

    @BeforeEach
    public void init() {
        Env.resetGlobalEnv();
    }

    @Test
    public void global_lookup() {
        NumberExpr num = new NumberExpr(5);
        SymbExpr sym = SymbExpr.of("a");
        Env.bindGlobal(sym, num);
        assertEquals(num, Env.lookupGlobal(sym));
    }

    @Test
    public void lookup() {
        NumberExpr num = new NumberExpr(5);
        SymbExpr sym = SymbExpr.of("a");
        Env env = Env.Env_();
        env = env.bind(sym, num);
        assertEquals(num, env.lookup(sym));
    }

    @Test
    public void lookup_shadows_global() {
        NumberExpr num = new NumberExpr(5);
        NumberExpr fallback = new NumberExpr(6);
        SymbExpr sym = SymbExpr.of("a");
        Env.bindGlobal(sym, fallback);
        Env env = Env.Env_();
        env = env.bind(sym, num);
        assertEquals(num, env.lookup(sym));
    }

    @Test
    public void lookup_fallback() {
        NumberExpr n5 = new NumberExpr(5);
        NumberExpr n6 = new NumberExpr(6);
        SymbExpr a = SymbExpr.of("a");
        SymbExpr b = SymbExpr.of("b");
        Env.bindGlobal(a, n5);
        Env env = Env.Env_();
        env = env.bind(b, n6);
        assertEquals(n5, env.lookup(a));
        assertEquals(n6, env.lookup(b));
    }

    @Test
    public void unbound_lookup() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    SymbExpr a = SymbExpr.of("a");
                    SymbExpr b = SymbExpr.of("b");
                    NumberExpr n6 = new NumberExpr(6);
                    Env.bindGlobal(b, n6);
                    Env env = Env.Env_();
                    env.lookup(a);
                });
    }
}
