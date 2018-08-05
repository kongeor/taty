package jisp;

import java.util.concurrent.atomic.AtomicLong;

public class Special {

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
            while (b != null) {
                SymbExpr car = (SymbExpr) b.car();
                JispExpr form = (JispExpr) ((Cons) b.cdr()).car();
                Object val = form.eval(env);
                env = env.bind(car, val);
                b = (Cons) ((Cons) b.cdr()).cdr();
            }

            Cons bd = body;
            Object result = null;
            while (bd != null) {
                result = ((JispExpr) bd.car()).eval(env);
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
                    while (p != null) {
                        SymbExpr sym = (SymbExpr) p.car();
                        lambdaEnv = lambdaEnv.bind(sym, a.car());
                        p = (Cons) p.cdr();
                        a = (Cons) a.cdr();
                    }

                    Cons b = body;
                    Object result = null;
                    while (b != null) {
                        result = ((JispExpr) b.car()).eval(lambdaEnv);
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
            Object result = null;

            Cons e = exprs;
            while (e != NilExpr.NIL) {
                result = ((JispExpr) e.car()).eval(env);
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
            Object result = val.eval(env);
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
            Object res = pred.eval(env);
            if (Bool.isTruthy(res)) {
                return ifExp.eval(env);
            } else {
                return elseExp.eval(env);
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

            while(pair != null && pair.car() != null && ((Cons)pair.cdr()).car() != null) {
                Object res = ((JispExpr) pair.car()).eval(env);
                if (Bool.isTruthy(res)) {
                    return ((JispExpr)((Cons)pair.cdr()).car()).eval(env);
                }
                pair = (Cons)((Cons)pair.cdr()).cdr();
            }

            throw new JispException("All clauses failed in cond. Consider adding a final true clause");
        }
    }

    public static JispExpr checkForm(Cons cons) {
        if (cons == null) {
            return null;
        } else {
            Object car = cons.car();
            if (SymbExpr.SymbExp_("let").equals(car)) {
                return new LetExpr((Cons)((Cons)cons.cdr()).car(), (Cons)((Cons)cons.cdr()).cdr());
            } else if (SymbExpr.SymbExp_("fn").equals(car)) {
                return new FnExpr((Cons)((Cons)cons.cdr()).car(), (Cons)((Cons)cons.cdr()).cdr());
            } else if (SymbExpr.SymbExp_("quote").equals(car)) {
                return new QuoteExpr(((Cons)cons.cdr()).car());
            } else if (SymbExpr.SymbExp_("do").equals(car)) {
                return new DoExpr((Cons)cons.cdr());
            } else if (SymbExpr.SymbExp_("def").equals(car)) {
                return new DefExpr((SymbExpr)((Cons)cons.cdr()).car(), (JispExpr)((Cons)((Cons)cons.cdr()).cdr()).car());
            } else if (SymbExpr.SymbExp_("if").equals(car)) {
                return new IfExpr((JispExpr)((Cons)cons.cdr()).car(),
                        (JispExpr)((Cons)((Cons)cons.cdr()).cdr()).car(),
                        (JispExpr)((Cons)(((Cons)((Cons)cons.cdr()).cdr()).cdr())).car());
            } else if (SymbExpr.SymbExp_("cond").equals(car)) {
                return new CondExpr((Cons) cons.cdr());
            }

            return cons;
        }
    }
}
