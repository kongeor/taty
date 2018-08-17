package taty;

import java.util.concurrent.atomic.AtomicLong;

public class Eval {

    private static TatyExpr evalLet(Env env, Cons binds, Cons body) {
        Cons b = binds;
        while (b != NilExpr.NIL) {
            SymbExpr car = (SymbExpr) b.car();
            TatyExpr form = (TatyExpr) ((Cons) b.cdr()).car();
            Object val = Eval.eval(env, form);
            env = env.bind(car, val);
            b = (Cons) ((Cons) b.cdr()).cdr();
        }

        Cons bd = body;
        TatyExpr result = NilExpr.NIL;
        while (bd != NilExpr.NIL) {
            result = Eval.eval(env, (TatyExpr) bd.car());
            bd = (Cons) bd.cdr();
        }

        return result;
    }

    private static final AtomicLong ids = new AtomicLong(1);

    private static TatyExpr evalFn(Env env, Cons params, Cons body) {
        final long id = ids.getAndIncrement();
        return new IFn() {
            @Override
            public Object apply(Env env1, Cons args) {
                Env lambdaEnv = env; // must use top env

                Cons p = params;
                Cons a = args;
                while (p != NilExpr.NIL) {
                    SymbExpr sym = (SymbExpr) p.car();
                    lambdaEnv = lambdaEnv.bind(sym, a.car());
                    p = (Cons) p.cdr();
                    a = (Cons) a.cdr();
                }

                Cons b = body;
                Object result = NilExpr.NIL;
                while (b != NilExpr.NIL) {
                    result = Eval.eval(lambdaEnv, (TatyExpr) b.car());
                    b = (Cons) b.cdr();
                }

                return result;
            }

            @Override
            public String toString() {
                return "<Lambda:" + id + ">";
            }
        };
    }

    private static TatyExpr evalDo(Env env, Cons exprs) {
        TatyExpr result = NilExpr.NIL;

        Cons e = exprs;
        while (e != NilExpr.NIL) {
            result = Eval.eval(env, (TatyExpr) e.car());
            e = (Cons) e.cdr();
        }

        return result;
    }

    private static TatyExpr evalDef(Env env, SymbExpr symb, TatyExpr val) {
        TatyExpr result = Eval.eval(env, val);
        Env.bindGlobal(symb, result);
        return result;
    }

    private static TatyExpr evalIf(Env env, TatyExpr pred, TatyExpr ifExp, TatyExpr elseExp) {
        TatyExpr res = Eval.eval(env, pred);
        if (Bool.isTruthy(res)) {
            return Eval.eval(env, ifExp);
        } else {
            return Eval.eval(env, elseExp);
        }
    }

    private static TatyExpr evalCond(Env env, Cons clauses) {
        // TODO check pairs
        Cons pair = clauses;

        while(pair != NilExpr.NIL && pair.car() != NilExpr.NIL) {
            Object res = Eval.eval(env, (TatyExpr) pair.car());
            if (Bool.isTruthy(res)) {
                return Eval.eval(env, (TatyExpr)((Cons)pair.cdr()).car());
            }
            pair = (Cons)((Cons)pair.cdr()).cdr();
        }

        throw new TatyException("All clauses failed in cond. Consider adding a final true clause");
    }

    private static TatyExpr evalApply(Env env, IFn f, Cons args) {
        if (f == null) {
            throw new TatyException("Undefined is not a function");
        }
        return (TatyExpr) f.apply(env, evalArgs(env, args));
    }

    private static Cons evalArgs(Env env, Cons args) {
        if (args == NilExpr.NIL) {
            return NilExpr.NIL;
        } else {
            return new Cons(Eval.eval(env, (TatyExpr) args.car()),
                    evalArgs(env, (Cons)args.cdr()));
        }
    }

    private static TatyExpr evalSexpr(Env env, SymbExpr symb, Cons form) {

        String s = symb.name();

        switch (s) {
            case "if": return evalIf(env, (TatyExpr) form.nth(0), (TatyExpr) form.nth(1), (TatyExpr) form.nth(2));
            case "do": return evalDo(env, form);
            case "def": return evalDef(env, (SymbExpr)form.nth(0), (TatyExpr) form.nth(1));
            case "quote": return (TatyExpr) form.car();
            case "fn": return evalFn(env, (Cons) form.nth(0), (Cons) form.cdr());
            case "resolve": return (TatyExpr) Env.lookupGlobal((SymbExpr) eval(env, (TatyExpr) form.car()));
            case "cond": return evalCond(env, form);
            case "let": return evalLet(env, (Cons) form.nth(0), (Cons) form.cdr());
            default: return evalApply(env, (IFn) eval(env, symb), form);
        }
    }

    public static TatyExpr eval(Env env, TatyExpr form) {
        if (form instanceof Cons && form != NilExpr.NIL) {
            Cons cons = (Cons) form;
            if (cons.car() instanceof SymbExpr) {
                return evalSexpr(env, (SymbExpr)cons.car(), (Cons) cons.cdr());
            } else {
                return evalApply(env, (IFn)eval(env, (TatyExpr) cons.car()), (Cons) cons.cdr());
            }
        } else if (form instanceof SymbExpr) {
            return (TatyExpr) env.lookup((SymbExpr) form);
        } else {
            return form;
        }
    }

}
