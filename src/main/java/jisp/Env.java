package jisp;

import java.util.concurrent.atomic.AtomicReference;

import static jisp.Cons.Cons_;
import static jisp.SymbExpr.SymbExp_;

public class Env {
    
    private static final AtomicReference<Cons> globals =
        new AtomicReference<>(NilExpr.NIL);

    public static void bindGlobal(SymbExpr sym, Object val) {
        globals.updateAndGet(env -> Cons_(Cons_(sym, val), env));
    }

    public static void bindGlobal(String sym, Object val) {
        globals.updateAndGet(env -> Cons_(Cons_(SymbExp_(sym), val), env));
    }

    public static void resetGlobalEnv() {
        globals.updateAndGet(env -> NilExpr.NIL);
    }

    public static Object lookupGlobal(SymbExpr sym) {
        return globals.get().lookup(sym);
    }

    private final Cons cons;

    public Env(Cons cons) {
        this.cons = cons;
    }

    public static Env Env_() {
        return new Env(NilExpr.NIL);
    }

    public static Env Env_(Cons cons) {
        return new Env(cons);
    }

    public Object lookup(SymbExpr sym) {
        try {
            if (cons != null) {
                return cons.lookup(sym);
            }
        } catch (IllegalArgumentException e) {
        }
        return lookupGlobal(sym);
    }

    public Env bind(SymbExpr sym, Object val) {
        return Env_(Cons_(Cons_(sym, val), cons));
    }

    public static Env initBaseEnv() {
        Env.resetGlobalEnv();
        Env.bindGlobal("println", Builtin.PRINTLN);
        Env.bindGlobal("+", Builtin.PLUS);
        Env.bindGlobal("*", Builtin.MULT);
        Env.bindGlobal("-", Builtin.MINUS);
        Env.bindGlobal("/", Builtin.DIV);
        Env.bindGlobal("<", Builtin.LT);
        Env.bindGlobal(">", Builtin.GT);
        Env.bindGlobal("<=", Builtin.LTE);
        Env.bindGlobal(">=", Builtin.GTE);
        Env.bindGlobal("=", Builtin.EQ);
        Env.bindGlobal("dec", Builtin.DEC);
        Env.bindGlobal("inc", Builtin.INC);
        Env.bindGlobal("car", Builtin.CAR);
        Env.bindGlobal("cdr", Builtin.CDR);
        Env.bindGlobal("cons", Builtin.CONS);
        Env.bindGlobal("cons?", Builtin.CONS_Q);
        Env.bindGlobal("symbol?", Builtin.SYMBOL_Q);
        Env.bindGlobal("apply", Builtin.APPLY);
        Env.bindGlobal("nil?", Builtin.NIL_Q);
        Env.bindGlobal("vararg", Builtin.VARARG);
        Env.bindGlobal("read-file", Builtin.READ_FILE);
        Env.bindGlobal("load-file", Builtin.LOAD_FILE);
        Env.bindGlobal("die", Builtin.DIE);
        return Env_();
    }
}
