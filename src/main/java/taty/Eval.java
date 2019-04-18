package taty;

import java.util.concurrent.atomic.AtomicLong;

public class Eval {

    private static TatyExpr evalLet(Env env, TatyExpr binds, TatyExpr body) {
        TatyExpr b = binds;
        while (b != NilExpr.NIL) {
            SymbExpr car = (SymbExpr) ((Cons) b).car();
            TatyExpr form = ((Cons) (((Cons) b).cdr())).car();
            TatyExpr val = Eval.eval(env, form);
            env = env.bind(car, val);
            b = ((Cons) ((Cons) b).cdr()).cdr();
        }

        TatyExpr bd = body;
        TatyExpr result = NilExpr.NIL;
        while (bd != NilExpr.NIL) {
            result = Eval.eval(env, ((Cons) bd).car());
            bd = ((Cons) bd).cdr();
        }

        return result;
    }

    private static final AtomicLong ids = new AtomicLong(1);

    private static TatyExpr evalFn(Env env, TatyExpr params, TatyExpr body) {
        final long id = ids.getAndIncrement();
        return new IFn() {
            @Override
            public TatyExpr apply(Env env1, Cons args) {
                Env lambdaEnv = env; // must use top env

                TatyExpr p = params;
                TatyExpr a = args;
                while (p != NilExpr.NIL && a != NilExpr.NIL) {
                    SymbExpr sym = (SymbExpr) ((Cons) p).car();
                    lambdaEnv = lambdaEnv.bind(sym, ((Cons) a).car());
                    p = ((Cons) p).cdr();
                    a = ((Cons) a).cdr();
                }

                TatyExpr b = body;
                TatyExpr result = NilExpr.NIL;
                while (b != NilExpr.NIL) {
                    result = Eval.eval(lambdaEnv, ((Cons) b).car());
                    b = ((Cons) b).cdr();
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

        TatyExpr e = exprs;
        while (e != NilExpr.NIL) {
            result = Eval.eval(env, ((Cons) e).car());
            e = ((Cons) e).cdr();
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
        TatyExpr pair = clauses;

        while(pair != NilExpr.NIL && ((Cons) pair).car() != NilExpr.NIL) {
            TatyExpr res = Eval.eval(env, ((Cons) pair).car());
            if (Bool.isTruthy(res)) {
                return Eval.eval(env, ((Cons) ((Cons) pair).cdr()).car());
            }
            pair = ((Cons) ((Cons) pair).cdr()).cdr();
        }

        throw new TatyException("All clauses failed in cond. Consider adding a final true clause");
    }

    private static TatyExpr evalApply(Env env, IFn f, TatyExpr args) {
        if (f == null) {
            throw new TatyException("Undefined is not a function");
        }
        // TODO fix cast
        return f.apply(env, (Cons) evalArgs(env, args));
    }

    private static TatyExpr evalArgs(Env env, TatyExpr args) {
        if (args == NilExpr.NIL) {
            return NilExpr.NIL;
        } else {
            return new Cons(Eval.eval(env, ((Cons )args).car()),
                    evalArgs(env, ((Cons) args).cdr()));
        }
    }

    private static TatyExpr evalSexpr(Env env, SymbExpr symb, Cons form) {

        String s = symb.name();

        switch (s) {
            case "if": return evalIf(env, form.nth(0), form.nth(1), form.nth(2));
            case "do": return evalDo(env, form);
            case "def": return evalDef(env, (SymbExpr)form.nth(0), form.nth(1));
            case "quote": return form.car();
            case "fn": return evalFn(env, form.nth(0), form.cdr());
            case "resolve": return Env.lookupGlobal((SymbExpr) eval(env, form.car()));
            case "cond": return evalCond(env, form);
            case "let": return evalLet(env, form.nth(0), form.cdr());
            default: return evalApply(env, (IFn) eval(env, symb), form);
        }
    }

    public static TatyExpr eval(Env env, TatyExpr form) {
        if (form instanceof Cons && form != NilExpr.NIL) {
            Cons cons = (Cons) form;
            if (cons.car() instanceof SymbExpr) {
                return evalSexpr(env, (SymbExpr)cons.car(), (Cons) cons.cdr());
            } else {
                return evalApply(env, (IFn)eval(env, cons.car()), cons.cdr());
            }
        } else if (form instanceof SymbExpr) {
            return (TatyExpr) env.lookup((SymbExpr) form);
        } else {
            return form;
        }
    }

}
