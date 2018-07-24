package jisp;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static jisp.SymbExp.SymbExp_;
import static org.junit.Assert.*;

public class EnvTest {

    @Before
    public void init() {
        Env.resetGlobalEnv();
    }

    @Test
    public void global_lookup() {
        NumberExp num = new NumberExp(5);
        SymbExp sym = SymbExp_("a");
        Env.bindGlobal(sym, num);
        assertEquals(num, Env.lookupGlobal(sym));
    }

    @Test
    public void lookup() {
        NumberExp num = new NumberExp(5);
        SymbExp sym = SymbExp_("a");
        Env env = Env.Env_();
        env = env.bind(sym, num);
        assertEquals(num, env.lookup(sym));
    }

    @Test
    public void lookup_shadows_global() {
        NumberExp num = new NumberExp(5);
        NumberExp fallback = new NumberExp(6);
        SymbExp sym = SymbExp_("a");
        Env.bindGlobal(sym, fallback);
        Env env = Env.Env_();
        env = env.bind(sym, num);
        assertEquals(num, env.lookup(sym));
    }

    @Test
    public void lookup_fallback() {
        NumberExp n5 = new NumberExp(5);
        NumberExp n6 = new NumberExp(6);
        SymbExp a = SymbExp_("a");
        SymbExp b = SymbExp_("b");
        Env.bindGlobal(a, n5);
        Env env = Env.Env_();
        env = env.bind(b, n6);
        assertEquals(n5, env.lookup(a));
        assertEquals(n6, env.lookup(b));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unbound_lookup() {
        SymbExp a = SymbExp_("a");
        SymbExp b = SymbExp_("b");
        NumberExp n6 = new NumberExp(6);
        Env.bindGlobal(b, n6);
        Env env = Env.Env_();
        env.lookup(a);
    }
}
