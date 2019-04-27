package taty;

import java.util.HashMap;
import java.util.Map;

public class Env {

    private static Map<SymbExpr, TatyExpr> globals = new HashMap<>();
    
    public static void bindGlobal(SymbExpr sym, TatyExpr val) {
        globals.put(sym, val);
    }

    public static void bindGlobal(String sym, TatyExpr val) {
        globals.put(SymbExpr.of(sym), val);
    }

    public static void resetGlobalEnv() {
        globals.clear();
    }

    public static TatyExpr lookupGlobal(SymbExpr sym) {
        TatyExpr expr = globals.get(sym);
        if (expr == null) {
            throw new IllegalArgumentException("Symbol " + sym + " not found in env");
        } else {
            return expr;
        }
    }

    private final TatyExpr cons;

    public Env(TatyExpr cons) {
        this.cons = cons;
    }

    public static Env Env_() {
        return new Env(NilExpr.NIL);
    }

    public static Env Env_(Cons cons) {
        return new Env(cons);
    }

    public Object lookup(SymbExpr sym) {
        if (cons != NilExpr.NIL) {
            TatyExpr lookup = ((Cons) cons).lookup(sym);
            if (lookup != null) {
                return lookup;
            }
        }
        return lookupGlobal(sym);
    }

    public Env bind(SymbExpr sym, TatyExpr val) {
        return Env_(new Cons(new Cons(sym, val), cons));
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
