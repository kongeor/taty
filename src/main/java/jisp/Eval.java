package jisp;

import java.util.concurrent.atomic.AtomicLong;

public class Eval {

    public static class LetExpr extends JispExpr {

        private final Cons binds;
        private final Cons body;

        public LetExpr(Cons binds, Cons body) {
            this.binds = binds;
            this.body = body;
        }

        @Override
        public Object eval(Env env) {

            Cons b = binds;
            while (b != NilExpr.NIL) {
                SymbExpr car = (SymbExpr) b.car();
                JispExpr form = (JispExpr) ((Cons) b.cdr()).car();
                Object val = Eval.eval(env, form);
                env = env.bind(car, val);
                b = (Cons) ((Cons) b.cdr()).cdr();
            }

            Cons bd = body;
            Object result = NilExpr.NIL;
            while (bd != NilExpr.NIL) {
                result = Eval.eval(env, (JispExpr) bd.car());
                bd = (Cons) bd.cdr();
            }

            return result;
        }
    }

    public static class FnExpr extends JispExpr {

        private static final AtomicLong ids = new AtomicLong(1);

        private final Cons params;
        private final Cons body;
        private final long id;

        public FnExpr(Cons params, Cons body) {
            this.params = params;
            this.body = body;
            this.id = ids.getAndIncrement();
        }

        @Override
        public Object eval(Env parentEnv) {
            return new IFn() {
                @Override
                public Object apply(Env env, Cons args) {
                    Env lambdaEnv = parentEnv;

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
                        result = Eval.eval(lambdaEnv, (JispExpr) b.car());
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

    }

    public static class QuoteExpr extends JispExpr {

        private final Object body;

        public QuoteExpr(Object body) {
            this.body = body;
        }

        @Override
        public Object eval(Env env) {
            return body;
        }
    }

    public static class DoExpr extends JispExpr {

        private final Cons exprs;

        public DoExpr(Cons exprs) {
            this.exprs = exprs;
        }

        @Override
        public Object eval(Env env) {
            Object result = NilExpr.NIL;

            Cons e = exprs;
            while (e != NilExpr.NIL) {
                result = Eval.eval(env, (JispExpr) e.car());
                e = (Cons) e.cdr();
            }

            return result;
        }
    }

    public static class DefExpr extends JispExpr {

        private final SymbExpr symb;
        private final JispExpr val;

        public DefExpr(SymbExpr symb, JispExpr val) {
            this.symb = symb;
            this.val = val;
        }

        @Override
        public Object eval(Env env) {
            Object result = Eval.eval(env, val);
            Env.bindGlobal(symb, result);
            return result;
        }
    }

    public static class IfExpr extends JispExpr {

        private final JispExpr pred;
        private final JispExpr ifExp;
        private final JispExpr elseExp;

        public IfExpr(JispExpr pred, JispExpr ifExp, JispExpr elseExp) {
            this.pred = pred;
            this.ifExp = ifExp;
            this.elseExp = elseExp;
        }

        @Override
        public Object eval(Env env) {
            Object res = Eval.eval(env, pred);
            if (Bool.isTruthy(res)) {
                return Eval.eval(env, ifExp);
            } else {
                return Eval.eval(env, elseExp);
            }
        }
    }

    public static class CondExpr extends JispExpr {

        private final Cons clauses;

        public CondExpr(Cons clauses) {
            this.clauses = clauses;
        }

        @Override
        public Object eval(Env env) {
            // TODO check pairs
            Cons pair = clauses;

            while(pair != NilExpr.NIL && pair.car() != NilExpr.NIL) {
                Object res = Eval.eval(env, (JispExpr) pair.car());
                if (Bool.isTruthy(res)) {
                    return Eval.eval(env, (JispExpr)((Cons)pair.cdr()).car());
                }
                pair = (Cons)((Cons)pair.cdr()).cdr();
            }

            throw new JispException("All clauses failed in cond. Consider adding a final true clause");
        }
    }

    private static JispExpr evalApply(Env env, IFn f, Cons args) {
        if (f == null) {
            throw new JispException("Undefined is not a function");
        }
        return (JispExpr) f.apply(env, evalArgs(env, args));
    }

    private static Cons evalArgs(Env env, Cons args) {
        if (args == NilExpr.NIL) {
            return NilExpr.NIL;
        } else {
            return new Cons(((JispExpr)args.car()).eval(env),
                    evalArgs(env, (Cons)args.cdr()));
        }
    }

    private static JispExpr evalSexpr(Env env, SymbExpr symb, Cons form) {

        String s = symb.name();

        switch (s) {
            case "if": return (JispExpr) new IfExpr((JispExpr) form.nth(0), (JispExpr) form.nth(1), (JispExpr) form.nth(2)).eval(env);
            case "do": return (JispExpr) new DoExpr(form).eval(env);
            case "def": return (JispExpr) new DefExpr((SymbExpr)form.nth(0), (JispExpr) form.nth(1)).eval(env);
            case "quote": return (JispExpr) form.car();
            case "fn": return (JispExpr) new FnExpr((Cons) form.nth(0), (Cons) form.cdr()).eval(env);
            case "resolve": return null; // TODO
            case "cond": return (JispExpr) new CondExpr(form).eval(env);
            case "let": return (JispExpr) new LetExpr((Cons) form.nth(0), (Cons) form.cdr()).eval(env);
            default: return evalApply(env, (IFn)symb.eval(env), form);
        }
    }

    public static JispExpr eval(Env env, JispExpr form) {
        if (form instanceof Cons) {
            Cons cons = (Cons) form;
            if (cons.car() instanceof SymbExpr) {
                return evalSexpr(env, (SymbExpr)cons.car(), (Cons) cons.cdr());
            } else {
                return evalApply(env, (IFn)((JispExpr) cons.car()).eval(env), (Cons) cons.cdr());
            }
        } else if (form instanceof SymbExpr) {
            return (JispExpr) form.eval(env);
        } else {
            return form;
        }
    }

}
