package jisp;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static jisp.Cons.Cons_;
import static jisp.SymbExp.SymbExp_;

public class Env {
    
    private static final AtomicReference<Cons> globals =
        new AtomicReference<>(null);

    public static void bindGlobal(SymbExp sym, Object val) {
        globals.updateAndGet(env -> Cons_(Cons_(sym, val), env));
    }

    public static void bindGlobal(String sym, Object val) {
        globals.updateAndGet(env -> Cons_(Cons_(SymbExp_(sym), val), env));
    }

    public static void resetGlobalEnv() {
        globals.updateAndGet(env -> null);
    }

    public static Object lookupGlobal(SymbExp sym) {
        return globals.get().lookup(sym);
    }

    private final Cons cons;

    public Env(Cons cons) {
        this.cons = cons;
    }

    public static Env Env_() {
        return new Env(null);
    }

    public static Env Env_(Cons cons) {
        return new Env(cons);
    }

    public Object lookup(SymbExp sym) {
        try {
            if (cons != null) {
                return cons.lookup(sym);
            }
        } catch (IllegalArgumentException e) {
        }
        return lookupGlobal(sym);
    }

    public Env bind(SymbExp sym, Object val) {
        return Env_(Cons_(Cons_(sym, val), cons));
    }

    public static Env initBaseEnv() {
        Env.resetGlobalEnv();
        Env.bindGlobal("true", Boolean.TRUE);
        Env.bindGlobal("false", Boolean.FALSE);
        Env.bindGlobal("+", Builtin.PLUS);
        Env.bindGlobal("-", Builtin.MINUS);
        Env.bindGlobal("nil", null);
        Env.bindGlobal("<", Builtin.LT);
        Env.bindGlobal("println", Builtin.PRINTLN);
        return Env_();
    }
}
